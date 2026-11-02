package ihm;

import java.util.Scanner;
import controleur.Controleur;

public class IhmTerminal 
{
    private Controleur ctrl;
    private Scanner scanner;

    public IhmTerminal(Controleur ctrl) 
    {
        this.ctrl = ctrl;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche la grille 9x9 avec les numéros de lignes et colonnes (1..9) pour faciliter la saisie.
     */
    public void afficherPlateau() 
    {
        System.out.println("\n    1 2 3   4 5 6   7 8 9 (colonnes)");
        System.out.println("  +-------+-------+-------+");

        for (int ligGlob = 0; ligGlob < 9; ligGlob++) 
        {
            System.out.print((ligGlob + 1) + " | "); // Numéro de ligne de 1 à 9

            for (int colGlob = 0; colGlob < 9; colGlob++) 
            {
                int ligBloc = ligGlob / 3;
                int ligCase = ligGlob % 3;
                int colBloc = colGlob / 3;
                int colCase = colGlob % 3;

                int valeur = this.ctrl.getNombre(ligBloc, colBloc, ligCase, colCase);
                System.out.print((valeur == 0 ? "." : valeur) + " ");

                if ((colGlob + 1) % 3 == 0) 
                {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((ligGlob + 1) % 3 == 0) 
            {
                System.out.println("  +-------+-------+-------+");
            }
        }
        System.out.println();
    }

    /**
     * Boucle principale de la partie dans la console.
     */
    public void lancerPartie() 
    {
        System.out.println("==========================================");
        System.out.println("         PARTIE DE SUDOKU CONSOLE         ");
        System.out.println("==========================================");
        System.out.println("Commandes :");
        System.out.println("  - Poser un chiffre : ligne colonne valeur (ex: 1 3 5)");
        System.out.println("  - Effacer une case : ligne colonne 0     (ex: 1 3 0)");
        System.out.println("  - Quitter          : q");

        while (!this.ctrl.getPlateau().estResolu()) 
        {
            this.afficherPlateau();
            System.out.print("Votre coup (ligne col val) ou 'q' : ");
            String ligneSaisie = scanner.nextLine().trim();

            if (ligneSaisie.equalsIgnoreCase("q")) 
            {
                System.out.println("Partie interrompue. Au revoir !");
                return;
            }

            String[] parties = ligneSaisie.split("\\s+");
            if (parties.length != 3) 
            {
                System.out.println("⚠️ Format invalide. Entrez 3 chiffres séparés par un espace (ex: 2 4 7).");
                continue;
            }

            try 
            {
                int lig = Integer.parseInt(parties[0]);
                int col = Integer.parseInt(parties[1]);
                int val = Integer.parseInt(parties[2]);

                if (lig < 1 || lig > 9 || col < 1 || col > 9 || val < 0 || val > 9) 
                {
                    System.out.println("⚠️ Coordonnées (1..9) ou valeur (0..9) hors limites !");
                    continue;
                }

                // Conversion 1..9 vers les coordonnées de vos classes (indices 0..2)
                int ligGlob = lig - 1;
                int colGlob = col - 1;

                int ligBloc = ligGlob / 3;
                int ligCase = ligGlob % 3;
                int colBloc = colGlob / 3;
                int colCase = colGlob % 3;

                // Action : effacement ou pose
                if (val == 0) 
                {
                    this.ctrl.effacerNombre(ligBloc, colBloc, ligCase, colCase);
                    System.out.println("-> Case effacée.");
                } 
                else 
                {
                    int avant = this.ctrl.getNombre(ligBloc, colBloc, ligCase, colCase);
                    this.ctrl.setNombre(ligBloc, colBloc, ligCase, colCase, val);
                    int apres = this.ctrl.getNombre(ligBloc, colBloc, ligCase, colCase);

                    if (avant == apres && apres != val) 
                    {
                        System.out.println("❌ Coup refusé : doublon sur la ligne/colonne/bloc ou case non modifiable !");
                    } 
                    else 
                    {
                        System.out.println("✅ Coup enregistré !");
                    }
                }

            } 
            catch (NumberFormatException e) 
            {
                System.out.println("⚠️ Saisie invalide : veuillez entrer uniquement des nombres entiers.");
            }
        }

        this.afficherPlateau();
        System.out.println("🎉 FÉLICITATIONS ! Vous avez résolu la grille !");
    }

    public static void main(String[] args) 
    {
        Controleur controleur = new Controleur();
        IhmTerminal jeu = new IhmTerminal(controleur);
        jeu.lancerPartie();
    }
}