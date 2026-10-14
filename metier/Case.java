package metier;

public class Case 
{
    private int x;
    private int y ;
    private int nombre ; 
    private boolean estModifiable ;

    public Case ( int x , int y)
    {
        this.x      = x ;
        this.y      = y ;
        this.nombre = 0 ;
        this.estModifiable = true ;
    }

    public int  getX       () { return this.x ;}
    public int  getY       () { return this.y ;}
    public int  getNombre  () { return this.nombre ;}

    public void setX         (int     x)      { this.x = x ;}
    public void setY         (int     y)      { this.y = y ;}
    public void setNombre    (int     nombre) 
    { 
        if ( estModifiable )
            this.nombre = nombre ;
    }
    public void setEstModifiable ( boolean bool) { this.estModifiable = bool ;}

    public boolean estVide       () { return this.nombre == 0   ;}
    public boolean estModifiable () { return this.estModifiable ;}
}