public class Magia extends Carta implements Activable{

    public Magia(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }

    @Override
    public void ejecutarEfecto(Jugador usuario, Jugador oponente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ejecutarEfecto'");
    }
    
    
}
