package ihm;

import controleur.Controleur;

public class IhmTerminalTeste 
{
    private Controleur ctrl;

    public IhmTerminalTeste(Controleur ctrl) 
    {
        this.ctrl = ctrl;
    }

    /**
     * Affiche la grille complète 9x9 avec les délimitations des sous-grilles 3x3.
     */
    public void afficherPlateau() 
    {
        System.out.println("+-------+-------+-------+");
        for (int ligGlob = 0; ligGlob < 9; ligGlob++) 
        {
            System.out.print("| ");
            for (int colGlob = 0; colGlob < 9; colGlob++) 
            {
                // Conversion des coordonnées 0..8 en coordonnées [bloc][case]
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
                System.out.println("+-------+-------+-------+");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) 
    {
        System.out.println("==========================================");
        System.out.println("       BATTERIE DE TESTS TERMINAL         ");
        System.out.println("==========================================\n");

        Controleur controleur = new Controleur();
        IhmTerminalTeste ihm = new IhmTerminalTeste(controleur);

        // --- TEST 1 : Initialisation ---
        System.out.println("--- TEST 1 : Grille après initialisation ---");
        ihm.afficherPlateau();

        // --- TEST 2 : Placement d'un nombre valide ---
        System.out.println("--- TEST 2 : Placement du chiffre 7 en [0,0] (bloc [0,0], case [0,0]) ---");
        controleur.setNombre(0, 0, 0, 0, 7);
        ihm.afficherPlateau();

        // --- TEST 3 : Conflit / Doublon sur la même ligne globale ---
        System.out.println("--- TEST 3 : Doublon (tenter de poser 7 sur la même ligne globale en bloc [0,1], case [0,1]) ---");
        controleur.setNombre(0, 1, 0, 1, 7);
        ihm.afficherPlateau();
        System.out.println("-> Résultat attendu : le 7 n'a pas dû être placé car déjà présent sur la ligne.\n");

        // --- TEST 4 : Suppression / Effacement ---
        System.out.println("--- TEST 4 : Effacer la valeur en [0,0] (bloc [0,0], case [0,0]) ---");
        controleur.effacerNombre(0, 0, 0, 0);
        ihm.afficherPlateau();

        // --- TEST 5 : Vérification de victoire ---
        System.out.println("--- TEST 5 : Test de résolution / Victoire ---");
        boolean resolu = controleur.getPlateau().estResolu();
        System.out.println("Le plateau est-il entièrement résolu/gagné ? " + resolu);
    }
}