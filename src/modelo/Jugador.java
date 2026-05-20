package modelo;

import modelo.enums.Estado;
import modelo.enums.Posicion;


public class Jugador {

    private String  nombreJugador;
    private short   vida;
    private boolean ganador;
    private boolean yaInvoco;
    private boolean cambioPosicion;
    private boolean ataqueNegado         = false;
    private boolean battlePhaseTerminada = false;
    private boolean trampasBloqueadas    = false;
    private boolean magiaBloqueada       = false;
    private int     indiceAtacanteRival  = -1;
    private int     llamadaDeLosCondenados = -1;
    private int     indiceTrampaLlamada  = -1;

    private Carta[] mano;
    private Carta[] campoMonstruos;
    private Carta[] campoMagias;
    private Carta[] cementerio;
    private Carta[] baraja;

    public enum Fases { ROBO, PRINCIPAL, BATALLA, FINAL }



    public Jugador(String nombreJugador, Carta[] barajaInicial) {
        this.nombreJugador = nombreJugador;
        this.vida          = 8000;
        this.baraja        = barajaInicial;
        this.mano          = new Carta[5];
        this.campoMonstruos= new Carta[5];
        this.campoMagias   = new Carta[5];
        this.cementerio    = new Carta[25];
        this.ganador       = false;
        this.yaInvoco      = false;
        this.cambioPosicion= false;
    }

    // ── Getters / Setters ────────────────────────────────────────────

public String getNombreJugador() {
    return nombreJugador;
}

public short getVida() {
    return vida;
}

public void setVida(short v) {
    this.vida = v;
}

public boolean isGanador() {
    return ganador;
}

public void setGanador(boolean g) {
    this.ganador = g;
}

public Carta[] getMano() {
    return mano;
}

public Carta[] getCampo() {
    return campoMonstruos;
}

public Carta[] getCampoMagias() {
    return campoMagias;
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

public void setAtaqueNegado(boolean b) {
    this.ataqueNegado = b;
}

public boolean isBattlePhaseTerminada() {
    return battlePhaseTerminada;
}

public void setBattlePhaseTerminada(boolean b) {
    this.battlePhaseTerminada = b;
}

public boolean isTrampasBloqueadas() {
    return trampasBloqueadas;
}

public void setTrampasBloqueadas(boolean b) {
    this.trampasBloqueadas = b;
}

public boolean isMagiaBloqueada() {
    return magiaBloqueada;
}

public void setMagiaBloqueada(boolean b) {
    this.magiaBloqueada = b;
}

public int getIndiceAtacanteRival() {
    return indiceAtacanteRival;
}

public void setIndiceAtacanteRival(int i) {
    this.indiceAtacanteRival = i;
}

public int getLlamadaDeLosCondenados() {
    return llamadaDeLosCondenados;
}

public void setLlamadaDeLosCondenados(int i) {
    this.llamadaDeLosCondenados = i;
}

public int getIndiceTrampaLlamada() {
    return indiceTrampaLlamada;
}

public void setIndiceTrampaLlamada(int i) {
    this.indiceTrampaLlamada = i;
}

public boolean isYaInvoco() {
    return yaInvoco;
}




    public void resetTurno() {
        yaInvoco         = false;
        cambioPosicion   = false;
        trampasBloqueadas= false;
        magiaBloqueada   = false;
        for (Carta c : campoMonstruos)
            if (c instanceof Mounstro) ((Mounstro) c).setYaAtaco(false);
    }


    public String robarCarta() {
        for (int i = 0; i < baraja.length; i++) {
            if (baraja[i] != null) {
                for (int j = 0; j < mano.length; j++) {
                    if (mano[j] == null) {
                        mano[j] = baraja[i];
                        baraja[i] = null;
                        mano[j].setEstado(Estado.MANO);
                        return mano[j].getNombre();
                    }
                }
                return null; // mano llena
            }
        }
        // baraja vacía → pierde
        this.vida = 0;
        return null;
    }

    /** Invoca un monstruo de la mano (1-4 estrellas). */
    public boolean invocarMonstruo(int indiceMano, Posicion posicion) {
        if (yaInvoco) return false;
        if (indiceMano < 0 || indiceMano >= mano.length || mano[indiceMano] == null) return false;
        if (!(mano[indiceMano] instanceof Mounstro)) return false;

        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] == null) {
                Mounstro m = (Mounstro) mano[indiceMano];
                m.setPosicion(posicion);
                campoMonstruos[i] = mano[indiceMano];
                mano[indiceMano]  = null;
                yaInvoco = true;
                return true;
            }
        }
        return false; // campo lleno
    }

    /** Invoca por sacrificio (5+ estrellas). El Controlador ya sacrificó los monstruos. */
    public boolean invocarMonstruoConSacrificio(int indiceMano, Posicion posicion) {
        if (indiceMano < 0 || indiceMano >= mano.length || mano[indiceMano] == null) return false;
        if (!(mano[indiceMano] instanceof Mounstro)) return false;

        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] == null) {
                Mounstro m = (Mounstro) mano[indiceMano];
                m.setPosicion(posicion);
                campoMonstruos[i] = mano[indiceMano];
                mano[indiceMano]  = null;
                yaInvoco = true;
                return true;
            }
        }
        return false;
    }

    /** Envía una carta al cementerio. */
    public void enviarAlCementerio(Carta c) {
        for (int i = 0; i < cementerio.length; i++) {
            if (cementerio[i] == null) {
                cementerio[i] = c;
                c.setEstado(Estado.CEMENTERIO);
                return;
            }
        }
    }

    /** Destruye el monstruo en la posición dada y lo manda al cementerio. */
    public void muereMounstro(int indice) {
        Carta c = campoMonstruos[indice];
        campoMonstruos[indice] = null;
        if (c instanceof Mounstro && ((Mounstro) c).isParalizado())
            ((Mounstro) c).setParalizado(false);

        if (indice == llamadaDeLosCondenados && indiceTrampaLlamada != -1) {
            campoMagias[indiceTrampaLlamada] = null;
            llamadaDeLosCondenados = -1;
            indiceTrampaLlamada    = -1;
        }
        enviarAlCementerio(c);
    }

    /** Coloca una mágica/trampa boca abajo en el campo. */
    public boolean colocarEnCampoMagiaTrampa(int indiceMano) {
        if (indiceMano < 0 || indiceMano >= mano.length || mano[indiceMano] == null) return false;
        if (!(mano[indiceMano] instanceof Magia) && !(mano[indiceMano] instanceof Trampa)) return false;

        for (int i = 0; i < campoMagias.length; i++) {
            if (campoMagias[i] == null) {
                campoMagias[i] = mano[indiceMano];
                mano[indiceMano] = null;
                campoMagias[i].setEstado(Estado.CAMPO);
                return true;
            }
        }
        return false; // campo lleno
    }

    /** Cambia la posición de un monstruo en el campo. */
    public boolean cambiarPosicionMonstruo(int indice, Posicion nuevaPosicion) {
        if (cambioPosicion) return false;
        if (indice < 0 || indice >= campoMonstruos.length || campoMonstruos[indice] == null) return false;

        Mounstro m = (Mounstro) campoMonstruos[indice];
        if (m.isParalizado()) return false;

        m.setPosicion(nuevaPosicion);
        cambioPosicion = true;
        return true;
    }


    public void atacar(byte indiceAtacante, Jugador oponente, byte indiceObjetivo) {
        if (indiceAtacante < 0 || indiceAtacante >= campoMonstruos.length
                || campoMonstruos[indiceAtacante] == null) return;

        Mounstro atacante = (Mounstro) campoMonstruos[indiceAtacante];
        if (atacante.isParalizado() || atacante.getPosicion() == Posicion.DEFENSA) return;

        if (indiceObjetivo == -1) {
            oponente.setVida((short)(oponente.getVida() - atacante.getAtaque()));
            return;
        }

        if (indiceObjetivo < 0 || indiceObjetivo >= oponente.getCampo().length
                || oponente.getCampo()[indiceObjetivo] == null) return;

        Mounstro defensor = (Mounstro) oponente.getCampo()[indiceObjetivo];

        if (defensor.getPosicion() == Posicion.ATAQUE) {
            if (atacante.getAtaque() > defensor.getAtaque()) {
                oponente.setVida((short)(oponente.getVida()
                        - (atacante.getAtaque() - defensor.getAtaque())));
                oponente.muereMounstro(indiceObjetivo);
            } else if (atacante.getAtaque() < defensor.getAtaque()) {
                setVida((short)(getVida() - (defensor.getAtaque() - atacante.getAtaque())));
                muereMounstro(indiceAtacante);
            } else {
                muereMounstro(indiceAtacante);
                oponente.muereMounstro(indiceObjetivo);
            }
        } else { // DEFENSA
            if (atacante.getAtaque() > defensor.getDefensa()) {
                oponente.muereMounstro(indiceObjetivo);
            } else if (atacante.getAtaque() < defensor.getDefensa()) {
                setVida((short)(getVida() - (defensor.getDefensa() - atacante.getAtaque())));
            }
        }
    }

    public void destruirTrampaLlamada() {
        if (indiceTrampaLlamada == -1) return;
        Carta trampa = campoMagias[indiceTrampaLlamada];
        campoMagias[indiceTrampaLlamada] = null;
        if (llamadaDeLosCondenados != -1 && campoMonstruos[llamadaDeLosCondenados] != null)
            muereMounstro(llamadaDeLosCondenados);
        llamadaDeLosCondenados = -1;
        indiceTrampaLlamada    = -1;
        if (trampa != null) enviarAlCementerio(trampa);
    }

    // ── Consultas ────────────────────────────────────────────────────

    public boolean tieneMonstruos() {
        for (Carta c : campoMonstruos) if (c != null) return true;
        return false;
    }

    public int contarMonstruos() {
        int c = 0;
        for (Carta carta : campoMonstruos) if (carta != null) c++;
        return c;
    }

    public int obtenerNumeroEstrellas(Mounstro.Estrellas e) {
        switch (e) {
            case Una:   return 1;  case Dos:   return 2;
            case Tres:  return 3;  case Cuatro:return 4;
            case Cinco: return 5;  case Seis:  return 6;
            case Siete: return 7;  case Ocho:  return 8;
            case Nueve: return 9;  case Diez:  return 10;
            default:    return 0;
        }
    }
}
