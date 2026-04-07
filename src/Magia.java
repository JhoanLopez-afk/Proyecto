import java.util.Scanner;
public class Magia extends Carta implements Activable {
    public Magia(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }

    @Override
    public void ejecutarEfecto(Jugador usuario, Jugador oponente) {

    Scanner sc = new Scanner(System.in);

    if (getNombre().equals("Olla de la codicia")) {
        usuario.robarCarta();
        usuario.robarCarta();
        System.out.println("Roba 2 cartas");
    }

    if (getNombre().equals("Hinotama")) {
        oponente.setVida((short)(oponente.getVida() - 500));
        System.out.println("Hace 500 de daño");
    }
    if (getNombre().equals("Monstruo renacido")) {

        System.out.println("Cementerio:");
        for (int i = 0; i < usuario.getCementerio().length; i++) {
            if(usuario.getCementerio()[i] != null){
                System.out.println(i + ": " + usuario.getCementerio()[i].getNombre());
            }
        }
        System.out.print("Elige carta a revivir: ");
        int opcion = sc.nextInt();

        Carta carta = usuario.getCementerio()[opcion];
        usuario.getCementerio()[opcion] = null;
        for(int i = 0; i < usuario.getCampo().length; i++){
            if(usuario.getCampo()[i] == null){
                usuario.getCampo()[i] = carta;
                carta.setEstado(Estado.CAMPO);
                break;
            }
        }
        System.out.println("Monstruo invocado");
    }
    if (getNombre().equals("Agujero Oscuro")) {
        for(int i = 0; i < usuario.getCampo().length; i++){
            if(usuario.getCampo()[i] != null){ usuario.muereMounstro(i); }
        }
        for(int i = 0; i < oponente.getCampo().length; i++){
            if(oponente.getCampo()[i] != null){ oponente.muereMounstro(i); }
        }
        System.out.println("Todos los monstruos destruidos");
    }
    if (getNombre().equals("Tifon del espacio Mistico")) {
        System.out.println("Campo del oponente:");
        for (int i = 0; i < oponente.getCampo().length; i++) {
            if(oponente.getCampo()[i] != null){
                System.out.println(i + ": " + oponente.getCampo()[i].getNombre());
            }
        }
        System.out.print("Elige carta a destruir: ");
        int opcion = sc.nextInt();
        oponente.muereMounstro(opcion);
        System.out.println("Carta destruida");
    }

    if (getNombre().equals("Caridad elegante")) {

        usuario.robarCarta();
        usuario.robarCarta();
        usuario.robarCarta();

        for (int i = 0; i < 2; i++) {
            System.out.println("Tu mano:");
            for (int j = 0; j < usuario.getMano().length; j++) {
                if(usuario.getMano()[j] != null){
                    System.out.println(j + ": " + usuario.getMano()[j].getNombre());
                }
            }
            System.out.print("Elige carta a descartar: ");
            int opcion = sc.nextInt();
            Carta descartada = usuario.getMano()[opcion];
            usuario.getMano()[opcion] = null;
            for(int k = 0; k < usuario.getCementerio().length; k++){
                if(usuario.getCementerio()[k] == null){
                    usuario.getCementerio()[k] = descartada;
                    descartada.setEstado(Estado.CEMENTERIO);
                    break;
                }
            }
        }
        System.out.println("Roba 3 y descarta 2");
    }

    if (getNombre().equals("Dian Keto, el Señora de la Curación")) {
    usuario.setVida((short)(usuario.getVida() + 1000));
    System.out.println("Recuperas 1000 puntos de vida");
    }

    if (getNombre().equals("Reproduccion de hechizo")) {

        int descartadas = 0;

        while (descartadas < 2) {
            System.out.println("Elige mágicas a descartar:");

            for (int i = 0; i < usuario.getMano().length; i++) {
                if(usuario.getMano()[i] != null){
                    System.out.println(i + ": " + usuario.getMano()[i].getNombre());
                }
            }

            int opcion = sc.nextInt();
            Carta c = usuario.getMano()[opcion];

            if (c instanceof Magia) {
                usuario.getMano()[opcion] = null;
                for(int k = 0; k < usuario.getCementerio().length; k++){
                    if(usuario.getCementerio()[k] == null){
                        usuario.getCementerio()[k] = c;
                        c.setEstado(Estado.CEMENTERIO);
                        break;
                    }
                }
                descartadas++;
            } else {
                System.out.println("Esa no es mágica");
            }
        }
        System.out.println("Descartaste 2 mágicas");
    }

    if (getNombre().equals("Intercambio")) {
        System.out.println("Tu mano:");
        for (int i = 0; i < usuario.getMano().length; i++) {
            if(usuario.getMano()[i] != null){
                System.out.println(i + ": " + usuario.getMano()[i].getNombre());
            }
        }
        System.out.print("Elige tu carta: ");
        int tuCarta = sc.nextInt();
        System.out.println("Mano del oponente:");
        for (int i = 0; i < oponente.getMano().length; i++) {
            if(oponente.getMano()[i] != null){
                System.out.println(i + ": " + oponente.getMano()[i].getNombre());
            }
        }
        System.out.print("Elige carta del oponente: ");
        int cartaOponente = sc.nextInt();
        Carta c1 = usuario.getMano()[tuCarta];
        Carta c2 = oponente.getMano()[cartaOponente];
        usuario.getMano()[tuCarta] = c2;
        oponente.getMano()[cartaOponente] = c1;
        System.out.println("Cartas intercambiadas");
    }
    }

    @Override
    public void usar(Jugador jugador, Jugador oponente) {
        ejecutarEfecto(jugador, oponente);
        for(int i = 0; i < jugador.getCementerio().length; i++){
            if(jugador.getCementerio()[i] == null){
                jugador.getCementerio()[i] = this;
                setEstado(Estado.CEMENTERIO);
                break;
            }
        }
    }
}
