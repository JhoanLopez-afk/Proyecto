import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {

        List<Carta> BarajaDeCartas = generarBaraja();

        








    }

    public static List<Carta> generarBaraja() {
    List<Carta> mazoCompleto = new ArrayList<>();
    String[] nombres = {"Mago Oscuro", "Dragón Blanco","Fantasma Flamigero", "Dragon Negro", "Guardián Celta",
     "El Mega-Ciber Demniaco","Gaitero Dragon","Dama Arpia", "Craneo Convocado", "Araña Cazadora","Gilasaurus","Dama Pantera",
     "Caballero Robotico","Guardian Fiable","Demonio Menor","Gran Tiki Anciano", "Arquera Amazoness", "Caiman Toon",
     "Dragon Craneo Demonio", "Capitan Merodeador", "Ave de Craneo Rojo", "Balter Oscuro","Cazador de Espadas",
     "Baron de la Espada Demoniaca","Egotista Elegante", "Bebe Dragon","Cebra Oscura","Espia Inexperto", "Behemoth",
     "Bruja Oscura"};
    
    // Crear 30 Monstruos con stats variados
    for (int i = 0; i < 30; i++) {
        String nombre = nombres[i];
        short atk = (short) (1000 + ((int)(Math.random()*181)+200)); // El ataque sube un poco en cada uno
        short def = (short) (800 + ((int)(Math.random()*21)+50));
        Mounstro.Estrellas aleatoria = Mounstro.Estrellas.values()[new Random().nextInt(Mounstro.Estrellas.values().length)]; 
        
        mazoCompleto.add(new Mounstro(nombre, atk, def, TipoCarta.MOUNSTRUO, 
                         aleatoria, false, "Sin efecto", Estado.BARAJA));
    }

    // Crear 10 Magias
    for (int i = 0; i < 10; i++) {
        NombreMagia NM = NombreMagia.values()[new Random().nextInt(NombreMagia.values().length)];


        mazoCompleto.add(new Magia(NM + "i", TipoCarta.MAGICA, 
                         false, "Efecto básico", Estado.BARAJA));
    }

    // ¡IMPORTANTE! Barajar para que sea al azar (Requisito RF2)
    Collections.shuffle(mazoCompleto);
    
    return mazoCompleto;
}

} 
