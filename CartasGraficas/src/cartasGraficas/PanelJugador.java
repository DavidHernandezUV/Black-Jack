package cartasGraficas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class PanelJugador extends JPanel {
	
	private static final int ANCHO = 200;
	private static final int ALTO = 150;
	private BlackJackGUI clase;
	
	private int coordenadaX;
	private ArrayList<Recuerdo> cartasRecuerdo;
	
	public PanelJugador(String nombreJugador, BlackJackGUI bk) {
		clase=bk;
		coordenadaX=5;
		cartasRecuerdo = new ArrayList<Recuerdo>();
		//this.setBackground(Color.GREEN);
		this.setPreferredSize(new Dimension(ANCHO,ALTO));
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(nombreJugador);
		this.setBorder(bordes);
	}

	public void dibujarCarta(Carta carta) {
		cartasRecuerdo.add(new Recuerdo(carta.getImagen(),coordenadaX));
		coordenadaX+=17;
		repaint();
	}

	public void paintComponent(Graphics g)  {
		super.paintComponent(g); 
		//Pinta con memoria
		
			for(Recuerdo carta : cartasRecuerdo) {
				g.drawImage(carta.getImagenRecordar(), carta.getxRecordar(),20, this);
			}
			clase.seguir=true;		
		}

	private class Recuerdo{
		private Image imagenRecordar;
		private int xRecordar;

		public Recuerdo(Image imagenRecordar, int xRecordar) {
			this.imagenRecordar = imagenRecordar;
			this.xRecordar = xRecordar;
		}

		public Image getImagenRecordar() {
			return imagenRecordar;
		}

		public int getxRecordar() {
			return xRecordar;
		}
	}

}
