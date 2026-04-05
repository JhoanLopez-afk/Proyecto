public abstract class Carta {
   private String nombre;
   private TipoCarta tipo;
   private boolean visible;
   private String efecto;
   private Estado estado;

    public Carta(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.visible = visible;
        this.efecto = efecto;
        this.estado = estado;
        }
        

public void setNombre(String nombre) {
    this.nombre = nombre;
}
public String getNombre() {
    return nombre;
}
public void setTipo(TipoCarta tipo) {
    this.tipo = tipo;
}
public TipoCarta getTipo() {
    return tipo;
}
public void setVisible(boolean visible) {
    this.visible = visible;
}
public boolean isVisible() {
    return visible;
}

public String getEfecto() {
    return efecto;
}

public void setEfecto(String efecto) {
    this.efecto = efecto;
}

public Estado getEstado() {
    return estado;
}

public void setEstado(Estado estado) {
    this.estado = estado;
}

//método usar
public abstract void usar(Jugador jugador, Jugador oponente);
}