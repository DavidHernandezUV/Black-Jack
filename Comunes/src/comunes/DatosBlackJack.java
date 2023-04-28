/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package comunes;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class DatosBlackJack.
 */
public class DatosBlackJack implements Serializable{
	private String[] idJugadores;
	private int[] capitalJugadores;
	private ArrayList<Carta> manoJugador1, manoJugador2, manoJugador3, manoDealer;
	private int[] valorManos;
	private Carta carta;
	private String mensaje;
	private String jugador,jugadorEstado;
	private boolean enJuego = true;
	private boolean prueba = true;
	private String idJugador1, idJugador2, idJugador3;
	private int capitalJugador1,capitalJugador2,capitalJugador3;
		
	/**
	 * Gets the capital jugador 1.
	 *
	 * @return the capital jugador 1
	 */
	public int getCapitalJugador1() {
		return capitalJugador1;
	}

	/**
	 * Sets the capital jugador 1.
	 *
	 * @param capitalJugador1 the new capital jugador 1
	 */
	public void setCapitalJugador1(int capitalJugador1) {
		this.capitalJugador1 = capitalJugador1;
	}

	/**
	 * Gets the capital jugador 2.
	 *
	 * @return the capital jugador 2
	 */
	public int getCapitalJugador2() {
		return capitalJugador2;
	}

	/**
	 * Sets the capital jugador 2.
	 *
	 * @param capitalJugador2 the new capital jugador 2
	 */
	public void setCapitalJugador2(int capitalJugador2) {
		this.capitalJugador2 = capitalJugador2;
	}

	/**
	 * Gets the capital jugador 3.
	 *
	 * @return the capital jugador 3
	 */
	public int getCapitalJugador3() {
		return capitalJugador3;
	}

	/**
	 * Sets the capital jugador 3.
	 *
	 * @param capitalJugador3 the new capital jugador 3
	 */
	public void setCapitalJugador3(int capitalJugador3) {
		this.capitalJugador3 = capitalJugador3;
	}
	private int count=0;
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Gets the id jugador 1.
	 *
	 * @return the id jugador 1
	 */
	public String getIdJugador1() {
		return idJugador1;
	}

	/**
	 * Sets the id jugador 1.
	 *
	 * @param idJugador1 the new id jugador 1
	 */
	public void setIdJugador1(String idJugador1) {
		this.idJugador1 = idJugador1;
	}

	/**
	 * Gets the id jugador 2.
	 *
	 * @return the id jugador 2
	 */
	public String getIdJugador2() {
		return idJugador2;
	}

	/**
	 * Sets the id jugador 2.
	 *
	 * @param idJugador2 the new id jugador 2
	 */
	public void setIdJugador2(String idJugador2) {
		this.idJugador2 = idJugador2;
	}

	/**
	 * Gets the id jugador 3.
	 *
	 * @return the id jugador 3
	 */
	public String getIdJugador3() {
		return idJugador3;
	}

	/**
	 * Sets the id jugador 3.
	 *
	 * @param idJugador3 the new id jugador 3
	 */
	public void setIdJugador3(String idJugador3) {
		this.idJugador3 = idJugador3;
	}

	/**
	 * Checks if is prueba.
	 *
	 * @return true, if is prueba
	 */
	public boolean isPrueba() {
		return prueba;
	}

	/**
	 * Sets the prueba.
	 *
	 * @param prueba the new prueba
	 */
	public void setPrueba(boolean prueba) {
		this.prueba = prueba;
	}


	/**
	 * Checks if is en juego.
	 *
	 * @return true, if is en juego
	 */
	public boolean isEnJuego() {
		return enJuego;
	}
	
	/**
	 * Sets the en juego.
	 *
	 * @param enJuego the new en juego
	 */
	public void setEnJuego(boolean enJuego) {
		this.enJuego = enJuego;
	}
	
	/**
	 * Gets the capital jugadores.
	 *
	 * @return the capital jugadores
	 */
	public int[] getCapitalJugadores() {
		return capitalJugadores;
	}
	
	/**
	 * Sets the capital jugadores.
	 *
	 * @param capitalJugadores the new capital jugadores
	 */
	public void setCapitalJugadores(int[] capitalJugadores) {
		this.capitalJugadores = capitalJugadores;
	}
	
	/**
	 * Gets the jugador.
	 *
	 * @return the jugador
	 */
	public String getJugador() {
		return jugador;
	}
	
	/**
	 * Sets the jugador.
	 *
	 * @param jugador the new jugador
	 */
	public void setJugador(String jugador) {
		this.jugador = jugador;
	}
	
	/**
	 * Gets the jugador estado.
	 *
	 * @return the jugador estado
	 */
	public String getJugadorEstado() {
		return jugadorEstado;
	}
	
	/**
	 * Sets the jugador estado.
	 *
	 * @param jugadorEstado the new jugador estado
	 */
	public void setJugadorEstado(String jugadorEstado) {
		this.jugadorEstado = jugadorEstado;
	}
		
	/**
	 * Gets the id jugadores.
	 *
	 * @return the id jugadores
	 */
	public String[] getIdJugadores() {
		System.out.println("En DatosBlackJack, getIdJugadores");
	
		return idJugadores;
	}
	
	/**
	 * Sets the id jugadores.
	 *
	 * @param idJugadores the new id jugadores
	 */
	public void setIdJugadores(String[] idJugadores) {
		this.idJugadores = idJugadores;
	}
	
	/**
	 * Gets the mano jugador 1.
	 *
	 * @return the mano jugador 1
	 */
	public ArrayList<Carta> getManoJugador1() {
		return manoJugador1;
	}
	
	/**
	 * Sets the mano jugador 1.
	 *
	 * @param manoJugador1 the new mano jugador 1
	 */
	public void setManoJugador1(ArrayList<Carta> manoJugador1) {
		this.manoJugador1 = manoJugador1;
	}
	
	/**
	 * Gets the mano jugador 2.
	 *
	 * @return the mano jugador 2
	 */
	public ArrayList<Carta> getManoJugador2() {
		return manoJugador2;
	}
	
	/**
	 * Sets the mano jugador 2.
	 *
	 * @param manoJugador2 the new mano jugador 2
	 */
	public void setManoJugador2(ArrayList<Carta> manoJugador2) {
		this.manoJugador2 = manoJugador2;
	}
	
	/**
	 * Gets the mano jugador 3.
	 *
	 * @return the mano jugador 3
	 */
	public ArrayList<Carta> getManoJugador3() {
		return manoJugador3;
	}
	
	/**
	 * Sets the mano jugador 3.
	 *
	 * @param manoJugador3 the new mano jugador 3
	 */
	public void setManoJugador3(ArrayList<Carta> manoJugador3) {
		this.manoJugador3 = manoJugador3;
	}
	
	/**
	 * Gets the mano dealer.
	 *
	 * @return the mano dealer
	 */
	public ArrayList<Carta> getManoDealer() {
		return manoDealer;
	}
	
	/**
	 * Sets the mano dealer.
	 *
	 * @param manoDealer the new mano dealer
	 */
	public void setManoDealer(ArrayList<Carta> manoDealer) {
		this.manoDealer = manoDealer;
	}
	
	/**
	 * Gets the mensaje.
	 *
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}
	
	/**
	 * Sets the mensaje.
	 *
	 * @param mensaje the new mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * Sets the valor manos.
	 *
	 * @param valorManos the new valor manos
	 */
	public void setValorManos(int[] valorManos) {
		this.valorManos=valorManos;
	}
	
	/**
	 * Gets the valor manos.
	 *
	 * @return the valor manos
	 */
	public int[] getValorManos() {
		return valorManos;	
	}
	
	/**
	 * Sets the carta.
	 *
	 * @param carta the new carta
	 */
	public void setCarta(Carta carta) {
		this.carta=carta;
	}
	
	/**
	 * Gets the carta.
	 *
	 * @return the carta
	 */
	public Carta getCarta() {
		return carta;
	}
}
