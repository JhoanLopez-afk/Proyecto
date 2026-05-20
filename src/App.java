import controlador.ControladorJuego;
import vista.VentanaJuego;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaJuego vista = new VentanaJuego();
            ControladorJuego controlador = new ControladorJuego(vista);
            controlador.iniciar();
        });
    }
}