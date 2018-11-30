package modelo;

public class Caballo {

	
	private String nombre;
	private int[] posicion;
	private boolean isMoving;
	private int apuesta;
	
	

	public Caballo(String nombre, int y) {

		this.nombre=nombre;
		posicion=new int[] {0,y};
		isMoving=true;
		apuesta=0;
	
	}
	
	public int getApuesta() {
		return apuesta;
	}

	public void setApuesta(int apuesta) {
		this.apuesta = apuesta;
	}

	public String getNombre() {
		return nombre;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public int[] getPosicion() {
		return posicion;
	}
	
	
	public void setPosicion(int[] posicion) {
		this.posicion = posicion;
	}

	public boolean isMoving() {
		return isMoving;
	}
	
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	
	public boolean isParar() {
		
		if(this.posicion[0]==1000) {
			isMoving=false;
			
		}
		
		return isMoving;
	}
	
	
	
	
}
