package conexion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

import javax.net.ssl.SSLServerSocketFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import http.WebServer;
import modelo.Caballo;
import modelo.HiloCaballo;
import musica.CancionUDPServer;


/*
*this class represents the server of the application
*/

public class Servidor {

	public static final String KEYSTORE_LOCATION = "C:/Windows/System32/keystore.jks";
	public static final String KEYSTORE_PASSWORD = "123456";

	private static Caballo[] caballos;
	private static ArrayList<ServidorHilo> clientes;
	private static final byte audioBuffer[] = new byte[10000];
	private static TargetDataLine targetDataLine;
	static private ArrayList<Caballo> listGanador;
	
	private static WebServer webServer;

	
	
	/*
	*this method is responsible for initializing the server,
	*creating the horses and giving them initial positions.
	*Bets are initialized according to the time it has been decided that the server will be running 
	*to receive bets, after this time the server closes the bet and begins the transmission of the race and the narration
	*/

	public static void main(String[] args) throws IOException {
		
		/*
		*if you want to use ssl you have to enable the following
		*four lines and disable the soket that has port 5555
		*/
		
//		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
//		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
//		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//		ServerSocket server= ssf.createServerSocket(26000);

		clientes= new ArrayList<ServidorHilo>();
		listGanador= new ArrayList<>();
		
		
		Caballo caballoGanador= null;
		

		Caballo caballo1= new Caballo("Spirit",20);
		Caballo caballo2= new Caballo("Rocinante",40);
		Caballo caballo3= new Caballo("Pegaso",20);
		Caballo caballo4= new Caballo("la maquina",20);
		Caballo caballo5= new Caballo("DeTroya",20);
		Caballo caballo6= new Caballo("Tornado",20);

		caballos = new Caballo[] {caballo1,caballo2,caballo3,caballo4,caballo5,caballo6};

		ServerSocket server= new ServerSocket(5555);
		
		long tiempoInicial =  System.currentTimeMillis();
		long fin=  System.currentTimeMillis();;


		HiloTiempo tiempo=new HiloTiempo();
		tiempo.start();
		
		CancionUDPServer cancion= new CancionUDPServer();
		cancion.start();
		
		webServer= new WebServer();
		webServer.start();
		
		
		while(fin-tiempoInicial<60000) {
		
		
			Socket cliente = server.accept();
			ServidorHilo servidorHiloCliente= new ServidorHilo(cliente,caballos);
			clientes.add(servidorHiloCliente);
			servidorHiloCliente.start();
			fin =  System.currentTimeMillis();


		}
		System.out.println("caballo 1 "+caballos[0].getApuesta()+"\n caballo 2 "+caballos[1].getApuesta()+"\n caballo 3 "+caballos[2].getApuesta()+"\n caballo 4 "+caballos[3].getApuesta()+
				"\n caballo 5 "+caballos[4].getApuesta()+"\n caballo 6 "+caballos[5].getApuesta());

		server.close();
		HiloVoz voz=new HiloVoz();
		voz.start();

		HiloCaballo hilo1= new HiloCaballo(caballo1, listGanador);
		HiloCaballo hilo2= new HiloCaballo(caballo2,listGanador);
		HiloCaballo hilo3= new HiloCaballo(caballo3,listGanador);
		HiloCaballo hilo4= new HiloCaballo(caballo4,listGanador);
		HiloCaballo hilo5= new HiloCaballo(caballo5,listGanador);
		HiloCaballo hilo6= new HiloCaballo(caballo6,listGanador);


		hilo1.start();
		hilo2.start();
		hilo3.start();
		hilo4.start();
		hilo5.start();
		hilo6.start();


		boolean termino=true;


		MulticastSocket socket = new MulticastSocket();
		InetAddress group = InetAddress.getByName("228.5.6.7");
		socket.joinGroup(group);



		int s= 0;
		while (listGanador.size()<6) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			termino=caballo1.isMoving()||caballo2.isMoving()||caballo3.isMoving()||
					caballo4.isMoving()||caballo5.isMoving()||caballo6.isMoving();

			String mensaje= caballo1.getPosicion()[0]+","+caballo2.getPosicion()[0]+","+caballo3.getPosicion()[0]
					+","+caballo4.getPosicion()[0]+","+caballo5.getPosicion()[0]+","+caballo6.getPosicion()[0]+",";

			byte[] dato = mensaje.getBytes();
			DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("228.5.6.7"), 40000);

			socket.send(dgp);

			
		}
		String gandor=listGanador.get(0).getNombre();
		System.out.println(gandor);
		byte[] dato= gandor.getBytes();

		DatagramPacket posiso= new DatagramPacket(dato, dato.length, group,40000);
		socket.send(posiso);
	}


	/*
	*This method return the Audioformat of the narration
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
	*this method allows the transmission of the audio through the adress 230.0.0.0  creating a adress group
	*/
	private static void broadcastAudio() {
		try {
			MulticastSocket socket = new MulticastSocket();
			InetAddress group = InetAddress.getByName("230.0.0.0");
			socket.joinGroup(group);
			//InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
			// ...
			while (true) {
				int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
				if (count > 0) {
					DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, group, 9786);
					socket.send(packet);
				}
			}

		} catch (Exception ex) {
			// Handle exceptions
		}
	}


	/*
	*this method set up the audio
	*/
	private static void setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (Exception ex) {
			
			System.exit(0);
		}
	}

	/*
	*this class allows the transmission of the narraiton through of a thread 
	*/
	public static class HiloVoz extends Thread{


		public HiloVoz() {

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			setupAudio();
			broadcastAudio();

		}
	}
	

}

