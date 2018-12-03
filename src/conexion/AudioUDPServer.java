package conexion;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;


/*
*this class it allows sending an audio through UDP  
*
*
*/
public class AudioUDPServer {

	private final byte audioBuffer[] = new byte[10000];
	private TargetDataLine targetDataLine;

	/*
	*this method create a AudioUDPServer 
	*/
	public AudioUDPServer() {
		// TODO Auto-generated constructor stub

		setupAudio();
		broadcastAudio();
	}
	
	/*
	*this method return a AudioFormat 
	*/

	private AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/*
	*This method establish contection with AudioUDPCLient to play the song
	*through adress 230.0.0.0 creating a group adress 
	*/
	private void broadcastAudio() {
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
	*this method is in charge of set up the audio 
	*/
	private void setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new AudioUDPServer();
	}

}
