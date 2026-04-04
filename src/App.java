import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {

        List<Carta> BarajaDeCartas = generarBaraja();

        Carta[] barajaJugador1 = new Carta[20];
        Carta[] barajaJugador2 = new Carta[20];
        
        for(int i = 0; i < 40; i++){
            if(i<20){
                barajaJugador1[i] = BarajaDeCartas.get(i);
            }else{
                barajaJugador2[i] = BarajaDeCartas.get(i);
            }
        }

        Jugador Jugador1 = new Jugador("Quevedo", barajaJugador1);
        Jugador Jugador2 = new Jugador("Camilo", barajaJugador2);

        System.out.println("-----INICIA LA PELEA!!!-----");
        System.out.println("Combatiente 1: "+Jugador1.getNombreJugador()+" tiene su baraja lista.");
        System.out.println("Combatiente 2: "+Jugador2.getNombreJugador()+" tiene su baraja lista.");

        /*System.out.println("Creen sus manos iniciales: ");
        for(int i = 0; i < 5; i++){
            
        }*/

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
