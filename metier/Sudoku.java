package metier;

public class Sudoku 
{
    private Plateau plateau ;
    private int     difficulte ;
    private Pile    pile ;

    public Sudoku () 
    {
        this.plateau = new Plateau() ;
        this.setPlateauJeu();
        this.difficulte = 0 ;
    }

    public Plateau getPlateau () { return this.plateau ;}
    public Pile    getPile    () { return this.pile    ;}

    public int getNombre ( int lig , int col , int lig2 , int col2 )
    {
        return this.plateau.getGrille( lig, col ).getCase(lig2, col2).getNombre() ;
    }

    public void setNombre  ( int lig , int col , int lig2 , int col2 ,int nbr ) 
    { 
        if ( this.difficulte == 2 )
            this.plateau.getGrille( lig, col ).getCase(lig2, col2).setNombre(0) ;
        else 
            this.plateau.setNombre(lig, col, lig2, col2, nbr); 
    }
    public void effacerNombre  ( int lig , int col , int lig2 , int col2 ) 
    {
        this.plateau.getGrille( lig, col ).getCase(lig2, col2).setNombre(0) ;
    }

    public void    echangerNbr(int lig1, int col1, int lig2, int col2 , int lig3, int col3, int lig4, int col4) 
    {
        
    }

    public boolean estGagne () { return this.plateau.estResolu() ; }

    public void    setPlateauJeu ()
    {
        this.plateau.setPlateauJeu( difficulte ) ;
    }
    
}
