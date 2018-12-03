package conexion;
import java.awt.BorderLayout;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

import javax.net.ssl.SSLSocketFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import musica.CancionUDPCliente;




/*
*this class represent a Client in the application
*and the interfaz of the application
*/
public class Cliente extends JFrame{

	public static final String TRUSTTORE_LOCATION = "C:/Windows/System32/keystore.jks";
	
	private PanelApuestas panelApuestas;
	private PanelCarrera panelCarrera;
	private static PrintWriter writerC;
	
	private static String[] caballo;
	
	private static int apuestaC;
	private static String nombreC;
	private static String cedula;
	private static String ganador;
	
	
	
	static AudioInputStream audioInputStream;
	static SourceDataLine sourceDataLine;
	
	/*
	*this method create a cliente
	*/
	public Cliente() {
		
		
		panelApuestas= new PanelApuestas(this);
		panelCarrera= new PanelCarrera(this);

		JPanel panelAux= new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.add(panelApuestas, BorderLayout.CENTER);
		
		caballo= new String[] {"0","0","0","0","0","0"};
		
		setLayout(new BorderLayout());
		
		add(panelCarrera, BorderLayout.CENTER);
		add(panelAux, BorderLayout.NORTH);
		setSize(1100,600);
		
		
	}
	
	/*
	*This method is in charge of set the position of the caballo Array 
	*each position in the array represent the position of the horse
	*/
	public void setCaballos(String[] caballo) {
		this.caballo=caballo;
		
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		cedula= JOptionPane.showInputDialog(null,"Ingrese No. Documento");

		
		File fileData= new File("Datos/cedulas.txt");
		BufferedWriter buf= new BufferedWriter(new FileWriter(fileData, true));
		
		buf.newLine();
		buf.write(cedula);
		buf.close();
	
		/*
		*In this part we can use SSL but we have to create a KeyStore in our computer
		*in that case we have to delete the socket with port 5555
		*/
//		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
//		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
//		Socket client =  sf.createSocket("localhost", 26000);
		
		
		Socket client= new Socket("localhost", 5555);
		
		Cliente cliente= new Cliente();
		cliente.setVisible(true);
		
 
		BufferedReader readerC =  new BufferedReader(new InputStreamReader(client.getInputStream())); 
		writerC =  new PrintWriter(client.getOutputStream(), true); 
		
		
		
		
		MulticastSocket escucha = new MulticastSocket(40000);

		
		escucha.joinGroup(InetAddress.getByName("228.5.6.7"));
		
		
		HiloVoz voz= new HiloVoz();
		voz.start();
		
		CancionUDPCliente cancion= new CancionUDPCliente();
		cancion.start();
		
		HiloMover hilo= new HiloMover(escucha, cliente,caballo);
		hilo.start();
		
		initiateAudio();
		
							

		
		
	}
	
	/*
	*this method create a file with the information of a cliente in a bet
	*each time the client bets, the information of the amount of the bet, the name of the horse, 
	*if it won or not and the date of the bet is saved
	*/
	public void escribirArchivo() {
		try {
		File filedData= new File("Datos/"+cedula+".txt");
		boolean archivoExiste= !filedData.exists();
		BufferedWriter base= new BufferedWriter(new FileWriter(filedData, true));
		Calendar c= Calendar.getInstance();
		String dia=Integer.toString(c.get(Calendar.DATE));
		String mes=Integer.toString(c.get(Calendar.MONTH));
		String annio=Integer.toString(c.get(Calendar.YEAR));
		if(!archivoExiste) {
			base.newLine();
		}
		base.write("El monto de la apuesta fue: "+ apuestaC+" Aposto por: "+nombreC+" "+" ¿Caballo ganador?: "+ganador+" En la fecha:"+"Dia: "+dia+" Mes: "+mes+" Año: "+annio);
		base.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/*
	*this method return the audioFormat 
	*/
	private static AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/*
	*this method play the audio on the client's side
	*/
	private static void playAudio() {
		byte[] buffer = new byte[10000];
		try {
			int count;
			while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					sourceDataLine.write(buffer, 0, count);
				}
			}
		} catch (Exception e) {
			// Handle exceptions
		}
	}

	/*
	*this method allows the client to get the audio throug UDP conection 
	*initializing the transfer of the narration
	*/
	private static void initiateAudio() {
		try {
			MulticastSocket socket = new MulticastSocket(9786);
			InetAddress group = InetAddress.getByName("230.0.0.0");
			socket.joinGroup(group);
			byte[] audioBuffer = new byte[10000];
			// ...
			while (true) {
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				socket.receive(packet);
				// ...

				try {
					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					AudioFormat audioFormat = getAudioFormat();
					audioInputStream = new AudioInputStream(byteInputStream, audioFormat,
							audioData.length / audioFormat.getFrameSize());
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					playAudio();
				} catch (Exception e) {
					// Handle exceptions
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/*
	*this method allows to a cliente make a bet 
	*with parameter the name of the horse
	*/
	public void apostar(String caballo, int apuesta) {
		nombreC=caballo;
		apuestaC=apuesta;
		writerC.println(caballo+" "+apuesta);
		panelCarrera.ponerNombre(caballo);
		
	}
	
	/*
	*This method shows the change of the position of the horses during the race 
	*/
	public void mostrar() {
		

		int[] posicion=new int[] {0,0,0,0,0,0};
		panelCarrera.ponerPosicion(posicion);
		panelCarrera.repaint();
		this.repaint();
		
		posicion[0]=Integer.parseInt(caballo[0]);
		posicion[1]=Integer.parseInt(caballo[1]);
		posicion[2]=Integer.parseInt(caballo[2]);
		posicion[3]=Integer.parseInt(caballo[3]);
		posicion[4]=Integer.parseInt(caballo[4]);
		posicion[5]=Integer.parseInt(caballo[5]);
		
		
	}
	/*
	*this class is a thread that play the audio of the narration
	*of the race
	*/
	public static class HiloVoz extends Thread{


		public HiloVoz() {

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			initiateAudio();

		}
	}
	
	/*
	*this method shows the horse that won the race
	*receive as a parameter the name of the horse that won
	*/
	public void mostrarGanador(String[] h) {
		if(nombreC.equals(h)) {
			ganador="Si";
		}else {
			ganador="No";
		}
		escribirArchivo();
		JOptionPane.showMessageDialog(this, h, "Ganador Carrera",JOptionPane.INFORMATION_MESSAGE);	
	}
	
	}

	





