import java.util.Scanner;

public class Trampa extends Carta implements Activable {
     public Trampa(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        super(nombre, tipo, visible, efecto, estado);
    }
    @Override
    public void ejecutarEfecto(Jugador jugador, Jugador oponente) {
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
    
   else  if (getNombre().equals("Negar Ataque")) {
        jugador.setAtaqueNegado(true);
        jugador.setBattlePhaseTerminada(true);

        System.out.println("El ataque fue negado.");
        System.out.println("La Battle Phase termina.");
    }

    else if (getNombre().equals("Círculo Atahechizos")) {
        boolean existeMounstro = false;
        System.out.println("Monstruos del oponente:");
        for (int i = 0; i < oponente.getCampo().length; i++) {
            if (oponente.getCampo()[i] instanceof Mounstro) {
                Mounstro m = (Mounstro) oponente.getCampo()[i];
                System.out.println(i + ": " + m.getNombre() + " (Pos: " + m.getPosicion() + ")");
                existeMounstro = true;
            }
        }
        if (!existeMounstro) {
            System.out.println("No hay monstruos para seleccionar.");
            return;
        }
        System.out.print("Selecciona el índice del monstruo a paralizar: ");
        int indice = sc.nextInt();
        if (indice >= 0 && indice < oponente.getCampo().length && oponente.getCampo()[indice] instanceof Mounstro) {
            Mounstro m = (Mounstro) oponente.getCampo()[indice];
            m.setParalizado(true);
            System.out.println("El monstruo " + m.getNombre() + " ha sido paralizado y no podrá atacar ni cambiar de posición.");
        } else {
            System.out.println("Índice inválido. No se ha paralizado ningún monstruo.");
        }
            
            return;
        }
         else if (getNombre().equals("Juicio Solemne")) {
            short mitad = (short) (jugador.getVida() / 2);
            jugador.setVida((short) (jugador.getVida() - mitad));
            oponente.setAtaqueNegado(true);
            oponente.setMagiaBloqueada(true);
             System.out.println("pagas " + mitad + " LP (te quedan " + jugador.getVida() + ")");
         }
         else if (getNombre().equals("Agujero Trampa Sin Fondo")) {
            int destruidos = 0;
            for (int i = 0; i < oponente.getCampo().length; i++) 
                if (oponente.getCampo()[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) oponente.getCampo()[i];
                    if (m.getAtaque() >= 1500) {
                         System.out.println("  Destruido: " + m.getNombre() + " (ATK " + m.getAtaque() + ")");
                         oponente.muereMounstro(i);
                         destruidos++;
                         }
                    }
            if (destruidos == 0) {
                System.out.println("No se destruyó ningún monstruo porque todos tenían un ATK menor a 1500.");
            } else {
                System.out.println("Agujero Trampa Sin Fondo: " + destruidos + " monstruos destruidos.");
            }
        }

        else if (getNombre().equals("Artilugio de Evacuación Compulsiva")) {
            System.out.println("¿De qué campo quieres devolver un monstruo?");
            System.out.println("  1. Mi campo   2. Campo del oponente");
            int opcion = sc.nextInt();
            Jugador objetivo;
            if (opcion == 1) {
                objetivo = jugador;
            } else if (opcion == 2) {
                objetivo = oponente;
            } else {
                System.out.println("Opción inválida. No se ha devuelto ningún monstruo.");
                return;
            }

            boolean existeMounstro = false;
            System.out.println("Campo de " + objetivo.getNombreJugador() + ":");
            for (int i = 0; i < objetivo.getCampo().length; i++) {
                if (objetivo.getCampo()[i] instanceof Mounstro) {
                    Mounstro m = (Mounstro) objetivo.getCampo()[i];
                    System.out.println(i + ": " + m.getNombre() + " (Pos: " + m.getPosicion() + ")");
                    existeMounstro = true;
                }
            }
            if (!existeMounstro) {
                System.out.println("No hay monstruos para seleccionar.");
                return;
            }
            System.out.print("Selecciona el índice del monstruo a devolver: ");
            int indice = sc.nextInt();
             if (indice >= 0 && indice < objetivo.getCampo().length && objetivo.getCampo()[indice] instanceof Mounstro) {
                Carta carta = objetivo.getCampo()[indice];
                objetivo.getCampo()[indice] = null;
                ((Mounstro) carta).setParalizado(false);
                
                for (int i = 0; i < objetivo.getMano().length; i++) {
                    if (objetivo.getMano()[i] == null) {
                        objetivo.getMano()[i] = carta;
                        carta.setEstado(Estado.MANO);
                        System.out.println("El monstruo " + carta.getNombre() + " ha sido devuelto a la mano de " + objetivo.getNombreJugador());
                        return;
                    }
                }
             } else { System.out.println("Índice inválido. No se ha devuelto ningún monstruo.");
            }
        }
        else if (getNombre().equals("cilindros mágicos")) {
            int indice = oponente.getIndiceAtacanteRival();

            if (indice < 0 || indice >= oponente.getCampo().length || !(oponente.getCampo()[indice] instanceof Mounstro)) {
                System.out.println("No hay un monstruo atacando.");
                return;
            }
            Mounstro atacante = (Mounstro) oponente.getCampo()[indice];
            short daño = atacante.getAtaque();
            oponente.setVida((short)(oponente.getVida() - daño));
            System.out.println(atacante.getNombre() + " ha recibido " + daño + " puntos de daño.");
        }
        else if (getNombre().equals("llamada de los condenados")) {
            boolean existeMounstro = false;
            System.out.println("Cementerio de " + jugador.getNombreJugador() + ":");
            for (int i = 0; i < jugador.getCementerio().length; i++) {
                if (jugador.getCementerio()[i] instanceof Mounstro) {
                    System.out.println(i + ": " + jugador.getCementerio()[i].getNombre());
                    existeMounstro = true;
                    }
                }
            if (!existeMounstro) {
                System.out.println("No hay monstruos en el cementerio para seleccionar.");
                return;
                }
            System.out.print("Selecciona el índice del monstruo a revivir: ");
            int indice = sc.nextInt();
            if (indice >= 0 && indice < jugador.getCementerio().length  && jugador.getCementerio()[indice] instanceof Mounstro) {

                Carta carta = jugador.getCementerio()[indice];

                int espacioMounstro = -1;
                for (int i = 0; i < jugador.getCampo().length; i++) {
                    if (jugador.getCampo()[i] == null) {
                        espacioMounstro = i;
                        break;
                    }
                }
            if (espacioMounstro == -1) {
                System.out.println("Campo de monstruos lleno, no se puede invocar.");
                return;
            }
             int espacioMagia = -1;
             for (int i = 0; i < jugador.getCampoMagias().length; i++) {
                if (jugador.getCampoMagias()[i] == null) {
                    espacioMagia = i;
                    break;
                }
            }
            if (espacioMagia == -1) {
                 System.out.println("Campo de mágicas/trampas lleno, no se puede activar Llamada de los Condenados.");
            return;
            }

            jugador.getCementerio()[indice] = null;
            Mounstro m = (Mounstro) carta;
            m.setPosicion(Posicion.ATAQUE);
            jugador.getCampo()[espacioMounstro] = carta;
            carta.setEstado(Estado.CAMPO);

             jugador.getCampoMagias()[espacioMagia] = this;
             this.setEstado(Estado.CAMPO);
             jugador.setLlamadaDeLosCondenados(espacioMounstro);
             jugador.setIndiceTrampaLlamada(espacioMagia);

             System.out.println(carta.getNombre() + " invocado en ATK.");
             System.out.println("Llamada de los Condenados permanece en el campo de mágicas/trampas.");
            } else {
                System.out.println("Índice inválido. No se ha revivido ningún monstruo.");
        }
    }
}
    @Override
    public void usar(Jugador jugador, Jugador oponente) {
        ejecutarEfecto(jugador, oponente);
        for (int i = 0; i < jugador.getCementerio().length; i++) {
            if (jugador.getCementerio()[i] == null) {
                jugador.getCementerio()[i] = this;
                this.setEstado(Estado.CEMENTERIO);
                break;
            }
        }
}
}


        
