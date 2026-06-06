package vista;

import controlador.ControladorJuego;
import controlador.GestorArchivos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
  Pantalla de inicio del juego.
  Permite nuevo duelo, cargar partida y ver estadísticas.
 **/
public class MenuPrincipal extends JFrame {

    private JButton btnNuevoDuelo;
    private JButton btnCargarPartida;
    private JButton btnEstadisticas;
    private JButton btnSalir;
    private JLabel  lblPartidaGuardada;

    public MenuPrincipal() {
        setTitle("Yu-Gi-Oh! - Menú Principal");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        construirUI();
        verificarPartidaGuardada();
    }

    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(new Color(15, 30, 70));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ── Título ────────────────────────────────────────────────────
        JPanel panelTitulo = new JPanel(new GridLayout(3, 1, 0, 5));
        panelTitulo.setBackground(new Color(15, 30, 70));

        JLabel lblTitulo = new JLabel("YU-GI-OH!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblTitulo.setForeground(new Color(255, 215, 0));

        JLabel lblSubtitulo = new JLabel("¡Es hora de duelo!", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.ITALIC, 16));
        lblSubtitulo.setForeground(Color.WHITE);

        lblPartidaGuardada = new JLabel("", SwingConstants.CENTER);
        lblPartidaGuardada.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblPartidaGuardada.setForeground(new Color(100, 200, 255));

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);
        panelTitulo.add(lblPartidaGuardada);

        // ── Botones ───────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 12));
        panelBotones.setBackground(new Color(15, 30, 70));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnNuevoDuelo    = crearBoton("  Nuevo Duelo",        new Color(0, 120, 60));
        btnCargarPartida = crearBoton("  Cargar Partida",     new Color(0, 80, 160));
        btnEstadisticas  = crearBoton("  Estadísticas",       new Color(100, 50, 150));
        btnSalir         = crearBoton("  Salir",              new Color(100, 30, 30));

        btnNuevoDuelo.addActionListener(e    -> accionNuevoDuelo());
        btnCargarPartida.addActionListener(e -> accionCargarPartida());
        btnEstadisticas.addActionListener(e  -> accionEstadisticas());
        btnSalir.addActionListener(e         -> System.exit(0));

        panelBotones.add(btnNuevoDuelo);
        panelBotones.add(btnCargarPartida);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnSalir);

        raiz.add(panelTitulo,  BorderLayout.NORTH);
        raiz.add(panelBotones, BorderLayout.CENTER);

        add(raiz);
    }

    private void verificarPartidaGuardada() {
        if (GestorArchivos.existePartidaGuardada()) {
            lblPartidaGuardada.setText("✔ Hay una partida guardada disponible");
            btnCargarPartida.setEnabled(true);
        } else {
            lblPartidaGuardada.setText("No hay partida guardada");
            btnCargarPartida.setEnabled(false);
        }
    }

    // ── Acciones ──────────────────────────────────────────────────────

    private void accionNuevoDuelo() {
        String n1 = JOptionPane.showInputDialog(this,
            "Nombre del Duelista 1:", "Nuevo Duelo", JOptionPane.QUESTION_MESSAGE);
        if (n1 == null || n1.trim().isEmpty()) n1 = "Jugador 1";

        String n2 = JOptionPane.showInputDialog(this,
            "Nombre del Duelista 2:", "Nuevo Duelo", JOptionPane.QUESTION_MESSAGE);
        if (n2 == null || n2.trim().isEmpty()) n2 = "Jugador 2";

        final String nombre1 = n1.trim();
        final String nombre2 = n2.trim();

        JOptionPane.showMessageDialog(this,
            "¡Que comience el duelo!\n\n" + nombre1 + " vs " + nombre2,
            "¡Es hora de duelo!", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        SwingUtilities.invokeLater(() -> {
            VentanaJuego vista = new VentanaJuego();
            ControladorJuego controlador = new ControladorJuego(vista);
            controlador.iniciarNuevo(nombre1, nombre2);
        });
    }

    private void accionCargarPartida() {
        try {
            GestorArchivos.EstadoPartida estado = GestorArchivos.cargarPartida();
            if (estado == null) {
                JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna partida guardada.",
                    "Sin partida", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int resp = JOptionPane.showConfirmDialog(this,
                "Cargar partida de:\n"
                + estado.jugador1.getNombreJugador() + " (LP: " + estado.jugador1.getVida() + ")"
                + " vs "
                + estado.jugador2.getNombreJugador() + " (LP: " + estado.jugador2.getVida() + ")"
                + "\n\nTurno actual: " + (estado.turnoDe == 1
                    ? estado.jugador1.getNombreJugador()
                    : estado.jugador2.getNombreJugador())
                + "\n¿Deseas continuar?",
                "Cargar Partida", JOptionPane.YES_NO_OPTION);

            if (resp != JOptionPane.YES_OPTION) return;

            dispose();
            SwingUtilities.invokeLater(() -> {
                VentanaJuego vista = new VentanaJuego();
                ControladorJuego controlador = new ControladorJuego(vista);
                controlador.iniciarDesdeGuardado(estado);
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar la partida:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEstadisticas() {
        try {
            String stats = GestorArchivos.generarEstadisticas();

            JTextArea area = new JTextArea(stats);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setBackground(new Color(15, 25, 50));
            area.setForeground(new Color(200, 220, 255));

            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(420, 320));

            JOptionPane.showMessageDialog(this, scroll,
                "Estadísticas históricas", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al leer estadísticas:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Utilidades ────────────────────────────────────────────────────

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }
}
