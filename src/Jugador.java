public class Jugador {
    private String nombreJugador;
    private short vida;
    private boolean turno;
    private Fases fase;
    private boolean ganador;
    private Carta[] mano;
    private Carta[] campoMonstruos;
    private Carta[] campoMagias;
    private Carta[] cementerio;
    private Carta[] baraja;
    private boolean cambioPosicion;


    private boolean yaInvoco;

    public Jugador(String nombreJugador, Carta[] barajaInicial) {
        this.nombreJugador = nombreJugador;
        this.vida = 8000;
        this.baraja = barajaInicial;
        this.mano = new Carta[6];
        this.campoMonstruos = new Carta[5];
        this.campoMagias = new Carta[5];
        this.cementerio = new Carta[20];
        this.ganador = false;
        this.yaInvoco = false;
        this.cambioPosicion = false;
    }

    public enum Fases {
        ROBO, PRINCIPAL, BATALLA, FINAL
    }

public short getVida() {
    return vida;
}

public void setVida(short vida) {
    this.vida = vida;
}

public String getNombreJugador() {
    return nombreJugador;
}

public boolean isGanador() {
    return ganador;
}

public void setGanador(boolean ganador) {
    this.ganador = ganador;
}

public Carta[] getMano() {
    return mano;
}

public Carta[] getCampo() {
    return campoMonstruos;
}

public Carta[] getCementerio() {
    return cementerio;
}

public Carta[] getBaraja() {
    return baraja;
}


    public void resetTurno() {
        yaInvoco = false;
        cambioPosicion = false;
    }

    public boolean tieneMonstruos() {
        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] != null) return true;
        }
        return false;
    }

 
    public void robarCarta() {
        Carta cartaNueva = null;
        int indice = -1;

        for (int i = 0; i < baraja.length; i++) {
            if (baraja[i] != null) {
                cartaNueva = baraja[i];
                indice = i;
                break;
            }
        }

        if (cartaNueva == null) {
            System.out.println(nombreJugador + " no puede robar, por ende perdiste");
            ganador = false;
            return;
        }

        for (int i = 0; i < mano.length; i++) {
            if (mano[i] == null) {
                mano[i] = cartaNueva;
                baraja[indice] = null;
                cartaNueva.setEstado(Estado.MANO);
                System.out.println(nombreJugador + " robó: " + cartaNueva.getNombre());
                return;
            }
        }

        System.out.println("Mano llena");
    }

    public void mostrarMano() {
        System.out.println("Mano de " + nombreJugador);
        for (int i = 0; i < mano.length; i++) {
            if (mano[i] != null) {
                System.out.println(i + ". " + mano[i].getNombre());
            }
        }
    }

   
    public void invocarMonstruo(int indice, Posicion posicion) {

        if (yaInvoco) {
            System.out.println("Ya invocaste este turno");
            return;
        }

        if (indice < 0 || indice >= mano.length || mano[indice] == null) {
            System.out.println("Posición inválida");
            return;
        }

        if (!(mano[indice] instanceof Mounstro)) {
            System.out.println("No es un monstruo");
            return;
        }

        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] == null) {
                Mounstro m = (Mounstro) mano[indice];
                m.setPosicion(posicion);
                campoMonstruos[i] = mano[indice];
                mano[indice] = null;
                yaInvoco = true;
                System.out.println("Invocaste: " + campoMonstruos[i].getNombre() + " en posición " + posicion);
                return;
            }
        }

        System.out.println("Campo lleno");
    }

 public void mostrarCampo() {
    System.out.println("=== MONSTRUOS ===");

    boolean vacio = true;

    for (int i = 0; i < campoMonstruos.length; i++) {
        if (campoMonstruos[i] != null) {

            if (campoMonstruos[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) campoMonstruos[i];

                System.out.println(i + ". [ " + m.getNombre() + " ]");
                System.out.println("    ATK: " + m.getAtaque());
                System.out.println("    DEF: " + m.getDefensa());
                System.out.println("   Pos: " + m.getPosicion());
            }

            vacio = false;
        }
    }

    if (vacio) {
        System.out.println("No hay monstruos en el campo");
    }

    System.out.println("=== MAGICAS ===");

    vacio = true;

    for (int i = 0; i < campoMagias.length; i++) {
        if (campoMagias[i] != null) {
            System.out.println(i + ". [ " + campoMagias[i].getNombre() + " ]");
            vacio = false;
        }
    }

    if (vacio) {
        System.out.println("No hay mágicas en el campo");
    }
}

    public void usarMagia(int indice, Jugador oponente) {

    if (indice < 0 || indice >= mano.length || mano[indice] == null) {
        System.out.println("Posición inválida");
        return;
    }

    if (!(mano[indice] instanceof Magia)) {
        System.out.println("No es mágica");
        return;
    }


    Carta magia = mano[indice];
    mano[indice] = null;

    magia.usar(this, oponente);


    for (int i = 0; i < cementerio.length; i++) {
        if (cementerio[i] == null) {
            cementerio[i] = magia;
            magia.setEstado(Estado.CEMENTERIO);
            break;
        }
    }
}

   
public void atacar(byte indiceMiMounstro, Jugador oponente, byte indiceObjetivo) {

    if (indiceMiMounstro < 0 || indiceMiMounstro >= campoMonstruos.length || campoMonstruos[indiceMiMounstro] == null) {
        System.out.println("No existe un monstruo en esa posición para atacar");
        return;
    }

    Mounstro atacante = (Mounstro) campoMonstruos[indiceMiMounstro];

    if (atacante.getPosicion() == Posicion.DEFENSA) {
    System.out.println("Un monstruo en defensa no puede atacar");
    return;
    }

    if (oponente.tieneMonstruos() && indiceObjetivo == -1) {
        System.out.println("No puedes hacer ataque directo si el rival tiene monstruos");
        return;
    }

    if (indiceObjetivo == -1) {
        oponente.setVida((short) (oponente.getVida() - atacante.getAtaque()));
        return;
    }

    if (indiceObjetivo < 0 || indiceObjetivo >= oponente.getCampo().length || oponente.getCampo()[indiceObjetivo] == null) {
        System.out.println("No existe un objetivo válido");
        return;
    }

    Mounstro defensor = (Mounstro) oponente.getCampo()[indiceObjetivo];

    if (defensor.getPosicion() == Posicion.ATAQUE) {

        if (atacante.getAtaque() > defensor.getAtaque()) {
            short daño = (short) (atacante.getAtaque() - defensor.getAtaque());
            oponente.setVida((short) (oponente.getVida() - daño));
            oponente.muereMounstro(indiceObjetivo);

        } else if (atacante.getAtaque() < defensor.getAtaque()) {
            short daño = (short) (defensor.getAtaque() - atacante.getAtaque());
            setVida((short) (getVida() - daño));
            muereMounstro(indiceMiMounstro);

        } else {
            muereMounstro(indiceMiMounstro);
            oponente.muereMounstro(indiceObjetivo);
        }

    } else {

        if (atacante.getAtaque() > defensor.getDefensa()) {
            oponente.muereMounstro(indiceObjetivo);

        } else if (atacante.getAtaque() < defensor.getDefensa()) {
            short daño = (short) (defensor.getDefensa() - atacante.getAtaque());
            setVida((short) (getVida() - daño));
        }
    }
}

    public void muereMounstro(int indice) {
        Carta c = campoMonstruos[indice];
        campoMonstruos[indice] = null;

        for (int i = 0; i < cementerio.length; i++) {
            if (cementerio[i] == null) {
                cementerio[i] = c;
                return;
            }
        }
    }

    public void cambiarPosicionMonstruo(int indice, Posicion nuevaPosicion) {
        if (cambioPosicion) {
            System.out.println("Ya cambiaste la posición de un monstruo este turno");
            return;
        }

        if (indice < 0 || indice >= campoMonstruos.length || campoMonstruos[indice] == null) {
            System.out.println("No existe un monstruo en esa posición para cambiar su posición");
            return;
        }

        Mounstro m = (Mounstro) campoMonstruos[indice];
        m.setPosicion(nuevaPosicion);
        cambioPosicion = true;
        System.out.println("Cambiaste la posición de " + m.getNombre() + " a " + nuevaPosicion);
    }
}