package musica;	
	import java.io.*;
	import java.net.*;
	import javax.sound.sampled.*;


	public class CancionUDPCliente  extends Thread{
		
		public CancionUDPCliente() {
			
		}
		
	   // public static void main(String[] args) throws Exception {
		
		public void run() {
	        
	           
	            // play soundfile from server
	            System.out.println("Client: reading from 127.0.0.1:6666");
	            try (Socket socket = new Socket("127.0.0.1", 6666)) {
	            	while(true) {
	                if (socket.isConnected()) {
	                    InputStream in = new BufferedInputStream(socket.getInputStream());
	                    play(in);
	                }
	                }
	            }catch (Exception e) {
	            	
	            }
	        

	        System.out.println("Client: end");
	    }


	    private  synchronized void play(final InputStream in) throws Exception {
	        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
	        try (Clip clip = AudioSystem.getClip()) {
	            clip.open(ais);
	            clip.start();
	            Thread.sleep(100); // given clip.drain a chance to start
	            clip.drain();
	        }
	    }
	}
	
