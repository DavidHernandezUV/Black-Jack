/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */

package clientebj;


import java.awt.SystemColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;


import comunes.DatosBlackJack;

// TODO: Auto-generated Javadoc
/**
 * The Class ClienteBlackJack.
 * 
 */

public class ClienteBlackJack extends JFrame implements Runnable {
	// Constantes de Interfaz Grafica
	public static final int WIDTH = 670;
	public static final int HEIGHT = 550;

	// Constantes de conexión con el Servidor BlackJack
	public static final int PUERTO = 7377;
	public static final String IP = "127.0.0.1";

	// variables de control del juego
	private String idYo, otroJugador, ultimoJugador;
	private int capitalYo, capitalOtroJugador, capitalUltimoJugador;
	private boolean turno;
	private DatosBlackJack datosRecibidos;
	// Apuesta inicial
	private int capital=1000;

	// variables para manejar la conexión con el Servidor BlackJack
	private Socket conexion;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	// Componentes Graficos
	private JDesktopPane containerInternalFrames;
	private VentanaEntrada ventanaEntrada;
	private VentanaEspera ventanaEspera;
	private VentanaSalaJuego ventanaSalaJuego;
	
	private boolean repetirRonda = true;

	/**
	 * Instantiates a new cliente black jack.
	 */
	public ClienteBlackJack() {
		initGUI();

		// default window settings
		this.setTitle("Juego BlackJack");
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Inits the GUI.
	 */
	private void initGUI() {

		turno = false;
		// Set up JComponents
		this.setBackground(SystemColor.activeCaption);
		containerInternalFrames = new JDesktopPane();
		containerInternalFrames.setOpaque(false);
		this.setContentPane(containerInternalFrames);
		adicionarInternalFrame(new VentanaEntrada(this));
	}

	/**
	 * Gets the desktop width.
	 *	
	 * @return the desktop width
	 */
	public int getDesktopWidth() {

		return containerInternalFrames.getWidth();
	}

	/**
	 * Gets the desktop height.
	 *
	 * @return the desktop height
	 */
	public int getDesktopHeight() {

		return containerInternalFrames.getHeight();
	}

	/**
	 * Adicionar internal frame.
	 *
	 * @param nuevoInternalFrame the nuevo internal frame
	 */
	public void adicionarInternalFrame(JInternalFrame nuevoInternalFrame) {
		add(nuevoInternalFrame);
	}

	/**
	 * Iniciar hilo.
	 */
	public void iniciarHilo() {
		ExecutorService hiloCliente = Executors.newFixedThreadPool(1);
		hiloCliente.execute(this);
	}

	/**
	 * Sets the id yo.
	 *
	 * @param id the new id yo
	 */
	public void setIdYo(String id) {
		idYo = id;
	}

	/**
	 * Mostrar mensajes.
	 *
	 * @param mensaje the mensaje
	 */
	private void mostrarMensajes(String mensaje) {
		System.out.println(mensaje);
	}

	/**
	 * Enviar mensaje servidor.
	 *
	 * @param mensaje the mensaje
	 */
	public void enviarMensajeServidor(String mensaje) {
		try {
			out.writeObject(mensaje);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Enviar apuesta servidor.
	 *
	 * @param apuesta the apuesta
	 */
	public void enviarApuestaServidor(int apuesta) {
		try {
			out.writeObject(apuesta);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Buscar servidor.
	 */
	public void buscarServidor() {
		mostrarMensajes("Jugador buscando al servidor...");

		try {
			// buscar el servidor
			conexion = new Socket(IP, PUERTO);
			// obtener flujos E/S
			out = new ObjectOutputStream(conexion.getOutputStream());
			out.flush();
			in = new ObjectInputStream(conexion.getInputStream());

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mostrarMensajes("Jugador conectado al servidor");
		mostrarMensajes("Jugador estableció Flujos E/S");
		// mandar nombre jugador
		mostrarMensajes("Jugador envio nombre " + idYo);
		enviarMensajeServidor(idYo);
		//capital-=10;
		enviarApuestaServidor(capital);
		// procesar comunicación con el ServidorBlackJack
		iniciarHilo();
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {
		while(repetirRonda) {
			// datosRecibidos = new DatosBlackJack();
			// TODO Auto-generated method stub
			// mostrar bienvenida al jugador
			System.out.println("Cliente " + idYo + " iniciando ronda");
			datosRecibidos = new DatosBlackJack();
			try {
				
				System.out.println("Cliente " + idYo + " esperando lectura");
				datosRecibidos = (DatosBlackJack) in.readObject();
				
				// lee los datos con los que construye la mesa
				if (datosRecibidos.getIdJugadores()[0].equals(idYo)) {
					capitalYo = datosRecibidos.getCapitalJugador1();

					otroJugador = datosRecibidos.getIdJugadores()[1];
					capitalOtroJugador = datosRecibidos.getCapitalJugador2();

					ultimoJugador = datosRecibidos.getIdJugadores()[2];
					capitalUltimoJugador = datosRecibidos.getCapitalJugador3();
					turno = true;
				} else if (datosRecibidos.getIdJugadores()[1].equals(idYo)) {
					capitalYo = datosRecibidos.getCapitalJugador2();

					otroJugador = datosRecibidos.getIdJugadores()[0];
					capitalOtroJugador = datosRecibidos.getCapitalJugador1();

					ultimoJugador = datosRecibidos.getIdJugadores()[2];
					capitalUltimoJugador = datosRecibidos.getCapitalJugador3();
				} else { // Yo estoy en la posición 2
					capitalYo = datosRecibidos.getCapitalJugador3();

					otroJugador = datosRecibidos.getIdJugadores()[0];
					capitalOtroJugador = datosRecibidos.getCapitalJugador1();

					ultimoJugador = datosRecibidos.getIdJugadores()[1];
					capitalUltimoJugador = datosRecibidos.getCapitalJugador2();

				}

				this.habilitarSalaJuego(datosRecibidos);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// buscando nombre del OtroJugador

			// procesar turnos

			while (true) {
				try {
					datosRecibidos = new DatosBlackJack();
					datosRecibidos = (DatosBlackJack) in.readObject();
					
					mostrarMensajes("Cliente hilo run recibiendo mensaje servidor ");
					ventanaSalaJuego.pintarTurno(datosRecibidos);
					
					if (!datosRecibidos.isEnJuego()) {
						//Activar el botón de reinicio
						ventanaSalaJuego.activarBotonOtraRonda(true);
						
						break;
					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	/**
	 * Habilitar sala juego.
	 * Método encargado de habilitar la sala de juego, para poder jugar
	 * @param datosRecibidos the datos recibidos
	 */
	private void habilitarSalaJuego(DatosBlackJack datosRecibidos) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ventanaEspera = (VentanaEspera) containerInternalFrames.getComponent(0);
				ventanaEspera.cerrarSalaEspera();
				ventanaSalaJuego = new VentanaSalaJuego(idYo, capitalYo, otroJugador, capitalOtroJugador,
						ultimoJugador, capitalUltimoJugador, getDesktopWidth(), getDesktopHeight());
				ventanaSalaJuego.pintarCartasInicio(datosRecibidos);
				adicionarInternalFrame(ventanaSalaJuego);
				if (turno) {
					ventanaSalaJuego.activarBotones(turno);
				}
			}

		});
	}

	/**
	 * Reiniciar juego.
	 * Método encargado de reiniciar el juego, para poder jugar más rondas
	 */
	public void reiniciarJuego() {
		
		ventanaSalaJuego.cerrarVentanaSalaJuego();
		ventanaEspera = new VentanaEspera(idYo);
		add(ventanaEspera);
		repetirRonda = true;
		//Avisando al servidor
		enviarMensajeServidor(idYo);
	}
	
	/**
	 * Cerrar conexion.
	 * Cierra la conexión con el servidor
	 */
	public void cerrarConexion() {
		// TODO Auto-generated method stub
		try {
			in.close();
			out.close();
			conexion.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets the turno.
	 *
	 * @param turno the new turno
	 */
	public void setTurno(boolean turno) {
		this.turno = turno;
	}
}
