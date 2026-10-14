package metier;

public class Case 
{
    private int x;
    private int y ;
    private int nombre ; 

    public Case ( int x , int y)
    {
        this.x      = x ;
        this.y      = y ;
        this.nombre = 0 ;
    }

    public int  getX       () { return this.x ;}
    public int  getY       () { return this.y ;}
    public int  getNombre  () { return this.nombre ;}

    public void setX         (int     x)      { this.x = x ;}
    public void setY         (int     y)      { this.y = y ;}
    public void setNombre    (int     nombre) { this.nombre = nombre ;}

    public boolean estVide   () { return this.nombre == 0   ;}
}