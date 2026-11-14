package controleur;

import ihm.FrameJeu;
import java.awt.Dimension;
import metier.Plateau;
import metier.Sudoku;

public class Controleur
{
    private Sudoku   metier;
    private FrameJeu frameJeu;
    private boolean  enCoursDAnnulation;

    private Dimension       tailleEcran;

    int hJeu, lJeu;

    public Controleur()
	{	
        this.frameJeu   = new FrameJeu (this);

        tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        hJeu = (int) tailleEcran.getHeight();
		lJeu = (int) tailleEcran.getWidth ();

        this.frameJeu.setSize(tailleEcran);
    }

    public void initPlateau(int difficulte)
    {
        this.metier = new Sudoku(this, difficulte);
    }

    public void resetJeu()
    {
        this.initPlateau(this.getDifficulte());
    }

    public int getDifficulte()
    {
        return this.metier.getDifficulte();
    }

    public Plateau getPlateau()
    {
        return this.metier.getPlateau();
    }

    public int getNombre(int lig, int col, int lig2, int col2)
    {
        return this.metier.getNombre(lig, col, lig2, col2);
    }

    public int getNbOccurrences(int nbr)
    {
        return this.metier.getPlateau().getNbOccurrences(nbr);
    }

    public void setNbrCase(int lig, int col, int lig2, int col2, int nbr)
    {
        int ancien = this.getNombre(lig, col, lig2, col2);
        this.metier.setNbrCase(lig, col, lig2, col2, nbr);
        int nouveau = this.getNombre(lig, col, lig2, col2);

        if (!this.enCoursDAnnulation && nouveau != ancien)
        {
            this.metier.getPile().empiler("setNbrCase " + ancien + " [" + lig + "," + col + "] [" + lig2 + "," + col2 + "]");
        }
    }

    public void effacerChiffre(int lig, int col, int lig2, int col2)
    {
        int num = this.getNombre(lig, col, lig2, col2);
        this.metier.effacerChiffre(lig, col, lig2, col2);

        if (!this.enCoursDAnnulation && num != 0)
        {
            this.metier.getPile().empiler("effacerChiffre " + num + " [" + lig + "," + col + "] [" + lig2 + "," + col2 + "]");
        }
    }

    public void echangerNbr(int lig1, int col1, int lig2, int col2, int lig3, int col3, int lig4, int col4)
    {
        this.metier.echangerNbr(lig1, col1, lig2, col2, lig3, col3, lig4, col4);

        if (!this.enCoursDAnnulation)
        {
            this.metier.getPile().empiler("echangerNbr [" + lig1 + "," + col1 + "] [" + lig2 + "," + col2 + "] [" + lig3 + "," + col3 + "] [" + lig4 + "," + col4 + "]");
        }
    }

    public void depilerCase()
    {
        this.enCoursDAnnulation = true;
        this.metier.getPile().depiler();
        this.enCoursDAnnulation = false;
    }

    public boolean peutAnnuler()
    {
        return !this.metier.getPile().estVide();
    }

    public void donnerIndice()
    {
        this.metier.donnerIndice();
    }

    public boolean estGagne()
    {
        return this.metier.estGagne();
    }

    public boolean estComplet(int nombre)
    {
        return this.metier.getPlateau().estComplet(nombre);
    }

    public boolean estCaseModifiable(int lig, int col, int lig2, int col2)
    {
        return this.metier.getPlateau().getGrille(lig, col).getCase(lig2, col2).estModifiable();
    }

    public boolean estCaseValide(int lig, int col, int lig2, int col2)
    {
        return this.metier.estCaseValide(lig, col, lig2, col2);
    }

    public static void main(String[] args)
    {
        new Controleur();
    }
}