package vista;

import modelo.Carta;
import modelo.Jugador;
import modelo.Magia;
import modelo.Mounstro;

public class ConsolaVista {

    public static void mostrarMano(Jugador jugador) {
        System.out.println("----- Mano de " + jugador.getNombreJugador() + " -----");

        Carta[] mano = jugador.getMano();

        for (int i = 0; i < mano.length; i++) {
            if (mano[i] != null) {

                if (mano[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) mano[i];
                    System.out.println(i + ". " + m.getNombre() +
                            " ATK: " + m.getAtaque() +
                            " DEF: " + m.getDefensa());
                } else if (mano[i] instanceof Magia) {
                    System.out.println(i + ". " + mano[i].getNombre() + " (Magia)");
                } else {System.out.println(i + ". " + mano[i].getNombre() + " (Trampa)");
            }
        }
    }
}
    
}
