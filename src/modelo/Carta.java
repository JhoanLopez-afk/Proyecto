
package modelo;

import modelo.enums.Estado;
import modelo.enums.TipoCarta;


public abstract class Carta {

    private String    nombre;
    private TipoCarta tipo;
    private boolean   visible;
    private String    efecto;
    private Estado    estado;

    public Carta(String nombre, TipoCarta tipo, boolean visible, String efecto, Estado estado) {
        this.nombre  = nombre;
        this.tipo    = tipo;
        this.visible = visible;
        this.efecto  = efecto;
        this.estado  = estado;
    }

    // ── Getters / Setters ────────────────────────────────────────────

    public String getNombre()           
     { 
        return nombre; 
    }
    public void   setNombre(String n)   
    { 
        this.nombre = n; 
    }

    public TipoCarta getTipo()           
    { 
        return tipo; 
    }
    public void      setTipo(TipoCarta t)
    { 
        this.tipo = t; 
    }

    public boolean isVisible()           
    { 
        return visible; 
    }
    public void    setVisible(boolean v) 
    { 
        this.visible = v; 
    }

    public String getEfecto()            
    { 
        return efecto; 
    }
    public void   setEfecto(String e)    
    { 
        this.efecto = e; 
    }

    public Estado getEstado()            
    { 
        return estado; 
    }
    public void   setEstado(Estado e)    
    { 
        this.estado = e; 
    }

   
    public abstract void usar(Jugador jugador, Jugador oponente);
}