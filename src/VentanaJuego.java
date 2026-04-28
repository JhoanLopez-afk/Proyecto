import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VentanaJuego extends JFrame {

    //Estado del juego 
    private Jugador jugador1;
    private Jugador jugador2;
    private byte turnoDe = 1;
    private boolean primerTurno = true;
    private int cartaSeleccionadaEnMano = -1;

    // Constantes
    private static final int ANCHO_CARTA  = 85;
    private static final int ALTO_CARTA   = 115;

    //Slots del campo 
    private JButton[] slotsCampoJ1 = new JButton[5];
    private JButton[] slotsCampoJ2 = new JButton[5];

    // Slots de la mano
    private JButton[] slotsMano = new JButton[5];

    // ── Botones de acción ────────────────────────────────────────────
    private JButton btnInvocar;
    private JButton btnMagia;
    private JButton btnAtacar;
    private JButton btnCambiarPos;
    private JButton btnFinTurno;
    private JButton btnCementerioJ1;
    private JButton btnCementerioJ2;

    // ── Info LP ──────────────────────────────────────────────────────
    private JLabel lblLPJ1, lblLPJ2;
    private JProgressBar barraVidaJ1, barraVidaJ2;

    // ── Info turno ───────────────────────────────────────────────────
    private JLabel lblTurno;
    private JLabel lblFase;
    private JLabel lblMano;

    // ── Log ──────────────────────────────────────────────────────────
    private JTextArea areaLog;

    // ─────────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────
    public VentanaJuego() {
        Jugador[] combatientes = Juego.iniciarJuego();
        jugador1 = combatientes[0];
        jugador2 = combatientes[1];

        setTitle("Yu-Gi-Oh! Duelo: " + jugador1.getNombreJugador()
                + " vs " + jugador2.getNombreJugador());
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        construirUI();
        actualizarUI();

        agregarLog("¡El duelo ha comenzado!");
        agregarLog("Turno de " + jugador1.getNombreJugador());
    }

    // ─────────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE LA UI
    // ─────────────────────────────────────────────────────────────────
    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout(5, 5));
        raiz.setBackground(new Color(20, 60, 30));
        raiz.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        raiz.add(crearBarraLP(jugador2, false), BorderLayout.NORTH);
        raiz.add(crearPanelCentro(),            BorderLayout.CENTER);
        raiz.add(crearBarraLP(jugador1, true),  BorderLayout.SOUTH);

        add(raiz);
    }

    // ── Barra de LP ──────────────────────────────────────────────────
    private JPanel crearBarraLP(Jugador jugador, boolean esJ1) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        panel.setBackground(new Color(10, 30, 15));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));

        JLabel lblNombre = new JLabel(jugador.getNombreJugador());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(new Color(255, 215, 0));

        JLabel lblLP = new JLabel("LP: " + jugador.getVida());
        lblLP.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblLP.setForeground(Color.WHITE);

        JProgressBar barra = new JProgressBar(0, 8000);
        barra.setValue(jugador.getVida());
        barra.setPreferredSize(new Dimension(280, 18));
        barra.setForeground(esJ1 ? new Color(0, 200, 80) : new Color(200, 50, 50));
        barra.setBackground(new Color(40, 40, 40));

        if (esJ1) { lblLPJ1 = lblLP; barraVidaJ1 = barra; }
        else       { lblLPJ2 = lblLP; barraVidaJ2 = barra; }

        panel.add(lblNombre);
        panel.add(lblLP);
        panel.add(barra);
        return panel;
    }

    // ── Panel central ─────────────────────────────────────────────────
    private JPanel crearPanelCentro() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(20, 60, 30));
        panel.add(crearTablero(),      BorderLayout.CENTER);
        panel.add(crearPanelDerecho(), BorderLayout.EAST);
        return panel;
    }

    // ── Tablero: campo J2 + campo J1 + mano ──────────────────────────
    private JPanel crearTablero() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(new Color(20, 60, 30));

        // Info turno y fase
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 4));
        panelInfo.setBackground(new Color(10, 40, 20));
        lblTurno = new JLabel("Turno: " + jugador1.getNombreJugador());
        lblTurno.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTurno.setForeground(new Color(255, 215, 0));
        lblFase = new JLabel("Fase: Principal");
        lblFase.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFase.setForeground(Color.WHITE);
        panelInfo.add(lblTurno);
        panelInfo.add(lblFase);

        // Los dos campos
        JPanel campos = new JPanel(new GridLayout(2, 1, 0, 5));
        campos.setBackground(new Color(20, 60, 30));
        campos.add(crearPanelCampo(false)); // J2 arriba
        campos.add(crearPanelCampo(true));  // J1 abajo

        panel.add(panelInfo,       BorderLayout.NORTH);
        panel.add(campos,          BorderLayout.CENTER);
        panel.add(crearPanelMano(), BorderLayout.SOUTH);
        return panel;
    }

    // ── Panel de un campo (5 slots) ───────────────────────────────────
    private JPanel crearPanelCampo(boolean esJ1) {
        JPanel wrap = new JPanel(new BorderLayout(0, 2));
        wrap.setBackground(new Color(20, 60, 30));

        JLabel titulo = new JLabel(esJ1
            ? "  Tu campo (" + jugador1.getNombreJugador() + "):"
            : "  Campo rival (" + jugador2.getNombreJugador() + "):");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        titulo.setForeground(esJ1
            ? new Color(100, 255, 150)
            : new Color(255, 100, 100));

        JPanel panelSlots = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelSlots.setBackground(new Color(15, 50, 25));
        panelSlots.setBorder(BorderFactory.createLineBorder(
            esJ1 ? new Color(0, 150, 60) : new Color(150, 0, 0), 1));

        for (int i = 0; i < 5; i++) {
            JButton slot = crearSlotVacio();
            final int idx = i;
            if (esJ1) {
                slotsCampoJ1[i] = slot;
                slot.addActionListener(e -> seleccionarMonstruoPropio(idx));
            } else {
                slotsCampoJ2[i] = slot;
                slot.addActionListener(e -> seleccionarObjetivoAtaque(idx));
            }
            panelSlots.add(slot);
        }

        wrap.add(titulo,     BorderLayout.NORTH);
        wrap.add(panelSlots, BorderLayout.CENTER);
        return wrap;
    }

    // ── Panel de la mano ─────────────────────────────────────────────
    private JPanel crearPanelMano() {
        JPanel wrap = new JPanel(new BorderLayout(0, 2));
        wrap.setBackground(new Color(20, 60, 30));

        lblMano = new JLabel("  Mano de " + jugador1.getNombreJugador() + ":");
        lblMano.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblMano.setForeground(new Color(255, 215, 0));

        JPanel panelSlots = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelSlots.setBackground(new Color(10, 30, 50));
        panelSlots.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));

        for (int i = 0; i < 5; i++) {
            JButton slot = crearSlotVacio();
            final int idx = i;
            slot.addActionListener(e -> seleccionarCartaMano(idx));
            slotsMano[i] = slot;
            panelSlots.add(slot);
        }

        wrap.add(lblMano,    BorderLayout.NORTH);
        wrap.add(panelSlots, BorderLayout.CENTER);
        return wrap;
    }

    // ── Panel derecho: botones + cementerios + log ────────────────────
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setPreferredSize(new Dimension(165, 0));
        panel.setBackground(new Color(10, 30, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Botones de acción
        JPanel panelAcciones = new JPanel(new GridLayout(5, 1, 0, 5));
        panelAcciones.setBackground(new Color(10, 30, 15));

        btnInvocar    = crearBotonAccion("Invocar monstruo", new Color(0, 110, 50));
        btnMagia      = crearBotonAccion("Usar mágica",      new Color(20, 60, 170));
        btnAtacar     = crearBotonAccion("Atacar",           new Color(170, 20, 20));
        btnCambiarPos = crearBotonAccion("Cambiar posición", new Color(110, 70, 0));
        btnFinTurno   = crearBotonAccion("Fin de turno",     new Color(70, 70, 70));

        btnAtacar.setEnabled(false);

        btnInvocar.addActionListener(e    -> accionInvocar());
        btnMagia.addActionListener(e      -> accionMagia());
        btnAtacar.addActionListener(e     -> accionAtacar());
        btnCambiarPos.addActionListener(e -> accionCambiarPosicion());
        btnFinTurno.addActionListener(e   -> accionFinTurno());

        panelAcciones.add(btnInvocar);
        panelAcciones.add(btnMagia);
        panelAcciones.add(btnAtacar);
        panelAcciones.add(btnCambiarPos);
        panelAcciones.add(btnFinTurno);

        // Botones de cementerio
        JPanel panelCem = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCem.setBackground(new Color(10, 30, 15));

        btnCementerioJ1 = crearBotonAccion(
            "Cementerio " + jugador1.getNombreJugador(), new Color(60, 30, 80));
        btnCementerioJ2 = crearBotonAccion(
            "Cementerio " + jugador2.getNombreJugador(), new Color(60, 30, 80));

        btnCementerioJ1.addActionListener(e -> verCementerio(jugador1));
        btnCementerioJ2.addActionListener(e -> verCementerio(jugador2));

        panelCem.add(btnCementerioJ1);
        panelCem.add(btnCementerioJ2);

        // Log
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(new Color(5, 15, 10));
        areaLog.setForeground(new Color(180, 255, 180));
        areaLog.setFont(new Font("SansSerif", Font.PLAIN, 10));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 60), 1),
            "Comentarios del juego", 0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(0, 200, 80)));

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 8));
        panelSuperior.setBackground(new Color(10, 30, 15));
        panelSuperior.add(panelAcciones, BorderLayout.NORTH);
        panelSuperior.add(panelCem,      BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollLog,     BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────
    //  ACCIONES
    // ─────────────────────────────────────────────────────────────────

    private void accionInvocar() {
        if (cartaSeleccionadaEnMano == -1) {
            JOptionPane.showMessageDialog(this,
                "Primero selecciona un monstruo de tu mano.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jugador activo = jugadorActivo();
        Carta carta = activo.getMano()[cartaSeleccionadaEnMano];

        if (carta == null || !(carta instanceof Mounstro)) {
            JOptionPane.showMessageDialog(this,
                "La carta seleccionada no es un monstruo.",
                "No es monstruo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = {"ATAQUE", "DEFENSA"};
        int resp = JOptionPane.showOptionDialog(this,
            "¿En qué posición invocar a " + carta.getNombre() + "?",
            "Posición de invocación",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opciones, opciones[0]);

        if (resp == JOptionPane.CLOSED_OPTION) return;

        Posicion pos = (resp == 1) ? Posicion.DEFENSA : Posicion.ATAQUE;
        activo.invocarMonstruo(cartaSeleccionadaEnMano, pos);
        agregarLog(nombreActivo() + " invocó: " + carta.getNombre() + " en " + pos);

        cartaSeleccionadaEnMano = -1;
        actualizarUI();
        revisarFinJuego();
    }

    private void accionMagia() {
    if (cartaSeleccionadaEnMano == -1) {
        JOptionPane.showMessageDialog(this,
            "Primero selecciona una carta mágica de tu mano.",
            "Sin selección", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Jugador activo = jugadorActivo();
    Jugador rival  = jugadorRival();
    Carta carta = activo.getMano()[cartaSeleccionadaEnMano];

    if (carta == null || !(carta instanceof Magia)) {
        JOptionPane.showMessageDialog(this,
            "La carta seleccionada no es una mágica.",
            "No es mágica", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String nombre = carta.getNombre();
    agregarLog(nombreActivo() + " usó: " + nombre);

    // ── Magias sin selección ─────────────────────────────────────────
    if (nombre.equals("Olla de la codicia")) {
        activo.robarCarta();
        activo.robarCarta();
        agregarLog("Robaste 2 cartas.");
        moverMagiaCementerio(activo, cartaSeleccionadaEnMano);

    } else if (nombre.equals("Hinotama")) {
        rival.setVida((short)(rival.getVida() - 500));
        agregarLog(rival.getNombreJugador() + " recibe 500 de daño.");
        moverMagiaCementerio(activo, cartaSeleccionadaEnMano);

    } else if (nombre.equals("Dian Keto, el Señora de la Curación")) {
        activo.setVida((short)(activo.getVida() + 1000));
        agregarLog("Recuperaste 1000 LP.");
        moverMagiaCementerio(activo, cartaSeleccionadaEnMano);

    } else if (nombre.equals("Agujero Oscuro")) {
        for (int i = 0; i < activo.getCampo().length; i++) {
            if (activo.getCampo()[i] != null) activo.muereMounstro(i);
        }
        for (int i = 0; i < rival.getCampo().length; i++) {
            if (rival.getCampo()[i] != null) rival.muereMounstro(i);
        }
        agregarLog("Todos los monstruos fueron destruidos.");
        moverMagiaCementerio(activo, cartaSeleccionadaEnMano);

    } else if (nombre.equals("Tormenta Fuerte")) {
        for (int i = 0; i < activo.getCampoMagias().length; i++) {
            if (activo.getCampoMagias()[i] != null) {
                activo.getCementerio()[i] = activo.getCampoMagias()[i];
                activo.getCampoMagias()[i] = null;
            }
        }
        for (int i = 0; i < rival.getCampoMagias().length; i++) {
            if (rival.getCampoMagias()[i] != null) {
                rival.getCementerio()[i] = rival.getCampoMagias()[i];
                rival.getCampoMagias()[i] = null;
            }
        }
        agregarLog("Todas las mágicas y trampas fueron destruidas.");
        moverMagiaCementerio(activo, cartaSeleccionadaEnMano);

    // ── Magias con selección ─────────────────────────────────────────
    } else if (nombre.equals("Monstruo renacido")) {
        manejarMonstruoRenacido(activo, rival);
        return;

    } else if (nombre.equals("Caridad elegante")
            || nombre.equals("Fuerza de Resabastecimiento")) {
        manejarMagiaRobarDescartar(activo, 3, 2, false);
        return;

    } else if (nombre.equals("Reproduccion de hechizo")) {
        manejarMagiaRobarDescartar(activo, 0, 2, true);
        return;

    } else if (nombre.equals("Intercambio")) {
        manejarIntercambio(activo, rival);
        return;

    } else if (nombre.equals("Tifon del espacio Mistico")) {
        manejarTifon(activo, rival);
        return;
    }

    cartaSeleccionadaEnMano = -1;
    actualizarUI();
    revisarFinJuego();
}

private void manejarMonstruoRenacido(Jugador activo, Jugador rival) {
    Carta[] cem = activo.getCementerio();
    java.util.ArrayList<String>  ops = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> idx = new java.util.ArrayList<>();

    for (int i = 0; i < cem.length; i++) {
        if (cem[i] instanceof Mounstro) {
            ops.add(cem[i].getNombre());
            idx.add(i);
        }
    }
    if (ops.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "No hay monstruos en tu cementerio.",
            "Cementerio vacío", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    String sel = (String) JOptionPane.showInputDialog(this,
        "Selecciona el monstruo a revivir:", "Monstruo Renacido",
        JOptionPane.PLAIN_MESSAGE, null, ops.toArray(), ops.get(0));
    if (sel == null) return;

    int i = idx.get(ops.indexOf(sel));
    Carta revivir = cem[i];
    cem[i] = null;

    for (int j = 0; j < activo.getCampo().length; j++) {
        if (activo.getCampo()[j] == null) {
            activo.getCampo()[j] = revivir;
            revivir.setEstado(Estado.CAMPO);
            ((Mounstro) revivir).setPosicion(Posicion.ATAQUE);
            break;
        }
    }

    agregarLog(nombreActivo() + " revivió: " + revivir.getNombre());
    moverMagiaCementerio(activo, cartaSeleccionadaEnMano);
    cartaSeleccionadaEnMano = -1;
    actualizarUI();
}

private void manejarMagiaRobarDescartar(Jugador activo, int robar, int descartar, boolean soloMagias) {
    // Robar cartas primero
    for (int r = 0; r < robar; r++) activo.robarCarta();

    // Pedir al jugador que descarte
    for (int d = 0; d < descartar; d++) {
        java.util.ArrayList<String>  ops = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> idx = new java.util.ArrayList<>();

        for (int i = 0; i < activo.getMano().length; i++) {
            if (activo.getMano()[i] != null && i != cartaSeleccionadaEnMano) {
                if (!soloMagias || activo.getMano()[i] instanceof Magia) {
                    ops.add(activo.getMano()[i].getNombre());
                    idx.add(i);
                }
            }
        }

        if (ops.isEmpty()) break;

        String sel = (String) JOptionPane.showInputDialog(this,
            "Descarta una carta (" + (d + 1) + "/" + descartar + "):"
            + (soloMagias ? "\n(Solo puedes descartar mágicas)" : ""),
            "Descartar", JOptionPane.PLAIN_MESSAGE, null,
            ops.toArray(), ops.get(0));
        if (sel == null) break;

        int i = idx.get(ops.indexOf(sel));
        Carta descartada = activo.getMano()[i];
        activo.getMano()[i] = null;
        for (int k = 0; k < activo.getCementerio().length; k++) {
            if (activo.getCementerio()[k] == null) {
                activo.getCementerio()[k] = descartada;
                descartada.setEstado(Estado.CEMENTERIO);
                break;
            }
        }
        agregarLog("Descartaste: " + descartada.getNombre());
    }

    moverMagiaCementerio(activo, cartaSeleccionadaEnMano);
    cartaSeleccionadaEnMano = -1;
    actualizarUI();
}

private void manejarIntercambio(Jugador activo, Jugador rival) {
    // Elegir carta propia
    java.util.ArrayList<String>  opsProp = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> idxProp = new java.util.ArrayList<>();
    for (int i = 0; i < activo.getMano().length; i++) {
        if (activo.getMano()[i] != null && i != cartaSeleccionadaEnMano) {
            opsProp.add(activo.getMano()[i].getNombre());
            idxProp.add(i);
        }
    }
    if (opsProp.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No tienes cartas para intercambiar.");
        return;
    }
    String selP = (String) JOptionPane.showInputDialog(this,
        "Elige tu carta a entregar:", "Intercambio",
        JOptionPane.PLAIN_MESSAGE, null, opsProp.toArray(), opsProp.get(0));
    if (selP == null) return;
    int iP = idxProp.get(opsProp.indexOf(selP));

    // Elegir carta del rival
    java.util.ArrayList<String>  opsRiv = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> idxRiv = new java.util.ArrayList<>();
    for (int i = 0; i < rival.getMano().length; i++) {
        if (rival.getMano()[i] != null) {
            opsRiv.add(rival.getMano()[i].getNombre());
            idxRiv.add(i);
        }
    }
    if (opsRiv.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El rival no tiene cartas.");
        return;
    }
    String selR = (String) JOptionPane.showInputDialog(this,
        "Elige la carta del rival:", "Intercambio",
        JOptionPane.PLAIN_MESSAGE, null, opsRiv.toArray(), opsRiv.get(0));
    if (selR == null) return;
    int iR = idxRiv.get(opsRiv.indexOf(selR));

    // Hacer el intercambio
    Carta c1 = activo.getMano()[iP];
    Carta c2 = rival.getMano()[iR];
    activo.getMano()[iP] = c2;
    rival.getMano()[iR]  = c1;

    agregarLog(nombreActivo() + " intercambió " + c1.getNombre()
        + " por " + c2.getNombre());
    moverMagiaCementerio(activo, cartaSeleccionadaEnMano);
    cartaSeleccionadaEnMano = -1;
    actualizarUI();
}

private void manejarTifon(Jugador activo, Jugador rival) {
    java.util.ArrayList<String>  ops = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> idx = new java.util.ArrayList<>();

    for (int i = 0; i < rival.getCampo().length; i++) {
        if (rival.getCampo()[i] != null) {
            ops.add(rival.getCampo()[i].getNombre() + " [Monstruo]");
            idx.add(i);
        }
    }
    for (int i = 0; i < rival.getCampoMagias().length; i++) {
        if (rival.getCampoMagias()[i] != null) {
            ops.add(rival.getCampoMagias()[i].getNombre() + " [M/T]");
            idx.add(-(i + 1));
        }
    }

    if (ops.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "El rival no tiene cartas en el campo.",
            "Campo vacío", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    String sel = (String) JOptionPane.showInputDialog(this,
        "Elige la carta a destruir:", "Tifón del Espacio Místico",
        JOptionPane.PLAIN_MESSAGE, null, ops.toArray(), ops.get(0));
    if (sel == null) return;

    int i = idx.get(ops.indexOf(sel));
    if (i >= 0) {
        rival.muereMounstro(i);
        agregarLog("Destruiste un monstruo del rival.");
    } else {
        int im = -(i + 1);
        Carta c = rival.getCampoMagias()[im];
        rival.getCampoMagias()[im] = null;
        for (int k = 0; k < rival.getCementerio().length; k++) {
            if (rival.getCementerio()[k] == null) {
                rival.getCementerio()[k] = c;
                c.setEstado(Estado.CEMENTERIO);
                break;
            }
        }
        agregarLog("Destruiste una mágica/trampa del rival.");
    }

    moverMagiaCementerio(activo, cartaSeleccionadaEnMano);
    cartaSeleccionadaEnMano = -1;
    actualizarUI();
}

    private void accionAtacar() {
        Jugador activo = jugadorActivo();
        Jugador rival  = jugadorRival();

        java.util.ArrayList<String> opsAtac = new java.util.ArrayList<>();
        java.util.ArrayList<Byte>   idxAtac = new java.util.ArrayList<>();

        for (int i = 0; i < activo.getCampo().length; i++) {
            if (activo.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) activo.getCampo()[i];
                if (m.getPosicion() == Posicion.ATAQUE && !m.isParalizado()) {
                    opsAtac.add(m.getNombre() + "  ATK:" + m.getAtaque());
                    idxAtac.add((byte) i);
                }
            }
        }

        if (opsAtac.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No tienes monstruos disponibles para atacar.",
                "Sin atacantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selAtac = (String) JOptionPane.showInputDialog(this,
            "¿Con cuál monstruo atacas?", "Seleccionar atacante",
            JOptionPane.PLAIN_MESSAGE, null, opsAtac.toArray(), opsAtac.get(0));
        if (selAtac == null) return;

        byte indAtac = idxAtac.get(opsAtac.indexOf(selAtac));
        Mounstro mAtac = (Mounstro) activo.getCampo()[indAtac];

        if (rival.tieneMonstruos()) {
            java.util.ArrayList<String> opsObj = new java.util.ArrayList<>();
            java.util.ArrayList<Byte>   idxObj = new java.util.ArrayList<>();

            for (int i = 0; i < rival.getCampo().length; i++) {
                if (rival.getCampo()[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) rival.getCampo()[i];
                    opsObj.add(m.getNombre()
                        + " (" + m.getPosicion() + ")"
                        + "  ATK:" + m.getAtaque()
                        + "  DEF:" + m.getDefensa());
                    idxObj.add((byte) i);
                }
            }

            String selObj = (String) JOptionPane.showInputDialog(this,
                "¿A cuál monstruo atacas?", "Seleccionar objetivo",
                JOptionPane.PLAIN_MESSAGE, null, opsObj.toArray(), opsObj.get(0));
            if (selObj == null) return;

            byte indObj = idxObj.get(opsObj.indexOf(selObj));

            agregarLog(nombreActivo() + " ataca con " + mAtac.getNombre());
            rival.activarTrampaAtaque(rival, activo, indAtac);

            if (!activo.isAtaqueNegado()) {
                activo.atacar(indAtac, rival, indObj);
                agregarLog("LP " + rival.getNombreJugador() + ": " + rival.getVida());
            } else {
                agregarLog("¡El ataque fue negado!");
                activo.setAtaqueNegado(false);
            }

        } else {
            agregarLog(nombreActivo() + " hace ataque directo con " + mAtac.getNombre());
            rival.activarTrampaAtaque(rival, activo, indAtac);

            if (!activo.isAtaqueNegado()) {
                activo.atacar(indAtac, rival, (byte) -1);
                agregarLog("LP " + rival.getNombreJugador() + ": " + rival.getVida());
            } else {
                agregarLog("¡El ataque fue negado!");
                activo.setAtaqueNegado(false);
            }
        }

        actualizarUI();
        revisarFinJuego();
    }

    private void accionCambiarPosicion() {
        Jugador activo = jugadorActivo();

        java.util.ArrayList<String> ops = new java.util.ArrayList<>();
        java.util.ArrayList<Byte>   idx = new java.util.ArrayList<>();

        for (int i = 0; i < activo.getCampo().length; i++) {
            if (activo.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) activo.getCampo()[i];
                ops.add(m.getNombre() + " (" + m.getPosicion() + ")");
                idx.add((byte) i);
            }
        }

        if (ops.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No tienes monstruos en el campo.",
                "Sin monstruos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sel = (String) JOptionPane.showInputDialog(this,
            "¿Qué monstruo cambias de posición?", "Cambiar posición",
            JOptionPane.PLAIN_MESSAGE, null, ops.toArray(), ops.get(0));
        if (sel == null) return;

        byte indice = idx.get(ops.indexOf(sel));

        String[] posiciones = {"ATAQUE", "DEFENSA"};
        int resp = JOptionPane.showOptionDialog(this, "Nueva posición:", "Posición",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, posiciones, posiciones[0]);
        if (resp == JOptionPane.CLOSED_OPTION) return;

        Posicion nuevaPos = (resp == 1) ? Posicion.DEFENSA : Posicion.ATAQUE;
        activo.cambiarPosicionMonstruo(indice, nuevaPos);
        agregarLog(nombreActivo() + " cambió posición a " + nuevaPos);
        actualizarUI();
    }

    private void accionFinTurno() {
        agregarLog("--- Fin del turno de " + nombreActivo() + " ---");
        jugadorActivo().resetTurno();

        turnoDe = (turnoDe == 1) ? (byte) 2 : (byte) 1;
        primerTurno = false;
        cartaSeleccionadaEnMano = -1;

        jugadorActivo().robarCarta();
        agregarLog("Turno de " + nombreActivo());
        btnAtacar.setEnabled(true);
        actualizarUI();
    }

    private void verCementerio(Jugador jugador) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cementerio de ").append(jugador.getNombreJugador()).append(":\n\n");

        boolean vacio = true;
        for (Carta c : jugador.getCementerio()) {
            if (c != null) {
                String tipo = c instanceof Mounstro ? "[Monstruo]"
                            : c instanceof Magia    ? "[Mágica]" : "[Trampa]";
                sb.append(tipo).append(" ").append(c.getNombre()).append("\n");
                vacio = false;
            }
        }
        if (vacio) sb.append("(vacío)");

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 250));

        JOptionPane.showMessageDialog(this, scroll,
            "Cementerio de " + jugador.getNombreJugador(),
            JOptionPane.PLAIN_MESSAGE);
    }

    // ─────────────────────────────────────────────────────────────────
    //  SELECCIÓN CON CLICK EN SLOTS
    // ─────────────────────────────────────────────────────────────────

    private void seleccionarCartaMano(int indice) {
        cartaSeleccionadaEnMano = indice;
        for (int i = 0; i < slotsMano.length; i++) {
            slotsMano[i].setBorder(BorderFactory.createLineBorder(
                i == indice ? Color.YELLOW : new Color(80, 80, 80),
                i == indice ? 3 : 1));
        }
    }

    private void seleccionarMonstruoPropio(int indice) {
        for (int i = 0; i < slotsCampoJ1.length; i++) {
            slotsCampoJ1[i].setBorder(BorderFactory.createLineBorder(
                i == indice ? Color.CYAN : new Color(0, 100, 40), 1));
        }
    }

    private void seleccionarObjetivoAtaque(int indice) {
        for (int i = 0; i < slotsCampoJ2.length; i++) {
            slotsCampoJ2[i].setBorder(BorderFactory.createLineBorder(
                i == indice ? Color.RED : new Color(100, 0, 0), 1));
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  ACTUALIZACIÓN DE LA UI
    // ─────────────────────────────────────────────────────────────────

    private void actualizarUI() {
        lblLPJ1.setText("LP: " + jugador1.getVida());
        lblLPJ2.setText("LP: " + jugador2.getVida());
        barraVidaJ1.setValue(Math.max(0, jugador1.getVida()));
        barraVidaJ2.setValue(Math.max(0, jugador2.getVida()));

        lblTurno.setText("Turno: " + nombreActivo());
        lblMano.setText("  Mano de " + nombreActivo() + ":");

        actualizarCampo(slotsCampoJ1, jugador1.getCampo());
        actualizarCampo(slotsCampoJ2, jugador2.getCampo());
        actualizarMano(slotsMano, jugadorActivo().getMano());
    }

    private void actualizarCampo(JButton[] slots, Carta[] campo) {
        for (int i = 0; i < slots.length; i++) {
            if (campo[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) campo[i];
                slots[i].setText("<html><center>"
                    + m.getNombre() + "<br>"
                    + m.getPosicion() + "<br>"
                    + "ATK:" + m.getAtaque() + "<br>"
                    + "DEF:" + m.getDefensa()
                    + (m.isParalizado() ? "<br><font color='red'>PARALIZADO</font>" : "")
                    + "</center></html>");
                slots[i].setBackground(m.getPosicion() == Posicion.ATAQUE
                    ? new Color(60, 20, 20)
                    : new Color(20, 20, 80));
                cargarImagenEnSlot(slots[i], m.getNombre());
            } else {
                resetSlot(slots[i]);
            }
        }
    }

    private void actualizarMano(JButton[] slots, Carta[] mano) {
        for (int i = 0; i < slots.length; i++) {
            if (mano[i] != null) {
                Carta c = mano[i];
                String tipo = c instanceof Mounstro ? "M"
                            : c instanceof Magia    ? "Mg" : "T";
                String stats = "";
                if (c instanceof Mounstro) {
                    Mounstro m = (Mounstro) c;
                    stats = "<br>A:" + m.getAtaque() + " D:" + m.getDefensa();
                }
                slots[i].setText("<html><center>[" + tipo + "]<br>"
                    + c.getNombre() + stats + "</center></html>");
                slots[i].setBackground(
                    c instanceof Mounstro ? new Color(40, 80, 40)
                  : c instanceof Magia    ? new Color(20, 40, 120)
                                          : new Color(100, 40, 40));
                cargarImagenEnSlot(slots[i], c.getNombre());
            } else {
                resetSlot(slots[i]);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  IMÁGENES
    // ─────────────────────────────────────────────────────────────────

    private void cargarImagenEnSlot(JButton slot, String nombreCarta) {
        File img = new File("src/imagenes/" + nombreCarta + ".png");
        if (!img.exists()) img = new File("src/imagenes/" + nombreCarta + ".jpg");

        if (img.exists()) {
            ImageIcon icono = new ImageIcon(img.getPath());
            Image imagen = icono.getImage()
                .getScaledInstance(ANCHO_CARTA, ALTO_CARTA, Image.SCALE_SMOOTH);
            slot.setIcon(new ImageIcon(imagen));
            slot.setText("");
        } else {
            slot.setIcon(null);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  FIN DEL JUEGO
    // ─────────────────────────────────────────────────────────────────

    private void revisarFinJuego() {
        if (jugador1.getVida() <= 0 || jugador2.getVida() <= 0) {
            String ganador  = jugador1.getVida() > 0
                ? jugador1.getNombreJugador()
                : jugador2.getNombreJugador();
            String perdedor = jugador1.getVida() <= 0
                ? jugador1.getNombreJugador()
                : jugador2.getNombreJugador();

            jugador1.setGanador(jugador1.getVida() > 0);
            jugador2.setGanador(jugador2.getVida() > 0);

            String[] frases = {
                "¡El corazón de las cartas ha hablado!",
                "¡Es el poder del milenio!",
                "¡La baraja del campeón no miente!",
                "¡Cree en tus cartas y ellas creerán en ti!"
            };
            String frase = frases[(int)(Math.random() * frases.length)];

            JOptionPane.showMessageDialog(this,
                "⚡ ¡" + ganador + " gana el duelo! ⚡\n\n"
                + perdedor + " ha sido derrotado.\n\n"
                + "\"" + frase + "\"",
                "¡Duelo terminado!",
                JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────────────────────────────

    private void moverMagiaCementerio(Jugador activo, int indice) {
        Carta magia = activo.getMano()[indice];
        if (magia == null) return;
        activo.getMano()[indice] = null;
        for (int i = 0; i < activo.getCementerio().length; i++) {
            if (activo.getCementerio()[i] == null) {
                activo.getCementerio()[i] = magia;
                magia.setEstado(Estado.CEMENTERIO);
                break;
            }
        }
    }

    private Jugador jugadorActivo() { return turnoDe == 1 ? jugador1 : jugador2; }
    private Jugador jugadorRival()  { return turnoDe == 1 ? jugador2 : jugador1; }
    private String  nombreActivo()  { return jugadorActivo().getNombreJugador(); }

    private void agregarLog(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private JButton crearSlotVacio() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(ANCHO_CARTA, ALTO_CARTA));
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 9));
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        btn.setFocusPainted(false);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        return btn;
    }

    private void resetSlot(JButton slot) {
        slot.setText("");
        slot.setIcon(null);
        slot.setBackground(new Color(30, 30, 30));
        slot.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
    }

    private JButton crearBotonAccion(String texto, Color color) {
        JButton btn = new JButton("<html><center>" + texto + "</center></html>");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        return btn;
    }
}