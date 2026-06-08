package modelo.estructuras;

import java.util.LinkedList;
import java.util.Queue;

public class LogEventosTurno {

    private final Queue<String> cola;
    private static final int CAPACIDAD_MAXIMA = 100;

    public LogEventosTurno() {
        this.cola = new LinkedList<>();
    }

    public void registrarEvento(String mensaje) {
        if (cola.size() >= CAPACIDAD_MAXIMA) {
            cola.poll();
        }
        cola.offer(mensaje);
    }

    public String procesarEvento() {
        return cola.poll();
    }

    public String vaciarComoTexto() {
        StringBuilder sb = new StringBuilder();
        String evento;
        while ((evento = cola.poll()) != null) {
            sb.append(evento).append("\n");
        }
        return sb.toString();
    }

    public String peek() {
        return cola.peek();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int size() {
        return cola.size();
    }

    public void limpiar() {
        cola.clear();
    }
}