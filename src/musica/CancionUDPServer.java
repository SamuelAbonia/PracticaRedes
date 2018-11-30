package musica;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CancionUDPServer extends Thread{


	//	public static void main(String[] args) throws IOException {}

	public CancionUDPServer() {
		
	}

	public void run() {
		File soundFile = AudioUtil.getSoundFile("./Data/cancion.wav");

		System.out.println("server: " + soundFile);

		try (ServerSocket serverSocker = new ServerSocket(6666); 
				FileInputStream in = new FileInputStream(soundFile)) {
			if (serverSocker.isBound()) {
				Socket client = serverSocker.accept();
				OutputStream out = client.getOutputStream();

				byte buffer[] = new byte[20480];
				int count;
				while ((count = in.read(buffer)) != -1)
					out.write(buffer, 0, count);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("server: shutdown");
	}
}