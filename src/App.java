import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        //Juego con interfaz gráfica

        //JUEGO POR CONSOLA
       /** Scanner teclado = new Scanner(System.in);
        Jugador[] combatientes = Juego.iniciarJuego();
        Jugador Jugador1 = combatientes[0];
        Jugador Jugador2 = combatientes[1];

        byte turnoDe = 1;
        boolean primerTurno = true;

        while (!Jugador1.isGanador() && !Jugador2.isGanador()) {

            System.out.println("\n==========================================");
            System.out.println(" " + Jugador1.getNombreJugador() + " LP: " + Jugador1.getVida() +
                               "   |    " + Jugador2.getNombreJugador() + " LP: " + Jugador2.getVida());
            System.out.println("==========================================");

            System.out.println("\n===== CAMPO ACTUAL =====");
            System.out.println("---- " + Jugador1.getNombreJugador() + " ----");
            Jugador1.mostrarCampo();
            System.out.println("---- " + Jugador2.getNombreJugador() + " ----");
            Jugador2.mostrarCampo();

            if (turnoDe == 1) {

                System.out.println("\n>> Turno de " + Jugador1.getNombreJugador());

                Jugador1.resetTurno();

                System.out.println("\nFASE DE ROBO");
                Jugador1.robarCarta();

                System.out.println("\nFASE PRINCIPAL");

                boolean faseBatalla = false;

                while (!faseBatalla) {

                    mostrarMano(Jugador1);

                    System.out.println("¿Qué deseas hacer?");
                    System.out.println("1. Invocar monstruo");
                    System.out.println("2. Usar mágica");
                    System.out.println("3. Ver campo");
                    System.out.println("4. Ir a fase de batalla");
                    System.out.println("5. Cambiar posición de monstruo");

                    byte opcion = teclado.nextByte();

                    if (opcion == 1) {
                        System.out.print("Elige índice para invocar: ");
                        byte id = teclado.nextByte();
                        System.out.println("Posición 1. ATAQUE  2. DEFENSA:");
                        byte pos = teclado.nextByte();

                        Posicion posicion;

                    if (pos == 2) {
                        posicion = Posicion.DEFENSA;
                    } else {
                        posicion = Posicion.ATAQUE;}

                    Jugador1.invocarMonstruo(id, posicion);

                    } else if (opcion == 2) {
                        System.out.print("Elige índice de la mágica: ");
                        byte id = teclado.nextByte();
                        Jugador1.usarMagia(id, Jugador2);

                    } else if (opcion == 3) {
                        System.out.println("\n===== CAMPO ACTUAL =====");
                        System.out.println("---- " + Jugador1.getNombreJugador() + " ----");
                        Jugador1.mostrarCampo();
                        System.out.println("---- " + Jugador2.getNombreJugador() + " ----");
                        Jugador2.mostrarCampo();

                    } else if (opcion == 4) {
                        faseBatalla = true;
                    } else if (opcion == 5) {
                        System.out.print("Elige índice del monstruo: ");
                        byte id = teclado.nextByte();
                        
                        System.out.print("Nueva posición 1. ATAQUE  2. DEFENSA: ");
                        byte pos = teclado.nextByte();

                        Posicion nuevaPosicion;

                        if (pos == 2) {
                            nuevaPosicion = Posicion.DEFENSA;
                        } else {
                            nuevaPosicion = Posicion.ATAQUE;
                        }
                        Jugador1.cambiarPosicionMonstruo(id, nuevaPosicion);
                    }
                }
            

                System.out.println("\nFASE DE BATALLA");

                if (primerTurno) {
                    System.out.println("No puedes atacar en el primer turno");
                } else {

                    System.out.println("¿Deseas atacar? 1.Sí 2.No");
                    byte atk = teclado.nextByte();

                    if (atk == 1) {

                        System.out.print("Elige tu monstruo: ");
                        byte ind = teclado.nextByte();

                        if (Jugador2.tieneMonstruos()) {

                            System.out.print("Elige monstruo rival: ");
                            byte obj = teclado.nextByte();

                            Jugador2.activarTrampaAtaque(Jugador2, Jugador1, ind);

                            if (!Jugador1.isAtaqueNegado()) {
                                Jugador1.atacar(ind, Jugador2, obj);
                            } else {
                                System.out.println("El ataque fue negado");
                                Jugador1.setAtaqueNegado(false);
                            }

                        } else {

                            Jugador2.activarTrampaAtaque(Jugador2, Jugador1, ind);

                            if (!Jugador1.isAtaqueNegado()) {
                                Jugador1.atacar(ind, Jugador2, (byte)-1);
                            } else {
                                System.out.println("El ataque fue negado");
                                Jugador1.setAtaqueNegado(false);
                            }
                        }
                    }
                }

                System.out.println("\n===== CAMPO ACTUAL =====");
                Jugador1.mostrarCampo();
                Jugador2.mostrarCampo();

                System.out.println("\nFASE FINAL");

                turnoDe = 2;

            } else {

                System.out.println("\n>> Turno de " + Jugador2.getNombreJugador());

                Jugador2.resetTurno();

                System.out.println("\nFASE DE ROBO");
                Jugador2.robarCarta();

                System.out.println("\nFASE PRINCIPAL");

                boolean faseBatalla = false;

                while (!faseBatalla) {

                    mostrarMano(Jugador2);

                    System.out.println("¿Qué deseas hacer?");
                    System.out.println("1. Invocar monstruo");
                    System.out.println("2. Usar mágica");
                    System.out.println("3. Ver campo");
                    System.out.println("4. Ir a fase de batalla");
                    System.out.println("5. Cambiar posición de monstruo");

                    byte opcion = teclado.nextByte();

                    if (opcion == 1) {
                        System.out.print("Elige índice para invocar: ");
                        byte id = teclado.nextByte();
                        System.out.println("Posición 1. ATAQUE  2. DEFENSA:");
                        byte pos = teclado.nextByte();
                        Posicion posicion;
                    if (pos == 2) {
                        posicion = Posicion.DEFENSA;
                    } else {
                        posicion = Posicion.ATAQUE;}
                        Jugador2.invocarMonstruo(id, posicion);

                    } else if (opcion == 2) {
                        System.out.print("Elige índice de la mágica: ");
                        byte id = teclado.nextByte();
                        Jugador2.usarMagia(id, Jugador1);

                    } else if (opcion == 3) {
                        System.out.println("\n===== CAMPO ACTUAL =====");
                        System.out.println("---- " + Jugador1.getNombreJugador() + " ----");
                        Jugador1.mostrarCampo();
                        System.out.println("---- " + Jugador2.getNombreJugador() + " ----");
                        Jugador2.mostrarCampo();

                    } else if (opcion == 4) {
                        faseBatalla = true;
                    }else if (opcion == 5) {
                        System.out.print("Elige índice del monstruo: ");
                        byte id = teclado.nextByte();
                        
                        System.out.print("Nueva posición 1. ATAQUE  2. DEFENSA: ");
                        byte pos = teclado.nextByte();

                        Posicion nuevaPosicion;

                        if (pos == 2) {
                            nuevaPosicion = Posicion.DEFENSA;
                        } else {
                            nuevaPosicion = Posicion.ATAQUE;
                        }
                        Jugador2.cambiarPosicionMonstruo(id, nuevaPosicion);
                }
                }
                System.out.println("\nFASE DE BATALLA");

                if (primerTurno) {
                    System.out.println("No puedes atacar en el primer turno");
                } else {

                    System.out.println("¿Deseas atacar? 1.Sí 2.No");
                    byte atk = teclado.nextByte();

                    if (atk == 1) {

                        System.out.print("Elige tu monstruo: ");
                        byte ind = teclado.nextByte();

                        if (Jugador1.tieneMonstruos()) {

                            System.out.print("Elige monstruo rival: ");
                            byte obj = teclado.nextByte();

                            Jugador1.activarTrampaAtaque(Jugador1, Jugador2, ind);

                            if (!Jugador2.isAtaqueNegado()) {
                                Jugador2.atacar(ind, Jugador1, obj);
                            } else {
                                System.out.println("El ataque fue negado");
                                Jugador2.setAtaqueNegado(false);
                            }

                        } else {

                            Jugador1.activarTrampaAtaque(Jugador1, Jugador2, ind);

                            if (!Jugador2.isAtaqueNegado()) {
                                Jugador2.atacar(ind, Jugador1, (byte)-1);
                            } else {
                                System.out.println("El ataque fue negado");
                                Jugador2.setAtaqueNegado(false);
                            }
                        }
                    }
                }

                System.out.println("\n===== CAMPO ACTUAL =====");
                Jugador1.mostrarCampo();
                Jugador2.mostrarCampo();

                System.out.println("\nFASE FINAL");

                turnoDe = 1;
            }

            primerTurno = false;

            if (Jugador1.getVida() <= 0) {
                Jugador2.setGanador(true);
            } else if (Jugador2.getVida() <= 0) {
                Jugador1.setGanador(true);
            }
        }

        System.out.println("\n========== FIN DEL DUELO ==========");
        System.out.println(Jugador1.getNombreJugador() + " LP: " + Jugador1.getVida() + " | " + Jugador2.getNombreJugador() + " LP: " + Jugador2.getVida());

        if (Jugador1.isGanador()) {
            System.out.println("¡El Duelista " + Jugador1.getNombreJugador() + " ha ganado!");
        } else if (Jugador2.isGanador()) {
            System.out.println("¡El Duelista " + Jugador2.getNombreJugador() + " ha ganado!");
        } else {
            System.out.println("Empate");
        }
    } **/
}

    public static void mostrarMano(Jugador jugador) {
        System.out.println("----- Mano de " + jugador.getNombreJugador() + " -----");

        Carta[] mano = jugador.getMano();

        for (int i = 0; i < mano.length; i++) {
            if (mano[i] != null) {

                if (mano[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) mano[i];
                    System.out.println(i + ". " + m.getNombre() +
                            " ATK: " + m.getAtaque() +
                            " DEF: " + m.getDefensa());
                } else if (mano[i] instanceof Magia) {
                    System.out.println(i + ". " + mano[i].getNombre() + " (Magia)");
                } else {System.out.println(i + ". " + mano[i].getNombre() + " (Trampa)");
            }
        }
    }
}
}
