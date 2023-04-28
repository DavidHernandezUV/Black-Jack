/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package comunes;

import java.awt.Image;
import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Carta.
 */
public class Carta implements Serializable{
	
	public static final int WIDTH = 45;
	public static final int HEIGHT = 60;
    private String valor;
    private String palo;
    private transient Image imagen;
    private int coordenadaX, coordenadaY;
    private boolean valorCambiado = false;
	
    /**
     * Checks if is valor cambiado.
     *
     * @return true, if is valor cambiado
     */
    public boolean isValorCambiado() {
		return valorCambiado;
	}

	/**
	 * Sets the valor cambiado.
	 *
	 * @param valorCambiado the new valor cambiado
	 */
	public void setValorCambiado(boolean valorCambiado) {
		this.valorCambiado = valorCambiado;
	}

	/**
	 * Instantiates a new carta.
	 *
	 * @param valor the valor
	 * @param palo the palo
	 */
	public Carta(String valor, String palo) {
		this.valor = valor;
		this.palo = palo;
	}

	/**
	 * Gets the valor.
	 *
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Sets the valor.
	 *
	 * @param valor the new valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Gets the palo.
	 *
	 * @return the palo
	 */
	public String getPalo() {
		return palo;
	}

	/**
	 * Sets the palo.
	 *
	 * @param palo the new palo
	 */
	public void setPalo(String palo) {
		this.palo = palo;
	}
	
	/**
	 * Gets the coordenada X.
	 *
	 * @return the coordenada X
	 */
	public int getCoordenadaX() {
		return coordenadaX;
	}

	/**
	 * Sets the coordenada X.
	 *
	 * @param coordenadaX the new coordenada X
	 */
	public void setCoordenadaX(int coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	/**
	 * Gets the coordenada Y.
	 *
	 * @return the coordenada Y
	 */
	public int getCoordenadaY() {
		return coordenadaY;
	}

	/**
	 * Sets the coordenada Y.
	 *
	 * @param coordenadaY the new coordenada Y
	 */
	public void setCoordenadaY(int coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return valor+palo;
	}
	
	/**
	 * Gets the imagen.
	 *
	 * @return the imagen
	 */
	public Image getImagen() {
		return imagen;
	}

	/**
	 * Sets the imagen.
	 *
	 * @param imagen the new imagen
	 */
	public void setImagen(Image imagen) {
		this.imagen = imagen;
	}
}
