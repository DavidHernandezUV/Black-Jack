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
import java.awt.Font;

import javax.swing.JLabel;

public class Titulos extends JLabel {
    public Titulos(String texto, int tamano, Color colorFondo, Color colorLetra) {
    	
    	this.setText(texto);
    	Font font = new Font (Font.SERIF,Font.BOLD+Font.ITALIC,tamano);
    	setFont(font);
    	this.setForeground(colorLetra);
    	this.setBackground(colorFondo);
    	this.setOpaque(true);
    	this.setHorizontalAlignment(JLabel.CENTER);
    	this.setVerticalAlignment(JLabel.CENTER);
    }

}
