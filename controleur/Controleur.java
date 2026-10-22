package controleur;

import ihm.FrameJeu;

import metier.Case;
import metier.Grille;
import metier.Plateau;
import metier.Sudoku;

public class Controleur
{

    private Sudoku   metier ;
    private FrameJeu frameJeu ;

    public Controleur()
    {
        this.initPlateau();
        this.frameJeu   = new FrameJeu (this);
    }

    public void initPlateau()
    {
        this.metier = new Sudoku () ;
    }

    public Plateau getPlateau () { return this.metier.getPlateau() ;}

    public int  getNombre     ( int lig , int col , int lig2 , int col2          ) { return this.metier.getNombre(lig , col , lig2 ,col2);}
    public void setNombre     ( int lig , int col , int lig2 , int col2 ,int nbr ) { this.metier.setNombre    (lig, col, lig2, col2, nbr);}
    public void effacerNombre ( int lig , int col , int lig2 , int col2          ) { this.metier.effacerNombre(lig, col, lig2, col2     );}
   public static void main (String[] args) { new Controleur(); }
}