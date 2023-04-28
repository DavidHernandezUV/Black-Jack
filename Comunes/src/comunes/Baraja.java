/* Autores: Jose David Barona Hernández - 1727590
 *                  Andrés Felipe Rincón    - 1922840
 * Correos: jose.david.barona@correounivalle.edu.co 
 *             andres.rincon.lopez@correounivalle.edu.co
 * Mini proyecto 4: Black Jack
 * Fecha: 16/12/2020
 * 
 * */
package comunes;


import java.util.ArrayList;
import java.util.Random;


// TODO: Auto-generated Javadoc
/**
 * The Class Baraja.
 */
public class Baraja {
   public static final String RUTA_FILE = "/recursos/cards.png";
   private ArrayList<Carta> mazo;
   private Random aleatorio;
   
   /**
    * Instantiates a new baraja.
    */
   public Baraja() {
	   aleatorio = new Random();
	   mazo = new ArrayList<Carta>();
	   String valor;
	   for(int i=1;i<=4;i++) {
		   for(int j=2;j<=14;j++) {
			   switch(j) {
			   case 11: valor="J";break;
			   case 12: valor="Q";break;
			   case 13: valor="K";break;
			   case 14: valor="As";break;
			   default: valor= String.valueOf(j);break;
			   } 
			   switch(i) {
			   case 1: mazo.add(new Carta(valor,"C"));break;
			   case 2: mazo.add(new Carta(valor,"D"));break;
			   case 3: mazo.add(new Carta(valor,"P"));break;
			   case 4: mazo.add(new Carta(valor,"T"));break;
			   }
		   }
	   }
	   asignarCoordenadas();
   }
   
   /**
    * Asignar coordenadas.
    * Método encargado de asignar las coordenadas de corte en x y y de la carta, para poder sacar la carta que se necesita del mazo
    */
   private void asignarCoordenadas() {   
	   
	    	int index=0;
		    for(int j=0;j<=180;j+=60){
		    	for(int i=0;i<=540;i+=45) {
		    		mazo.get(index).setCoordenadaX(i);//Asigna la coordenada de corte en X de la carta
		    		mazo.get(index).setCoordenadaY(j);//Asigna la coordenada de corte en Y de la carta
		    		index++;
		    	}
		    }
		    
  }

   /**
    * Gets the carta.
    *
    * @return the carta
    */
   public Carta getCarta() {
	   int index = aleatorio.nextInt(mazoSize());
	   Carta carta = mazo.get(index);
	   mazo.remove(index); //elimina del mazo la carta usada
	   return carta;
   }
   
   /**
    * Mazo size.
    *
    * @return the int
    */
   public int mazoSize() {
	   return mazo.size();
   }
}
