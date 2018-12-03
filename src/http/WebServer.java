package http;

import java.io.IOException;
import java.net.*;

public class WebServer extends Thread {
	
	public WebServer()
	{
		
		
	}
	
	
	
	
	
	/*
	*this method create a webServer and manage the HTTP requests that are made through the web page
	*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		super.run();
		System.out.println("Webserver Started");
		try {
			ServerSocket serverSocket =  new ServerSocket(80);
			while(true) 
			{
				System.out.println("Waiting for the client request");
				Socket remote = serverSocket.accept();
				System.out.println("Connection made");
				new Thread(new ClientHandler(remote)).start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
