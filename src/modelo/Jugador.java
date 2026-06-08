package modelo;

import modelo.enums.Estado;
import modelo.enums.Posicion;
import modelo.estructuras.MazoBaraja;
import modelo.estructuras.ManoJugador;


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

    // ── Estructuras de datos ─────────────────────────────────────────
    private ManoJugador manoJugador;   // LinkedList — mano del jugador
    private MazoBaraja  mazo;          // Stack      — mazo de robo

    private Carta[] campoMonstruos;
    private Carta[] campoMagias;
    private Carta[] cementerio;

    public enum Fases { ROBO, PRINCIPAL, BATALLA, FINAL }


    public Jugador(String nombreJugador, MazoBaraja mazo) {
        this.nombreJugador  = nombreJugador;
        this.vida           = 8000;
        this.mazo           = mazo;
        this.manoJugador    = new ManoJugador();
        this.campoMonstruos = new Carta[5];
        this.campoMagias    = new Carta[5];
        this.cementerio     = new Carta[25];
        this.ganador        = false;
        this.yaInvoco       = false;
        this.cambioPosicion = false;
    }

    // ── Getters / Setters ────────────────────────────────────────────

    public String getNombreJugador() { return nombreJugador; }

    public short getVida() { return vida; }
    public void  setVida(short v) { this.vida = v; }

    public boolean isGanador() { return ganador; }
    public void    setGanador(boolean g) { this.ganador = g; }

    /**
     * Devuelve la mano como arreglo fijo de 5 posiciones para la vista.
     * Internamente la mano es una LinkedList (ManoJugador).
     */
    public Carta[] getMano() {
        return manoJugador.toArrayFijo(5);
    }

    /** Acceso directo a la LinkedList de la mano (para el controlador y las cartas). */
    public ManoJugador getManoLinkedList() {
        return manoJugador;
    }

    /** Acceso directo al Stack del mazo (para GestorArchivos). */
    public MazoBaraja getMazo() {
        return mazo;
    }

    /**
     * Devuelve el mazo como arreglo para compatibilidad con GestorArchivos.
     */
    public Carta[] getBaraja() {
        return mazo.toArray();
    }

    public Carta[] getCampo()       { return campoMonstruos; }
    public Carta[] getCampoMagias() { return campoMagias; }
    public Carta[] getCementerio()  { return cementerio; }

    public boolean isAtaqueNegado()           { return ataqueNegado; }
    public void    setAtaqueNegado(boolean b) { this.ataqueNegado = b; }

    public boolean isBattlePhaseTerminada()           { return battlePhaseTerminada; }
    public void    setBattlePhaseTerminada(boolean b) { this.battlePhaseTerminada = b; }

    public boolean isTrampasBloqueadas()           { return trampasBloqueadas; }
    public void    setTrampasBloqueadas(boolean b) { this.trampasBloqueadas = b; }

    public boolean isMagiaBloqueada()           { return magiaBloqueada; }
    public void    setMagiaBloqueada(boolean b) { this.magiaBloqueada = b; }

    public int getIndiceAtacanteRival()       { return indiceAtacanteRival; }
    public void setIndiceAtacanteRival(int i) { this.indiceAtacanteRival = i; }

    public int getLlamadaDeLosCondenados()        { return llamadaDeLosCondenados; }
    public void setLlamadaDeLosCondenados(int i)  { this.llamadaDeLosCondenados = i; }

    public int getIndiceTrampaLlamada()       { return indiceTrampaLlamada; }
    public void setIndiceTrampaLlamada(int i) { this.indiceTrampaLlamada = i; }

    public boolean isYaInvoco() { return yaInvoco; }


    // ── Lógica de turno ──────────────────────────────────────────────

    public void resetTurno() {
        yaInvoco          = false;
        cambioPosicion    = false;
        trampasBloqueadas = false;
        magiaBloqueada    = false;
        for (Carta c : campoMonstruos)
            if (c instanceof Mounstro) ((Mounstro) c).setYaAtaco(false);
    }

    /**
     * Roba una carta del tope del mazo (Stack) y la agrega a la mano (LinkedList).
     * Si el mazo está vacío el jugador pierde (vida = 0).
     */
    public String robarCarta() {
        Carta carta = mazo.robar();          // pop() del Stack
        if (carta == null) {
            this.vida = 0;                   // baraja vacía → pierde
            return null;
        }
        if (manoJugador.estaLlena()) return null; // mano llena (10 cartas)
        carta.setEstado(Estado.MANO);
        manoJugador.agregarCarta(carta);     // addLast() de la LinkedList
        return carta.getNombre();
    }

    /**
     * Invoca un monstruo de la mano (1-4 estrellas).
     *
     * CORRECCIÓN: guarda referencia a la carta ANTES de llamar
     * vaciarPosicion(), porque vaciarPosicion ahora hace remove()
     * y la lista se encoge — si primero ponemos en campo y luego
     * borramos, el índice puede apuntar a otra carta.
     */
    public boolean invocarMonstruo(int indiceMano, Posicion posicion) {
        if (yaInvoco) return false;

        // 1. Leer la carta ANTES de tocar la lista
        Carta carta = manoJugador.getCarta(indiceMano);
        if (carta == null || !(carta instanceof Mounstro)) return false;

        // 2. Buscar espacio en el campo
        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] == null) {
                Mounstro m = (Mounstro) carta;
                m.setPosicion(posicion);
                m.setEstado(Estado.CAMPO);

                // 3. Eliminar de la mano ANTES de ponerla en campo
                //    (vaciarPosicion hace remove(), así que la lista se encoge aquí)
                manoJugador.vaciarPosicion(indiceMano);

                // 4. Poner en campo
                campoMonstruos[i] = carta;
                yaInvoco = true;
                return true;
            }
        }
        return false; // campo lleno
    }

    /**
     * Invoca por sacrificio (5+ estrellas).
     * Mismo patrón de corrección que invocarMonstruo.
     */
    public boolean invocarMonstruoConSacrificio(int indiceMano, Posicion posicion) {
        // 1. Leer la carta ANTES de tocar la lista
        Carta carta = manoJugador.getCarta(indiceMano);
        if (carta == null || !(carta instanceof Mounstro)) return false;

        // 2. Buscar espacio en el campo
        for (int i = 0; i < campoMonstruos.length; i++) {
            if (campoMonstruos[i] == null) {
                Mounstro m = (Mounstro) carta;
                m.setPosicion(posicion);
                m.setEstado(Estado.CAMPO);

                // 3. Eliminar de la mano primero
                manoJugador.vaciarPosicion(indiceMano);

                // 4. Poner en campo
                campoMonstruos[i] = carta;
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

    /**
     * Coloca una mágica/trampa boca abajo en el campo.
     * Mismo patrón de corrección: leer carta primero, eliminar de mano,
     * luego colocar en campo.
     */
    public boolean colocarEnCampoMagiaTrampa(int indiceMano) {
        // 1. Leer la carta ANTES de tocar la lista
        Carta carta = manoJugador.getCarta(indiceMano);
        if (carta == null) return false;
        if (!(carta instanceof Magia) && !(carta instanceof Trampa)) return false;

        // 2. Buscar espacio en el campo de magias
        for (int i = 0; i < campoMagias.length; i++) {
            if (campoMagias[i] == null) {
                // 3. Eliminar de la mano primero
                manoJugador.vaciarPosicion(indiceMano);

                // 4. Poner en campo
                campoMagias[i] = carta;
                carta.setEstado(Estado.CAMPO);
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
        } else {
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
            case Una:    return 1;  case Dos:    return 2;
            case Tres:   return 3;  case Cuatro: return 4;
            case Cinco:  return 5;  case Seis:   return 6;
            case Siete:  return 7;  case Ocho:   return 8;
            case Nueve:  return 9;  case Diez:   return 10;
            default:     return 0;
        }
    }
}