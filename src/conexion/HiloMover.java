package conexion;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class HiloMover extends Thread {

	MulticastSocket escucha;
	String[] caballo;
	private Cliente cliente;
	
	
	public HiloMover(MulticastSocket escucha, Cliente cliente, String[] caballo) {
		// TODO Auto-generated constructor stub
		this.escucha=escucha;
		this.caballo=caballo;
		this.cliente=cliente;
		
	}
	
	
	@Override
	public void run() {
		
		while(true) {
			
			try {
				
			byte[] dato = new byte [1024];

			
			DatagramPacket pgc = new DatagramPacket(dato, dato.length);
			escucha.receive(pgc);

			
			dato = pgc.getData();
			
			String p= new String(dato,"UTF-8");
			
			String[] h=p.split(",");
			if(h.length>1) {
				cliente.setCaballos(h);	
				cliente.mostrar();
			}
			else {
				cliente.mostrarGanador(h);
			}
			
			
			}catch(Exception e) {
				
			}
		}
	}
	
	
	
}
