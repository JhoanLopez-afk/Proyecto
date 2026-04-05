import java.util.ArrayList;

public class Jugador {
    private String nombreJugador;
    private short vida;
    private boolean turno;
    private Fases fase;
    private boolean ganador;
    private ArrayList<Carta> mano;
    private ArrayList<Carta>campo;
    private ArrayList<Carta> cementerio;
    private ArrayList<Carta> baraja;
    public Jugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.vida = 8000;   /// lo pongo por defecto, porque se supone que todos los jugadores inician con esta vida
        this.turno = false;
        this.fase = Fases.ROBO;
        this.ganador = false;
        this.mano = new ArrayList<>();
        this.campo = new ArrayList<>();
        this.cementerio = new ArrayList<>();
        this.baraja = new ArrayList<>();
    }
    public enum Fases{
    ROBO, PRINCIPAL , BATALLA, PRINCIPAL2,FINAL
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
    public Carta[] getBaraja() {
        return baraja;
    }
    public void setBaraja(Carta[] baraja) {
        this.baraja = baraja;
    }
    public Fases getFase() {
        return fase;
    }
    public void setFase(Fases fase) {
        this.fase = fase;
    }
}
