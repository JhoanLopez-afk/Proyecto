package controlador;

import modelo.*;
import modelo.enums.Posicion;
import vista.IVistaJuego;

import java.util.ArrayList;

public class ControladorJuego {

    private final IVistaJuego vista;
    private Jugador jugador1;
    private Jugador jugador2;

    private byte    turnoDe            = 1;
    private boolean primerTurno        = true;
    private int     cartaSeleccionadaEnMano = -1;

    public ControladorJuego(IVistaJuego vista) {
        this.vista = vista;
        vista.setControlador(this);
    }

    public void iniciar() {
        String[] nombres = vista.pedirNombresJugadores();

        Jugador[] combatientes = Juego.iniciarJuego(nombres[0], nombres[1]);
        jugador1 = combatientes[0];
        jugador2 = combatientes[1];

        vista.mostrar();
        vista.actualizarUI(jugador1, jugador2, nombreActivo(), primerTurno);
        vista.agregarLog("¡El duelo ha comenzado!");
        vista.agregarLog("Turno de " + nombreActivo());
    }

    public void onSeleccionarCartaMano(int indice) {
        cartaSeleccionadaEnMano = indice;
        if (vista instanceof vista.VentanaJuego)
            ((vista.VentanaJuego) vista).resaltarSlotMano(indice);
    }

    public void onSlotCampoPropio(int indice) {
    }

    public void onSlotCampoRival(int indice) {
    }

    public void onInvocar() {
        if (cartaSeleccionadaEnMano == -1) {
            vista.mostrarError("Sin selección", "Primero selecciona un monstruo de tu mano.");
            return;
        }
        Jugador activo = jugadorActivo();
        Carta carta = activo.getMano()[cartaSeleccionadaEnMano];

        if (carta == null || !(carta instanceof Mounstro)) {
            vista.mostrarError("No es monstruo", "La carta seleccionada no es un monstruo.");
            return;
        }
        if (activo.isYaInvoco()) {
            vista.mostrarError("Ya invocaste", "Ya invocaste un monstruo este turno.");
            return;
        }

        Mounstro m       = (Mounstro) carta;
        int      estrellas = activo.obtenerNumeroEstrellas(m.getEstrellas());

        if (estrellas >= 5 && estrellas <= 6) {
            if (!activo.tieneMonstruos()) {
                vista.mostrarError("Sin sacrificio",
                    "Necesitas 1 monstruo en el campo para invocar a " + m.getNombre());
                return;
            }
            if (!realizarSacrificios(activo, 1, m.getNombre())) return;

        } else if (estrellas >= 7) {
            if (activo.contarMonstruos() < 2) {
                vista.mostrarError("Sin sacrificios",
                    "Necesitas 2 monstruos en el campo para invocar a " + m.getNombre());
                return;
            }
            if (!realizarSacrificios(activo, 2, m.getNombre())) return;
        }

        int resp = vista.pedirOpcion("Posición de invocación",
            "¿En qué posición invocar a " + carta.getNombre() + "?",
            new String[]{"ATAQUE", "DEFENSA"});
        if (resp == -1) return;

        Posicion pos = (resp == 1) ? Posicion.DEFENSA : Posicion.ATAQUE;

        boolean ok;
        if (estrellas >= 5) ok = activo.invocarMonstruoConSacrificio(cartaSeleccionadaEnMano, pos);
        else                 ok = activo.invocarMonstruo(cartaSeleccionadaEnMano, pos);

        if (ok) {
            vista.agregarLog(nombreActivo() + " invocó: " + carta.getNombre() + " en " + pos);
        } else {
            vista.mostrarError("Campo lleno", "No hay espacio en el campo de monstruos.");
        }

        cartaSeleccionadaEnMano = -1;
        refrescarVista();
        verificarFinJuego();
    }

    public void onColocarMagia() {
        if (cartaSeleccionadaEnMano == -1) {
            vista.mostrarError("Sin selección", "Primero selecciona una mágica o trampa de tu mano.");
            return;
        }
        Jugador activo = jugadorActivo();
        Carta carta = activo.getMano()[cartaSeleccionadaEnMano];

        if (carta == null || (!(carta instanceof Magia) && !(carta instanceof Trampa))) {
            vista.mostrarError("No válida", "La carta seleccionada no es una mágica ni trampa.");
            return;
        }

        boolean ok = activo.colocarEnCampoMagiaTrampa(cartaSeleccionadaEnMano);
        if (ok) vista.agregarLog(nombreActivo() + " colocó una carta boca abajo.");
        else    vista.mostrarError("Campo lleno", "No hay espacio en el campo de mágicas/trampas.");

        cartaSeleccionadaEnMano = -1;
        refrescarVista();
    }

    public void onActivarMagia() {
        Jugador activo = jugadorActivo();
        Jugador rival  = jugadorRival();

        ArrayList<String>  ops = new ArrayList<>();
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < activo.getCampoMagias().length; i++) {
            Carta c = activo.getCampoMagias()[i];
            if (c instanceof Magia) { ops.add(c.getNombre()); idx.add(i); }
        }

        if (ops.isEmpty()) {
            vista.mostrarError("Sin mágicas", "No tienes mágicas en el campo para activar.");
            return;
        }

        int resp = vista.pedirOpcion("Activar Mágica",
            "Selecciona la mágica a activar:", ops.toArray(new String[0]));
        if (resp == -1) return;

        int    indiceCampo = idx.get(resp);
        Magia  magia       = (Magia) activo.getCampoMagias()[indiceCampo];

        if (verificarYAplicarDisruptorMagico(rival, activo, magia, indiceCampo)) return;

        activo.getCampoMagias()[indiceCampo] = null;
        magia.setVisible(true);
        vista.agregarLog(nombreActivo() + " activó: " + magia.getNombre());

        despacharEfectoMagia(activo, rival, magia);

        activo.enviarAlCementerio(magia);
        refrescarVista();
        verificarFinJuego();
    }

    public void onAtacar() {
        Jugador activo = jugadorActivo();
        Jugador rival  = jugadorRival();

        ArrayList<String> opsAtac = new ArrayList<>();
        ArrayList<Byte>   idxAtac = new ArrayList<>();
        for (int i = 0; i < activo.getCampo().length; i++) {
            if (activo.getCampo()[i] instanceof Mounstro) {
                Mounstro mn = (Mounstro) activo.getCampo()[i];
                if (mn.getPosicion() == Posicion.ATAQUE && !mn.isParalizado() && !mn.isYaAtaco()) {
                    opsAtac.add(mn.getNombre() + "  ATK:" + mn.getAtaque());
                    idxAtac.add((byte) i);
                }
            }
        }
        if (opsAtac.isEmpty()) {
            vista.mostrarError("Sin atacantes", "No tienes monstruos disponibles para atacar.");
            return;
        }

        int rAtac = vista.pedirOpcion("Seleccionar atacante",
            "¿Con cuál monstruo atacas?", opsAtac.toArray(new String[0]));
        if (rAtac == -1) return;

        byte   indAtac = idxAtac.get(rAtac);
        Mounstro mAtac = (Mounstro) activo.getCampo()[indAtac];

        vista.agregarLog(nombreActivo() + " ataca con " + mAtac.getNombre());

        rival.setIndiceAtacanteRival(indAtac);
        if (activarTrampaRival(rival, activo, indAtac)) {
            if (activo.isAtaqueNegado()) {
                vista.agregarLog("¡El ataque fue negado!");
                activo.setAtaqueNegado(false);
                mAtac.setYaAtaco(true);
                refrescarVista();
                verificarFinJuego();
                return;
            }
        }

        if (rival.tieneMonstruos()) {
            ArrayList<String> opsObj = new ArrayList<>();
            ArrayList<Byte>   idxObj = new ArrayList<>();
            for (int i = 0; i < rival.getCampo().length; i++) {
                if (rival.getCampo()[i] instanceof Mounstro) {
                    Mounstro mn = (Mounstro) rival.getCampo()[i];
                    opsObj.add(mn.getNombre()
                        + " (" + mn.getPosicion() + ")"
                        + "  ATK:" + mn.getAtaque()
                        + "  DEF:" + mn.getDefensa());
                    idxObj.add((byte) i);
                }
            }
            int rObj = vista.pedirOpcion("Seleccionar objetivo",
                "¿A cuál monstruo atacas?", opsObj.toArray(new String[0]));
            if (rObj == -1) return;

            byte indObj = idxObj.get(rObj);
            activo.atacar(indAtac, rival, indObj);
        } else {
            vista.agregarLog(nombreActivo() + " hace ataque directo.");
            activo.atacar(indAtac, rival, (byte) -1);
        }

        vista.agregarLog("LP " + rival.getNombreJugador() + ": " + rival.getVida());
        mAtac.setYaAtaco(true);
        refrescarVista();
        verificarFinJuego();
    }

    public void onCambiarPosicion() {
        Jugador activo = jugadorActivo();

        ArrayList<String> ops = new ArrayList<>();
        ArrayList<Byte>   idx = new ArrayList<>();
        for (int i = 0; i < activo.getCampo().length; i++) {
            if (activo.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) activo.getCampo()[i];
                ops.add(m.getNombre() + " (" + m.getPosicion() + ")");
                idx.add((byte) i);
            }
        }
        if (ops.isEmpty()) {
            vista.mostrarError("Sin monstruos", "No tienes monstruos en el campo.");
            return;
        }

        int rSel = vista.pedirOpcion("Cambiar posición",
            "¿Qué monstruo cambias de posición?", ops.toArray(new String[0]));
        if (rSel == -1) return;

        int rPos = vista.pedirOpcion("Nueva posición", "Nueva posición:",
            new String[]{"ATAQUE", "DEFENSA"});
        if (rPos == -1) return;

        Posicion nuevaPos = (rPos == 1) ? Posicion.DEFENSA : Posicion.ATAQUE;
        boolean ok = activo.cambiarPosicionMonstruo(idx.get(rSel), nuevaPos);

        if (ok) vista.agregarLog(nombreActivo() + " cambió posición a " + nuevaPos);
        else    vista.mostrarError("No permitido",
            "No puedes cambiar la posición (ya cambiaste este turno o el monstruo está paralizado).");

        refrescarVista();
    }

    public void onFinTurno() {
        vista.agregarLog("--- Fin del turno de " + nombreActivo() + " ---");
        jugadorActivo().resetTurno();

        turnoDe = (turnoDe == 1) ? (byte) 2 : (byte) 1;
        primerTurno = false;
        cartaSeleccionadaEnMano = -1;

        String cartaRobada = jugadorActivo().robarCarta();
        if (cartaRobada != null) vista.agregarLog(nombreActivo() + " robó: " + cartaRobada);
        else                     vista.agregarLog(nombreActivo() + " no pudo robar (baraja vacía o mano llena).");

        vista.agregarLog("Turno de " + nombreActivo());
        refrescarVista();
        verificarFinJuego();
    }

    public void onVerCementerio(int jugador) {
        Jugador j = (jugador == 1) ? jugador1 : jugador2;
        StringBuilder sb = new StringBuilder();
        sb.append("Cementerio de ").append(j.getNombreJugador()).append(":\n\n");
        boolean vacio = true;
        for (Carta c : j.getCementerio()) {
            if (c != null) {
                String tipo = c instanceof Mounstro ? "[Monstruo]"
                            : c instanceof Magia    ? "[Mágica]" : "[Trampa]";
                sb.append(tipo).append(" ").append(c.getNombre()).append("\n");
                vacio = false;
            }
        }
        if (vacio) sb.append("(vacío)");
        vista.mostrarMensaje("Cementerio de " + j.getNombreJugador(), sb.toString());
    }

    private boolean realizarSacrificios(Jugador activo, int cantidad, String nombreMonstruo) {
        for (int s = 0; s < cantidad; s++) {
            ArrayList<String>  ops = new ArrayList<>();
            ArrayList<Integer> idx = new ArrayList<>();
            for (int i = 0; i < activo.getCampo().length; i++) {
                if (activo.getCampo()[i] instanceof Mounstro) {
                    Mounstro mn = (Mounstro) activo.getCampo()[i];
                    ops.add(mn.getNombre() + " ATK:" + mn.getAtaque());
                    idx.add(i);
                }
            }
            if (ops.isEmpty()) {
                vista.mostrarError("Sin sacrificio", "No hay monstruos para sacrificar.");
                return false;
            }
            int r = vista.pedirOpcion(
                "Sacrificio " + (s+1) + "/" + cantidad,
                "Elige monstruo a sacrificar para invocar " + nombreMonstruo + ":",
                ops.toArray(new String[0]));
            if (r == -1) return false;

            int indiceSacrificio = idx.get(r);
            vista.agregarLog(nombreActivo() + " sacrificó: "
                + activo.getCampo()[indiceSacrificio].getNombre());
            activo.muereMounstro(indiceSacrificio);
        }
        return true;
    }

    private boolean activarTrampaRival(Jugador defensor, Jugador atacante, int indiceAtacante) {
        if (defensor.isTrampasBloqueadas()) {
            vista.agregarLog("Las trampas de " + defensor.getNombreJugador() + " están bloqueadas.");
            defensor.setTrampasBloqueadas(false);
            return false;
        }

        ArrayList<String>  ops = new ArrayList<>();
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < defensor.getCampoMagias().length; i++) {
            Carta c = defensor.getCampoMagias()[i];
            if (c instanceof Trampa && i != defensor.getIndiceTrampaLlamada()) {
                ops.add(c.getNombre()); idx.add(i);
            }
        }
        if (ops.isEmpty()) return false;

        boolean quiere = vista.pedirConfirmacion(
            "Activar trampa",
            defensor.getNombreJugador() + ", ¿deseas activar una trampa?");
        if (!quiere) return false;

        int r = vista.pedirOpcion("Trampas disponibles",
            "Selecciona la trampa a activar:", ops.toArray(new String[0]));
        if (r == -1) return false;

        int    indiceCampo = idx.get(r);
        Trampa t = (Trampa) defensor.getCampoMagias()[indiceCampo];

        if (verificarYAplicarDisruptorTrampa(atacante, defensor, t, indiceCampo)) return false;

        defensor.getCampoMagias()[indiceCampo] = null;
        t.setVisible(true);
        vista.agregarLog(defensor.getNombreJugador() + " activó trampa: " + t.getNombre());

        despacharEfectoTrampa(t, defensor, atacante, indiceAtacante);

        if (!t.getNombre().equals("llamada de los condenados"))
            defensor.enviarAlCementerio(t);

        refrescarVista();
        return true;
    }

    private boolean verificarYAplicarDisruptorMagico(Jugador rival, Jugador activo,
                                                      Magia magia, int indiceCampo) {
        int indDisruptor = -1;
        for (int i = 0; i < rival.getCampoMagias().length; i++) {
            Carta c = rival.getCampoMagias()[i];
            if (c instanceof Trampa && c.getNombre().equals("Disruptor Mágico")) {
                indDisruptor = i; break;
            }
        }
        if (indDisruptor == -1) return false;

        boolean quiere = vista.pedirConfirmacion("Disruptor Mágico",
            rival.getNombreJugador() + ", ¿activar Disruptor Mágico para negar "
            + magia.getNombre() + "?");
        if (!quiere) return false;

        ArrayList<String>  opsDes = new ArrayList<>();
        ArrayList<Integer> idxDes = new ArrayList<>();
        for (int i = 0; i < rival.getMano().length; i++) {
            if (rival.getMano()[i] != null) {
                opsDes.add(rival.getMano()[i].getNombre()); idxDes.add(i);
            }
        }
        if (opsDes.isEmpty()) {
            vista.agregarLog(rival.getNombreJugador() + " no tiene cartas. Disruptor sin efecto.");
            return false;
        }
        int rDes = vista.pedirOpcion("Disruptor Mágico",
            rival.getNombreJugador() + ", descarta una carta:",
            opsDes.toArray(new String[0]));
        if (rDes == -1) return false;

        int   iDes    = idxDes.get(rDes);
        Carta descartada = rival.getMano()[iDes];
        rival.getMano()[iDes] = null;
        rival.enviarAlCementerio(descartada);
        vista.agregarLog(rival.getNombreJugador() + " descartó " + descartada.getNombre());

        activo.getCampoMagias()[indiceCampo] = null;
        activo.enviarAlCementerio(magia);
        Trampa disruptor = (Trampa) rival.getCampoMagias()[indDisruptor];
        rival.getCampoMagias()[indDisruptor] = null;
        rival.enviarAlCementerio(disruptor);

        vista.agregarLog("¡" + magia.getNombre() + " fue negada por Disruptor Mágico!");
        refrescarVista();
        return true;
    }

    private boolean verificarYAplicarDisruptorTrampa(Jugador atacante, Jugador defensor,
                                                      Trampa trampa, int indiceTrampaEnCampo) {
        int indDisruptor = -1;
        for (int i = 0; i < atacante.getCampoMagias().length; i++) {
            Carta c = atacante.getCampoMagias()[i];
            if (c instanceof Trampa && c.getNombre().equals("Disruptor de Trampa")) {
                indDisruptor = i; break;
            }
        }
        if (indDisruptor == -1) return false;

        boolean quiere = vista.pedirConfirmacion("Disruptor de Trampa",
            atacante.getNombreJugador() + ", ¿activar Disruptor de Trampa para negar "
            + trampa.getNombre() + "?");
        if (!quiere) return false;

        Trampa disruptor = (Trampa) atacante.getCampoMagias()[indDisruptor];
        atacante.getCampoMagias()[indDisruptor] = null;
        atacante.enviarAlCementerio(disruptor);
        defensor.enviarAlCementerio(trampa);

        vista.agregarLog(atacante.getNombreJugador() + " negó "
            + trampa.getNombre() + " con Disruptor de Trampa.");
        refrescarVista();
        return true;
    }

    private void despacharEfectoMagia(Jugador activo, Jugador rival, Magia magia) {
        String nombre = magia.getNombre();

        if (nombre.equals("Monstruo renacido")) {
            int indice = elegirDelCementerio(activo, "Monstruo Renacido",
                "Selecciona el monstruo a revivir:");
            if (indice != -1) {
                magia.ejecutarMonstruoRenacido(activo, indice);
                vista.agregarLog(nombreActivo() + " revivió: "
                    + activo.getCampo()[encontrarUltimoOcupadoCampo(activo)] != null
                    ? "" : "");
            }

        } else if (nombre.equals("Tifon del espacio Mistico")) {
            ArrayList<String>  ops = new ArrayList<>();
            ArrayList<Integer> idx = new ArrayList<>();
            for (int i = 0; i < rival.getCampoMagias().length; i++) {
                if (rival.getCampoMagias()[i] != null) {
                    ops.add(rival.getCampoMagias()[i].getNombre()); idx.add(i);
                }
            }
            if (ops.isEmpty()) { vista.mostrarMensaje("Tifón", "El rival no tiene cartas en el campo."); return; }
            int r = vista.pedirOpcion("Tifón del Espacio Místico",
                "Elige la carta a destruir:", ops.toArray(new String[0]));
            if (r == -1) return;
            magia.ejecutarTifon(rival, idx.get(r));
            vista.agregarLog("Tifón destruyó una carta del campo rival.");

        } else if (nombre.equals("Caridad elegante") || nombre.equals("Fuerza de Resabastecimiento")) {
            int[] descartes = elegirDescartes(activo, 2, false,
                "Selecciona cartas a descartar (2)");
            if (descartes != null) magia.ejecutarRobarDescartar(activo, descartes);
            vista.agregarLog(nombreActivo() + " robó 3 y descartó 2.");

        } else if (nombre.equals("Reproduccion de hechizo")) {
            int[] descartes = elegirDescartes(activo, 2, true,
                "Selecciona 2 mágicas a descartar");
            if (descartes != null) magia.ejecutarReproduccionHechizo(activo, descartes);
            vista.agregarLog(nombreActivo() + " descartó 2 mágicas.");

        } else if (nombre.equals("Intercambio")) {
            int iP = elegirDeMano(activo, "Intercambio", "Elige tu carta a entregar:", false);
            if (iP == -1) return;
            int iR = elegirDeMano(rival, "Intercambio", "Elige la carta del rival:", false);
            if (iR == -1) return;
            magia.ejecutarIntercambio(activo, iP, rival, iR);
            vista.agregarLog(nombreActivo() + " intercambió cartas.");

        } else {
            magia.ejecutarEfecto(activo, rival);
            vista.agregarLog("Efecto de " + nombre + " aplicado.");
        }
    }

    private void despacharEfectoTrampa(Trampa t, Jugador defensor,
                                        Jugador atacante, int indiceAtacante) {
        String nombre = t.getNombre();

        if (nombre.equals("Círculo Atahechizos")) {
            ArrayList<String>  ops = new ArrayList<>();
            ArrayList<Integer> idx = new ArrayList<>();
            for (int i = 0; i < atacante.getCampo().length; i++) {
                if (atacante.getCampo()[i] instanceof Mounstro) {
                    Mounstro mn = (Mounstro) atacante.getCampo()[i];
                    ops.add(mn.getNombre() + " (" + mn.getPosicion() + ")"); idx.add(i);
                }
            }
            if (ops.isEmpty()) { vista.mostrarMensaje("Círculo", "No hay monstruos para paralizar."); return; }
            int r = vista.pedirOpcion("Círculo Atahechizos",
                "Selecciona el monstruo a paralizar:", ops.toArray(new String[0]));
            if (r == -1) return;
            t.ejecutarCirculoAtahechizos(atacante, idx.get(r));
            vista.agregarLog(((Mounstro) atacante.getCampo()[idx.get(r)]).getNombre() + " fue paralizado.");

        } else if (nombre.equals("Artilugio de Evacuación Compulsiva")) {
            String[] campos = {
                "Mi campo (" + defensor.getNombreJugador() + ")",
                "Campo del oponente (" + atacante.getNombreJugador() + ")"
            };
            int rCampo = vista.pedirOpcion("Artilugio",
                "¿De qué campo devolver un monstruo?", campos);
            if (rCampo == -1) return;
            Jugador objetivo = (rCampo == 0) ? defensor : atacante;

            ArrayList<String>  ops = new ArrayList<>();
            ArrayList<Integer> idx = new ArrayList<>();
            for (int i = 0; i < objetivo.getCampo().length; i++) {
                if (objetivo.getCampo()[i] instanceof Mounstro) {
                    Mounstro mn = (Mounstro) objetivo.getCampo()[i];
                    ops.add(mn.getNombre() + " (" + mn.getPosicion() + ")"); idx.add(i);
                }
            }
            if (ops.isEmpty()) { vista.mostrarMensaje("Artilugio", "No hay monstruos."); return; }
            int r = vista.pedirOpcion("Artilugio",
                "Selecciona el monstruo a devolver:", ops.toArray(new String[0]));
            if (r == -1) return;
            t.ejecutarArtilugio(objetivo, idx.get(r));
            vista.agregarLog("Un monstruo fue devuelto a la mano.");

        } else if (nombre.equals("Disruptor Mágico")) {
            ArrayList<String>  opsDes = new ArrayList<>();
            ArrayList<Integer> idxDes = new ArrayList<>();
            for (int i = 0; i < defensor.getMano().length; i++) {
                if (defensor.getMano()[i] != null) {
                    opsDes.add(defensor.getMano()[i].getNombre()); idxDes.add(i);
                }
            }
            if (opsDes.isEmpty()) { vista.agregarLog("Sin cartas para descartar. Sin efecto."); return; }
            int rDes = vista.pedirOpcion("Disruptor Mágico",
                defensor.getNombreJugador() + ", descarta una carta:",
                opsDes.toArray(new String[0]));
            if (rDes == -1) return;
            t.ejecutarDisruptorMagico(defensor, idxDes.get(rDes), atacante);
            vista.agregarLog("Disruptor Mágico: mágicas del rival bloqueadas.");

        } else if (nombre.equals("llamada de los condenados")) {
            int indiceCem = elegirDelCementerio(defensor, "Llamada de los Condenados",
                "Selecciona el monstruo a revivir:");
            if (indiceCem == -1) return;

            int emMonstruo = -1, emMagia = -1;
            for (int i = 0; i < defensor.getCampo().length; i++)
                if (defensor.getCampo()[i] == null) { emMonstruo = i; break; }
            for (int i = 0; i < defensor.getCampoMagias().length; i++)
                if (defensor.getCampoMagias()[i] == null) { emMagia = i; break; }

            if (emMonstruo == -1 || emMagia == -1) {
                vista.mostrarError("Campo lleno", "No hay espacio en el campo.");
                return;
            }
            t.ejecutarLlamadaCondenados(defensor, indiceCem, emMonstruo, emMagia);
            vista.agregarLog("Monstruo revivido por Llamada de los Condenados.");

        } else {
            t.ejecutarEfecto(defensor, atacante);
            vista.agregarLog("Trampa " + nombre + " activada.");
        }
    }

    private int elegirDelCementerio(Jugador jugador, String titulo, String mensaje) {
        ArrayList<String>  ops = new ArrayList<>();
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < jugador.getCementerio().length; i++) {
            if (jugador.getCementerio()[i] instanceof Mounstro) {
                ops.add(jugador.getCementerio()[i].getNombre()); idx.add(i);
            }
        }
        if (ops.isEmpty()) {
            vista.mostrarMensaje(titulo, "No hay monstruos en el cementerio.");
            return -1;
        }
        int r = vista.pedirOpcion(titulo, mensaje, ops.toArray(new String[0]));
        return r == -1 ? -1 : idx.get(r);
    }

    private int elegirDeMano(Jugador jugador, String titulo, String mensaje, boolean soloMagias) {
        ArrayList<String>  ops = new ArrayList<>();
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < jugador.getMano().length; i++) {
            Carta c = jugador.getMano()[i];
            if (c != null && (!soloMagias || c instanceof Magia)) {
                ops.add(c.getNombre()); idx.add(i);
            }
        }
        if (ops.isEmpty()) {
            vista.mostrarError(titulo, "No hay cartas disponibles.");
            return -1;
        }
        int r = vista.pedirOpcion(titulo, mensaje, ops.toArray(new String[0]));
        return r == -1 ? -1 : idx.get(r);
    }

    private int[] elegirDescartes(Jugador jugador, int cantidad,
                                   boolean soloMagias, String titulo) {
        int[] result = new int[cantidad];
        for (int d = 0; d < cantidad; d++) {
            int idx = elegirDeMano(jugador, titulo,
                "Descarta una carta (" + (d+1) + "/" + cantidad + "):", soloMagias);
            if (idx == -1) return null;
            result[d] = idx;
        }
        return result;
    }

    private int encontrarUltimoOcupadoCampo(Jugador j) {
        for (int i = j.getCampo().length - 1; i >= 0; i--)
            if (j.getCampo()[i] != null) return i;
        return -1;
    }

    private void refrescarVista() {
        vista.actualizarUI(jugador1, jugador2, nombreActivo(), primerTurno);
    }

    private void verificarFinJuego() {
        if (jugador1.getVida() <= 0 || jugador2.getVida() <= 0) {
            String ganador  = jugador1.getVida() > 0
                ? jugador1.getNombreJugador() : jugador2.getNombreJugador();
            String perdedor = jugador1.getVida() <= 0
                ? jugador1.getNombreJugador() : jugador2.getNombreJugador();
            jugador1.setGanador(jugador1.getVida() > 0);
            jugador2.setGanador(jugador2.getVida() > 0);
            vista.mostrarFinJuego(ganador, perdedor);
        }
    }

    private Jugador jugadorActivo() { return turnoDe == 1 ? jugador1 : jugador2; }
    private Jugador jugadorRival()  { return turnoDe == 1 ? jugador2 : jugador1; }
    private String  nombreActivo()  { return jugadorActivo().getNombreJugador(); }
}