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
	
		
//		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
//		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
//		
		Socket client= new Socket("localhost", 5555);
		Cliente cliente= new Cliente();
		cliente.setVisible(true);
		
//		Socket client =  sf.createSocket("localhost", 26000); 
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
		
							
//		
//		byte [] dato = new byte [1024];
//
//		
//		DatagramPacket dgp = new DatagramPacket(dato, dato.length);
//		escucha.receive(dgp);
//
//		
//		dato = dgp.getData();
//		
//		String p= new String(dato,"UTF-8");
//		System.out.println(p);
		
		
		
		
//		Scanner lector= new Scanner(System.in);
//		
//		System.out.println("Escriba el numero del caballo [espacio]  cantidad");
//		
//		String linea = lector.nextLine();
//		writerC.println(linea);
//		
//		String termino= readerC.readLine();
//		System.out.println(termino);
//		while(!termino.equals("termino")) {
//			
//			
//			linea = lector.nextLine(); 
//			writerC.println(linea);
//			termino= readerC.readLine();
//			System.out.println(termino);
//
//			 
//		}
//		System.out.println(termino);
		
		
	}
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
	
	private static AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

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


	public void apostar(String caballo, int apuesta) {
		nombreC=caballo;
		apuestaC=apuesta;
		writerC.println(caballo+" "+apuesta);
		panelCarrera.ponerNombre(caballo);
		
	}
	
	
	public void mostrar() {
		
//		System.out.println(caballo[0]+" "+caballo[1]+" "+caballo[2]+" "+caballo[3]+" "+caballo[4]+" "+caballo[5]+"");
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
		
		
//		for (int i = 0; i < caballo.length; i++) {
//			posicion[i]=Integer.parseInt(caballo[i]);
//		}
	}
	
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

	





