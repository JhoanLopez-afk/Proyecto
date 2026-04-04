public class Jugador {
    private String nombreJugador;
    private short vida;
    private boolean turno;
    private Fases fase;
    private boolean ganador;
    private Carta[] mano;
    private Carta[] campo;
    private Carta[] cementerio;
    private Carta[] baraja;
    public Jugador(String nombreJugador, Carta[] barajaInicial) {
        this.nombreJugador = nombreJugador;
        this.vida = 8000;   /// lo pondo por defecto, porque se supone que todos los jugadores inician con esta vida
        this.baraja = barajaInicial;
        this.mano = new Carta[10];
        this.campo = new Carta[5];
        this.cementerio = new Carta[20];
        this.ganador = false;
    }
    public enum Fases{
    Una, Dos, Tres, Cuatro, Cinco, Seis,
    Siete, Ocho, Nueve, Diez
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
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    public boolean isTurno() {
        return turno;
    }
    public void setTurno(boolean turno) {
        this.turno = turno;
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
    public void setMano(Carta[] mano) {
        this.mano = mano;
    }
    public Carta[] getCampo() {
        return campo;
    }
    public void setCampo(Carta[] campo) {
        this.campo = campo;
    }
    public Carta[] getCementerio() {
        return cementerio;
    }
    public void setCementerio(Carta[] cementerio) {
        this.cementerio = cementerio;
    }
    public Fases getFase() {
        return fase;
    }
    public void setFase(Fases fase) {
        this.fase = fase;
    }
    public Carta[] getBaraja() {
        return baraja;
    }
    public void setBaraja(Carta[] baraja) {
        this.baraja = baraja;
    }
}
