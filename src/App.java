public class App {
    public static void main(String[] args) throws Exception {
        Mounstro carta1 = new Mounstro("Carta1", (short) 3000, (short) 2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cinco, true, "Efecto de la carta1", Estado.MANO);
        Magia carta2 = new Magia("Carta2", TipoCarta.MAGICA, false , "Efecto de la carta2", Estado.CEMENTERIO);
        Trampa carta3 = new Trampa("Carta3", TipoCarta.TRAMPA, true, "Efecto de la carta3", Estado.CAMPO);

        System.out.println(carta1.getNombre() + " - " + carta1.getTipo() + " - " + carta1.isVisible() + " - " + carta1.getEstrellas() + " - " + carta1.getAtaque() + " - " + carta1.getDefensa() + " - " + carta1.getEfecto() + " - " + carta1.getEstado());
        System.out.println(carta2.getNombre() + " - " + carta2.getTipo() + " - " + carta2.isVisible() + " - " + carta2.getEfecto() + " - " + carta2.getEstado());
        System.out.println(carta3.getNombre() + " - " + carta3.getTipo() + " - " + carta3.isVisible() + " - " + carta3.getEfecto() + " - " + carta3.getEstado());
    }
} 
