package modelo;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

public class Juego {

    public static Jugador[] iniciarJuego(){
        ArrayList<Carta> BarajaDeCartas = FabricaCartas.crearCartas();
        Collections.shuffle(BarajaDeCartas);

        Carta[] barajaJugador1 = new Carta[25];
        Carta[] barajaJugador2 = new Carta[25];
        
        for(int i = 0; i < 50; i++){
            if(i<25){
                barajaJugador1[i] = BarajaDeCartas.get(i);
            }else{
                barajaJugador2[i-25] = BarajaDeCartas.get(i);
            }
        }

        String[] nombres = pedirNombres();

        Jugador Jugador1 = new Jugador(nombres[0], barajaJugador1);
        Jugador Jugador2 = new Jugador(nombres[1], barajaJugador2);

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
    public static String[] pedirNombres() {

        String jugador1 = JOptionPane.showInputDialog(
                null,
                "Ingresa el nombre del Jugador 1:",
                "🃏 Bienvenidos Combatientes",
                JOptionPane.QUESTION_MESSAGE
        );

        String jugador2 = JOptionPane.showInputDialog(
                null,
                "Ingresa el nombre del Jugador 2:",
                "🃏 Bienvenidos Combatientes",
                JOptionPane.QUESTION_MESSAGE
        );

        // Validaciones
        if (jugador1 == null || jugador1.trim().isEmpty()) {
            jugador1 = "Jugador 1";
        }

        if (jugador2 == null || jugador2.trim().isEmpty()) {
            jugador2 = "Jugador 2";
        }

        JOptionPane.showMessageDialog(
                null,
                "⚔️ ¡Que comience el duelo!\n\n" +
                jugador1 + " vs " + jugador2,
                "Inicio del Juego",
                JOptionPane.INFORMATION_MESSAGE
        );

        return new String[]{jugador1, jugador2};
    }
}
