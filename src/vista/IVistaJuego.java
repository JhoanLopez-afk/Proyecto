package vista;

import modelo.Carta;
import modelo.Jugador;

public interface IVistaJuego {

    void mostrar();

    void actualizarUI(Jugador jugador1, Jugador jugador2,
                      String nombreActivo, boolean primerTurno);

    void setControlador(controlador.ControladorJuego controlador);

    void agregarLog(String mensaje);

    void mostrarMensaje(String titulo, String mensaje);

    void mostrarError(String titulo, String mensaje);

    String[] pedirNombresJugadores();

    int pedirOpcion(String titulo, String mensaje, String[] opciones);

    boolean pedirConfirmacion(String titulo, String mensaje);

    void mostrarFinJuego(String ganador, String perdedor);
}
