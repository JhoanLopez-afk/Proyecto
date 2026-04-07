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

   
    public void invocarMonstruo(int indice) {

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
                campoMonstruos[i] = mano[indice];
                mano[indice] = null;
                yaInvoco = true;
                System.out.println("Invocaste: " + campoMonstruos[i].getNombre());
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

    if (indiceMiMounstro < 0 || indiceMiMounstro >= campoMonstruos.length || this.campoMonstruos[indiceMiMounstro] == null) {
        System.out.println("No existe un monstruo en esa posición para atacar");
        return;
    }

    if (!(this.campoMonstruos[indiceMiMounstro] instanceof Mounstro)) {
        System.out.println("La carta en esa posición no es un monstruo");
        return;
    }

    Mounstro mounstroAtacante = (Mounstro) this.campoMonstruos[indiceMiMounstro];

    System.out.println(" -CAMPO ANTES DEL ATAQUE -");
    System.out.println("---- " + this.getNombreJugador() + " ----");
    this.mostrarCampo();
    System.out.println("---- " + oponente.getNombreJugador() + " ----");
    oponente.mostrarCampo();

    if (oponente.tieneMonstruos() && indiceObjetivo == -1) {
        System.out.println("No puedes hacer ataque directo si el rival tiene monstruos");
        return;
    }

    if (indiceObjetivo == -1 || !oponente.tieneMonstruos()) {
        System.out.println(" Ataque directo " + mounstroAtacante.getNombre() + " ataca con " + mounstroAtacante.getAtaque());
        oponente.setVida((short) (oponente.getVida() - mounstroAtacante.getAtaque()));
        System.out.println(oponente.getNombreJugador() + " pierde " + mounstroAtacante.getAtaque() + " LP");
        return;
    }

    if (indiceObjetivo < 0 || indiceObjetivo >= oponente.getCampo().length || oponente.getCampo()[indiceObjetivo] == null) {
        System.out.println("No existe un monstruo rival en esa posición");
        return;
    }

    Mounstro MounstroDefensor = (Mounstro) oponente.getCampo()[indiceObjetivo];

    System.out.println(mounstroAtacante.getNombre() + " (ATK " + mounstroAtacante.getAtaque() + ") ATACA A " + MounstroDefensor.getNombre() + " (ATK " + MounstroDefensor.getAtaque() + ")");

    if (mounstroAtacante.getAtaque() > MounstroDefensor.getAtaque()) {

        short daño = (short) (mounstroAtacante.getAtaque() - MounstroDefensor.getAtaque());
        oponente.setVida((short) (oponente.getVida() - daño));
        System.out.println(oponente.getNombreJugador() + " pierde " + daño + " LP");
        oponente.muereMounstro(indiceObjetivo);

    } else if (mounstroAtacante.getAtaque() < MounstroDefensor.getAtaque()) {

        short daño = (short) (MounstroDefensor.getAtaque() - mounstroAtacante.getAtaque());
        setVida((short) (getVida() - daño));
        System.out.println(getNombreJugador() + " pierde " + daño + " LP");
        muereMounstro(indiceMiMounstro);

    } else {

        System.out.println("¡Empate! Ambos monstruos son destruidos");
        muereMounstro(indiceMiMounstro);
        oponente.muereMounstro(indiceObjetivo);
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
}