package modelo;

import java.util.ArrayList;
import java.util.Random;


public class HiloCaballo extends Thread{

	private Caballo caballo;
	private ArrayList<Caballo> ganador;
	
	public HiloCaballo(Caballo caballo, ArrayList<Caballo> gan) {
		this.caballo=caballo;
		ganador=gan;
		
	}
	
	/*
	*this method is in charge of change the position of a horse, generating a random number
	*/
	@Override
	public void run() {
		super.run();
		
//		caballo.setMoving(true);
		int numero=0;
		while(numero<1000) {
			
			try {
			this.sleep(1000);
			caballo.setPosicion(new int[] {(int) (caballo.getPosicion()[0]+((Math.random()*50)+10)),caballo.getPosicion()[1]});
			numero=caballo.getPosicion()[0];
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ganador.add(caballo);
		
	}
	
	
}
