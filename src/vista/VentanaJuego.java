package vista;

import controlador.ControladorJuego;
import modelo.Carta;
import modelo.Jugador;
import modelo.Magia;
import modelo.Mounstro;
import modelo.Trampa;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VentanaJuego extends JFrame implements IVistaJuego {

    private ControladorJuego controlador;

    private static final int ANCHO_CARTA = 85;
    private static final int ALTO_CARTA  = 115;

    private JButton[] slotsCampoJ1   = new JButton[5];
    private JButton[] slotsCampoJ2   = new JButton[5];
    private JButton[] slotsMagiasJ1  = new JButton[5];
    private JButton[] slotsMagiasJ2  = new JButton[5];
    private JButton[] slotsMano      = new JButton[5];

    private JButton btnInvocar, btnMagia, btnActivarMagia;
    private JButton btnAtacar, btnCambiarPos, btnFinTurno;
    private JButton btnCementerioJ1, btnCementerioJ2;
    private JButton btnGuardar;

    private JLabel      lblLPJ1, lblLPJ2;
    private JProgressBar barraVidaJ1, barraVidaJ2;
    private JLabel      lblTurno, lblFase, lblMano;
    private JTextArea   areaLog;

    public VentanaJuego() {
        setTitle("Yu-Gi-Oh! Duelo");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        construirUI();
    }

    @Override
    public void setControlador(ControladorJuego c) {
        this.controlador = c;
    }

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public String[] pedirNombresJugadores() {
        String n1 = JOptionPane.showInputDialog(null,
            "Ingresa el nombre del Jugador 1:", "Bienvenidos Combatientes",
            JOptionPane.QUESTION_MESSAGE);
        String n2 = JOptionPane.showInputDialog(null,
            "Ingresa el nombre del Jugador 2:", "Bienvenidos Combatientes",
            JOptionPane.QUESTION_MESSAGE);

        if (n1 == null || n1.trim().isEmpty()) n1 = "Jugador 1";
        if (n2 == null || n2.trim().isEmpty()) n2 = "Jugador 2";

        JOptionPane.showMessageDialog(null,
            "¡Que comience el duelo!\n\n" + n1 + " vs " + n2,
            "Inicio del Juego", JOptionPane.INFORMATION_MESSAGE);

        return new String[]{ n1, n2 };
    }

    @Override
    public void actualizarUI(Jugador j1, Jugador j2,
                             String nombreActivo, boolean primerTurno) {
        lblLPJ1.setText("LP: " + j1.getVida());
        lblLPJ2.setText("LP: " + j2.getVida());
        barraVidaJ1.setValue(Math.max(0, j1.getVida()));
        barraVidaJ2.setValue(Math.max(0, j2.getVida()));

        lblTurno.setText("Turno: " + nombreActivo);
        lblMano.setText("  Mano de " + nombreActivo + ":");

        actualizarCampo(slotsCampoJ1,  j1.getCampo());
        actualizarCampo(slotsCampoJ2,  j2.getCampo());
        actualizarCampo(slotsMagiasJ1, j1.getCampoMagias());
        actualizarCampo(slotsMagiasJ2, j2.getCampoMagias());

        Jugador activo = nombreActivo.equals(j1.getNombreJugador()) ? j1 : j2;
        actualizarMano(slotsMano, activo.getMano());

        btnAtacar.setEnabled(!primerTurno);
    }

    @Override
    public void agregarLog(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    @Override
    public void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo,
            JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo,
            JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public int pedirOpcion(String titulo, String mensaje, String[] opciones) {
        if (opciones == null || opciones.length == 0) return -1;
        String sel = (String) JOptionPane.showInputDialog(this,
            mensaje, titulo, JOptionPane.PLAIN_MESSAGE,
            null, opciones, opciones[0]);
        if (sel == null) return -1;
        for (int i = 0; i < opciones.length; i++)
            if (opciones[i].equals(sel)) return i;
        return -1;
    }

    @Override
    public boolean pedirConfirmacion(String titulo, String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, titulo,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public void mostrarFinJuego(String ganador, String perdedor) {
        String[] frases = {
            "¡El corazón de las cartas ha hablado!",
            "¡Es el poder del milenio!",
            "¡La baraja del campeón no miente!",
            "¡Cree en tus cartas y ellas creerán en ti!"
        };
        String frase = frases[(int)(Math.random() * frases.length)];
        JOptionPane.showMessageDialog(this,
            "¡¡" + ganador + " gana el duelo!!\n\n"
            + perdedor + " ha sido derrotado.\n\n\"" + frase + "\"",
            "¡Duelo terminado!", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout(5, 5));
        raiz.setBackground(new Color(15, 30, 70));
        raiz.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        lblLPJ1 = new JLabel("LP: 8000");
        lblLPJ2 = new JLabel("LP: 8000");
        barraVidaJ1 = new JProgressBar(0, 8000); barraVidaJ1.setValue(8000);
        barraVidaJ2 = new JProgressBar(0, 8000); barraVidaJ2.setValue(8000);

        raiz.add(crearPanelLP(lblLPJ2, barraVidaJ2, "Jugador 2", false), BorderLayout.NORTH);
        raiz.add(crearPanelCentro(),                                       BorderLayout.CENTER);
        raiz.add(crearPanelLP(lblLPJ1, barraVidaJ1, "Jugador 1", true),   BorderLayout.SOUTH);

        add(raiz);
    }

    private JPanel crearPanelLP(JLabel lbl, JProgressBar barra,
                                 String nombre, boolean esJ1) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        p.setBackground(new Color(10, 20, 50));
        p.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(new Color(255, 215, 0));

        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        barra.setPreferredSize(new Dimension(280, 18));
        barra.setForeground(esJ1 ? new Color(0, 120, 80) : new Color(200, 50, 50));
        barra.setBackground(new Color(40, 40, 40));

        p.add(lblNombre); p.add(lbl); p.add(barra);
        return p;
    }

    private JPanel crearPanelCentro() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(new Color(20, 60, 30));
        p.add(crearTablero(),      BorderLayout.CENTER);
        p.add(crearPanelDerecho(), BorderLayout.EAST);
        return p;
    }

    private JPanel crearTablero() {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(new Color(15, 30, 70));

        JPanel info = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 4));
        info.setBackground(new Color(10, 25, 60));
        lblTurno = new JLabel("Turno: ...");
        lblTurno.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTurno.setForeground(new Color(255, 215, 0));
        lblFase = new JLabel("Fase: Principal");
        lblFase.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFase.setForeground(Color.WHITE);
        info.add(lblTurno); info.add(lblFase);

        JPanel campos = new JPanel(new GridLayout(2, 1, 0, 5));
        campos.setBackground(new Color(15, 30, 70));
        campos.add(crearPanelCampo(false));
        campos.add(crearPanelCampo(true));

        p.add(info,           BorderLayout.NORTH);
        p.add(campos,         BorderLayout.CENTER);
        p.add(crearPanelMano(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearPanelCampo(boolean esJ1) {
        JPanel wrap = new JPanel(new BorderLayout(0, 2));
        wrap.setBackground(new Color(15, 30, 70));

        JLabel titulo = new JLabel(esJ1 ? "  Tu campo:" : "  Campo rival:");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        titulo.setForeground(esJ1 ? new Color(100,255,150) : new Color(255,100,100));

        JPanel panelMonstruos = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 3));
        panelMonstruos.setBackground(new Color(10, 35, 80));
        panelMonstruos.setBorder(BorderFactory.createLineBorder(
            esJ1 ? new Color(0,80,180) : new Color(150,0,0), 1));

        for (int i = 0; i < 5; i++) {
            JButton slot = crearSlotVacio();
            final int idx = i;
            if (esJ1) {
                slotsCampoJ1[i] = slot;
                slot.addActionListener(e -> controlador.onSlotCampoPropio(idx));
            } else {
                slotsCampoJ2[i] = slot;
                slot.addActionListener(e -> controlador.onSlotCampoRival(idx));
            }
            panelMonstruos.add(slot);
        }

        JPanel panelMagias = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelMagias.setBackground(new Color(10, 35, 80));
        panelMagias.setBorder(BorderFactory.createLineBorder(
            esJ1 ? new Color(0,80,180) : new Color(150,0,0), 1));

        for (int i = 0; i < 5; i++) {
            JButton slot = crearSlotVacio();
            if (esJ1) slotsMagiasJ1[i] = slot;
            else      slotsMagiasJ2[i] = slot;
            panelMagias.add(slot);
        }

        wrap.add(titulo,       BorderLayout.NORTH);
        wrap.add(panelMonstruos, BorderLayout.CENTER);
        wrap.add(panelMagias,    BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel crearPanelMano() {
        JPanel wrap = new JPanel(new BorderLayout(0, 2));
        wrap.setBackground(new Color(15, 30, 70));

        lblMano = new JLabel("  Mano:");
        lblMano.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblMano.setForeground(new Color(255, 215, 0));

        JPanel panelSlots = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelSlots.setBackground(new Color(10, 20, 60));
        panelSlots.setBorder(BorderFactory.createLineBorder(new Color(255,215,0), 1));

        for (int i = 0; i < 5; i++) {
            JButton slot = crearSlotVacio();
            final int idx = i;
            slot.addActionListener(e -> controlador.onSeleccionarCartaMano(idx));
            slotsMano[i] = slot;
            panelSlots.add(slot);
        }

        wrap.add(lblMano,    BorderLayout.NORTH);
        wrap.add(panelSlots, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setPreferredSize(new Dimension(165, 0));
        panel.setBackground(new Color(10, 20, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelAcciones = new JPanel(new GridLayout(7, 1, 0, 5));
        panelAcciones.setBackground(new Color(10, 30, 15));

        btnInvocar      = crearBoton("Invocar monstruo",         new Color(0, 110, 50));
        btnMagia        = crearBoton("Colocar Mágica/Trampa",    new Color(20, 60, 170));
        btnActivarMagia = crearBoton("Activar Mágica",           new Color(0, 100, 180));
        btnAtacar       = crearBoton("Atacar",                   new Color(170, 20, 20));
        btnCambiarPos   = crearBoton("Cambiar posición",         new Color(110, 70, 0));
        btnFinTurno     = crearBoton("Fin de turno",             new Color(70, 70, 70));
        btnAtacar.setEnabled(false);

        btnInvocar.addActionListener(     e -> controlador.onInvocar());
        btnMagia.addActionListener(       e -> controlador.onColocarMagia());
        btnActivarMagia.addActionListener(e -> controlador.onActivarMagia());
        btnAtacar.addActionListener(      e -> controlador.onAtacar());
        btnCambiarPos.addActionListener(  e -> controlador.onCambiarPosicion());
        btnFinTurno.addActionListener(    e -> controlador.onFinTurno());

        panelAcciones.add(btnInvocar);
        panelAcciones.add(btnMagia);
        panelAcciones.add(btnActivarMagia);
        panelAcciones.add(btnAtacar);
        panelAcciones.add(btnCambiarPos);
        panelAcciones.add(btnFinTurno);
        btnGuardar = crearBoton("  Guardar partida", new Color(0, 100, 80));
        btnGuardar.addActionListener(e -> controlador.onGuardarPartida());
        panelAcciones.add(btnGuardar);

        JPanel panelCem = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCem.setBackground(new Color(10, 30, 15));

        btnCementerioJ1 = crearBoton("Cementerio J1", new Color(60, 30, 80));
        btnCementerioJ2 = crearBoton("Cementerio J2", new Color(60, 30, 80));
        btnCementerioJ1.addActionListener(e -> controlador.onVerCementerio(1));
        btnCementerioJ2.addActionListener(e -> controlador.onVerCementerio(2));
        panelCem.add(btnCementerioJ1);
        panelCem.add(btnCementerioJ2);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(new Color(5, 10, 30));
        areaLog.setForeground(new Color(180, 210, 255));
        areaLog.setFont(new Font("SansSerif", Font.PLAIN, 10));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0,80,180), 1),
            "Comentarios del juego", 0, 0,
            new Font("SansSerif", Font.BOLD, 10), new Color(0,200,80)));

        JPanel panelSup = new JPanel(new BorderLayout(0, 8));
        panelSup.setBackground(new Color(10, 30, 15));
        panelSup.add(panelAcciones, BorderLayout.NORTH);
        panelSup.add(panelCem,      BorderLayout.SOUTH);

        panel.add(panelSup, BorderLayout.NORTH);
        panel.add(scrollLog, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarCampo(JButton[] slots, Carta[] campo) {
        for (int i = 0; i < slots.length; i++) {
            if (campo[i] != null) {
                Carta c = campo[i];
                if (!c.isVisible() && !(c instanceof Mounstro)) {
                    slots[i].setText("<html><center>???<br>(Boca abajo)</center></html>");
                    slots[i].setBackground(new Color(50, 0, 80));
                    slots[i].setIcon(null);
                } else if (c instanceof Mounstro) {
                    Mounstro m = (Mounstro) c;
                    slots[i].setText("<html><center>"
                        + m.getNombre() + "<br>" + m.getPosicion()
                        + "<br>ATK:" + m.getAtaque()
                        + "<br>DEF:" + m.getDefensa()
                        + (m.isParalizado() ? "<br><font color='red'>PARALIZADO</font>" : "")
                        + "</center></html>");
                    slots[i].setBackground(m.getPosicion() == modelo.enums.Posicion.ATAQUE
                        ? new Color(60,20,20) : new Color(20,20,80));
                    cargarImagenEnSlot(slots[i], m.getNombre());
                } else {
                    slots[i].setText("<html><center>" + c.getNombre() + "</center></html>");
                    slots[i].setBackground(new Color(20, 40, 120));
                    slots[i].setIcon(null);
                }
            } else {
                resetSlot(slots[i]);
            }
        }
    }

    private void actualizarMano(JButton[] slots, Carta[] mano) {
        for (int i = 0; i < slots.length; i++) {
            if (mano[i] != null) {
                Carta c = mano[i];
                String tipo  = c instanceof Mounstro ? "M" : c instanceof Magia ? "Mg" : "T";
                String stats = "";
                if (c instanceof Mounstro) {
                    Mounstro m = (Mounstro) c;
                    stats = "<br>A:" + m.getAtaque() + " D:" + m.getDefensa();
                }
                slots[i].setText("<html><center>[" + tipo + "]<br>"
                    + c.getNombre() + stats + "</center></html>");
                slots[i].setBackground(c instanceof Mounstro ? new Color(40,80,40)
                    : c instanceof Magia ? new Color(20,40,120) : new Color(100,40,40));
                cargarImagenEnSlot(slots[i], c.getNombre());
            } else {
                resetSlot(slots[i]);
            }
        }
    }

    public void resaltarSlotMano(int indice) {
        for (int i = 0; i < slotsMano.length; i++)
            slotsMano[i].setBorder(BorderFactory.createLineBorder(
                i == indice ? Color.YELLOW : new Color(80,80,80),
                i == indice ? 3 : 1));
    }

    private void cargarImagenEnSlot(JButton slot, String nombreCarta) {
        try {
            String base = System.getProperty("user.dir");
            File img = new File(base + "/src/imagenes/" + nombreCarta + ".jpg");
            if (!img.exists()) img = new File(base + "/src/imagenes/" + nombreCarta + ".png");
            if (img.exists()) {
                java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(img);
                if (bi != null) {
                    slot.setIcon(new ImageIcon(
                        bi.getScaledInstance(ANCHO_CARTA, ALTO_CARTA, Image.SCALE_SMOOTH)));
                    slot.setText("");
                    return;
                }
            }
            slot.setIcon(null);
        } catch (Exception ex) {
            slot.setIcon(null);
        }
    }

    private JButton crearSlotVacio() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(ANCHO_CARTA, ALTO_CARTA));
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 9));
        btn.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 1));
        btn.setFocusPainted(false);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        return btn;
    }

    private void resetSlot(JButton slot) {
        slot.setText("");
        slot.setIcon(null);
        slot.setBackground(new Color(30, 30, 30));
        slot.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 1));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton("<html><center>" + texto + "</center></html>");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        return btn;
    }
    @Override
    public void mostrarBotonGuardar() {
    
    }
}