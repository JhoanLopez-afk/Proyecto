package modelo.estructuras;

import modelo.Carta;
import java.util.Stack;

public class MazoBaraja {

    private final Stack<Carta> pila;

    public MazoBaraja() {
        this.pila = new Stack<>();
    }

    public void agregarCarta(Carta carta) {
        pila.push(carta);
    }

    public Carta robar() {
        if (pila.isEmpty()) return null;
        return pila.pop();
    }

    public boolean estaVacio() {
        return pila.isEmpty();
    }

    public int cantidadRestante() {
        return pila.size();
    }

    public Carta[] toArray() {
        Carta[] arr = new Carta[pila.size()];
        Object[] raw = pila.toArray();
        for (int i = 0; i < raw.length; i++) {
            arr[raw.length - 1 - i] = (Carta) raw[i];
        }
        return arr;
    }

    public static MazoBaraja desdeArray(Carta[] arr) {
        MazoBaraja mazo = new MazoBaraja();
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] != null) mazo.pila.push(arr[i]);
        }
        return mazo;
    }
}