import java.util.ArrayList;
import java.util.Collections;

public class App {
    public static void main(String[] args) throws Exception {
       ArrayList<Carta> cartas = FabricaCartas.crearCartas();
       //Esto se utiliza para reorganizar el arreglo aleatoriamente
       Collections.shuffle(cartas);

       Jugador j1 = new Jugador("Quevedo");
       Jugador j2 = new Jugador("Agapito");

       for (int i = 0; i < 20; i++){
        j1.agregarCartaABaraja(cartas.get(i));
       }
       for (int i = 20; i < 40; i++){
        j2.agregarCartaABaraja(cartas.get(i));
       }



    }
} 
