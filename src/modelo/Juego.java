package modelo;

import java.util.ArrayList;
import java.util.Collections;


public class Juego {


    public static Jugador[] iniciarJuego(String nombre1, String nombre2) {
        ArrayList<Carta> pool = FabricaCartas.crearCartas();
        Collections.shuffle(pool);

        Carta[] baraja1 = new Carta[25];
        Carta[] baraja2 = new Carta[25];

        for (int i = 0; i < 50; i++) {
            if (i < 25) baraja1[i] = pool.get(i);
            else        baraja2[i - 25] = pool.get(i);
        }

        Jugador j1 = new Jugador(nombre1, baraja1);
        Jugador j2 = new Jugador(nombre2, baraja2);

        // Repartir mano inicial (5 cartas)
        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }

        return new Jugador[]{ j1, j2 };
    }
}