package modelo.estructuras;

import modelo.Carta;
import java.util.LinkedList;
import java.util.Iterator;

public class ManoJugador {

    private static final int CAPACIDAD_MAXIMA = 10;
    private final LinkedList<Carta> lista;

    public ManoJugador() {
        this.lista = new LinkedList<>();
    }

    public boolean agregarCarta(Carta carta) {
        if (lista.size() >= CAPACIDAD_MAXIMA) return false;
        lista.addLast(carta);
        return true;
    }

    public Carta getCarta(int indice) {
        if (indice < 0 || indice >= lista.size()) return null;
        return lista.get(indice);
    }

    public Carta jugarCarta(int indice) {
        if (indice < 0 || indice >= lista.size()) return null;
        return lista.remove(indice);
    }

    public void vaciarPosicion(int indice) {
        if (indice < 0 || indice >= lista.size()) return;
        lista.set(indice, null);
    }

    public void setCarta(int indice, Carta carta) {
        if (indice < 0 || indice >= lista.size()) return;
        lista.set(indice, carta);
    }

    public int size() {
        return lista.size();
    }

    public boolean estaVacia() {
        return lista.isEmpty();
    }

    public boolean estaLlena() {
        return lista.size() >= CAPACIDAD_MAXIMA;
    }

    public Carta[] toArrayFijo(int tamano) {
        Carta[] arr = new Carta[tamano];
        Iterator<Carta> it = lista.iterator();
        for (int i = 0; i < tamano && it.hasNext(); i++) {
            arr[i] = it.next();
        }
        return arr;
    }

    public static ManoJugador desdeArray(Carta[] arr) {
        ManoJugador mano = new ManoJugador();
        for (Carta c : arr) {
            if (c != null) mano.lista.addLast(c);
        }
        return mano;
    }

    public Iterator<Carta> iterator() {
        return lista.iterator();
    }
}