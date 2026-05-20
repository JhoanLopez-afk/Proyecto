package modelo;

import modelo.enums.Estado;
import modelo.enums.TipoCarta;

import java.util.ArrayList;


public class FabricaCartas {

    public static ArrayList<Carta> crearCartas() {
        ArrayList<Carta> cartas = new ArrayList<>();

        // ── Monstruos ────────────────────────────────────────────────
        cartas.add(new Mounstro("Abismo Reluciente",          (short)1600,(short)1800, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Utiliza tanto los poderes de la luz como los de la oscuridad",                                                                             Estado.BARAJA));
        cartas.add(new Mounstro("Bruja Oscura Dunames",       (short)1800,(short)1050, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Aun cuando las posibilidades estén en su contra, esta brava hada avanzará.",                                                               Estado.BARAJA));
        cartas.add(new Mounstro("Buey de Barbacoa",           (short)1000,(short)1000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Un monstruoso buey con una lengua muy fina.",                                                                                              Estado.BARAJA));
        cartas.add(new Mounstro("Apligarto",                  (short)1500,(short)0,    TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Un dragón antropomórfico nacido del alma de un dragón.",                                                                                   Estado.BARAJA));
        cartas.add(new Mounstro("Cementerio de Mamuts",       (short)1200,(short)800,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Un mamut que protege las tumbas de su manada.",                                                                                            Estado.BARAJA));
        cartas.add(new Mounstro("Cerebro Antiguo",            (short)1000,(short)700,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Una hada caída que es poderosa en la oscuridad.",                                                                                          Estado.BARAJA));
        cartas.add(new Mounstro("Ave de fe",                  (short)1500,(short)1100, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Este pájaro de cola larga ciega a sus enemigos con una luz mística.",                                                                      Estado.BARAJA));
        cartas.add(new Mounstro("Asesino de espada",          (short)1450,(short)1500, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cinco,   true, "Un motor letal de destrucción que empuña dos hojas bien afiladas.",                                                                        Estado.BARAJA));
        cartas.add(new Mounstro("Brazo Derecho del prohibido",(short)200, (short)300,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Una,     true, "Un brazo derecho prohibido, sellado mágicamente.",                                                                                         Estado.BARAJA));
        cartas.add(new Mounstro("Brazo Izquierdo del prohibido",(short)200,(short)300, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Una,     true, "Un brazo izquierdo prohibido, sellado mágicamente.",                                                                                        Estado.BARAJA));
        cartas.add(new Mounstro("Pierna izquierda del prohibido",(short)200,(short)300,TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Una,     true, "Una pierna izquierda prohibida, sellada mágicamente.",                                                                                      Estado.BARAJA));
        cartas.add(new Mounstro("Pierna Derecha del prohibido",(short)200,(short)300,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Una,     true, "Una pierna derecha prohibida, sellada mágicamente.",                                                                                        Estado.BARAJA));
        cartas.add(new Mounstro("Bebé Dragón",                (short)1200,(short)700,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Mucho más que sólo un niño, este dragón está dotado de un poder sin descubrir.",                                                            Estado.BARAJA));
        cartas.add(new Mounstro("Devorador de aire",          (short)2100,(short)1600, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Seis,    true, "Un monstruo que se alimenta de oxígeno.",                                                                                                  Estado.BARAJA));
        cartas.add(new Mounstro("Dama de la fé",              (short)1100,(short)800,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Calma las almas de los demás entonando un misterioso hechizo.",                                                                             Estado.BARAJA));
        cartas.add(new Mounstro("El caballero ilusorio",      (short)1500,(short)1600, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Llevando ropas muy raras, este caballero es muy voluble.",                                                                                  Estado.BARAJA));
        cartas.add(new Mounstro("Gai el caballo feroz",       (short)2300,(short)2100, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Seis,    true, "Un caballero cuyo caballo galopa más rápido que el viento.",                                                                                Estado.BARAJA));
        cartas.add(new Mounstro("Dragón Negro de Ojos Rojos", (short)2400,(short)2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Siete,   true, "Un dragón feroz con un ataque mortal.",                                                                                                     Estado.BARAJA));
        cartas.add(new Mounstro("Mago oscuro",                (short)2500,(short)2100, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Siete,   true, "El más grande de los magos en cuanto al ataque y la defensa.",                                                                              Estado.BARAJA));
        cartas.add(new Mounstro("Dragón Blanco de Ojos Azules",(short)3000,(short)2500,TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Ocho,    true, "Este legendario dragón es una poderosa máquina de destrucción. Virtualmente invencible.",                                                   Estado.BARAJA));
        cartas.add(new Mounstro("M-Guerrero 1",               (short)1000,(short)500,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Especializado en ataques combinados.",                                                                                                      Estado.BARAJA));
        cartas.add(new Mounstro("Cráneo convocado",           (short)2500,(short)1200, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Seis,    true, "Un demonio con poderes oscuros para confundir al enemigo.",                                                                                 Estado.BARAJA));
        cartas.add(new Mounstro("Duende Místico",             (short)800, (short)2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Un elfo delicado que carece de ataque pero que tiene una defensa fantástica.",                                                              Estado.BARAJA));
        cartas.add(new Mounstro("Guerrero Lagarto Antiguo",   (short)1400,(short)1100, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Antes del amanecer de la humanidad, este guerrero lagarto gobernaba.",                                                                      Estado.BARAJA));
        cartas.add(new Mounstro("insecto básico",             (short)500, (short)700,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Dos,     true, "Generalmente se le encuentra viajando en enjambres.",                                                                                      Estado.BARAJA));
        cartas.add(new Mounstro("Madoor de Aqua",             (short)1200,(short)2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Un mago de las aguas que conjura un muro líquido.",                                                                                        Estado.BARAJA));
        cartas.add(new Mounstro("Nemuriko",                   (short)800, (short)700,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Tres,    true, "Una criatura de apariencia infantil que controla a un demonio del sueño.",                                                                  Estado.BARAJA));
        cartas.add(new Mounstro("Pavo Real",                  (short)1700,(short)1500, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cinco,   true, "Un gran pavo real que lanza plumas en un ataque letal.",                                                                                   Estado.BARAJA));
        cartas.add(new Mounstro("Tortuga Isla",               (short)1100,(short)2000, TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Cuatro,  true, "Una tortuga enorme que es a menudo confundida con una isla.",                                                                              Estado.BARAJA));
        cartas.add(new Mounstro("Soldado cientifico",         (short)800, (short)800,  TipoCarta.MOUNSTRUO, Mounstro.Estrellas.Dos,     true, "Soldado con tecnología avanzada.",                                                                                                         Estado.BARAJA));

        // ── Mágicas ──────────────────────────────────────────────────
        cartas.add(new Magia("Olla de la codicia",              TipoCarta.MAGICA, true, "Roba dos cartas.",                                                     Estado.BARAJA));
        cartas.add(new Magia("Monstruo renacido",               TipoCarta.MAGICA, true, "Selecciona 1 monstruo en cualquier Cementerio e invócalo.",            Estado.BARAJA));
        cartas.add(new Magia("Agujero Oscuro",                  TipoCarta.MAGICA, true, "Destruye todos los monstruos en el campo.",                            Estado.BARAJA));
        cartas.add(new Magia("Tifon del espacio Mistico",       TipoCarta.MAGICA, true, "Selecciona 1 mágica/trampa en el campo; destrúyela.",                  Estado.BARAJA));
        cartas.add(new Magia("Caridad elegante",                TipoCarta.MAGICA, true, "Roba tres cartas y manda 2 al cementerio.",                            Estado.BARAJA));
        cartas.add(new Magia("Fuerza de Resabastecimiento",     TipoCarta.MAGICA, true, "Roba 3 cartas y descarta 2 cartas de tu mano al Cementerio.",          Estado.BARAJA));
        cartas.add(new Magia("Dian Keto, el Señora de la Curación", TipoCarta.MAGICA, true, "Aumenta tus LP en 1000 puntos.",                                   Estado.BARAJA));
        cartas.add(new Magia("Tormenta Fuerte",                 TipoCarta.MAGICA, true, "Destruye todas las cartas Mágicas y Trampa en el campo.",              Estado.BARAJA));
        cartas.add(new Magia("Reproduccion de hechizo",         TipoCarta.MAGICA, true, "Manda al cementerio 2 Mágicas en tu mano.",                            Estado.BARAJA));
        cartas.add(new Magia("Intercambio",                     TipoCarta.MAGICA, true, "Intercambian sus cartas.",                                             Estado.BARAJA));
        cartas.add(new Magia("Hinotama",                        TipoCarta.MAGICA, true, "Inflige 500 puntos de daño a tu adversario.",                          Estado.BARAJA));

        // ── Trampas ──────────────────────────────────────────────────
        cartas.add(new Trampa("Fuerza de espejo",                    TipoCarta.TRAMPA, true, "Destruye todos los monstruos rivales en posición de Ataque.",    Estado.BARAJA));
        cartas.add(new Trampa("cilindros mágicos",                   TipoCarta.TRAMPA, true, "Inflige daño igual al ATK del monstruo atacante.",               Estado.BARAJA));
        cartas.add(new Trampa("Negar Ataque",                        TipoCarta.TRAMPA, true, "Niega el ataque y termina la Battle Phase.",                     Estado.BARAJA));
        cartas.add(new Trampa("llamada de los condenados",           TipoCarta.TRAMPA, true, "Invoca un monstruo del Cementerio en posición de Ataque.",       Estado.BARAJA));
        cartas.add(new Trampa("Círculo Atahechizos",                 TipoCarta.TRAMPA, true, "Paraliza un monstruo del oponente.",                             Estado.BARAJA));
        cartas.add(new Trampa("Disruptor de Trampa",                 TipoCarta.TRAMPA, true, "Niega la activación de una trampa rival.",                       Estado.BARAJA));
        cartas.add(new Trampa("Disruptor Mágico",                    TipoCarta.TRAMPA, true, "Niega una mágica rival; descarta 1 carta.",                      Estado.BARAJA));
        cartas.add(new Trampa("Artilugio de Evacuación Compulsiva",  TipoCarta.TRAMPA, true, "Devuelve un monstruo del campo a la mano.",                      Estado.BARAJA));
        cartas.add(new Trampa("Agujero Trampa Sin Fondo",            TipoCarta.TRAMPA, true, "Destruye monstruos con ATK ≥ 1500 al ser invocados.",            Estado.BARAJA));
        cartas.add(new Trampa("Juicio Solemne",                      TipoCarta.TRAMPA, true, "Paga mitad de LP; niega una invocación o activación.",           Estado.BARAJA));

        return cartas;
    }
}