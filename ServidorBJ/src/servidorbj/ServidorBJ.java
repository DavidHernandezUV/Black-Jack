/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package servidorbj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import comunes.Baraja;
import comunes.Carta;
import comunes.DatosBlackJack;

// TODO: Auto-generated Javadoc
/**
 * The Class ServidorBJ. Clase encargada de realizar la gestión del juego, esto
 * es, el manejo de turnos y estado del juego. También gestiona al jugador
 * Dealer. El Dealer tiene una regla de funcionamiento definida: Pide carta con
 * 16 o menos y Planta con 17 o mas.
 */

public class ServidorBJ implements Runnable {
	// constantes para manejo de la conexion.
	public static final int PUERTO = 7377;
	public static final String IP = "127.0.0.1";
	public static final int LONGITUD_COLA = 3;

	// variables para funcionar como servidor
	private ServerSocket server;
	private Socket conexionJugador;

	// variables para manejo de hilos
	private ExecutorService manejadorHilos;
	private Lock bloqueoJuego;
	private Condition esperarInicio, esperarTurno, finalizar, esperarReinicio;
	private Jugador[] jugadores;

	// variables de control del juego
	private String[] idJugadores;
	private int[] capital = new int[4];
	private String[] estadosJugadores = new String[4];
	private int jugadorEnTurno;
	private int contador = 0;
	private int orden = 0;
	private int ronda = 0;
	private int capitalInicial = 1000;
	// private boolean iniciarJuego;
	private Baraja mazo;
	private ArrayList<ArrayList<Carta>> manosJugadores;
	private ArrayList<Carta> manoJugador1;
	private ArrayList<Carta> manoJugador2;
	private ArrayList<Carta> manoJugador3;
	private ArrayList<Carta> manoDealer;
	private int[] valorManos;
	private DatosBlackJack datosEnviar;
	private boolean seTerminoRonda = false;

	/**
	 * Instantiates a new servidor BJ. Inicializa las variables de control de ronda
	 * y manejo de hilos
	 */
	public ServidorBJ() {
		// inicializar variables de control del juego
		inicializarVariablesControlRonda();
		// inicializar las variables de manejo de hilos
		inicializareVariablesManejoHilos();
		// crear el servidor
		try {
			mostrarMensaje("Iniciando el servidor...");
			server = new ServerSocket(PUERTO, LONGITUD_COLA); // Establecer la instancia como servidor
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inicializar variables manejo hilos.
	 */
	private void inicializareVariablesManejoHilos() {
		// TODO Auto-generated method stub
		manejadorHilos = Executors.newFixedThreadPool(LONGITUD_COLA);
		bloqueoJuego = new ReentrantLock();
		esperarInicio = bloqueoJuego.newCondition();
		esperarTurno = bloqueoJuego.newCondition();
		esperarReinicio = bloqueoJuego.newCondition();
		finalizar = bloqueoJuego.newCondition();
		jugadores = new Jugador[LONGITUD_COLA];
	}

	/**
	 * Inicializar variables control ronda.
	 */
	private void inicializarVariablesControlRonda() {
		// TODO Auto-generated method stub
		// Variables de control del juego.

		idJugadores = new String[LONGITUD_COLA]; // Jugadores en juego sin contar el dealer, clientes
		valorManos = new int[LONGITUD_COLA + 1]; // 3 jugadores y 1 dealer

		mazo = new Baraja();
		Carta carta;

		// Creación de las manos
		manoJugador1 = new ArrayList<Carta>();
		manoJugador2 = new ArrayList<Carta>();
		manoJugador3 = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();

		// reparto inicial jugadores 1 y 2
		for (int i = 1; i <= 2; i++) {
			// jugador 1
			carta = mazo.getCarta();
			manoJugador1.add(carta);
			calcularValorMano(manoJugador1, carta, 0);
			// jugador 2
			carta = mazo.getCarta();
			manoJugador2.add(carta);
			calcularValorMano(manoJugador2, carta, 1);
			// jugador 3
			carta = mazo.getCarta();
			manoJugador3.add(carta);
			calcularValorMano(manoJugador3, carta, 2);
		}
		// Carta inicial Dealer
		carta = mazo.getCarta();
		manoDealer.add(carta);
		calcularValorMano(manoDealer, carta, 3);

		// gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
		manosJugadores = new ArrayList<ArrayList<Carta>>(LONGITUD_COLA + 1);// JUgadores y el dealer
		manosJugadores.add(manoJugador1);
		manosJugadores.add(manoJugador2);
		manosJugadores.add(manoJugador3);
		manosJugadores.add(manoDealer);
	}

	/**
	 * Reiniciar variables. Reinicia las variables para jugar una nueva ronda.
	 */
	private void reiniciarVariables() {
		estadosJugadores = new String[4];
		contador = 0;

		capital[0] -= 10;
		capital[1] -= 10;
		capital[2] -= 10;

		seTerminoRonda = false;
		valorManos = new int[LONGITUD_COLA + 1]; // 3 jugadores y 1 dealer

		mazo = new Baraja();
		Carta carta;

		// Creación de las manos
		manoJugador1 = new ArrayList<Carta>();
		manoJugador2 = new ArrayList<Carta>();
		manoJugador3 = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();

		// reparto inicial jugadores 1,2 y 3
		for (int i = 1; i <= 2; i++) {
			// jugador 1
			carta = mazo.getCarta();
			manoJugador1.add(carta);
			calcularValorMano(manoJugador1, carta, 0);
			// jugador 2
			carta = mazo.getCarta();
			manoJugador2.add(carta);
			calcularValorMano(manoJugador2, carta, 1);
			// jugador 3
			carta = mazo.getCarta();
			manoJugador3.add(carta);
			calcularValorMano(manoJugador3, carta, 2);
		}
		// Carta inicial Dealer
		carta = mazo.getCarta();
		manoDealer.add(carta);
		calcularValorMano(manoDealer, carta, 3);

		// gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
		manosJugadores = new ArrayList<ArrayList<Carta>>(LONGITUD_COLA + 1);// JUgadores y el dealer
		manosJugadores.add(manoJugador1);
		manosJugadores.add(manoJugador2);
		manosJugadores.add(manoJugador3);
		manosJugadores.add(manoDealer);
		// Impresión
		for (ArrayList<Carta> mano : manosJugadores) {
			for (Carta cartaAux : mano) {
				mostrarMensaje(cartaAux.toString());
			}
		}

	}

	/**
	 * Calcular valor mano.
	 * 
	 * Calcula el valor de la mano del jugador i y lo guarda en el array valorManos
	 *
	 * @param mano  the mano
	 * @param carta the carta
	 * @param i     the i
	 */
	private void calcularValorMano(ArrayList<Carta> mano, Carta carta, int i) {
		// TODO Auto-generated method stub
		if (carta.getValor().equals("As")) {

			valorManos[i] += 11;

		} else {
			if (carta.getValor().equals("J") || carta.getValor().equals("Q") || carta.getValor().equals("K")) {
				valorManos[i] += 10;
			} else {
				valorManos[i] += Integer.parseInt(carta.getValor());
			}
		}
		// Revisar si tiene una carta As, su valor puede variar
		if (contieneAs(mano) && valorManos[i] > 21) {

			revisarAsMano(mano, i);
		}

		if (mano.size() == 2 && valorManos[i] == 21) {// Guarda si el jugador tiene un Black Jack, es decir un As, y una
														// J, Q, K, 10

			estadosJugadores[i] = "blackjack";

		} else {

			estadosJugadores[i] = "normal";
		}
		mostrarMensaje("En calcularValorMano, el valor " + i + " es de " + valorManos[i]);
	}

	/**
	 * Revisar as mano.
	 * 
	 * Analiza y le da un nuevo a valor a la mano cuando la carta As cambia su valor
	 * de 11 a 1.
	 *
	 * @param mano the mano
	 * @param i    the i
	 */

	private void revisarAsMano(ArrayList<Carta> mano, int i) {

		for (int j = 0; j < mano.size(); j++) {
			if (mano.get(j).getValor().equals("As")) {
				if (valorManos[i] > 21 && !mano.get(j).isValorCambiado()) {
					mano.get(j).setValorCambiado(true);
					valorManos[i] -= 10;
				}
			}
		}
	}

	/**
	 * Contiene as.
	 * 
	 * Retorna true si la lista contiene una carta As
	 *
	 * @param mano the mano
	 * @return true, if successful
	 */
	private boolean contieneAs(ArrayList<Carta> mano) {
		for (Carta carta : mano) {
			if (carta.getValor().equals("As")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Iniciar.
	 */
	public void iniciar() {
		// esperar a los clientes
		mostrarMensaje("Esperando a los jugadores...");

		for (int i = 0; i < LONGITUD_COLA; i++) {
			try {
				conexionJugador = server.accept();// estar pendiente a que llegue un cliente
				jugadores[i] = new Jugador(conexionJugador, i); // crea el hilo y lo agrega al arreglo de hilos
				manejadorHilos.execute(jugadores[i]); // Ejecutamos el hilo recién creado
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mostrar mensaje.
	 *
	 * @param mensaje the mensaje
	 */
	private void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}

	/**
	 * Iniciar ronda juego. Despierta al jugador 0 para iniciar la ronda de juego
	 * (después de sala de espera)
	 */
	private void iniciarRondaJuego() {

		this.mostrarMensaje("bloqueando al servidor para despertar al jugador 1");
		bloqueoJuego.lock();

		// despertar al jugador 1 porque es su turno
		try {
			this.mostrarMensaje("Despertando al jugador 1 para que inicie el juego");
			jugadores[0].setSuspendido(false);
			mostrarMensaje("Coloca el suspendido en false de jugadores " + jugadores[0].indexJugador);
			esperarInicio.signalAll(); // POR QUÉ?
		} catch (Exception e) {

		} finally {
			this.mostrarMensaje("Desbloqueando al servidor luego de despertar al jugador 1 para que inicie el juego");
			bloqueoJuego.unlock();
		}
	}

	/**
	 * Despertar al jugador 2. Despierta al jugador 1 para iniciar la ronda de juego
	 * (después de sala de espera)
	 */
	private void despertarAlJugador2() {

		this.mostrarMensaje("bloqueando al servidor para despertar al jugador 2");
		bloqueoJuego.lock();

		// despertar al jugador 1 porque es su turno
		try {
			this.mostrarMensaje("Despertando al jugador 2 para que inicie el juego");
			jugadores[1].setSuspendido(false);
			mostrarMensaje("Coloca el suspendido en false de jugadores " + jugadores[1].indexJugador);
			esperarInicio.signal(); // POR QUÉ?

		} catch (Exception e) {
		} finally {
			this.mostrarMensaje("Desbloqueando al servidor luego de despertar al jugador 1 para que inicie el juego");
			bloqueoJuego.unlock();
		}
	}

	/**
	 * Analizar mensaje. Método de control
	 * 
	 * @param entrada      the entrada
	 * @param indexJugador the index jugador
	 */
	private void analizarMensaje(String entrada, int indexJugador) {
		// TODO Auto-generated method stub
		// garantizar que solo se analice la petición del jugador en turno.
		while (indexJugador != jugadorEnTurno) {

			bloqueoJuego.lock();
			try {
				esperarTurno.await();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// PUSE EL FINALLY
			} finally {

				bloqueoJuego.unlock();
			}

		}
		// valida turnos para jugador 0 o 1

		if (entrada.equals("pedir")) {
			// dar carta
			mostrarMensaje("Se envió carta al jugador " + idJugadores[indexJugador]);
			Carta carta = mazo.getCarta();
			// adicionar la carta a la mano del jugador en turno
			manosJugadores.get(indexJugador).add(carta);
			calcularValorMano(manosJugadores.get(indexJugador), carta, indexJugador);

			datosEnviar = new DatosBlackJack();
			datosEnviar.setCapitalJugador1(capital[0]);
			datosEnviar.setCapitalJugador2(capital[1]);
			datosEnviar.setCapitalJugador3(capital[2]);
			// datosEnviar.setCapitalJugadores(capital);
			datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador(idJugadores[indexJugador]);
			// determinar qué sucede con la carta dada en la mano del jugador y
			// mandar mensaje a todos los jugadores
			if (valorManos[indexJugador] > 21) {
				// jugador Voló

				estadosJugadores[indexJugador] = "voló";

				datosEnviar
						.setMensaje(idJugadores[indexJugador] + " tienes " + valorManos[indexJugador] + " volaste :(");
				datosEnviar.setJugadorEstado("voló");

				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);

				// notificar a todos cuál jugador sigue
				if (jugadorEnTurno == 0) {

					datosEnviar = new DatosBlackJack();
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					// datosEnviar.setCapitalJugadores(capital);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					// avisa cuál es el jugador siguiente
					datosEnviar.setJugador(idJugadores[1]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[1] + " te toca jugar y tienes " + valorManos[1]);

					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);

					// levantar al jugador en espera de turno

					bloqueoJuego.lock();
					try {
						// esperarInicio.await();
						jugadores[0].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					} finally {
						bloqueoJuego.unlock();
						determinarRondaJuego(indexJugador);
					}
				} else if (jugadorEnTurno == 1) {
					datosEnviar = new DatosBlackJack();
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					// datosEnviar.setCapitalJugadores(capital);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					// avisa cuál es el jugador siguiente
					datosEnviar.setJugador(idJugadores[2]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[2] + " te toca jugar y tienes " + valorManos[2]);

					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);

					// levantar al jugador en espera de turno

					bloqueoJuego.lock();
					try {
						// esperarInicio.await();
						jugadores[1].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					} finally {
						bloqueoJuego.unlock();
						determinarRondaJuego(indexJugador);
					}
				} else {// era el jugador 3 entonces se debe iniciar el dealer
						// notificar a todos que le toca jugar al dealer
					datosEnviar = new DatosBlackJack();
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					// datosEnviar.setCapitalJugadores(capital);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador("dealer");
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje("Dealer se repartirá carta");

					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);

					// Orden variable de control, el jugador 3 reinicia el orden para poder
					// reiniciar el juego de manera adecuada y ordenada
					orden = 0;
					iniciarDealer();
					determinarRondaJuego(indexJugador);
				}
			} else {// jugador no se pasa de 21 puede seguir jugando
				datosEnviar.setCarta(carta);
				datosEnviar.setJugador(idJugadores[indexJugador]);
				datosEnviar.setMensaje(idJugadores[indexJugador] + " ahora tienes " + valorManos[indexJugador]);
				datosEnviar.setJugadorEstado("sigue");

				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);

			}
		} else {
			// jugador en turno plantó
			datosEnviar = new DatosBlackJack();
			datosEnviar.setCapitalJugador1(capital[0]);
			datosEnviar.setCapitalJugador2(capital[1]);
			datosEnviar.setCapitalJugador3(capital[2]);
			// datosEnviar.setCapitalJugadores(capital);
			datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setJugador(idJugadores[indexJugador]);
			datosEnviar.setMensaje(idJugadores[indexJugador] + " se plantó");
			datosEnviar.setJugadorEstado("plantó");

			jugadores[0].enviarMensajeCliente(datosEnviar);
			jugadores[1].enviarMensajeCliente(datosEnviar);
			jugadores[2].enviarMensajeCliente(datosEnviar);

			// notificar a todos el jugador que sigue en turno
			if (jugadorEnTurno == 0) {

				datosEnviar = new DatosBlackJack();
				datosEnviar.setCapitalJugador1(capital[0]);
				datosEnviar.setCapitalJugador2(capital[1]);
				datosEnviar.setCapitalJugador3(capital[2]);
				// datosEnviar.setCapitalJugadores(capital);
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[1]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[1] + " te toca jugar y tienes " + valorManos[1]);

				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				// levantar al jugador en espera de turno

				bloqueoJuego.lock();
				try {
					// esperarInicio.await();
					jugadores[indexJugador].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				} finally {
					bloqueoJuego.unlock();
					determinarRondaJuego(indexJugador);
				}
			} else if (jugadorEnTurno == 1) {

				datosEnviar = new DatosBlackJack();
				datosEnviar.setCapitalJugador1(capital[0]);
				datosEnviar.setCapitalJugador2(capital[1]);
				datosEnviar.setCapitalJugador3(capital[2]);
				// datosEnviar.setCapitalJugadores(capital);
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[2]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[2] + " te toca jugar y tienes " + valorManos[2]);

				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				// levantar al jugador en espera de turno

				bloqueoJuego.lock();
				try {
					// esperarInicio.await();
					jugadores[indexJugador].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				} finally {
					bloqueoJuego.unlock();
					determinarRondaJuego(indexJugador);
				}
			} else {
				// notificar a todos que le toca jugar al dealer
				datosEnviar = new DatosBlackJack();
				datosEnviar.setCapitalJugador1(capital[0]);
				datosEnviar.setCapitalJugador2(capital[1]);
				datosEnviar.setCapitalJugador3(capital[2]);
				// datosEnviar.setCapitalJugadores(capital);
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador("dealer");
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje("Dealer se repartirá carta");

				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);

				// Orden variable de control, el jugador 3 reinicia el orden para poder
				// reiniciar el juego de manera adecuada y ordenada
				orden = 0;
				iniciarDealer();
				determinarRondaJuego(indexJugador);
			}
		}
	}

	/**
	 * Determinar ronda juego.
	 * 
	 * Determina las ganancias, pérdidas y el mensaje para cada jugador al finalizar
	 * la ronda.
	 *
	 * @param indexJugador the index jugador
	 */
	private void determinarRondaJuego(int indexJugador) {
		mostrarMensaje("Entró a determinarRonda");
		datosEnviar = new DatosBlackJack();
		if (indexJugador != 3) {
			bloqueoJuego.lock();
			try {
				mostrarMensaje("jugador " + idJugadores[indexJugador] + " se fue a dormir en finalizar");
				finalizar.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				mostrarMensaje("Desbloquea al servidor después de que " + idJugadores[indexJugador] + " pasó");
				bloqueoJuego.unlock();
			}
		}
		/*
		 * EMPATE Ambos blackjack Ambos mismo valor Ambos pierden3
		 */
		if (indexJugador == 3) {

			for (int i = 0; i < 3; i++) {

				// Empate
				if ((estadosJugadores[i].equals(estadosJugadores[3]) && estadosJugadores[i].equals("blackjack"))
						|| (estadosJugadores[i].equals(estadosJugadores[3]) && estadosJugadores[i].equals("voló"))
						|| (valorManos[i] == valorManos[3])) {
					// Vuela el dealer

				} // El jugador tiene blackjack
				else if (estadosJugadores[i].equals("blackjack")) {
					capital[i] += 15;

				} else if (estadosJugadores[3].equals("voló")) {
					// Gana el que no voló
					capital[i] += 10;

				}
				// Vuela el jugador
				else if (estadosJugadores[i].equals("voló")) {
					capital[i] -= 10;

				}
				// El dealer tiene blackjack
				else if (estadosJugadores[3].equals("blackjack")) {
					// Gana el que tenga blackjack
					capital[i] -= 10;

				}

				// Gana quien esté más cerca del 21
				else if (valorManos[i] > valorManos[3]) {
					capital[i] += 10;

				} else {
					capital[i] -= 10;

				}
			}

			datosEnviar.setCapitalJugador1(capital[0]);
			datosEnviar.setCapitalJugador2(capital[1]);
			datosEnviar.setCapitalJugador3(capital[2]);

			for (int i = 0; i < 3; i++) {

				// Empate
				if ((estadosJugadores[i].equals(estadosJugadores[3]) && estadosJugadores[i].equals("blackjack"))
						|| (estadosJugadores[i].equals(estadosJugadores[3]) && estadosJugadores[i].equals("voló"))
						|| (valorManos[i] == valorManos[3])) {
					// Se le devuelve la capital

					datosEnviar.setMensaje("Empató el jugador " + idJugadores[i] + " con el dealer");
				}
				// El jugador tiene blackjack
				else if (estadosJugadores[i].equals("blackjack")) {

					datosEnviar.setMensaje("Gana el jugador" + idJugadores[i] + " y tiene blackjack");

				}
				// Vuela el dealer
				else if (estadosJugadores[3].equals("voló")) {
					// Gana el que no voló

					datosEnviar.setMensaje("Gana el jugador " + idJugadores[i] + " porque el dealer voló");
				}
				// Vuela el jugador
				else if (estadosJugadores[i].equals("voló")) {

					datosEnviar.setMensaje("Gana el dealer, porque " + idJugadores[i] + " voló");
				}
				// El dealer tiene blackjack
				else if (estadosJugadores[3].equals("blackjack")) {
					// Gana el que tenga blackjack

					datosEnviar.setMensaje("Gana el dealer y tiene blackjack, pierde " + idJugadores[i]);
				}
				// Gana quien esté más cerca del 21
				else if (valorManos[i] > valorManos[3]) {

					datosEnviar.setMensaje("Gana el jugador " + idJugadores[i] + " pues tiene " + valorManos[i]);
				} else {

					datosEnviar.setMensaje("Gana el dealer, porque " + idJugadores[i] + " tiene " + valorManos[i]
							+ " y el dealer tiene " + valorManos[3]);
				}

				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setJugador(idJugadores[i]);
				datosEnviar.setJugadorEstado("finalizar");
				datosEnviar.setEnJuego(false);

				mostrarMensaje("El booleano enJuego es " + datosEnviar.isEnJuego());

				jugadores[i].setSuspendido(true);
				jugadores[i].enviarMensajeCliente(datosEnviar);

			}

			// Dealer despierta los hilos.
			seTerminoRonda = true;
			mostrarMensaje("seTerminoRonda se vuelve " + seTerminoRonda);
			bloqueoJuego.lock();
			finalizar.signalAll();
			bloqueoJuego.unlock();
		}
		mostrarMensaje("Al final de determinarRondaJuego con jugador " + indexJugador);
	}

	/**
	 * Iniciar dealer.
	 */
	public void iniciarDealer() {
		// le toca turno al dealer.
		Thread dealer = new Thread(this);
		dealer.start();
	}

	/**
	 * The Class Jugador.
	 * 
	 * The Class Jugador. Clase interna que maneja el servidor para gestionar la
	 * comunicación con cada cliente Jugador que se conecte
	 */

	private class Jugador implements Runnable {

		// varibles para gestionar la comunicación con el cliente (Jugador) conectado
		private Socket conexionCliente;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private String entrada;

		// variables de control
		private int indexJugador;
		private boolean suspendido; // booleano de control del hilo jugador

		/**
		 * Instantiates a new jugador.
		 *
		 * @param conexionCliente the conexion cliente
		 * @param indexJugador    the index jugador
		 */
		public Jugador(Socket conexionCliente, int indexJugador) {
			this.conexionCliente = conexionCliente;
			this.indexJugador = indexJugador;
			suspendido = true;
			// crear los flujos de E/S
			try {
				out = new ObjectOutputStream(conexionCliente.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conexionCliente.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Sets the suspendido.
		 *
		 * Cambiar el estado de suspendido del hilo
		 * 
		 * @param suspendido the new suspendido
		 */

		private void setSuspendido(boolean suspendido) {
			this.suspendido = suspendido;
		}

		/**
		 * Run.
		 */
		@Override

		public void run() {
			// TODO Auto-generated method stub
			while (true) {

				// procesar los mensajes eviados por el cliente
				mostrarMensaje("Entró al run el indexJugador " + indexJugador);
				// ver cual jugador es
				if (indexJugador == 0) {
					// es jugador 1, debe ponerse en espera a la llegada del otro jugador

					try {
						// guarda el nombre del primer jugador
						mostrarMensaje("Jugador con indexJugador " + indexJugador + " va a esperar para leer");
						// Si se abre por primera vez, se hace la lectura de nombre y capital
						if (idJugadores[0] == null) {
							idJugadores[0] = (String) in.readObject();// Recoger el nombre del jugador
							capital[0] = (int) in.readObject();
						}

						mostrarMensaje(
								"Hilo establecido con jugador (0) " + idJugadores[0] + " con capital " + capital[0]);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 1");
					bloqueoJuego.lock(); // bloquea el servidor

					while (suspendido) {// Si el hilo está suspendido, se irá a dormir el hilo
						mostrarMensaje("Parando al Jugador 1 en espera del otro jugador...");
						if (ronda > 0) {
							esperarReinicio.signalAll();
							orden++;
						}
						try {
							esperarInicio.await();// Hilo duerme y despierta aquí
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 1");
							bloqueoJuego.unlock();
						}
					}

					// ya se conectaron todos los jugadores
					// le manda al jugador 1 todos los datos para montar la sala de Juego
					// le toca el turno a jugador 1
					mostrarMensaje("manda al jugador 1 todos los datos para montar SalaJuego");
					datosEnviar.setEnJuego(true);
					datosEnviar = new DatosBlackJack();
					datosEnviar.setManoDealer(manosJugadores.get(3));
					datosEnviar.setManoJugador1(manosJugadores.get(0));
					datosEnviar.setManoJugador2(manosJugadores.get(1));
					datosEnviar.setManoJugador3(manosJugadores.get(2));
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setMensaje("Inicias " + idJugadores[0] + " tienes " + valorManos[0]);

					enviarMensajeCliente(datosEnviar); // con esto construye la mesa
					despertarAlJugador2();
					jugadorEnTurno = 0;

				} else if (indexJugador == 1) {
					// es jugador 2, debe ponerse en espera a la llegada del otro jugador

					try {
						// guarda el nombre del segundo jugador la primera vez que abre el juego
						if (idJugadores[1] == null) {
							idJugadores[1] = (String) in.readObject();// Recoger el nombre del jugador
							capital[1] = (int) in.readObject();
						}
						mostrarMensaje(
								"Hilo establecido con jugador (1) " + idJugadores[1] + " con capital " + capital[1]);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					while (suspendido) {// Si el hilo está suspendido, se irá a dormir el hilo
						mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 2");
						bloqueoJuego.lock(); // bloquea el servidor
						try {
							mostrarMensaje("Parando al Jugador 2 en espera del otro jugador...");
							if (ronda > 0) {
								esperarReinicio.signalAll();
								orden++;
							}
							esperarInicio.await();// Hilo duerme y despierta aquí
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 2");
							bloqueoJuego.unlock();				
						}
					}

					// ya se conectaron todos los jugadores
					// le manda al jugador 2 todos los datos para montar la sala de Juego
					// le toca el turno a jugador 2

					mostrarMensaje("manda al jugador 2 todos los datos para montar SalaJuego");
					datosEnviar = new DatosBlackJack();
					datosEnviar.setManoDealer(manosJugadores.get(3));
					datosEnviar.setManoJugador1(manosJugadores.get(0));
					datosEnviar.setManoJugador2(manosJugadores.get(1));
					datosEnviar.setManoJugador3(manosJugadores.get(2));
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setMensaje("Inicias " + idJugadores[1] + " tienes " + valorManos[1]);
					datosEnviar.setPrueba(false);

					enviarMensajeCliente(datosEnviar);

					jugadorEnTurno = 0;
				} else {
					// Es jugador 2 (tercer jugador)
					// le manda al jugador 2 todos los datos para montar la sala de Juego
					// jugador 2 debe esperar su turno
					try {
						if (idJugadores[2] == null) {
							idJugadores[2] = (String) in.readObject();
							capital[2] = (int) in.readObject();
						}
						mostrarMensaje("Hilo jugador (3)" + idJugadores[2] + " con capital " + capital[2]);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					mostrarMensaje("manda al jugador 3 el nombre del jugador 1 y monta la sala");

					datosEnviar = new DatosBlackJack();
					datosEnviar.setManoDealer(manosJugadores.get(3));
					datosEnviar.setManoJugador1(manosJugadores.get(0));
					datosEnviar.setManoJugador2(manosJugadores.get(1));
					datosEnviar.setManoJugador3(manosJugadores.get(2));;
					datosEnviar.setCapitalJugador1(capital[0]);
					datosEnviar.setCapitalJugador2(capital[1]);
					datosEnviar.setCapitalJugador3(capital[2]);
					datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setMensaje("Inicias " + idJugadores[2] + " tienes " + valorManos[2]);
					enviarMensajeCliente(datosEnviar);

					iniciarRondaJuego(); // despertar al jugador 1 para iniciar el juego
					mostrarMensaje("Bloquea al servidor para poner en espera de turno al jugador 3");
					bloqueoJuego.lock();
					try {
						mostrarMensaje("Pone en espera de turno al jugador 3");
						esperarTurno.await();
						mostrarMensaje("Despierta de la espera de inicio del juego al jugador 1");
						//
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						mostrarMensaje("Desbloquea el servidor luego de dormir al jugador 3 esperando turno");
						bloqueoJuego.unlock();
					}
				}

				while (!seTerminoRonda) {
					try {
						entrada = (String) in.readObject();
						analizarMensaje(entrada, indexJugador);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// controlar cuando se cierra un cliente
					}
				}
				// Debería esperar al primero que haga click
				try {
					String aux = (String) in.readObject();
					contador++;
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ronda++;
				if (contador == 3) {
					reiniciarVariables();
				}
				// cerrar conexión

				if (indexJugador != 0) {

					while (orden != indexJugador) {
						bloqueoJuego.lock();
						try {
							esperarReinicio.await();// Hilo duerme y despierta aquí
							mostrarMensaje("El jugador: " + indexJugador + "se despierta en el while de reinicio");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							bloqueoJuego.unlock();
						}
					}
				}
			}
		}
		/**
		 * Enviar mensaje cliente.
		 *
		 * @param mensaje the mensaje
		 */
		public void enviarMensajeCliente(Object mensaje) {
			try {
				out.writeObject(mensaje);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}// fin inner class Jugador

	/**
	 * Run.
	 * Jugador dealer emulado por el servidor
	 */
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mostrarMensaje("Inicia el dealer ...");
		boolean pedir = true;

		while (pedir) {
			Carta carta = mazo.getCarta();
			// adicionar la carta a la mano del dealer
			manosJugadores.get(3).add(carta);
			calcularValorMano(manosJugadores.get(3), carta, 3);

			mostrarMensaje("El dealer recibe " + carta.toString() + " suma " + valorManos[3]);

			datosEnviar = new DatosBlackJack();
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador("dealer");

			if (valorManos[3] <= 16) {
				datosEnviar.setJugadorEstado("sigue");
				datosEnviar.setMensaje("Dealer ahora tiene " + valorManos[3]);
				mostrarMensaje("El dealer sigue jugando");
			} else {
				if (valorManos[3] > 21) {
					datosEnviar.setJugadorEstado("voló");
					estadosJugadores[3] = "voló";
					datosEnviar.setMensaje("Dealer ahora tiene " + valorManos[3] + " voló :(");
					pedir = false;
					mostrarMensaje("El dealer voló");
				} else {
					datosEnviar.setJugadorEstado("plantó");
					datosEnviar.setMensaje("Dealer ahora tiene " + valorManos[3] + " plantó");
					pedir = false;
					mostrarMensaje("El dealer plantó");
				}
			}
			// envia la jugada a los otros jugadores
			datosEnviar.setCapitalJugador1(capital[0]);
			datosEnviar.setCapitalJugador2(capital[1]);
			datosEnviar.setCapitalJugador3(capital[2]);
			datosEnviar.setCarta(carta);
			jugadores[0].enviarMensajeCliente(datosEnviar);
			jugadores[1].enviarMensajeCliente(datosEnviar);
			jugadores[2].enviarMensajeCliente(datosEnviar);

		} // fin while
		determinarRondaJuego(3);

	}

}// Fin class ServidorBJ
