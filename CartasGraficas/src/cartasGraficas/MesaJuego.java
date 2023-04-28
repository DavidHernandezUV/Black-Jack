package cartasGraficas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MesaJuego extends JPanel {
   private static final int ANCHO = 500;
   private static final int ALTO = 300;
   private JPanel zonaDealer, zonaJugador1, zonaJugador2, zonaJugador3;
   private PanelJugador dealer, jugador1, jugador2, jugador3; 
   
   public MesaJuego(BlackJackGUI clase) {
	   dealer = new PanelJugador("Dealer",clase);
	   jugador1 = new PanelJugador("Jugador1", clase);
	   jugador2 = new PanelJugador("Jugador2", clase);
	   this.setBackground(Color.GREEN);
	   this.setPreferredSize(new Dimension(ANCHO,ALTO));
	   this.setLayout(new BorderLayout());
	   zonaDealer = new JPanel();
	   zonaDealer.add(dealer);	
	   zonaJugador1 = new JPanel();
	   zonaJugador1.add(jugador1);
	   zonaJugador2 = new JPanel();
	   zonaJugador2.add(jugador2);
	   
	   this.add(zonaDealer,BorderLayout.NORTH);
	   this.add(zonaJugador1,BorderLayout.WEST);
	   this.add(zonaJugador2,BorderLayout.CENTER);
	   
   }
   
   public void pintarJugada(Carta carta) {
	  dealer.dibujarCarta(carta);	
   }
}
