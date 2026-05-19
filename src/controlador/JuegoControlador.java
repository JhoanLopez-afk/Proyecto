package controlador;

import vista.VentanaJuego;

public class JuegoControlador {

    private VentanaJuego vista;

    public JuegoControlador() {
        vista = new VentanaJuego();
    }

    public void iniciarJuego() {
        vista.setVisible(true);
    }
}