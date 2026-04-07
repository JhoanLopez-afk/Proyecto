public class Magia extends Carta implements Activable{

    public Magia(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }

    @Override
    public void ejecutarEfecto(Jugador yo, byte miInd, Jugador oponente, byte contraInd) {
        switch (this.getNombre()) {
            case "Olla de la codicia":
                yo.robarCarta();
                yo.robarCarta();
                System.out.println(yo.getNombreJugador() + " ha robado 2 cartas.");
                break;

            case "Monstruo renacido":
                // Busca el primer muerto en tu cementerio e invócalo
                for (int i = 0; i < yo.getCementerio().length; i++) {
                    if (yo.getCementerio()[i] instanceof Mounstro) {
                        yo.invocarDesdeCementerio(); // Necesitarías este pequeño método en Jugador
                        System.out.println("¡Un monstruo ha regresado del abismo!");
                        break;
                    }
                }
                break;

            case "Agujero Oscuro":
                for (int i = 0; i < 5; i++) {
                    if (yo.getCampo()[i] != null){
                        yo.muereMounstro((byte)i);
                    }
                    if (oponente.getCampo()[i] != null){
                        oponente.muereMounstro((byte)i);
                    }
                }
                System.out.println("El campo quedó como Ucrania!");
                break;

            case "Tifon del espacio Mistico":
                // Destruye la carta en el índice que seleccionaste como objetivo
                if (oponente.getCampo()[contraInd] instanceof Magia) {
                    oponente.muereMounstro(contraInd);
                    System.out.println("Hechizo enemigo destruido.");
                } else {
                    System.out.println("El objetivo no era una carta mágica.");
                }
                break;

            case "Caridad elegante":
                // manda las 2 primeras cartas de la mano al cementerio
                yo.muereMounstro(0); 
                yo.muereMounstro(1);

                yo.robarCarta(); yo.robarCarta(); yo.robarCarta();

                System.out.println("Robaste 3 y descartaste 2.");
                break;

            case "Dian Keto, el Señora de la Curación":
                yo.setVida((short)(yo.getVida() + 1000));
                System.out.println(yo.getNombreJugador() + " recupera 1000 LP.");
                break;

            case "Tormenta Fuerte":
                for (int i = 0; i < 5; i++) {
                    if (yo.getCampo()[i] instanceof Magia){
                        yo.muereMounstro((byte)i);
                    }
                    if (oponente.getCampo()[i] instanceof Magia){
                        oponente.muereMounstro((byte)i);
                    }
                }
                System.out.println("Todas las cartas mágicas han sido destruidas.");
                break;
//-------------------------------------------------------------------------------------------
            case "Reproduccion de hechizo":
                // Lógica simple: sacrificas vida por cartas
                yo.setVida((short)(yo.getVida() - 1000));
                yo.robarCarta();
                System.out.println("Cambiaste energía vital por conocimiento.");
                break;
//---------------------------------------------------------------------------------------------
            case "Intercambio":
                // Intercambia los puntos de vida de ambos jugadores
                Carta[] inter= yo.getMano();
                yo.setMano(oponente.getMano());;
                oponente.setMano(inter);
                System.out.println("¡Las cartas han sido intercambiadas!");
                break;

            case "Hinotama":
                oponente.setVida((short)(oponente.getVida() - 500));
                System.out.println("500 de daño directo a " + oponente.getNombreJugador());
                break;

            default:
                System.out.println("Efecto no implementado o no encontrado.");
                break;
        }

    }
    
    
}
