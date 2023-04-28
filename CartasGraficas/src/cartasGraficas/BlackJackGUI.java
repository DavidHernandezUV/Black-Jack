package cartasGraficas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BlackJackGUI extends JFrame {
	
	private MesaJuego mesa;
	private JPanel panelDealer, jugador;
	private PanelJugador dealer;
	private Baraja mazo;
	private JButton pedir,plantar;
	private JPanel panelBotones;
	private Escucha escucha; 
	private Random aleatorio;
	public boolean seguir=true;
	
	public BlackJackGUI() {
		initGUI();
		
		setTitle("BlackJack");
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initGUI() {
		// TODO Auto-generated method stub
		//Escucha
		escucha = new Escucha();
		mazo = new Baraja();
		aleatorio = new Random();
		//GUI
		mesa = new MesaJuego(this);
		add(mesa,BorderLayout.CENTER);
			
		pedir = new JButton("Carta");
		pedir.addActionListener(escucha);
		plantar = new JButton("Plantar");
		pedir.addActionListener(escucha);
		
		panelBotones = new JPanel();
		panelBotones.add(pedir);
		panelBotones.add(plantar);
		add(panelBotones,BorderLayout.SOUTH);
		
	}

	
	private class Escucha implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// TODO Auto-generated method stub
			//mesa.pintarJugada();	
			if(actionEvent.getSource()==pedir && seguir) {
				Carta carta = mazo.getCarta();
				mesa.pintarJugada(carta);
				seguir=false;
		    }
		
	    }
	}	
}
