package ihm;

import controleur.Controleur;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class PanelJeu extends JPanel 
{
    private Controleur ctrl;

    // Tableau 4D pour retrouver le panneau et le label de chaque case
    private JPanel[][][][] tabPnlCases;
    private JLabel[][][][] tabLblCases;

    // Coordonnées de la case actuellement sélectionnée (-1 si aucune)
    private int ligSel  = -1;
    private int colSel  = -1;
    private int lig2Sel = -1;
    private int col2Sel = -1;

    // Couleurs
    private static final Color COULEUR_SELECTION = new Color(185, 215, 250);
    private static final Color COULEUR_SURVOL    = new Color(230, 240, 255);
    private static final Color COULEUR_DEFAUT    = Color.WHITE;

    public PanelJeu(Controleur ctrl) 
    {
        this.ctrl = ctrl;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        this.tabPnlCases = new JPanel[3][3][3][3];
        this.tabLblCases = new JLabel[3][3][3][3];

        // Construction du plateau graphique
        this.dessinerPlateau();

        // Clic dans le vide pour désélectionner
        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                ligSel = colSel = lig2Sel = col2Sel = -1;
                majPlateau();
            }
        });
    }

    public void dessinerPlateau() 
    {
        this.removeAll();

        // Panneau conteneur de la grille 3x3 de blocs
        JPanel pnlGrilleGlobale = new JPanel(new GridLayout(3, 3, 3, 3));
        pnlGrilleGlobale.setBackground(Color.BLACK); // Les séparateurs épais entre les 9 gros blocs

        Border bordureCase = BorderFactory.createLineBorder(new Color(200, 200, 200), 1);

        for (int lig = 0; lig < 3; lig++) 
        {
            for (int col = 0; col < 3; col++) 
            {
                // Sous-grille 3x3 pour chaque bloc
                JPanel pnlBloc = new JPanel(new GridLayout(3, 3, 1, 1));
                pnlBloc.setBackground(Color.GRAY); // Bordure fine entre les cases du même bloc

                for (int lig2 = 0; lig2 < 3; lig2++) 
                {
                    for (int col2 = 0; col2 < 3; col2++) 
                    {
                        // 1. Panneau pour la cellule
                        JPanel pnlCellule = new JPanel(new BorderLayout());
                        pnlCellule.setBackground(COULEUR_DEFAUT);
                        pnlCellule.setBorder(bordureCase);

                        // 2. Label affichant le chiffre
                        JLabel lblChiffre = new JLabel("", SwingConstants.CENTER);
                        lblChiffre.setFont(new Font("Sans-Serif", Font.BOLD, 22));
                        pnlCellule.add(lblChiffre, BorderLayout.CENTER);

                        // 3. Stockage en mémoire
                        this.tabPnlCases[lig][col][lig2][col2] = pnlCellule;
                        this.tabLblCases[lig][col][lig2][col2] = lblChiffre;

                        // 4. Écouteur de souris (clic + survol comme dans PanelJeu_2)
                        pnlCellule.addMouseListener(new GereSouris(lig, col, lig2, col2));

                        pnlBloc.add(pnlCellule);
                    }
                }
                pnlGrilleGlobale.add(pnlBloc);
            }
        }

        this.add(pnlGrilleGlobale, BorderLayout.CENTER);

        // Affiche les chiffres initiaux
        this.majPlateau();
    }

    /**
     * Met à jour l'affichage des valeurs et des couleurs (sélection, survol)
     */
    public void majPlateau() 
    {
        for (int lig = 0; lig < 3; lig++) 
        {
            for (int col = 0; col < 3; col++) 
            {
                for (int lig2 = 0; lig2 < 3; lig2++) 
                {
                    for (int col2 = 0; col2 < 3; col2++) 
                    {
                        int nbr = this.ctrl.getNombre(lig, col, lig2, col2);
                        JLabel lbl = this.tabLblCases[lig][col][lig2][col2];
                        JPanel pnl = this.tabPnlCases[lig][col][lig2][col2];

                        // Affichage du chiffre
                        if (nbr != 0) 
                        {
                            lbl.setText(String.valueOf(nbr));
                            lbl.setForeground(new Color(20, 40, 120));
                        } 
                        else 
                        {
                            lbl.setText("");
                        }

                        // Mise en évidence si la case est sélectionnée
                        if (lig == this.ligSel && col == this.colSel && 
                            lig2 == this.lig2Sel && col2 == this.col2Sel) 
                        {
                            pnl.setBackground(COULEUR_SELECTION);
                        } 
                        else 
                        {
                            pnl.setBackground(COULEUR_DEFAUT);
                        }
                    }
                }
            }
        }
        this.repaint();
    }

    // Classe interne pour gérer les clics et le survol comme dans PanelJeu_2
    private class GereSouris extends MouseAdapter 
    {
        private int l, c, l2, c2;

        public GereSouris(int l, int c, int l2, int c2) 
        {
            this.l  = l;
            this.c  = c;
            this.l2 = l2;
            this.c2 = c2;
        }

        @Override
        public void mouseClicked(MouseEvent e) 
        {
            // Si on clique sur la case déjà sélectionnée, on désélectionne
            if (ligSel == l && colSel == c && lig2Sel == l2 && col2Sel == c2) 
            {
                ligSel = colSel = lig2Sel = col2Sel = -1;
            } 
            else 
            {
                ligSel  = l;
                colSel  = c;
                lig2Sel = l2;
                col2Sel = c2;
            }
            majPlateau();
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            // Coloration au survol seulement si ce n'est pas la case active
            if (!(ligSel == l && colSel == c && lig2Sel == l2 && col2Sel == c2)) 
            {
                tabPnlCases[l][c][l2][c2].setBackground(COULEUR_SURVOL);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            if (!(ligSel == l && colSel == c && lig2Sel == l2 && col2Sel == c2)) 
            {
                tabPnlCases[l][c][l2][c2].setBackground(COULEUR_DEFAUT);
            }
        }
    }
}