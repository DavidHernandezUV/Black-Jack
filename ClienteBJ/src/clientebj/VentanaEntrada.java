/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package clientebj;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaEntrada.
 */
public class VentanaEntrada extends JInternalFrame {
	
	private JLabel bienvenida, labelNombre;
	private JPanel ingreso;
	private JTextField nombreJugador;
	private JButton ingresar;
	private VentanaEspera ventanaEspera;
	private ClienteBlackJack cliente;
	
	private Escucha escucha;
	
	/**
	 * Instantiates a new ventana entrada.
	 *
	 * @param cliente the cliente
	 */
	public VentanaEntrada(ClienteBlackJack cliente) {
		this.cliente = cliente;
		initInternalFrame();
		
		this.setTitle("Bienvenido a Black Jack");
		this.pack();
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inits the internal frame.
	 */
	private void initInternalFrame() {
		// TODO Auto-generated method stub
		escucha = new Escucha();
		this.getContentPane().setLayout(new BorderLayout());
		bienvenida = new JLabel("Registre su nombre para ingresar");
		add(bienvenida, BorderLayout.NORTH);

		ingreso = new JPanel(); 
		labelNombre= new JLabel("Nombre"); 
		nombreJugador =	new JTextField(10); 
		ingresar = new JButton("Ingresar");
		ingresar.addActionListener(escucha);
		ingreso.add(labelNombre); ingreso.add(nombreJugador); ingreso.add(ingresar);
		add(ingreso,BorderLayout.CENTER);
	}
	
	/**
	 * Gets the container frames.
	 *
	 * @return the container frames
	 */
	private Container getContainerFrames() {
		return this.getParent();
	}
 
	/**
	 * Cerrar ventana entrada.
	 */
	private void cerrarVentanaEntrada() {
		this.dispose();
	}
	
	/**
	 * The Class Escucha.
	 */
	private class Escucha implements ActionListener{
		
		/**
		 * Action performed.
		 *
		 * @param arg0 the arg 0
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//cargar Sala de Espera y cerrar Ventana Entrada
			if(nombreJugador.getText().length()==0) {
				JOptionPane.showMessageDialog(null, "Debes ingresar un nombre para identificarte!!");
			}else {
				cliente.setIdYo(nombreJugador.getText());
				ventanaEspera = new VentanaEspera(nombreJugador.getText());
				getContainerFrames().add(ventanaEspera);
				//BUSCA AL SERVIDOR AL HACER CLICK
				cliente.buscarServidor();
                cerrarVentanaEntrada();
			}	
		}
	}
	

}
