import java.util.ArrayList;
import java.util.Collections;

public class Juego {

    public static Jugador[] iniciarJuego(){
        ArrayList<Carta> BarajaDeCartas = FabricaCartas.crearCartas();
        Collections.shuffle(BarajaDeCartas);

        Carta[] barajaJugador1 = new Carta[20];
        Carta[] barajaJugador2 = new Carta[20];
        
        for(int i = 0; i < 40; i++){
            if(i<20){
                barajaJugador1[i] = BarajaDeCartas.get(i);
            }else{
                barajaJugador2[i-20] = BarajaDeCartas.get(i);
            }
        }

        Jugador Jugador1 = new Jugador("Quevedo", barajaJugador1);
        Jugador Jugador2 = new Jugador("Camilo", barajaJugador2);

        System.out.println("-----INICIA LA PELEA!!!-----");
        System.out.println("Combatiente 1: "+Jugador1.getNombreJugador()+" tiene su baraja lista.");
        System.out.println("Combatiente 2: "+Jugador2.getNombreJugador()+" tiene su baraja lista.");

        System.out.println(" ");
        System.out.println(" ");

        System.out.println("Creando sus manos iniciales... ");
        System.out.println(" ");
        for(int i = 0; i < 5; i++){
            Jugador1.robarCarta();
            Jugador2.robarCarta();
        }

        return new Jugador[]{Jugador1, Jugador2};
    }
}
