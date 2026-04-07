public interface Activable {
    // Esto permite que cada magia haga algo distinto
    void ejecutarEfecto(Jugador dueño, byte miInd, Jugador oponente, byte contraInd);
}
