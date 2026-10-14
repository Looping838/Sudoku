package metier;

public class Grille 
{
    private Case[][] grille ;

    public Grille ()
    {
        this.grille = new Case[3][3] ;
        this.initGrille(); 
    }

    public void     initGrille ()
    {
        for ( int ligne = 0 ; ligne < this.grille.length ; ligne ++ )
        {
            for ( int colonne = 0 ; colonne < this.grille[ligne].length ; colonne ++ )
            {
                this.grille[ligne][colonne] = new Case( ligne , colonne ) ;
            }
        }
    }

    public Case[][] getGrille ()                    { return this.grille           ;}
    public Case     getCase   ( int lig , int col ) { return this.grille[lig][col] ;}

    public void     setCase   ( int lig , int col , int nbr ) 
    { 
        if ( !this.estPresent(nbr) )
            this.grille[lig][col].setNombre(nbr);
    }

    public boolean  estPresent (int nombre ) 
    {
        for ( int ligne = 0 ; ligne < this.grille.length ; ligne ++ )
        {
            for ( int colonne = 0 ; colonne < this.grille[ligne].length ; colonne ++ )
            {
                if ( this.grille[ligne][colonne].getNombre() == nombre )
                    return true ;
            }
        }

        return false ;
    }
    
}
