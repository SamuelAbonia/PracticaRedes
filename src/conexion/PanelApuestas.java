package conexion;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelApuestas extends JPanel implements ActionListener{
	
	
	private JLabel lbApuesta;
	private JTextField txtApuesta;
	private JButton btApostar;
	private JComboBox<String> caballo;
	private Cliente principal;
	private JLabel lbCaballo;

	public PanelApuestas(Cliente i) {
		principal=i;
		
		
		lbApuesta= new JLabel("Ingrese su apuesta");
		txtApuesta= new JTextField();
		btApostar=  new JButton("Apostar");
		btApostar.setActionCommand("apostar");
		btApostar.addActionListener(this);
		lbCaballo= new JLabel("Seleccione un caballo");
		caballo= new JComboBox<String>(new String[] {"Spirit","Rocinante","Pegaso","la maquina","DeTroya","Tornado"});
		
		
		setLayout(new GridLayout(1, 5));
		add(lbCaballo);
		add(caballo);
		add(lbApuesta);
		add(txtApuesta);
		add(btApostar);
		
	}

	public void desabilidarCaballo() {
		caballo.setEnabled(false);
	}
	public int apuesta() {
		return Integer.parseInt(txtApuesta.getText());
	}
	
	public String caballo() {
		return (String) caballo.getSelectedItem();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	String comando= e.getActionCommand();
	if(comando.equals("apostar")) {
		principal.apostar(caballo(), apuesta());
		caballo.setEditable(false);
		btApostar.setEnabled(false);
	}
		
	}
}
