/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package clientebj;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaEspera.
 */
public class VentanaEspera extends JInternalFrame {
	private JLabel enEspera, jugador;
	
	/**
	 * Instantiates a new ventana espera.
	 *
	 * @param jugador the jugador
	 */
	public VentanaEspera(String jugador) {
        initInternalFrame(jugador);
		
		this.setTitle("Bienvenido a la sala de espera");
		this.pack();
		this.setResizable(true);
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inits the internal frame.
	 *
	 * @param idJugador the id jugador
	 */
	private void initInternalFrame(String idJugador) {
		// TODO Auto-generated method stub
		this.getContentPane().setLayout(new FlowLayout());
		
		jugador = new JLabel(idJugador);
		Font font = new Font(Font.DIALOG,Font.BOLD,15);
		jugador.setFont(font);
		jugador.setForeground(Color.BLUE);
		add(jugador);
		enEspera = new JLabel();
		enEspera.setText("debes esperar al otro jugador...");
		enEspera.setFont(font);
		add(enEspera);
	}
	
	/**
	 * Cerrar sala espera.
	 */
	public void cerrarSalaEspera() {
		this.dispose();
	}

}
