package conexion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import modelo.Caballo;

public class ServidorHilo extends Thread{

	
	private Socket cliente;
	private boolean tiempoTermino;
	private static Caballo[] caballos;
	BufferedReader readerS;
	private boolean termino;
	
	public ServidorHilo(Socket c,Caballo[] caballos) {
		super();
		cliente = c;
		this.caballos=caballos;
		termino=false;
		
	}
	
	@Override
	public void run() {
		
		try {
			readerS = new BufferedReader(new InputStreamReader(cliente.getInputStream())); 
			PrintWriter writerS= new PrintWriter(cliente.getOutputStream(),true);
			String linea2="";
			
			
			while(((linea2=readerS.readLine())!=null)&&!termino) {
				
				
				
				System.out.println(termino);
				String[] linea = linea2.split(" ");
				
//				int[] apuesta = new int[2];
//				
//				apuesta[0]= Integer.parseInt(linea[0]);
//				apuesta[1]= Integer.parseInt(linea[1]);
				
				if(linea[0].equals(caballos[0].getNombre()))
					caballos[0].setApuesta(caballos[0].getApuesta()+Integer.parseInt(linea[1]));
				else if(linea[0].equals(caballos[1].getNombre()))
					caballos[1].setApuesta(caballos[4].getApuesta()+Integer.parseInt(linea[1]));
				else if(linea[0].equals(caballos[2].getNombre()))
					caballos[2].setApuesta(caballos[2].getApuesta()+Integer.parseInt(linea[1]));
				else if(linea[0].equals(caballos[3].getNombre()))
					caballos[3].setApuesta(caballos[3].getApuesta()+Integer.parseInt(linea[1]));
				else if(linea[0].equals(caballos[4].getNombre()))
					caballos[4].setApuesta(caballos[4].getApuesta()+Integer.parseInt(linea[1]));
				else if(linea[0].equals(caballos[5].getNombre())) 
					caballos[5].setApuesta(caballos[5].getApuesta()+Integer.parseInt(linea[1]));
				
				
				writerS.println("Su apuesta ha sido aceptada");
			}
			
			writerS.println("termino");
			readerS.close();
			writerS.close();
//			cliente.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		
	}

	public void setTermino(boolean b) {
		// TODO Auto-generated method stub
		termino=b;
	}
}
