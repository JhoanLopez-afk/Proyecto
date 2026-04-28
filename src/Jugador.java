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
    private boolean ataqueNegado = false;
    private boolean battlePhaseTerminada = false;
    private boolean yaInvoco;
    private boolean trampasBloqueadas = false;
    private boolean magiaBloqueada = false;
    private int indiceAtacanteRival = -1;
    private int llamadaDeLosCondenados = -1;
    private int indiceTrampaLlamada = -1;

    public Jugador(String nombreJugador, Carta[] barajaInicial) {
        this.nombreJugador = nombreJugador;
        this.vida = 8000;
        this.baraja = barajaInicial;
        this.mano = new Carta[5];
        this.campoMonstruos = new Carta[5];
        this.campoMagias = new Carta[5];
        this.cementerio = new Carta[25];
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

public boolean isAtaqueNegado() {
    return ataqueNegado;
}

public void setAtaqueNegado(boolean ataqueNegado) {
    this.ataqueNegado = ataqueNegado;
}

public boolean isBattlePhaseTerminada() {
    return battlePhaseTerminada;
}

public void setBattlePhaseTerminada(boolean battlePhaseTerminada) {
    this.battlePhaseTerminada = battlePhaseTerminada;
}
public void setTrampasBloqueadas(boolean b) {
    this.trampasBloqueadas = b;
}
public boolean isTrampasBloqueadas() {
    return trampasBloqueadas;  
}
public void setMagiaBloqueada(boolean b) {
    this.magiaBloqueada = b;
}
public boolean isMagiaBloqueada() {
    return magiaBloqueada;
}
public int getIndiceAtacanteRival() {
    return indiceAtacanteRival;
}
public void setIndiceAtacanteRival(int indice) {
    this.indiceAtacanteRival = indice;
}
public int getLlamadaDeLosCondenados() {
    return llamadaDeLosCondenados;
}
public void setLlamadaDeLosCondenados(int indice) {
    this.llamadaDeLosCondenados = indice;
}
public int getIndiceTrampaLlamada() {
    return indiceTrampaLlamada;
}
public void setIndiceTrampaLlamada(int indice) {
    this.indiceTrampaLlamada = indice;
}

    public void resetTurno() {
        yaInvoco = false;
        cambioPosicion = false;
        trampasBloqueadas = false;
        magiaBloqueada = false;
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
                if (m.isParalizado()) {
                    System.out.println(" /Paralizado/ ");
                }
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

    if (oponente.isMagiaBloqueada()) {
         System.out.println("tu oponente bloqueo tu magia");
         oponente.setMagiaBloqueada(false);
         Carta magia = mano[indice];
         mano[indice] = null;
         for (int i = 0; i < cementerio.length; i++) {
                if (cementerio[i] == null) {
                    cementerio[i] = magia;
                    magia.setEstado(Estado.CEMENTERIO);
                    break;
                }
            }
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

    if (oponente.isAtaqueNegado()) {
        System.out.println("El ataque fue negado.");
        oponente.setAtaqueNegado(false);
        return;
    }
    if (indiceMiMounstro < 0 || indiceMiMounstro >= campoMonstruos.length || campoMonstruos[indiceMiMounstro] == null) {
        System.out.println("No existe un monstruo en esa posición para atacar");
        return;
    }

    Mounstro atacante = (Mounstro) campoMonstruos[indiceMiMounstro];

    if (atacante.isParalizado()) {
        System.out.println("El monstruo está paralizado y no puede atacar");
        return;
    }

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

        if (c instanceof Mounstro) {
            Mounstro m = (Mounstro) c;
            if (m.isParalizado()) {
                m.setParalizado(false);
            }
        }
        if (indice == llamadaDeLosCondenados && indiceTrampaLlamada != -1) {
            System.out.println("Llamada de los Condenados se destruye porque su monstruo fue destruido.");
            campoMagias[indiceTrampaLlamada] = null;
            llamadaDeLosCondenados = -1;
            indiceTrampaLlamada = -1;
        }
        for (int i = 0; i < cementerio.length; i++) {
            if (cementerio[i] == null) {
                cementerio[i] = c;
                c.setEstado(Estado.CEMENTERIO);
                return;
            }
        }
    }

    public Carta[] getCampoMagias() {
    return campoMagias;
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

        if (m.isParalizado()) { 
            System.out.println("El monstruo está paralizado y no puede cambiar de posición");
            return;
        }

        m.setPosicion(nuevaPosicion);
        cambioPosicion = true;
        System.out.println("Cambiaste la posición de " + m.getNombre() + " a " + nuevaPosicion);
    }
    public void activarTrampaAtaque(Jugador defensor, Jugador atacante, int indiceAtacante) {
        if (defensor.isTrampasBloqueadas()) {
            System.out.println("tu oponente bloqueo tus trampas");
            defensor.setTrampasBloqueadas(false);
            return;
        }
        for (int i = 0; i < defensor.getMano().length; i++) {
            if (defensor.getMano()[i] instanceof Trampa) {
                Trampa t = (Trampa) defensor.getMano()[i];
                System.out.println(defensor.getNombreJugador() + " activa trampa: " + t.getNombre());
                defensor.getMano()[i] = null;
                defensor.setIndiceAtacanteRival(indiceAtacante);
                t.ejecutarEfecto(defensor, atacante);
                for (int k = 0; k < defensor.getCementerio().length; k++) {
                    if (defensor.getCementerio()[k] == null) {
                        defensor.getCementerio()[k] = t;
                        t.setEstado(Estado.CEMENTERIO);
                        break;
                    }
                }
                return;
            }
        }
    }
    public void destruirTrampaLlamada() {
    if (indiceTrampaLlamada != -1) {
        Carta trampa = campoMagias[indiceTrampaLlamada];
        campoMagias[indiceTrampaLlamada] = null;
        if (llamadaDeLosCondenados != -1 && campoMonstruos[llamadaDeLosCondenados] != null) {
            System.out.println("El monstruo de Llamada de los Condenados es destruido.");
            muereMounstro(llamadaDeLosCondenados);
        }
        llamadaDeLosCondenados = -1;
        indiceTrampaLlamada = -1;

        if (trampa != null) {
            for (int i = 0; i < cementerio.length; i++) {
                if (cementerio[i] == null) {
                    cementerio[i] = trampa;
                    trampa.setEstado(Estado.CEMENTERIO);
                     break;
                }
            }
        }
    }
}
}