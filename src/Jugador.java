public class Jugador {
    private String nombreJugador;
    private short vida;
    private boolean turno;
    private Fases fase;
    private boolean ganador;
    private Carta[] mano;
    private Carta[] campo;
    private Carta[] cementerio;
    private Carta[] baraja;
    public Jugador(String nombreJugador, Carta[] barajaInicial) {
        this.nombreJugador = nombreJugador;
        this.vida = 8000;   /// lo pondo por defecto, porque se supone que todos los jugadores inician con esta vida
        this.baraja = barajaInicial;
        this.mano = new Carta[6];
        this.campo = new Carta[5];
        this.cementerio = new Carta[20];
        this.ganador = false;
    }
    public enum Fases{
    Una, Dos, Tres, Cuatro, Cinco, Seis,
    Siete, Ocho, Nueve, Diez
    }
    
    public short getVida() {
        return vida;
    }
    public void setVida(short vida) {
        this.vida = vida;
    }
    public String getNombreJugador() {
        return nombreJugador;
    }
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    public boolean isTurno() {
        return turno;
    }
    public void setTurno(boolean turno) {
        this.turno = turno;
    }
    public boolean isGanador() {
        return ganador;
    }
    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }
    public Carta[] getMano() {
        return mano;
    }
    public void setMano(Carta[] mano) {
        this.mano = mano;
    }
    public Carta[] getCampo() {
        return campo;
    }
    public void setCampo(Carta[] campo) {
        this.campo = campo;
    }
    public Carta[] getCementerio() {
        return cementerio;
    }
    public void setCementerio(Carta[] cementerio) {
        this.cementerio = cementerio;
    }
    public Fases getFase() {
        return fase;
    }
    public void setFase(Fases fase) {
        this.fase = fase;
    }
    public Carta[] getBaraja() {
        return baraja;
    }
    public void setBaraja(Carta[] baraja) {
        this.baraja = baraja;
    }
    public void robarCarta(){
        Carta CartaNueva = null;
        int indice = 0;
        for(int i = 0; i < 20; i++){
            if(this.baraja[i] != null){
                CartaNueva = this.baraja[i];
                indice = i;
                break;
            }
        }
        if(CartaNueva == null){
            System.out.println("La baraja de "+getNombreJugador()+" ya se quedó vacia");
            setGanador(false);
            return;
        }
        boolean r = false;
        for (int i = 0; i < getMano().length; i++) {
            if (this.mano[i] == null) {
                this.mano[i] = CartaNueva;
                this.baraja[indice] = null;
            
                this.mano[i].setEstado(Estado.MANO); 
                
                System.out.println(getNombreJugador() + " robó: " + CartaNueva.getNombre());
                r = true;
                break;
            }
        }
        if(r==false){
            System.out.println(getNombreJugador()+" no pudo robar la carta, probablemente su mano esté lleno");
        }
    }
    public void mostrarMano(){
        System.out.println("----------Mano de "+getNombreJugador()+"----------");
        boolean f = true;
            for(int i = 0; i < this.mano.length; i++){
                Carta j = this.mano[i];
                if(j!=null){
                    if(j instanceof Mounstro){
                        Mounstro m = (Mounstro) j;
                        System.out.println(i+". "+j.getNombre()+" ATK: "+m.getAtaque()+" DEF: "+m.getDefensa());
                        f=false;
                    }else{
                        System.out.println(i+". "+j.getNombre());
                        f=false;
                    }
                }
            }
        if(f){System.out.println("===== La mano está vacia =====");}
    }
    public void invocarMonstruo(int indice){
        if(indice<0 || indice>=(mano.length) || mano[indice] == null){
            System.out.println("Posicion invalida");
            return;
        }
        Carta c = mano[indice];
        boolean espacio = true;
        for (int i = 0; i < campo.length; i++) {
            if (campo[i] == null) {
                campo[i] = c;
                campo[i].setEstado(Estado.CAMPO);
                mano[indice] = null;
                espacio=false;
                System.out.println("El jugador "+getNombreJugador()+" a invocado "+c.getNombre());
                break;
            }
        }
        if(espacio){
            System.out.println("No se a encontrado ningun espacio disponible");
        }
    }
    public void mostrarCampo(){
        System.out.println("----------Campo de "+getNombreJugador()+"----------");
        boolean f = true;
            for(int i = 0; i < campo.length; i++){
                Carta j = campo[i];
                if(j!=null){
                    if(j instanceof Mounstro){
                        Mounstro m = (Mounstro) j;
                        System.out.println("[ "+j.getNombre()+" ]");
                        f=false;
                    }else{
                        System.out.println("[ "+j.getNombre()+" ] (Magia)");
                        f=false;
                    }
                }
            }
        if(f){
            System.out.println("----------El campo está vacio----------");
        }
    }
    public void atacar(byte miInd, Jugador oponente, byte contraInd){
        if (this.campo[miInd] == null ) {
            System.out.println("No existe un monstro el cual ataque");
            return;
        }
        Mounstro miM = (Mounstro) this.campo[miInd];
        Mounstro suM = (Mounstro) oponente.getCampo()[contraInd];
        System.out.println(" ");
        if(suM == null){
            System.out.println("Ataque Directo!!!");
            oponente.setVida((short)(oponente.getVida()-miM.getAtaque()));
        }else{
            System.out.println(miM.getNombre()+" ( ATK-"+miM.getAtaque()+" ) ATACA A "+suM.getNombre()+" ( DEF-"+suM.getDefensa()+" )");
            if(miM.getAtaque()>suM.getDefensa()){
                short resultado = (short)(miM.getAtaque() - suM.getDefensa());
                oponente.setVida((short)((oponente.getVida())-resultado));
                oponente.muereMounstro(contraInd);
            }else if(miM.getAtaque()<suM.getDefensa()){
                short resultado = (short)(suM.getDefensa() - miM.getAtaque());
                setVida((short)((getVida())-resultado));
                muereMounstro(miInd);
            }
        }
    }

    public void muereMounstro(int indice){
        if(indice<0 || indice>=(campo.length) || campo[indice] == null){
            System.out.println("Posicion invalida");
            return;
        }
        Carta c = getCampo()[indice];
        boolean espacio = true;
        for (int i = 0; i < cementerio.length; i++) {
            if (getCementerio()[i] == null) {
                System.out.println("El monstruo "+c.getNombre()+" fue destruido");
                getCementerio()[i] = c;
                getCementerio()[i].setEstado(Estado.CEMENTERIO);
                getCampo()[indice] = null;
                espacio=false;
                break;
            }
        }
        if(espacio){
            System.out.println("No se a encontrado ningun espacio disponible");
        }
    }

}

