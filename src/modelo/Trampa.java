package modelo;

import modelo.enums.Estado;
import modelo.enums.Posicion;
import modelo.enums.TipoCarta;


public class Trampa extends Carta implements Activable {

    public Trampa(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }

    @Override
    public void ejecutarEfecto(Jugador defensor, Jugador atacante) {
        String nombre = getNombre();

        if (nombre.equals("Fuerza de espejo")) {
            destruirMonstruosEnAtaque(atacante);
        }
        else if (nombre.equals("Negar Ataque")) {
            atacante.setAtaqueNegado(true);
            atacante.setBattlePhaseTerminada(true);
        }
        else if (nombre.equals("Juicio Solemne")) {
            short mitad = (short)(defensor.getVida() / 2);
            defensor.setVida((short)(defensor.getVida() - mitad));
            atacante.setAtaqueNegado(true);
            atacante.setMagiaBloqueada(true);
        }
        else if (nombre.equals("Agujero Trampa Sin Fondo")) {
            destruirMonstruosConAtaqueAlto(atacante);
        }
        else if (nombre.equals("Disruptor de Trampa")) {
            atacante.setTrampasBloqueadas(true);
        }
        else if (nombre.equals("cilindros mágicos")) {
            aplicarCilindros(defensor, atacante);
        }
    }

    /** Círculo Atahechizos: paraliza el monstruo en el índice dado. */
    public void ejecutarCirculoAtahechizos(Jugador oponente, int indice) {
        if (indice >= 0 && indice < oponente.getCampo().length
                && oponente.getCampo()[indice] instanceof Mounstro) {
            ((Mounstro) oponente.getCampo()[indice]).setParalizado(true);
        }
    }

    /**
     * Artilugio de Evacuación Compulsiva: devuelve monstruo a la mano.
     * CORRECCIÓN: usa getManoLinkedList().agregarCarta() en vez de
     * getMano()[i] = carta (que escribiría en una copia descartada).
     */
    public void ejecutarArtilugio(Jugador objetivo, int indice) {
        if (indice < 0 || indice >= objetivo.getCampo().length
                || !(objetivo.getCampo()[indice] instanceof Mounstro)) return;

        Carta carta = objetivo.getCampo()[indice];
        objetivo.getCampo()[indice] = null;
        ((Mounstro) carta).setParalizado(false);
        carta.setEstado(Estado.MANO);

        // CORRECCIÓN: agregar directamente a la LinkedList real
        objetivo.getManoLinkedList().agregarCarta(carta);
    }

    /**
     * Disruptor Mágico: descarta una carta de la mano del defensor.
     * CORRECCIÓN: usa getManoLinkedList().vaciarPosicion() en vez de
     * getMano()[idx] = null (que modificaría una copia).
     */
    public void ejecutarDisruptorMagico(Jugador defensor, int indiceDescarte, Jugador atacante) {
        // Obtener la carta del arreglo de vista para saber cuál es
        Carta c = defensor.getMano()[indiceDescarte];
        if (c != null) {
            // CORRECCIÓN: vaciar en la LinkedList real, no en la copia
            defensor.getManoLinkedList().vaciarPosicion(indiceDescarte);
            defensor.enviarAlCementerio(c);
        }
        atacante.setMagiaBloqueada(true);
    }

    /** Llamada de los Condenados: revive un monstruo del cementerio. */
    public void ejecutarLlamadaCondenados(Jugador jugador, int indiceCementerio,
                                           int espacioMonstruo, int espacioMagia) {
        Carta carta = jugador.getCementerio()[indiceCementerio];
        jugador.getCementerio()[indiceCementerio] = null;

        Mounstro m = (Mounstro) carta;
        m.setPosicion(Posicion.ATAQUE);
        jugador.getCampo()[espacioMonstruo] = carta;
        carta.setEstado(Estado.CAMPO);

        jugador.getCampoMagias()[espacioMagia] = this;
        this.setEstado(Estado.CAMPO);
        jugador.setLlamadaDeLosCondenados(espacioMonstruo);
        jugador.setIndiceTrampaLlamada(espacioMagia);
    }

    private void destruirMonstruosEnAtaque(Jugador atacante) {
        for (int i = 0; i < atacante.getCampo().length; i++) {
            if (atacante.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) atacante.getCampo()[i];
                if (m.getPosicion() == modelo.enums.Posicion.ATAQUE) {
                    atacante.muereMounstro(i);
                }
            }
        }
    }

    private void destruirMonstruosConAtaqueAlto(Jugador atacante) {
        for (int i = 0; i < atacante.getCampo().length; i++) {
            if (atacante.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) atacante.getCampo()[i];
                if (m.getAtaque() >= 1500) atacante.muereMounstro(i);
            }
        }
    }

    private void aplicarCilindros(Jugador defensor, Jugador atacante) {
        int indice = atacante.getIndiceAtacanteRival();
        if (indice < 0 || indice >= atacante.getCampo().length
                || !(atacante.getCampo()[indice] instanceof Mounstro)) return;
        Mounstro mAtacante = (Mounstro) atacante.getCampo()[indice];
        atacante.setVida((short)(atacante.getVida() - mAtacante.getAtaque()));
    }

    @Override
    public void usar(Jugador jugador, Jugador oponente) {
        ejecutarEfecto(jugador, oponente);
        jugador.enviarAlCementerio(this);
    }
}