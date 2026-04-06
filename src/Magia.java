import java.util.Scanner;
public class Magia extends Carta implements Activable {
    public Magia(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }

public void activar(Jugador jugador, Jugador oponente) {

    Scanner sc = new Scanner(System.in);

    if (getNombre().equals("Olla de la codicia")) {
        jugador.robarCarta();
        jugador.robarCarta();
        System.out.println("Roba 2 cartas");
    }

    if (getNombre().equals("Hinotama")) {
        oponente.setVida((short)(oponente.getVida() - 500));
        System.out.println("Hace 500 de daño");
    }
    if (getNombre().equals("Monstruo renacido")) {

        System.out.println("Cementerio:");
        for (int i = 0; i < jugador.getCementerio().size(); i++) {
            System.out.println(i + ": " + jugador.getCementerio().get(i).getNombre());
        }
        System.out.print("Elige carta a revivir: ");
        int opcion = sc.nextInt();

        Carta carta = jugador.getCementerio().remove(opcion);
        jugador.getCampo().add(carta);
        carta.setEstado(Estado.CAMPO);
        System.out.println("Monstruo invocado");
    }
    if (getNombre().equals("Agujero Oscuro")) {
        jugador.getCampo().clear();
        oponente.getCampo().clear();
        System.out.println("Todos los monstruos destruidos");
    }
    if (getNombre().equals("Tifon del espacio Mistico")) {
        System.out.println("Campo del oponente:");
        for (int i = 0; i < oponente.getCampo().size(); i++) {
            System.out.println(i + ": " + oponente.getCampo().get(i).getNombre());
        }
        System.out.print("Elige carta a destruir: ");
        int opcion = sc.nextInt();
        Carta carta = oponente.getCampo().remove(opcion);
        oponente.getCementerio().add(carta);
        carta.setEstado(Estado.CEMENTERIO);
        System.out.println("Carta destruida");
    }

    if (getNombre().equals("Caridad elegante")) {

        jugador.robarCarta();
        jugador.robarCarta();
        jugador.robarCarta();

        for (int i = 0; i < 2; i++) {
            System.out.println("Tu mano:");
            for (int j = 0; j < jugador.getMano().size(); j++) {
                System.out.println(j + ": " + jugador.getMano().get(j).getNombre());
            }
            System.out.print("Elige carta a descartar: ");
            int opcion = sc.nextInt();
            Carta descartada = jugador.getMano().remove(opcion);
            jugador.getCementerio().add(descartada);
            descartada.setEstado(Estado.CEMENTERIO);
        }
        System.out.println("Roba 3 y descarta 2");
    }

    if (getNombre().equals("Dian Keto, el Señora de la Curación")) {
    jugador.setVida((short)(jugador.getVida() + 1000));
    System.out.println("Recuperas 1000 puntos de vida");
    }

    if (getNombre().equals("Reproduccion de hechizo")) {

        int descartadas = 0;

        while (descartadas < 2) {
            System.out.println("Elige mágicas a descartar:");

            for (int i = 0; i < jugador.getMano().size(); i++) {
                System.out.println(i + ": " + jugador.getMano().get(i).getNombre());
            }

            int opcion = sc.nextInt();
            Carta c = jugador.getMano().get(opcion);

            if (c instanceof Magia) {
                jugador.getMano().remove(opcion);
                jugador.getCementerio().add(c);
                c.setEstado(Estado.CEMENTERIO);
                descartadas++;
            } else {
                System.out.println("Esa no es mágica");
            }
        }
        System.out.println("Descartaste 2 mágicas");
    }


    if (getNombre().equals("Intercambio")) {
        System.out.println("Tu mano:");
        for (int i = 0; i < jugador.getMano().size(); i++) {
            System.out.println(i + ": " + jugador.getMano().get(i).getNombre());
        }
        System.out.print("Elige tu carta: ");
        int tuCarta = sc.nextInt();
        System.out.println("Mano del oponente:");
        for (int i = 0; i < oponente.getMano().size(); i++) {
            System.out.println(i + ": " + oponente.getMano().get(i).getNombre());
        }
        System.out.print("Elige carta del oponente: ");
        int cartaOponente = sc.nextInt();
        Carta c1 = jugador.getMano().get(tuCarta);
        Carta c2 = oponente.getMano().get(cartaOponente);
        jugador.getMano().set(tuCarta, c2);
        oponente.getMano().set(cartaOponente, c1);
        System.out.println("Cartas intercambiadas");
    }
}
    @Override
    public void usar(Jugador jugador, Jugador oponente) {
        activar(jugador, oponente);
        jugador.getCementerio().add(this);
        setEstado(Estado.CEMENTERIO);
        jugador.getCampo().remove(this);
}
}

