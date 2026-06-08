package controlador;

import modelo.*;
import modelo.enums.Estado;
import modelo.enums.Posicion;
import modelo.estructuras.MazoBaraja;
import modelo.estructuras.ManoJugador;
import modelo.estructuras.RegistroCartas;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Maneja toda la lectura y escritura de archivos de texto plano.
 */
public class GestorArchivos {

    private static final String ARCHIVO_PARTIDA    = "partida_guardada.txt";
    private static final String ARCHIVO_RESULTADOS = "resultados.txt";
    private static final RegistroCartas REGISTRO = RegistroCartas.getInstance();

    // ─────────────────────────────────────────────────────────────────
    //  GUARDAR PARTIDA
    // ─────────────────────────────────────────────────────────────────

    public static void guardarPartida(Jugador j1, Jugador j2,
                                       byte turnoDe, boolean primerTurno,
                                       int turnosJugados) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PARTIDA));

        bw.write("TURNO=" + turnoDe); bw.newLine();
        bw.write("PRIMER_TURNO=" + primerTurno); bw.newLine();
        bw.write("TURNOS_JUGADOS=" + turnosJugados); bw.newLine();

        escribirJugador(bw, j1, "JUGADOR1");
        escribirJugador(bw, j2, "JUGADOR2");

        bw.close();
    }

    private static void escribirJugador(BufferedWriter bw,
                                         Jugador j, String prefijo) throws IOException {
        bw.write(prefijo + "_NOMBRE=" + j.getNombreJugador()); bw.newLine();
        bw.write(prefijo + "_VIDA="   + j.getVida());          bw.newLine();

        // Mano
        bw.write(prefijo + "_MANO=" + serializarArreglo(j.getMano())); bw.newLine();

        // Campo monstruos
        bw.write(prefijo + "_CAMPO=" + serializarCampoMonstruos(j.getCampo())); bw.newLine();

        // Campo magias/trampas
        bw.write(prefijo + "_MAGIAS=" + serializarCampoMagias(j.getCampoMagias())); bw.newLine();

        // Cementerio
        bw.write(prefijo + "_CEMENTERIO=" + serializarArreglo(j.getCementerio())); bw.newLine();

        // Baraja
        bw.write(prefijo + "_BARAJA=" + serializarArreglo(j.getMazo().toArray())); bw.newLine();

        // Flags especiales
        bw.write(prefijo + "_ATAQUE_NEGADO="          + j.isAtaqueNegado());          bw.newLine();
        bw.write(prefijo + "_TRAMPAS_BLOQUEADAS="     + j.isTrampasBloqueadas());     bw.newLine();
        bw.write(prefijo + "_MAGIA_BLOQUEADA="        + j.isMagiaBloqueada());        bw.newLine();
        bw.write(prefijo + "_LLAMADA_CONDENADOS="     + j.getLlamadaDeLosCondenados()); bw.newLine();
        bw.write(prefijo + "_INDICE_TRAMPA_LLAMADA="  + j.getIndiceTrampaLlamada());  bw.newLine();
    }

    // ── Serialización de arreglos ─────────────────────────────────────

    /** Serializa un arreglo de cartas como: nombre1|nombre2|VACIO|nombre3 */
    private static String serializarArreglo(Carta[] cartas) {
        if (cartas == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cartas.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(cartas[i] == null ? "VACIO" : cartas[i].getNombre());
        }
        return sb.toString();
    }

    /** Serializa el campo de monstruos incluyendo posición y estado paralizado:
     * nombre:ATAQUE:false|VACIO|nombre:DEFENSA:true */
    private static String serializarCampoMonstruos(Carta[] campo) {
        if (campo == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campo.length; i++) {
            if (i > 0) sb.append("|");
            if (campo[i] == null) {
                sb.append("VACIO");
            } else if (campo[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) campo[i];
                sb.append(m.getNombre())
                  .append(":").append(m.getPosicion())
                  .append(":").append(m.isParalizado())
                  .append(":").append(m.isYaAtaco());
            }
        }
        return sb.toString();
    }

    /** Serializa el campo de magias/trampas incluyendo visibilidad:
     * nombre:true|VACIO|nombre:false */
    private static String serializarCampoMagias(Carta[] campo) {
        if (campo == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campo.length; i++) {
            if (i > 0) sb.append("|");
            if (campo[i] == null) {
                sb.append("VACIO");
            } else {
                sb.append(campo[i].getNombre())
                  .append(":").append(campo[i].isVisible());
            }
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────────
    //  CARGAR PARTIDA
    // ─────────────────────────────────────────────────────────────────

    public static EstadoPartida cargarPartida() throws IOException {
        File f = new File(ARCHIVO_PARTIDA);
        if (!f.exists()) return null;

        Map<String, String> datos = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea;
        while ((linea = br.readLine()) != null) {
            int sep = linea.indexOf('=');
            if (sep != -1) {
                datos.put(linea.substring(0, sep),
                          linea.substring(sep + 1));
            }
        }
        br.close();

        byte    turnoDe      = Byte.parseByte(datos.get("TURNO"));
        boolean primerTurno  = Boolean.parseBoolean(datos.get("PRIMER_TURNO"));
        int     turnosJugados= Integer.parseInt(datos.get("TURNOS_JUGADOS"));

        Jugador j1 = reconstruirJugador(datos, "JUGADOR1");
        Jugador j2 = reconstruirJugador(datos, "JUGADOR2");

        return new EstadoPartida(j1, j2, turnoDe, primerTurno, turnosJugados);
    }

    private static Jugador reconstruirJugador(Map<String, String> datos,
                                               String prefijo) {
        String nombre = datos.get(prefijo + "_NOMBRE");
        short  vida   = Short.parseShort(datos.get(prefijo + "_VIDA"));

        Carta[] barajaArr = reconstruirArreglo(datos.get(prefijo + "_BARAJA"), 25);
        MazoBaraja mazo   = MazoBaraja.desdeArray(barajaArr);

        Jugador j = new Jugador(nombre, mazo);
        j.setVida(vida);

        Carta[] manoArr = reconstruirArreglo(datos.get(prefijo + "_MANO"), 5);
        ManoJugador manoReconstruida = ManoJugador.desdeArray(manoArr);
        
        for (int i = 0; i < manoArr.length; i++) {
            if (manoArr[i] != null) manoArr[i].setEstado(Estado.MANO);
        }
        for (int i = 0; i < manoArr.length; i++) {
            if (manoArr[i] != null) {
                j.getManoLinkedList().agregarCarta(manoArr[i]);
            }
        }

        reconstruirCampoMonstruos(datos.get(prefijo + "_CAMPO"), j.getCampo());
        reconstruirCampoMagias(datos.get(prefijo + "_MAGIAS"), j.getCampoMagias());

        Carta[] cem = reconstruirArreglo(datos.get(prefijo + "_CEMENTERIO"), 25);
        for (int i = 0; i < cem.length; i++) {
            j.getCementerio()[i] = cem[i];
            if (cem[i] != null) cem[i].setEstado(Estado.CEMENTERIO);
        }

        j.setAtaqueNegado(     Boolean.parseBoolean(datos.get(prefijo + "_ATAQUE_NEGADO")));
        j.setTrampasBloqueadas(Boolean.parseBoolean(datos.get(prefijo + "_TRAMPAS_BLOQUEADAS")));
        j.setMagiaBloqueada(   Boolean.parseBoolean(datos.get(prefijo + "_MAGIA_BLOQUEADA")));
        j.setLlamadaDeLosCondenados(
            Integer.parseInt(datos.get(prefijo + "_LLAMADA_CONDENADOS")));
        j.setIndiceTrampaLlamada(
            Integer.parseInt(datos.get(prefijo + "_INDICE_TRAMPA_LLAMADA")));

        return j;
    }

    private static Carta[] reconstruirArreglo(String valor, int tamanio) {
        Carta[] arreglo = new Carta[tamanio];
        if (valor == null || valor.isEmpty()) return arreglo;

        String[] partes = valor.split("\\|", -1);
        for (int i = 0; i < partes.length && i < tamanio; i++) {
            if (!partes[i].equals("VACIO")) {
                arreglo[i] = clonarCarta(REGISTRO.buscarPorNombre(partes[i]));
            }
        }
        return arreglo;
    }

    private static void reconstruirCampoMonstruos(String valor, Carta[] campo) {
        if (valor == null || valor.isEmpty()) return;
        String[] partes = valor.split("\\|", -1);
        for (int i = 0; i < partes.length && i < campo.length; i++) {
            if (!partes[i].equals("VACIO")) {
                String[] sub = partes[i].split(":", -1);
                Carta c = clonarCarta(REGISTRO.buscarPorNombre(sub[0]));
                if (c instanceof Mounstro) {
                    Mounstro m = (Mounstro) c;
                    m.setPosicion(sub[1].equals("ATAQUE") ? Posicion.ATAQUE : Posicion.DEFENSA);
                    m.setParalizado(Boolean.parseBoolean(sub[2]));
                    if (sub.length > 3) m.setYaAtaco(Boolean.parseBoolean(sub[3]));
                    m.setEstado(Estado.CAMPO);
                    campo[i] = m;
                }
            }
        }
    }

    private static void reconstruirCampoMagias(String valor, Carta[] campo) {
        if (valor == null || valor.isEmpty()) return;
        String[] partes = valor.split("\\|", -1);
        for (int i = 0; i < partes.length && i < campo.length; i++) {
            if (!partes[i].equals("VACIO")) {
                String[] sub = partes[i].split(":", 2);
                Carta c = clonarCarta(REGISTRO.buscarPorNombre(sub[0]));
                if (c != null) {
                    c.setVisible(Boolean.parseBoolean(sub[1]));
                    c.setEstado(Estado.CAMPO);
                    campo[i] = c;
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  HISTORIAL DE RESULTADOS
    // ─────────────────────────────────────────────────────────────────

    public static void registrarResultado(String nombreJ1, String nombreJ2,
                                           String ganador, int turnos,
                                           short vidaJ1, short vidaJ2) throws IOException {
        BufferedWriter bw = new BufferedWriter(
            new FileWriter(ARCHIVO_RESULTADOS, true)); // true = append

        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        bw.write(fecha + "|" + nombreJ1 + "|" + nombreJ2 + "|"
               + ganador + "|" + turnos + "|"
               + nombreJ1 + ":" + vidaJ1 + "|"
               + nombreJ2 + ":" + vidaJ2);
        bw.newLine();
        bw.close();
    }

    // ─────────────────────────────────────────────────────────────────
    //  ESTADÍSTICAS
    // ─────────────────────────────────────────────────────────────────

    public static List<String[]> leerResultados() throws IOException {
        List<String[]> lista = new ArrayList<>();
        File f = new File(ARCHIVO_RESULTADOS);
        if (!f.exists()) return lista;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea;
        while ((linea = br.readLine()) != null) {
            if (!linea.trim().isEmpty()) {
                lista.add(linea.split("\\|"));
            }
        }
        br.close();
        return lista;
    }

    public static String generarEstadisticas() throws IOException {
        List<String[]> resultados = leerResultados();
        if (resultados.isEmpty()) return "No hay partidas registradas aún.";

        Map<String, Integer> victorias = new LinkedHashMap<>();
        Map<String, Integer> derrotas  = new LinkedHashMap<>();
        int maxTurnos = 0;
        String partidaMasLarga = "";
        int totalPartidas = resultados.size();

        for (String[] r : resultados) {
            if (r.length < 7) continue;

            String j1      = r[1];
            String j2      = r[2];
            String ganador = r[3];
            int    turnos  = Integer.parseInt(r[4]);

            if (!victorias.containsKey(j1)) { victorias.put(j1, 0); derrotas.put(j1, 0); }
            if (!victorias.containsKey(j2)) { victorias.put(j2, 0); derrotas.put(j2, 0); }

            victorias.put(ganador, victorias.getOrDefault(ganador, 0) + 1);
            String perdedor = ganador.equals(j1) ? j2 : j1;
            derrotas.put(perdedor, derrotas.getOrDefault(perdedor, 0) + 1);

            if (turnos > maxTurnos) {
                maxTurnos = turnos;
                partidaMasLarga = r[0] + " — " + j1 + " vs " + j2
                    + " (" + turnos + " turnos)";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════\n");
        sb.append("      ESTADÍSTICAS HISTÓRICAS\n");
        sb.append("═══════════════════════════════\n\n");
        sb.append("Total de partidas jugadas: ").append(totalPartidas).append("\n\n");

        sb.append("Victorias por duelista:\n");
        sb.append("─────────────────────────\n");
        for (Map.Entry<String, Integer> e : victorias.entrySet()) {
            sb.append(String.format("  %-20s  V:%2d  D:%2d\n",
                e.getKey(), e.getValue(), derrotas.get(e.getKey())));
        }

        sb.append("\nPartida más larga:\n");
        sb.append("  ").append(partidaMasLarga).append("\n");

        sb.append("\nÚltimas 5 partidas:\n");
        sb.append("─────────────────────────\n");
        int inicio = Math.max(0, resultados.size() - 5);
        for (int i = inicio; i < resultados.size(); i++) {
            String[] r = resultados.get(i);
            if (r.length >= 5) {
                sb.append(String.format("  %s  %s vs %s  →  Ganó: %s  (%s turnos)\n",
                    r[0], r[1], r[2], r[3], r[4]));
            }
        }

        return sb.toString();
    }

    public static boolean existePartidaGuardada() {
        return new File(ARCHIVO_PARTIDA).exists();
    }

    public static void eliminarPartidaGuardada() {
        new File(ARCHIVO_PARTIDA).delete();
    }

    // ─────────────────────────────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────────────────────────────

    /**
     * Crea una nueva instancia de la carta a partir del prototipo del mapa.
     * Necesario para que cada jugador tenga su propia instancia.
     */
    private static Carta clonarCarta(Carta proto) {
        if (proto == null) return null;

        if (proto instanceof Mounstro) {
            Mounstro m = (Mounstro) proto;
            return new Mounstro(m.getNombre(), m.getAtaque(), m.getDefensa(),
                m.getTipo(), m.getEstrellas(), m.isVisible(), m.getEfecto(), m.getEstado());
        } else if (proto instanceof Magia) {
            return new Magia(proto.getNombre(), proto.getTipo(),
                proto.isVisible(), proto.getEfecto(), proto.getEstado());
        } else if (proto instanceof Trampa) {
            return new Trampa(proto.getNombre(), proto.getTipo(),
                proto.isVisible(), proto.getEfecto(), proto.getEstado());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────
    //  CLASE INTERNA: Estado de la partida cargada
    // ─────────────────────────────────────────────────────────────────

    public static class EstadoPartida {
        public final Jugador jugador1;
        public final Jugador jugador2;
        public final byte    turnoDe;
        public final boolean primerTurno;
        public final int     turnosJugados;

        public EstadoPartida(Jugador j1, Jugador j2,
                              byte turnoDe, boolean primerTurno, int turnosJugados) {
            this.jugador1     = j1;
            this.jugador2     = j2;
            this.turnoDe      = turnoDe;
            this.primerTurno  = primerTurno;
            this.turnosJugados= turnosJugados;
        }
    }
}