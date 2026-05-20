package modelo;

import modelo.enums.Estado;
import modelo.enums.Posicion;
import modelo.enums.TipoCarta;


public class Magia extends Carta implements Activable {

    public Magia(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }


    @Override
    public void ejecutarEfecto(Jugador usuario, Jugador oponente) {
        String nombre = getNombre();

        if (nombre.equals("Olla de la codicia")) {
            usuario.robarCarta();
            usuario.robarCarta();
        }
        else if (nombre.equals("Hinotama")) {
            oponente.setVida((short)(oponente.getVida() - 500));
        }
        else if (nombre.equals("Agujero Oscuro")) {
            destruirTodosLosMonstruos(usuario, oponente);
        }
        else if (nombre.equals("Dian Keto, el Señora de la Curación")) {
            usuario.setVida((short)(usuario.getVida() + 1000));
        }
        else if (nombre.equals("Tormenta Fuerte")) {
            destruirTodasMagiasTrompas(usuario, oponente);
        }
    }



    /** Monstruo Renacido: revive la carta del cementerio en el índice dado. */
    public void ejecutarMonstruoRenacido(Jugador usuario, int indiceCementerio) {
        Carta carta = usuario.getCementerio()[indiceCementerio];
        if (carta == null || !(carta instanceof Mounstro)) return;
        usuario.getCementerio()[indiceCementerio] = null;
        for (int i = 0; i < usuario.getCampo().length; i++) {
            if (usuario.getCampo()[i] == null) {
                usuario.getCampo()[i] = carta;
                carta.setEstado(Estado.CAMPO);
                ((Mounstro) carta).setPosicion(Posicion.ATAQUE);
                break;
            }
        }
    }

    /** Tifón del Espacio Místico: destruye la carta en el campo de mágicas del oponente. */
    public void ejecutarTifon(Jugador oponente, int indiceCampoMagia) {
        Carta c = oponente.getCampoMagias()[indiceCampoMagia];
        if (c != null) {
            oponente.getCampoMagias()[indiceCampoMagia] = null;
            oponente.enviarAlCementerio(c);
        }
    }

    /** Caridad Elegante / Fuerza de Resabastecimiento: roba y descarta. */
    public void ejecutarRobarDescartar(Jugador usuario, int[] indicesDescartar) {
        usuario.robarCarta();
        usuario.robarCarta();
        usuario.robarCarta();
        for (int idx : indicesDescartar) {
            Carta c = usuario.getMano()[idx];
            if (c != null) {
                usuario.getMano()[idx] = null;
                usuario.enviarAlCementerio(c);
            }
        }
    }

    /** Reproducción de Hechizo: descarta 2 mágicas de la mano. */
    public void ejecutarReproduccionHechizo(Jugador usuario, int[] indicesMagicas) {
        for (int idx : indicesMagicas) {
            Carta c = usuario.getMano()[idx];
            if (c instanceof Magia) {
                usuario.getMano()[idx] = null;
                usuario.enviarAlCementerio(c);
            }
        }
    }

    /** Intercambio: intercambia una carta de cada mano. */
    public void ejecutarIntercambio(Jugador usuario, int idxUsuario,
                                     Jugador oponente, int idxOponente) {
        Carta c1 = usuario.getMano()[idxUsuario];
        Carta c2 = oponente.getMano()[idxOponente];
        usuario.getMano()[idxUsuario]   = c2;
        oponente.getMano()[idxOponente] = c1;
    }


    private void destruirTodosLosMonstruos(Jugador usuario, Jugador oponente) {
        for (int i = 0; i < usuario.getCampo().length; i++)
            if (usuario.getCampo()[i] != null) usuario.muereMounstro(i);
        for (int i = 0; i < oponente.getCampo().length; i++)
            if (oponente.getCampo()[i] != null) oponente.muereMounstro(i);
    }

    private void destruirTodasMagiasTrompas(Jugador usuario, Jugador oponente) {
        for (int i = 0; i < usuario.getCampoMagias().length; i++) {
            if (usuario.getCampoMagias()[i] != null) {
                usuario.enviarAlCementerio(usuario.getCampoMagias()[i]);
                usuario.getCampoMagias()[i] = null;
            }
        }
        for (int i = 0; i < oponente.getCampoMagias().length; i++) {
            if (oponente.getCampoMagias()[i] != null) {
                oponente.enviarAlCementerio(oponente.getCampoMagias()[i]);
                oponente.getCampoMagias()[i] = null;
            }
        }
    }

    @Override
    public void usar(Jugador jugador, Jugador oponente) {
        ejecutarEfecto(jugador, oponente);
        jugador.enviarAlCementerio(this);
    }
}