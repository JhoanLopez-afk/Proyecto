package modelo;

import java.util.ArrayList;
import java.util.Collections;
import modelo.estructuras.MazoBaraja;


public class Juego {


    public static Jugador[] iniciarJuego(String nombre1, String nombre2) {
        ArrayList<Carta> pool = FabricaCartas.crearCartas();
        Collections.shuffle(pool);

        MazoBaraja mazo1 = new MazoBaraja();
        MazoBaraja mazo2 = new MazoBaraja();

        for (int i = 0; i < 50 && i < pool.size(); i++) {
            if (i < 25) mazo1.agregarCarta(pool.get(i));
            else        mazo2.agregarCarta(pool.get(i));
        }

        Jugador j1 = new Jugador(nombre1, mazo1);
        Jugador j2 = new Jugador(nombre2, mazo2);

        // Repartir mano inicial (5 cartas)
        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }

        return new Jugador[]{ j1, j2 };
    }
}