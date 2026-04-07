import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);

        Jugador[] combatientes = Juego.iniciarJuego();
        Jugador Jugador1 = combatientes[0];
        Jugador Jugador2 = combatientes[1];

        byte turnoDe = 1;
        short contador = 0;

        while (Jugador1.isGanador() == false && Jugador2.isGanador() == false) {
            System.out.println("\n--- MOVIMIENTO " + (contador + 1) + " ---");

            if (turnoDe == 1) {
                System.out.println(">> Turno de " + Jugador1.getNombreJugador()+"\n");
                System.out.println("¿Deseas robar una carta? 1.Si, 2.No");
                byte eleccion = teclado.nextByte();
                if(eleccion==1){
                    Jugador1.robarCarta();
                }
                
                Jugador1.mostrarMano();

                System.out.println("¿Que deseas hacer? 1.Invocar monstruo, 2.Usar magica, 3.Nada");
                byte accion = teclado.nextByte();
                if(accion==1){
                    System.out.print("Elige índice para invocar: ");
                    byte id = teclado.nextByte();
                    Jugador1.invocarMonstruo(id);
                }else if(accion==2){
                    System.out.print("Elige índice de la magica: ");
                    byte id = teclado.nextByte();
                    Jugador1.usarMagia(id, Jugador2);
                }

                if(contador>0){
                    System.out.println("¿Deseas atacar en este turno? 1.Si, 2.No");
                    byte elegir = teclado.nextByte();
                    if(elegir==1){
                        System.out.println("Con que indice desea atacar: ");
                        byte ind = teclado.nextByte();
                        System.out.println("Indice del mounstro rival para atacar: ");
                        byte a = teclado.nextByte();
                        Jugador1.atacar((byte)ind, Jugador2, (byte)a); 
                    }
                }

                Jugador1.mostrarCampo();
                turnoDe = 2; 

            } else {
                System.out.println(">> Turno de " + Jugador2.getNombreJugador()+"\n");
                System.out.println("¿Deseas robar una carta? 1.Si, 2.No");
                byte eleccion = teclado.nextByte();
                if(eleccion==1){
                    Jugador2.robarCarta();
                }

                Jugador2.mostrarMano();

                System.out.println("¿Que deseas hacer? 1.Invocar monstruo, 2.Usar magica, 3.Nada");
                byte accion = teclado.nextByte();
                if(accion==1){
                    System.out.print("Elige índice para invocar: ");
                    byte idx = teclado.nextByte();
                    Jugador2.invocarMonstruo(idx);
                }else if(accion==2){
                    System.out.print("Elige índice de la magica: ");
                    byte idx = teclado.nextByte();
                    Jugador2.usarMagia(idx, Jugador1);
                }

                if(contador>0){
                    System.out.println("¿Deseas atacar en este turno? 1.Si, 2.No");
                    byte b = teclado.nextByte();
                    if(b==1){
                        System.out.println("Con que indice desea atacar: ");
                        byte ind = teclado.nextByte();
                        System.out.println("Indice del mounstro rival para atacar: ");
                        byte a = teclado.nextByte();
                        Jugador2.atacar((byte)ind, Jugador1, (byte)a); 
                    }
                }

                Jugador2.mostrarCampo();
                turnoDe = 1; 
            }
            contador++;
            if(Jugador1.getVida()<=0){Jugador2.setGanador(true);}else if(Jugador2.getVida()<=0){Jugador1.setGanador(true);}
            byte numC1 = 0;
            for(int i = 0; i<Jugador1.getCementerio().length;i++){
                if(Jugador1.getCementerio()[i]!=null){numC1++;}
            }
            if(numC1>=20){Jugador2.setGanador(true);}

            byte numC2 = 0;
            for(int i = 0; i<Jugador2.getCementerio().length;i++){
                if(Jugador2.getCementerio()[i]!=null){numC2++;}
            }
            if(numC2>=20){Jugador1.setGanador(true);}
        }

        if(Jugador1.isGanador()){
            System.out.println("El Combatiente "+Jugador1.getNombreJugador()+" a ganado la pelea");
        }else if(Jugador2.isGanador()){
            System.out.println("El Combatiente "+Jugador2.getNombreJugador()+" a ganado la pelea");
        }else{
            System.out.println("Ninguno ganó");
        }
    }
}
