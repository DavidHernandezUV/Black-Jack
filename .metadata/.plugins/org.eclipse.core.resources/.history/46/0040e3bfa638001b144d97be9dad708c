package servidorbj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import comunes.Baraja;
import comunes.Carta;
import comunes.DatosBlackJack;

/* Clase encargada de realizar la gestión del juego, esto es, el manejo de turnos y estado del juego.
 * También gestiona al jugador Dealer. 
 * El Dealer tiene una regla de funcionamiento definida:
 * Pide carta con 16 o menos y Planta con 17 o mas.
 */
public class ServidorBJ implements Runnable{
	//constantes para manejo de la conexion.
	public static final int PUERTO=7377;
	public static final String IP="127.0.0.1";
	public static final int LONGITUD_COLA=2;

	// variables para funcionar como servidor
	private ServerSocket server;
	private Socket conexionJugador;
	
	//variables para manejo de hilos
	private ExecutorService manejadorHilos;
	private Lock bloqueoJuego;
	private Condition esperarInicio, esperarTurno, finalizar;
	private Jugador[] jugadores;
	
	//variables de control del juego
	private String[] idJugadores;
	private int jugadorEnTurno;
	//private boolean iniciarJuego;
	private Baraja mazo;
	private ArrayList<ArrayList<Carta>> manosJugadores;
	private ArrayList<Carta> manoJugador1;
	private ArrayList<Carta> manoJugador2;
	private ArrayList<Carta> manoDealer;
	private int[] valorManos;
	private DatosBlackJack datosEnviar;
	
	public ServidorBJ() {
	    //inicializar variables de control del juego
		inicializarVariablesControlRonda();
	    //inicializar las variables de manejo de hilos
		inicializareVariablesManejoHilos();
		//crear el servidor
    	try {
    		mostrarMensaje("Iniciando el servidor...");
			server = new ServerSocket(PUERTO,LONGITUD_COLA);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private void inicializareVariablesManejoHilos() {
		// TODO Auto-generated method stub
    	manejadorHilos = Executors.newFixedThreadPool(LONGITUD_COLA);
		bloqueoJuego = new ReentrantLock();
		esperarInicio = bloqueoJuego.newCondition();
		esperarTurno = bloqueoJuego.newCondition();
		finalizar = bloqueoJuego.newCondition();
		jugadores = new Jugador[LONGITUD_COLA];	
	}

	private void inicializarVariablesControlRonda() {
		// TODO Auto-generated method stub
    	 //Variables de control del juego.
		
		idJugadores = new String[2];
		valorManos = new int[3];
		
		mazo = new Baraja();
		Carta carta;
		
		manoJugador1 = new ArrayList<Carta>();
		manoJugador2 = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();
		
		//reparto inicial jugadores 1 y 2
		for(int i=1;i<=2;i++) {
		  carta = mazo.getCarta();
		  manoJugador1.add(carta);
		  calcularValorMano(carta,0);
		  carta = mazo.getCarta();
		  manoJugador2.add(carta);
		  calcularValorMano(carta,1);
		}
		//Carta inicial Dealer
		carta = mazo.getCarta();
		manoDealer.add(carta);
		calcularValorMano(carta,2);
		
		//gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
		manosJugadores = new ArrayList<ArrayList<Carta>>(3);
		manosJugadores.add(manoJugador1);
		manosJugadores.add(manoJugador2);
		manosJugadores.add(manoDealer);
	}

	private void calcularValorMano(Carta carta, int i) {
		// TODO Auto-generated method stub
    	
			if(carta.getValor().equals("As")) {
				valorManos[i]+=11;
			}else {
				if(carta.getValor().equals("J") || carta.getValor().equals("Q")
						   || carta.getValor().equals("K")) {
					valorManos[i]+=10;
				}else {
					valorManos[i]+=Integer.parseInt(carta.getValor()); 
				}
		}
	}
	
	public void iniciar() {
       	//esperar a los clientes
    	mostrarMensaje("Esperando a los jugadores...");
    	
    	for(int i=0; i<LONGITUD_COLA;i++) {
    		try {
				conexionJugador = server.accept();
				jugadores[i] = new Jugador(conexionJugador,i);
	    		manejadorHilos.execute(jugadores[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	} 	
    }
    
	private void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	private void iniciarRondaJuego() {
		
		this.mostrarMensaje("bloqueando al servidor para despertar al jugador 1");
    	bloqueoJuego.lock();
    	
    	//despertar al jugador 1 porque es su turno
    	try {
    		this.mostrarMensaje("Despertando al jugador 1 para que inicie el juego");
        	jugadores[0].setSuspendido(false);
        	esperarInicio.signal();
    	}catch(Exception e) {
    		
    	}finally {
    		this.mostrarMensaje("Desbloqueando al servidor luego de despertar al jugador 1 para que inicie el juego");
    		bloqueoJuego.unlock();
    	}			
	}
	
    private boolean seTerminoRonda() {
       return false;	
    }
    
    private void analizarMensaje(String entrada, int indexJugador) {
		// TODO Auto-generated method stub
        //garantizar que solo se analice la petición del jugador en turno.
    	while(indexJugador!=jugadorEnTurno) {
    		bloqueoJuego.lock();
    		try {
				esperarTurno.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		bloqueoJuego.unlock();
    	}
    	
    	//valida turnos para jugador 0 o 1
        	
    	if(entrada.equals("pedir")) {
    		//dar carta 
    		mostrarMensaje("Se envió carta al jugador "+idJugadores[indexJugador]);
    		Carta carta = mazo.getCarta();
    		//adicionar la carta a la mano del jugador en turno
    		manosJugadores.get(indexJugador).add(carta);
    		calcularValorMano(carta, indexJugador);
    		
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		//determinar qué sucede con la carta dada en la mano del jugador y 
			//mandar mensaje a todos los jugadores
    		if(valorManos[indexJugador]>21) {
    			//jugador Voló
	    		datosEnviar.setMensaje(idJugadores[indexJugador]+" tienes "+valorManos[indexJugador]+" volaste :(");	
	    		datosEnviar.setJugadorEstado("voló");
	    		
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		
	    		//notificar a todos que jugador sigue
	    		if(jugadorEnTurno==0) {
	        		
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador(idJugadores[1]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[1]+" te toca jugar y tienes "+valorManos[1]);
					
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					
					//levantar al jugador en espera de turno
					
					bloqueoJuego.lock();
		    		try {
						//esperarInicio.await();
						jugadores[0].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					}finally {
						bloqueoJuego.unlock();
					}
	        	} else {//era el jugador 2 entonces se debe iniciar el dealer
	        		//notificar a todos que le toca jugar al dealer
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador("dealer");
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje("Dealer se repartirá carta");
					
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					
					iniciarDealer();
	        	}		
    		}else {//jugador no se pasa de 21 puede seguir jugando
    			datosEnviar.setCarta(carta);
    			datosEnviar.setJugador(idJugadores[indexJugador]);
    			datosEnviar.setMensaje(idJugadores[indexJugador]+" ahora tienes "+valorManos[indexJugador]);
	    		datosEnviar.setJugadorEstado("sigue");
	    		
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		
    		}
    	}else {
    		//jugador en turno plantó
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		datosEnviar.setMensaje(idJugadores[indexJugador]+" se plantó");
    		datosEnviar.setJugadorEstado("plantó");
    		
    		jugadores[0].enviarMensajeCliente(datosEnviar);		    		
    		jugadores[1].enviarMensajeCliente(datosEnviar);
    		
    		
    		//notificar a todos el jugador que sigue en turno
    		if(jugadorEnTurno==0) {
        		
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[1]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[1]+" te toca jugar y tienes "+valorManos[1]);
				
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				
				//levantar al jugador en espera de turno
				
				bloqueoJuego.lock();
	    		try {
					//esperarInicio.await();
					jugadores[indexJugador].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				}finally {
					bloqueoJuego.unlock();
				}
        	} else {
        		//notificar a todos que le toca jugar al dealer
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador("dealer");
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje("Dealer se repartirá carta");
				
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
			
				iniciarDealer();
        	}	
    	}
   } 
    
    public void iniciarDealer() {
       //le toca turno al dealer.
    	Thread dealer = new Thread(this);
    	dealer.start();
    }
    
    /*The Class Jugador. Clase interna que maneja el servidor para gestionar la comunicación
     * con cada cliente Jugador que se conecte
     */
    private class Jugador implements Runnable{
       
    	//varibles para gestionar la comunicación con el cliente (Jugador) conectado
        private Socket conexionCliente;
    	private ObjectOutputStream out;
    	private ObjectInputStream in;
    	private String entrada;
    	
    	//variables de control
    	private int indexJugador;
    	private boolean suspendido;
  
		public Jugador(Socket conexionCliente, int indexJugador) {
			this.conexionCliente = conexionCliente;
			this.indexJugador = indexJugador;
			suspendido = true;
			//crear los flujos de E/S
			try {
				out = new ObjectOutputStream(conexionCliente.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conexionCliente.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}	
				
		private void setSuspendido(boolean suspendido) {
			this.suspendido = suspendido;
		}
	   
		@Override
		public void run() {
			// TODO Auto-generated method stub	
			//procesar los mensajes eviados por el cliente
			
			//ver cual jugador es 
			if(indexJugador==0) {
				//es jugador 1, debe ponerse en espera a la llegada del otro jugador
				
				try {
					//guarda el nombre del primer jugador
					idJugadores[0] = (String)in.readObject();
					mostrarMensaje("Hilo establecido con jugador (1) "+idJugadores[0]);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 1");
				bloqueoJuego.lock(); //bloquea el servidor
				
				while(suspendido) {
					mostrarMensaje("Parando al Jugador 1 en espera del otro jugador...");
					try {
						esperarInicio.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 1");
						bloqueoJuego.unlock();
					}
				}
				
				//ya se conectó el otro jugador, 
				//le manda al jugador 1 todos los datos para montar la sala de Juego
				//le toca el turno a jugador 1
				
				mostrarMensaje("manda al jugador 1 todos los datos para montar SalaJuego");
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(2));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));		
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
				enviarMensajeCliente(datosEnviar);
				jugadorEnTurno=0;
			}else {
				   //Es jugador 2
				   //le manda al jugador 2 todos los datos para montar la sala de Juego
				   //jugador 2 debe esperar su turno
				try {
					idJugadores[1]=(String)in.readObject();
					mostrarMensaje("Hilo jugador (2)"+idJugadores[1]);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mostrarMensaje("manda al jugador 2 el nombre del jugador 1");
				
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(2));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));			
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
				enviarMensajeCliente(datosEnviar);
				
				iniciarRondaJuego(); //despertar al jugador 1 para iniciar el juego
				mostrarMensaje("Bloquea al servidor para poner en espera de turno al jugador 2");
				bloqueoJuego.lock();
				try {
					mostrarMensaje("Pone en espera de turno al jugador 2");
					esperarTurno.await();
					mostrarMensaje("Despierta de la espera de inicio del juego al jugador 1");
                    //
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					bloqueoJuego.unlock();
				}	
			}
			
			while(!seTerminoRonda()) {
				try {
					entrada = (String) in.readObject();
					analizarMensaje(entrada,indexJugador);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//controlar cuando se cierra un cliente
				}
			}
			//cerrar conexión
		}
		
		public void enviarMensajeCliente(Object mensaje) {
			try {  
				out.writeObject(mensaje);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
    }//fin inner class Jugador      

    //Jugador dealer emulado por el servidor
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mostrarMensaje("Incia el dealer ...");
        boolean pedir = true;
        
        while(pedir) {
		  	Carta carta = mazo.getCarta();
			//adicionar la carta a la mano del dealer
			manosJugadores.get(2).add(carta);
			calcularValorMano(carta, 2);
			
			mostrarMensaje("El dealer recibe "+carta.toString()+" suma "+ valorManos[2]);
			

    		datosEnviar = new DatosBlackJack();
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador("dealer");
				
			if(valorManos[2]<=16) {
				datosEnviar.setJugadorEstado("sigue");
				datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[2]);
				mostrarMensaje("El dealer sigue jugando");
			}else {
				if(valorManos[2]>21) {
					datosEnviar.setJugadorEstado("voló");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[2]+" voló :(");
					pedir=false;
					mostrarMensaje("El dealer voló");
				}else {
					datosEnviar.setJugadorEstado("plantó");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[2]+" plantó");
					pedir=false;
					mostrarMensaje("El dealer plantó");
				}
			}
			//envia la jugada a los otros jugadores
			datosEnviar.setCarta(carta);
			jugadores[0].enviarMensajeCliente(datosEnviar);
			jugadores[1].enviarMensajeCliente(datosEnviar);
				
        }//fin while
        
	}
    
}//Fin class ServidorBJ
