package metier;

public class Plateau 
{
    private Grille[][] plateau ;

    public Plateau()
    {
        this.plateau = new Grille[3][3] ;
        this.initPlateau();
    }

    public void         initPlateau ()
    {
        for ( int ligne = 0 ; ligne < this.plateau.length ; ligne ++ )
        {
            for ( int colonne = 0 ; colonne < this.plateau[ligne].length ; colonne ++ )
            {
                this.plateau[ligne][colonne] = new Grille() ;
                this.plateau[ligne][colonne].initGrille();
            }
        }
    }

    public Grille[][]   getPlateau ()                    { return this.plateau           ;}
    public Grille       getGrille  ( int lig , int col ) { return this.plateau[lig][col] ;}

    public void         setNombre  ( int lig , int col , int lig2 , int col2 ,int nbr ) 
    { 
        if (nbr < 0 || nbr > 9) return;

        if ( !this.estPresent( lig , col , lig2 , col2 , nbr) )
            this.plateau[lig][col].setCase(lig2 , col2 , nbr );
    }

    public boolean      estPresent (int lig, int col, int lig2, int col2, int nombre)
    {
        int ligneGlobale  = lig  * 3 + lig2;   // 0..8
        int colGlobale    = col  * 3 + col2;   // 0..8

        for (int ligne = 0; ligne < this.plateau.length; ligne++)
        {
            for (int colonne = 0; colonne < this.plateau[ligne].length; colonne++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        // même ligne globale OU même colonne globale ?
                        if (ligne * 3 + l == ligneGlobale || colonne * 3 + c == colGlobale)
                        {
                            if (this.plateau[ligne][colonne].getCase(l, c).getNombre() == nombre)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean      estResolu ()
    {
        for (int lig = 0; lig < 3; lig++)
            for (int col = 0; col < 3; col++)
                for (int l = 0; l < 3; l++)
                    for (int c = 0; c < 3; c++)
                        if (this.plateau[lig][col].getCase(l, c).estVide())
                            return false;
        return true;
    }

}
