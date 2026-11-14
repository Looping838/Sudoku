package metier;

import java.util.ArrayList;
import java.util.List;

public class Plateau
{
    private Grille[][] plateau;
    private int[][]    solution;

    public Plateau()
    {
        this.plateau  = new Grille[3][3];
        this.solution = new int[9][9];
        this.initPlateau();
    }

    public void initPlateau()
    {
        for (int ligne = 0; ligne < this.plateau.length; ligne++)
        {
            for (int colonne = 0; colonne < this.plateau[ligne].length; colonne++)
            {
                this.plateau[ligne][colonne] = new Grille();
            }
        }
    }

    public Grille[][] getPlateau()
    {
        return this.plateau;
    }

    public Grille getGrille(int lig, int col)
    {
        return this.plateau[lig][col];
    }

    public int getSolution(int ligGlob, int colGlob)
    {
        return this.solution[ligGlob][colGlob];
    }

    public int getNbOccurrences(int nombre)
    {
        int cpt = 0;
        for (int lig = 0; lig < 3; lig++)
        {
            for (int col = 0; col < 3; col++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        if (this.plateau[lig][col].getCase(l, c).getNombre() == nombre)
                        {
                            cpt++;
                        }
                    }
                }
            }
        }
        return cpt;
    }

    public void setNombre(int lig, int col, int lig2, int col2, int nbr)
    {
        if (nbr < 0 || nbr > 9)
        {
            return;
        }

        this.plateau[lig][col].setCase(lig2, col2, nbr);
    }

    public boolean estPresent(int lig, int col, int lig2, int col2, int nombre)
    {
        int ligneGlobale = lig * 3 + lig2;
        int colGlobale   = col * 3 + col2;

        for (int ligne = 0; ligne < this.plateau.length; ligne++)
        {
            for (int colonne = 0; colonne < this.plateau[ligne].length; colonne++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        if (ligne * 3 + l == ligneGlobale || colonne * 3 + c == colGlobale)
                        {
                            if (this.plateau[ligne][colonne].getCase(l, c).getNombre() == nombre)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean estResolu()
    {
        for (int lig = 0; lig < 3; lig++)
        {
            for (int col = 0; col < 3; col++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        if (!this.estCaseValide(lig, col, l, c))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private int[] melangerChiffres()
    {
        int[] chiffres = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = chiffres.length - 1; i > 0; i--)
        {
            int j   = (int) (Math.random() * (i + 1));
            int tmp = chiffres[i];
            chiffres[i] = chiffres[j];
            chiffres[j] = tmp;
        }

        return chiffres;
    }

    private boolean remplirGrille(int indexCase)
    {
        if (indexCase == 81)
        {
            return true;
        }

        int ligGlob = indexCase / 9;
        int colGlob = indexCase % 9;

        int ligBloc = ligGlob / 3;
        int colBloc = colGlob / 3;
        int ligCase = ligGlob % 3;
        int colCase = colGlob % 3;

        int[] chiffres = this.melangerChiffres();

        for (int i = 0; i < chiffres.length; i++)
        {
            int nbr = chiffres[i];

            if (!this.estPresent(ligBloc, colBloc, ligCase, colCase, nbr) &&
                !this.plateau[ligBloc][colBloc].estPresent(nbr))
            {
                this.plateau[ligBloc][colBloc].getCase(ligCase, colCase).setNombre(nbr);

                if (this.remplirGrille(indexCase + 1))
                {
                    return true;
                }

                this.plateau[ligBloc][colBloc].getCase(ligCase, colCase).setNombre(0);
            }
        }

        return false;
    }

    public void setPlateauJeu(int difficulte)
    {
        this.initPlateau();
        this.remplirGrille(0);

        for (int ligGlob = 0; ligGlob < 9; ligGlob++)
        {
            for (int colGlob = 0; colGlob < 9; colGlob++)
            {
                this.solution[ligGlob][colGlob] = this.plateau[ligGlob / 3][colGlob / 3]
                                                       .getCase(ligGlob % 3, colGlob % 3)
                                                       .getNombre();
            }
        }

        int nbCasesASupprimer = 40;
        if (difficulte == 1)
        {
            nbCasesASupprimer = 50;
        }
        else if (difficulte == 2)
        {
            nbCasesASupprimer = 58;
        }

        int supprimees = 0;
        while (supprimees < nbCasesASupprimer)
        {
            int ligGlob = (int) (Math.random() * 9);
            int colGlob = (int) (Math.random() * 9);

            int ligBloc = ligGlob / 3;
            int colBloc = colGlob / 3;
            int ligCase = ligGlob % 3;
            int colCase = colGlob % 3;

            if (!this.plateau[ligBloc][colBloc].getCase(ligCase, colCase).estVide())
            {
                this.plateau[ligBloc][colBloc].getCase(ligCase, colCase).setNombre(0);
                supprimees++;
            }
        }

        for (int lig = 0; lig < 3; lig++)
        {
            for (int col = 0; col < 3; col++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        if (!this.plateau[lig][col].getCase(l, c).estVide())
                        {
                            this.plateau[lig][col].getCase(l, c).setEstModifiable(false);
                        }
                    }
                }
            }
        }
    }

    public void donnerIndice()
    {
        List<int[]> lstIndices = new ArrayList<>();
        
        for (int lig1 = 0; lig1 < 3; lig1++)
        {
            for (int col1 = 0; col1 < 3; col1++)
            {
                for (int lig2 = 0; lig2 < 3; lig2++)
                {
                    for (int col2 = 0; col2 < 3; col2++)
                    {
                        if (this.plateau[lig1][col1].getCase(lig2, col2).estModifiable() && this.plateau[lig1][col1].getCase(lig2, col2).getNombre() != this.solution[lig1 * 3 + lig2][col1 * 3 + col2])
                        {
                            lstIndices.add(new int[]{lig1, col1, lig2, col2});
                        }
                    }
                }
            }
        }

        if (!lstIndices.isEmpty())
        {
            int[] indice = lstIndices.get((int) (Math.random() * lstIndices.size()));
            int lig1 = indice[0];
            int col1 = indice[1];
            int lig2 = indice[2];
            int col2 = indice[3];

            this.plateau[lig1][col1].getCase(lig2, col2).setNombre(this.solution[lig1 * 3 + lig2][col1 * 3 + col2]);
            this.plateau[lig1][col1].getCase(lig2, col2).setEstModifiable(false);
        }
    }

    public boolean estCaseValide(int ligBloc, int colBloc, int ligCase, int colCase)
    {
        return this.plateau[ligBloc][colBloc].getCase(ligCase, colCase).getNombre() == this.solution[ligBloc * 3 + ligCase][colBloc * 3 + colCase];
    }

    public boolean estComplet(int nombre)
    {
        return this.getNbOccurrences(nombre) >= 9;
    }
}