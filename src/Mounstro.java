public class Mounstro  extends Carta {
   private short ataque;
   private short defensa;
   private Estrellas estrellas;
    private Posicion posicion;
    public Mounstro(String nombre, short ataque, short defensa, TipoCarta tipo, Estrellas estrellas, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
        this.ataque = ataque;
        this.defensa = defensa;
        this.estrellas = estrellas;
        this.posicion = Posicion.ATAQUE;
        
    }
    public enum Estrellas{
    Una, Dos, Tres, Cuatro, Cinco, Seis,
    Siete, Ocho, Nueve, Diez
}
public void setAtaque(short ataque) {
    this.ataque = ataque;
}
public short getAtaque() {
    return ataque;
}
public void setDefensa(short defensa) {
    this.defensa = defensa;
}
public short getDefensa() {
    return defensa;
}
public void setEstrellas(Estrellas estrellas) {
    this.estrellas = estrellas;
}
public Posicion getPosicion() {
    return posicion;
}
public void setPosicion(Posicion posicion) {
    this.posicion = posicion;
}
public Estrellas getEstrellas() {
    return estrellas;
}
@Override
public void usar(Jugador jugador, Jugador oponente) {
    System.out.println("Se invoca " + getNombre());
}
}
