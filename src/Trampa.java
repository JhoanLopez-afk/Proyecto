import java.util.Scanner;

public class Trampa extends Carta implements Activable {
     public Trampa(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }
    @Override
    public void ejecutarEfecto(Jugador usuario, Jugador oponente) {
    Scanner sc = new Scanner(System.in);

    if (getNombre().equals("Fuerza de espejo")) {
        int destruidos = 0;
            for (int i = 0; i < oponente.getCampo().length; i++) {
                if (oponente.getCampo()[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) oponente.getCampo()[i];
                    if (m.getPosicion() == Posicion.ATAQUE) {
                        oponente.muereMounstro(i);
                        destruidos++;
                    }
                }
            }
            System.out.println("Fuerza de Espejo: " + destruidos + " monstruos en ATK destruidos.");
        }
    
    if (getNombre().equals("Negar Ataque")) {
        usuario.setAtaqueNegado(true);
        usuario.setBattlePhaseTerminada(true);

        System.out.println("El ataque fue negado.");
        System.out.println("La Battle Phase termina.");
    }

        
