package modelo;

import modelo.enums.Estado;
import modelo.enums.Posicion;
import modelo.enums.TipoCarta;

public class Mounstro extends Carta {

    private short    ataque;
    private short    defensa;
    private Estrellas estrellas;
    private Posicion posicion;
    private boolean  paralizado = false;
    private boolean  yaAtaco    = false;

    public enum Estrellas {
        Una, Dos, Tres, Cuatro, Cinco, Seis, Siete, Ocho, Nueve, Diez
    }

    public Mounstro(String nombre, short ataque, short defensa,
                    TipoCarta tipo, Estrellas estrellas,
                    boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
        this.ataque    = ataque;
        this.defensa   = defensa;
        this.estrellas = estrellas;
        this.posicion  = Posicion.ATAQUE;
    }

    // ── Getters / Setters ────────────────────────────────────────────

public short getAtaque() {
    return ataque;
}

public void setAtaque(short a) {
    this.ataque = a;
}

public short getDefensa() {
    return defensa;
}

public void setDefensa(short d) {
    this.defensa = d;
}

public Estrellas getEstrellas() {
    return estrellas;
}

public void setEstrellas(Estrellas e) {
    this.estrellas = e;
}

public Posicion getPosicion() {
    return posicion;
}

public void setPosicion(Posicion p) {
    this.posicion = p;
}

public boolean isParalizado() {
    return paralizado;
}

public void setParalizado(boolean p) {
    this.paralizado = p;
}

public boolean isYaAtaco() {
    return yaAtaco;
}

public void setYaAtaco(boolean v) {
    this.yaAtaco = v;
}

    @Override
    public void usar(Jugador jugador, Jugador oponente) {
    }
}