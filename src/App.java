public class App {
    public static void main(String[] args) throws Exception {
        Mounstro carta1 = new Mounstro("Carta1", (short) 3000, (short) 2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cinco, true, "Efecto de la carta1", Estado.MANO);
        Magia carta2 = new Magia("Carta2", TipoCarta.MAGICA, false , "Efecto de la carta2", Estado.CEMENTERIO);

        System.out.println(carta1.getNombre() + " - " + carta1.getTipo() + " - " + carta1.isVisible() + " - " + carta1.getEstrellas() + " - " + carta1.getAtaque() + " - " + carta1.getDefensa() + " - " + carta1.getEfecto() + " - " + carta1.getEstado());
        System.out.println(carta2.getNombre() + " - " + carta2.getTipo() + " - " + carta2.isVisible() + " - " + carta2.getEfecto() + " - " + carta2.getEstado());

    }
} 
