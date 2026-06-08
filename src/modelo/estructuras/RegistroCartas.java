package modelo.estructuras;

import modelo.Carta;
import modelo.FabricaCartas;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegistroCartas {

    private static RegistroCartas instancia;
    private final HashMap<String, Carta> indice;

    private RegistroCartas() {
        indice = new HashMap<>();
        for (Carta c : FabricaCartas.crearCartas()) {
            indice.put(c.getNombre(), c);
        }
    }

    public static RegistroCartas getInstance() {
        if (instancia == null) {
            instancia = new RegistroCartas();
        }
        return instancia;
    }

    public Carta buscarPorNombre(String nombre) { return indice.get(nombre); }
    public void registrar(Carta carta) { indice.put(carta.getNombre(), carta); }
    public boolean existe(String nombre) { return indice.containsKey(nombre); }
    public Collection<Carta> todasLasCartas() { return indice.values(); }
    public Map<String, Carta> getIndice() { return Collections.unmodifiableMap(indice); }
    public int size() { return indice.size(); }
}