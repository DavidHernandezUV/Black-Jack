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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import comunes.DatosBlackJack;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaSalaJuego.
 */
public class VentanaSalaJuego extends JInternalFrame {

	private PanelJugador dealer, yo, jugador2, jugador3;
	private JTextArea areaMensajes;
	private JButton pedir, plantar, otraRonda;
	private JPanel panelYo, panelBotones, yoFull, panelDealer, panelJugador2, panelJugador3, panelOtraRonda;

	private String yoId, jugador2Id, jugador3Id;
	private Escucha escucha;

	/**
	 * Instantiates a new ventana sala juego.
	 *
	 * @param yoId the yo id
	 * @param capitalYo the capital yo
	 * @param jugador2Id the jugador 2 id
	 * @param capital2 the capital 2
	 * @param jugador3Id the jugador 3 id
	 * @param capital3 the capital 3
	 * @param desktop_Width the desktop width
	 * @param desktop_height the desktop height
	 */
	public VentanaSalaJuego(String yoId,int capitalYo, String jugador2Id,int capital2, String jugador3Id, int capital3, int desktop_Width,int desktop_height) {
		this.yoId = yoId;
		this.jugador2Id = jugador2Id;
		this.jugador3Id = jugador3Id;
		this.setBackground(Color.GREEN);
		
		initGUI(capitalYo, capital2,capital3);

		// default window settings
		this.setTitle("Sala de juego BlackJack - Jugador: " + yoId);
		
		this.pack();
		this.setLocation((desktop_Width/2) - this.getWidth()/2, (desktop_height/2) - this.getHeight()/2);
		this.setResizable(false);
		this.show();
	}

	/**
	 * Inits the GUI.
	 *	método encargado de inicializar los gráficos de la ventana sala juego
	 * @param capitalYo the capital yo
	 * @param capital2 the capital 2
	 * @param capital3 the capital 3
	 */
	private void initGUI(int capitalYo,int capital2,int capital3) {
		// TODO Auto-generated method stub
		// set up JFrame Container y Layout
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		// Create Listeners objects
		escucha = new Escucha();
		// Create Control objects

		// Set up JComponents
		panelDealer = new JPanel();
		dealer = new PanelJugador("Dealer",10000);
		panelDealer.setBackground(Color.GREEN);
		panelDealer.add(dealer);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(panelDealer, constraints);

		panelJugador2 = new JPanel();
		jugador2 = new PanelJugador(jugador2Id,capital2);
		panelJugador2.setBackground(Color.GREEN);
		panelJugador2.add(jugador2);
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(panelJugador2, constraints);

		panelJugador3 = new JPanel();
		jugador3 = new PanelJugador(jugador3Id,capital3);
		panelJugador3.setBackground(Color.GREEN);
		panelJugador3.add(jugador3);
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(panelJugador3, constraints);

		areaMensajes = new JTextArea(8, 18);
		JScrollPane scroll = new JScrollPane(areaMensajes);
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(blackline, "Area de Mensajes");
		bordes.setTitleJustification(TitledBorder.CENTER);
		scroll.setBorder(bordes);
		areaMensajes.setOpaque(false);
		areaMensajes.setBackground(Color.WHITE);
		areaMensajes.setEditable(false);

		scroll.getViewport().setOpaque(false);
		//scroll.setOpaque(false);
		scroll.setBackground(Color.WHITE);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(scroll, constraints);

		panelYo = new JPanel();
		panelYo.setLayout(new BorderLayout());
		yo = new PanelJugador(yoId,capitalYo);
		panelYo.setBackground(Color.GREEN);
		panelYo.add(yo);

		pedir = new JButton("Carta");
		pedir.setEnabled(false);
		pedir.addActionListener(escucha);
		plantar = new JButton("Plantar");
		plantar.setEnabled(false);
		plantar.addActionListener(escucha);
		panelBotones = new JPanel();
		panelBotones.setBackground(Color.GREEN);
		panelBotones.add(pedir);
		panelBotones.add(plantar);

		yoFull = new JPanel();
		yoFull.setPreferredSize(new Dimension(206, 180));
		yoFull.setBackground(Color.GREEN);
		yoFull.add(panelYo);
		yoFull.add(panelBotones);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(yoFull, constraints);
		//Botón para jugar otra ronda al lado derecho inferior e inicia deshabilitado
		otraRonda = new JButton("Otra ronda");
		otraRonda.addActionListener(escucha);
		otraRonda.setEnabled(false);
		panelOtraRonda = new JPanel(new BorderLayout());
		panelOtraRonda.add(otraRonda, BorderLayout.EAST);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		add(panelOtraRonda, constraints);
			
	}

	/**
	 * Activar botones.
	 *
	 * @param turno the turno
	 */
	public void activarBotones(boolean turno) {
		pedir.setEnabled(turno);
		plantar.setEnabled(turno);
	}
	
	/**
	 * Activar boton otra ronda.
	 *
	 * @param activar the activar
	 */
	//Activa o desactiva el botón para jugar otra ronda 
	public void activarBotonOtraRonda(boolean activar) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				otraRonda.setEnabled(activar);
			}
			
		});
	}
	
	/**
	 * Pintar cartas inicio.
	 *	Función encargada de pintar las manos iniciales de los jugadores
	 * @param datosRecibidos the datos recibidos
	 */
	// Se llama cuando se crea la sala de juego por primera vez
	public void pintarCartasInicio(DatosBlackJack datosRecibidos) {
		//MENSAJE: Le están llegando en el orden incorrecto al reiniciar
		if (datosRecibidos.getIdJugadores()[0].equals(yoId)) {// Si yo estoy en la posición 0
			yo.pintarCartasInicio(datosRecibidos.getManoJugador1());// pinte cartas de inicio del jugador yo
			jugador2.pintarCartasInicio(datosRecibidos.getManoJugador2());// pinte las cartas del jugador 2
			jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
		} else if (datosRecibidos.getIdJugadores()[1].equals(yoId)) { // si yo estoy en la posición 1
			yo.pintarCartasInicio(datosRecibidos.getManoJugador2()); 
			jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
			jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
		} else { // Si yo estoy en la posición 2
			yo.pintarCartasInicio(datosRecibidos.getManoJugador3());
			jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
			jugador3.pintarCartasInicio(datosRecibidos.getManoJugador2());
		}
		dealer.pintarCartasInicio(datosRecibidos.getManoDealer());

		areaMensajes.append(datosRecibidos.getMensaje() + "\n");
	}

	/**
	 * Pintar turno.
	 *	Método encargado de pintar lo necesario en el turno de juego
	 * @param datosRecibidos the datos recibidos
	 */
	public void pintarTurno(DatosBlackJack datosRecibidos) {
		areaMensajes.append(datosRecibidos.getMensaje() + "\n");
		ClienteBlackJack cliente = (ClienteBlackJack) this.getTopLevelAncestor();//

		if(datosRecibidos.isEnJuego()) {
			if (datosRecibidos.getJugador().equals(yoId)) {
				// Si me manda "iniciar" activo botones
				if (datosRecibidos.getJugadorEstado().equals("iniciar")) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							activarBotones(true);
						}
					});
				} // Si me manda "plantó" desactivo botones
				else if (datosRecibidos.getJugadorEstado().equals("plantó")) {
					cliente.setTurno(false);
				} else { // Pintar la carta porque no inicié ni planté
					yo.pintarLaCarta(datosRecibidos.getCarta());
					// Si me manda "voló", desactivar botones y ceder turno
					if (datosRecibidos.getJugadorEstado().equals("voló")) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								activarBotones(false);
								cliente.setTurno(false);
							}
						});
					}
				}
	
			} else if (datosRecibidos.getJugador().equals(jugador2Id)) {
				// mensaje para PanelJuego jugador2
				// Si sigue o vuela, pinta la carta
				// D: Y SI PLANTÓ???
				if (datosRecibidos.getJugadorEstado().equals("sigue") || datosRecibidos.getJugadorEstado().equals("voló")) {
					jugador2.pintarLaCarta(datosRecibidos.getCarta());
				}
			} else if (datosRecibidos.getJugador().equals(jugador3Id)) {
				if (datosRecibidos.getJugadorEstado().equals("sigue") || datosRecibidos.getJugadorEstado().equals("voló")) {
					jugador3.pintarLaCarta(datosRecibidos.getCarta());
				}
			} else {
				// mensaje para PanelJuego dealer
				if (datosRecibidos.getJugadorEstado().equals("sigue") || datosRecibidos.getJugadorEstado().equals("voló")
						|| datosRecibidos.getJugadorEstado().equals("plantó")) {
					dealer.pintarLaCarta(datosRecibidos.getCarta());
				}
	
			}
		}
	}
	
	/**
	 * Enviar datos.
	 * Método encargado de enviarle datos al cliente
	 * @param mensaje the mensaje
	 */
	private void enviarDatos(String mensaje) {
		// TODO Auto-generated method stub
		ClienteBlackJack cliente = (ClienteBlackJack) this.getTopLevelAncestor();
		cliente.enviarMensajeServidor(mensaje);
	}
	
	/**
	 * Cierra la ventana sala juego eliminando todos sus componentes
	 * Cerrar ventana sala juego.
	 */
	public void cerrarVentanaSalaJuego() {
		this.dispose();
	}
	
	/**
	 * Gets the container frames.
	 *
	 * @return the container frames
	 */
	public Container getContainerFrames() {
		return this.getParent();
	}
    
	/**
	 * The Class Escucha.
	 */
	private class Escucha implements ActionListener {

		/**
		 * Action performed.
		 *
		 * @param actionEvent the action event
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// TODO Auto-generated method stub
			if (actionEvent.getSource() == pedir) {
				// enviar pedir carta al servidor
				enviarDatos("pedir");
			} else if(actionEvent.getSource() == plantar){
				// enviar plantar al servidor
				enviarDatos("plantar");
				activarBotones(false);
			}  //Otra ronda
			else {
				ClienteBlackJack cliente = (ClienteBlackJack) getTopLevelAncestor();
				int opcion = JOptionPane.showConfirmDialog(null, "Desea jugar otra vez?", "Jugar otra ronda",
						JOptionPane.YES_NO_OPTION);
				
				if (opcion == JOptionPane.YES_OPTION) {
					
					cliente.reiniciarJuego();
				}else {
					cliente.cerrarConexion();
				}
			}
		}
	}
}
