package modelo.estructuras;

import modelo.Carta;
import java.util.Iterator;
import java.util.LinkedList;


public class ManoJugador {

    private static final int CAPACIDAD_MAXIMA = 10;
    private final LinkedList<Carta> lista;
    private int count;

    public ManoJugador() {
        this.lista = new LinkedList<>();
        this.count = 0;
        // Inicializar todos los slots en null para tener posiciones fijas
        for (int i = 0; i < CAPACIDAD_MAXIMA; i++) {
            lista.add(null);
        }
    }

    /**
     * Agrega una carta en el primer hueco libre (slot null).
     * @return true si se agregó, false si la mano está llena.
     */
    public boolean agregarCarta(Carta carta) {
        for (int i = 0; i < CAPACIDAD_MAXIMA; i++) {
            if (lista.get(i) == null) {
                lista.set(i, carta);
                count++;
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve la carta en la posición dada sin eliminarla.
     * @return la carta o null si el índice es inválido o el slot está vacío.
     */
    public Carta getCarta(int indice) {
        if (indice < 0 || indice >= CAPACIDAD_MAXIMA) return null;
        return lista.get(indice);
    }

    /**
     * Vacía el slot en la posición dada dejando null — no desplaza nada.
     * Esto evita que toArrayFijo() muestre las cartas en posiciones incorrectas.
     */
    public void vaciarPosicion(int indice) {
        if (indice < 0 || indice >= CAPACIDAD_MAXIMA) return;
        if (lista.get(indice) != null) {
            lista.set(indice, null);
            count--;
        }
    }

    /**
     * Reemplaza la carta en el índice dado (para efecto Intercambio).
     */
    public void setCarta(int indice, Carta carta) {
        if (indice < 0 || indice >= CAPACIDAD_MAXIMA) return;
        boolean habia    = lista.get(indice) != null;
        boolean hayNueva = carta != null;
        if (!habia && hayNueva) count++;
        else if (habia && !hayNueva) count--;
        lista.set(indice, carta);
    }

    /**
     * Elimina y devuelve la carta en la posición dada.
     */
    public Carta jugarCarta(int indice) {
        Carta c = getCarta(indice);
        vaciarPosicion(indice);
        return c;
    }

    /** @return cuántas cartas reales hay (sin contar nulls). */
    public int size() { return count; }

    /** @return true si no hay ninguna carta. */
    public boolean estaVacia() { return count == 0; }

    /** @return true si todos los slots están ocupados. */
    public boolean estaLlena() { return count >= CAPACIDAD_MAXIMA; }


    public Carta[] toArrayFijo(int tamano) {
        Carta[] arr = new Carta[tamano];
        for (int i = 0; i < tamano && i < CAPACIDAD_MAXIMA; i++) {
            arr[i] = lista.get(i);
        }
        return arr;
    }

    public static ManoJugador desdeArray(Carta[] arr) {
        ManoJugador mano = new ManoJugador();
        for (int i = 0; i < arr.length && i < CAPACIDAD_MAXIMA; i++) {
            if (arr[i] != null) {
                mano.lista.set(i, arr[i]);
                mano.count++;
            }
        }
        return mano;
    }

    /** Iterador que omite los slots null. */
    public Iterator<Carta> iterator() {
        return lista.stream()
                .filter(c -> c != null)
                .iterator();
    }
}