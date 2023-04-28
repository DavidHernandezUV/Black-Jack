package cartasGraficas;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class PrincipalCartas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		}catch(Exception e) {e.printStackTrace();}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BlackJackGUI gui = new BlackJackGUI();
			}		
		});
	}
}
