package metier;

public class Sudoku 
{
    private Plateau plateau ;

    public Sudoku () 
    {
        this.plateau = new Plateau() ;
    }

    public int getNombre ( int lig , int col , int lig2 , int col2 )
    {
        return this.plateau.getGrille( lig, col ).getCase(lig2, col2).getNombre() ;
    }

    public void setNombre  ( int lig , int col , int lig2 , int col2 ,int nbr ) 
    { 
        this.plateau.setNombre(lig, col, lig2, col2, nbr); 
    }
    public void effacerNombre  ( int lig , int col , int lig2 , int col2 ) 
    {
        this.plateau.getGrille( lig, col ).getCase(lig2, col2).setNombre(0) ;
    }

    public boolean estGagne () { return this.plateau.estResolu() ; }
    
}
