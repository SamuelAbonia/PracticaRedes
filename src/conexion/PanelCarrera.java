package conexion;

import javax.swing.JPanel;

	import java.awt.Color;
	import java.awt.Graphics;
	import java.awt.Graphics2D;

	import javax.swing.ImageIcon;
	import javax.swing.JPanel;



	public class PanelCarrera extends JPanel{
		
		private Cliente principal;
		private int[] posicion;
		private String caballo;
		
		public PanelCarrera(Cliente cliente) {
		principal=cliente;	
		posicion= new int[] {0,0,0,0,0,0};
		caballo="";
		
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			
//			Graphics2D p= (Graphics2D) g;
//			p.setBackground(Color.BLACK);
//			ImageIcon a= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(a.getImage(), posicion[0], 0, this);
//			ImageIcon b= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(b.getImage(), posicion[1], 70, this);
//			ImageIcon c= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(c.getImage(), posicion[2], 140, this);
//			ImageIcon d= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(d.getImage(), posicion[3], 210, this);
//			ImageIcon e= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(e.getImage(), posicion[4], 280, this);
//			ImageIcon f= new ImageIcon("./Data/caballo_2.gif");
//			p.drawImage(f.getImage(), posicion[5], 350, this);
			
			Graphics2D p= (Graphics2D) g;
			p.setBackground(Color.BLACK);
			ImageIcon a= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(a.getImage(), posicion[0], 0, this);
			
			if(caballo.equals("Spirit")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[0]+20, 0, 20, 20);
				p.fillOval(posicion[0]+20, 0, 20, 20);
				
			}
			ImageIcon b= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(b.getImage(), posicion[1], 70, this);
			if(caballo.equals("Rocinante")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[1]+20, 70, 20, 20);
				p.fillOval(posicion[1]+20, 70, 20, 20);
				
			}
			ImageIcon c= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(c.getImage(), posicion[2], 140, this);
			if(caballo.equals("Pegaso")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[2]+20, 140, 20, 20);
				p.fillOval(posicion[2]+20,  140, 20, 20);
				
			}
			ImageIcon d= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(d.getImage(), posicion[3], 210, this);
			if(caballo.equals("la maquina")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[3]+20, 210, 20, 20);
				p.fillOval(posicion[3]+20, 210, 20, 20);
			
			}
			ImageIcon e= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(e.getImage(), posicion[4], 280, this);
			if(caballo.equals("DeTroya")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[4]+20, 280, 20, 20);
				p.fillOval(posicion[4]+20, 280, 20, 20);
				
			}
			ImageIcon f= new ImageIcon("./Data/caballo_2.gif");
			p.drawImage(f.getImage(), posicion[5], 350, this);
			if(caballo.equals("Tornado")) {
				p.setColor(Color.blue);
				p.drawOval(posicion[5]+20, 350, 20, 20);
				p.fillOval(posicion[5]+20, 350, 20, 20);
				
			}
			
			
			
		}
		
		public void ponerPosicion(int[] pos) {
			posicion=pos;
			
			
		}

		
		public void ponerNombre(String nombre) {
			this.caballo=nombre;
		}
	}
