package ihm;

import controleur.Controleur;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;

public class PanelJeu extends JPanel implements ActionListener
{   
    private JPanel[][][][]      tabPnlCases;

    private Controleur          ctrl;
    private FrameJeu            frame;
    private int                 indice;

    private Dimension           tailleEcran;
    private int                 taillePlateau;
    private int                 tailleBoutonPave;
    private int                 tailleBoutonOutil;

    private int                 ligCaseSelect;   
    private int                 colCaseSelect;   
    private int                 ligBlocSelect;   
    private int                 colBlocSelect;   

    private JPanel              pnlHaut;
    private JPanel              pnlCentre;
    private JPanel              pnlBas;
    
    private JPanel              pnlBandeau;
    private JPanel              pnlPlateau;
    private JPanel              pnlNbres;
    private JPanel              pnlBoutons;
    private JPanel              pnlOutils;
    private JPanel              pnlColonneDroite;

    private JSeparator          separateurHaut;
    private JSeparator          separateurBas;

    private JLabel              lblValidation;

    private JButton             btnValide;
    private JButton             btnReset;
    private JButton             btnRestart;
    private JButton             btnAccueil;
    private JButton             btnAnnuler;
    private JButton             btnGomme;
    private JButton             btnIndice;
    private JButton             btnBrouillon;

    private ButtonGroup         grpBtn;
    private JToggleButton       btnActif;

    private boolean             modeValidation  = false;
    private boolean             modeBrouillon   = false;

    private int                 nbIndicesRestants;
    private int                 nbValidationsRestantes;
    private int                 nbValidationsMax;

    public PanelJeu(Controleur ctrl, FrameJeu frame, int indice)
    {
        UIManager.put("ToggleButton.select", ConstantesIHM.COULEUR_SELECTION);
        UIManager.put("ToggleButton.disabledText", Color.BLACK);
        
        this.setLayout(new BorderLayout());
        this.setBackground(ConstantesIHM.COULEUR_FOND);

        this.ctrl           = ctrl;
        this.frame          = frame;
        this.indice         = indice;
        
        this.creerComposants();
        this.positionnerComposants();
        this.activerComposants();
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JToggleButton)
        {
            JToggleButton btnClique = (JToggleButton) e.getSource();

            if (this.btnActif == btnClique) 
            {
                this.grpBtn.clearSelection();
                this.btnActif = null;
            }
            else 
            {
                this.btnActif = btnClique;
            }

            if (this.grpBtn != null) 
            {
                java.util.Enumeration<AbstractButton> elements = this.grpBtn.getElements();
                while (elements.hasMoreElements()) 
                {
                    AbstractButton btn = elements.nextElement();
                    if (btn.getBackground() != ConstantesIHM.COULEUR_PLACE && !btn.isSelected()) 
                    {
                        btn.setBackground(Color.WHITE);
                    }
                }
            }

            this.ligBlocSelect = -1; this.colBlocSelect = -1;
            this.ligCaseSelect = -1; this.colCaseSelect = -1;
            this.majPlateau();
        }

        if (e.getSource() == this.btnGomme)
        {
           if (this.ligBlocSelect != -1 && this.colBlocSelect != -1 && this.ligCaseSelect != -1 && this.colCaseSelect != -1)
            {
                JLabel lblContenu = (JLabel) tabPnlCases[this.ligBlocSelect][this.colBlocSelect][this.ligCaseSelect][this.colCaseSelect].getComponent(0);
                String chiffreEfface = lblContenu.getText();
                    
                // On vérifie que le contrôleur autorise l'effacement
                // if (this.ctrl.getCasePlateau(this.ligBlocSelect, this.colBlocSelect, this.ligCaseSelect, this.colCaseSelect).estModifiable())
                {
                    this.ctrl.effacerNombre(this.ligBlocSelect, this.colBlocSelect, this.ligCaseSelect, this.colCaseSelect);
                    
                    lblContenu.setText(""); 
                    lblContenu.setForeground(null);

                    if (!chiffreEfface.equals("") && !chiffreEfface.equals("0") && this.grpBtn != null) 
                    {
                        java.util.Enumeration<AbstractButton> elements = this.grpBtn.getElements();
                        while (elements.hasMoreElements()) 
                        {
                            AbstractButton btn = elements.nextElement();
                            if (btn.getText().equals(chiffreEfface)) 
                            {
                                btn.setBackground(Color.WHITE);
                                break; 
                            }
                        }
                    }
                }

                this.modeValidation = false; 
                this.ligBlocSelect = -1; this.colBlocSelect = -1;
                this.ligCaseSelect = -1; this.colCaseSelect = -1;
                this.majPlateau();
            }
        }

        if (e.getSource() == this.btnIndice)
        {
            if (this.nbIndicesRestants > 0)
            {
                this.nbIndicesRestants--;
                this.btnIndice.setText("  " + this.nbIndicesRestants);
                if (this.nbIndicesRestants == 0)
                {
                    this.btnIndice.setBackground(Color.WHITE);
                    this.btnIndice.setEnabled(false);
                }

                // this.ctrl.donnerIndice(); // À décommenter quand implémenté par ton collègue
                this.majPlateau();
                // this.majBoutons();
            }
        }

        if (e.getSource() == this.btnBrouillon)
        {
            this.modeBrouillon = !this.modeBrouillon;
            this.ligBlocSelect = -1; this.colBlocSelect = -1;
            this.ligCaseSelect = -1; this.colCaseSelect = -1;

            if(this.modeBrouillon)
                this.btnBrouillon.setBackground(ConstantesIHM.COULEUR_BROUILLON_ACTIVE);
            else 
                this.btnBrouillon.setBackground(ConstantesIHM.COULEUR_SURVOL);

            this.majPlateau();
        }

        if (e.getSource() == this.btnValide)
        {
            this.modeValidation = true; 
            this.ligBlocSelect = -1; this.colBlocSelect = -1;
            this.ligCaseSelect = -1; this.colCaseSelect = -1;
            this.majPlateau();

            if (!this.ctrl.estGagne())
            {
                this.nbValidationsRestantes--;
                this.majAffichageValidation();

                if (this.nbValidationsRestantes <= 0)
                {
                    this.frame.afficherPanelResultat(false);
                    this.frame.setPnl(this.frame.getPnl(this.indice + 1));
                    // this.ctrl.resetJeu(); // À décommenter
                    return;
                }
            }
            else
            {
                this.btnGomme.setEnabled(false);
                if (this.grpBtn != null) {
                    java.util.Enumeration<AbstractButton> elements = this.grpBtn.getElements();
                    while (elements.hasMoreElements()) {
                        elements.nextElement().setEnabled(false);
                    }
                }
                this.frame.afficherPanelResultat(true);
            }
        }

        if (e.getSource() == this.btnReset)   { this.reset(); }
        if (e.getSource() == this.btnRestart) { this.restart(); }
        if (e.getSource() == this.btnAnnuler)
        {
            // this.ctrl.depilerCase(); // À décommenter
            this.majPlateau();
            //this.majBoutons();
        }
        if (e.getSource() == this.btnAccueil) { this.frame.setPnl(this.frame.getPnl(0)); }
    }

    /*-------------------------------*/
    /*      Méthodes utilitaires     */
    /*-------------------------------*/
    private void creerComposants()
    {
        this.tailleEcran            = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.taillePlateau          = (int) (tailleEcran.height * 0.55);
        this.tailleBoutonPave       = (int) (taillePlateau * 0.15);
        this.tailleBoutonOutil      = (int) (this.tailleBoutonPave * 3) + 20;

        this.tabPnlCases            = new JPanel[3][3][3][3];
        
        this.ligCaseSelect          = -1; this.colCaseSelect          = -1;
        this.ligBlocSelect          = -1; this.colBlocSelect          = -1;

        this.pnlHaut                = new JPanel(new BorderLayout());
        this.pnlHaut                .setOpaque(false);

        this.pnlBandeau             = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        this.pnlBandeau             .setOpaque(false);

        this.lblValidation          = new JLabel();
        this.lblValidation          .setOpaque(false);
        this.lblValidation          .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.lblValidation          .setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        this.initNbValidations();

        this.pnlCentre              = new JPanel(new GridBagLayout());
        this.pnlCentre              .setOpaque(false);
        
        this.pnlPlateau             = new JPanel(new GridLayout(3, 3));
        this.pnlPlateau             .setPreferredSize(new Dimension(600, 600));
        this.pnlPlateau             .setBackground(ConstantesIHM.COULEUR_FOND);

        this.pnlBas                 = new JPanel(new BorderLayout());
        this.pnlBas                 .setOpaque(false);

        this.pnlBoutons             = new JPanel(new FlowLayout(FlowLayout.CENTER , 20, 20));
        this.pnlBoutons             .setOpaque(false);

        this.pnlNbres               = new JPanel(new GridLayout(3, 3, 10, 10));
        this.pnlNbres               .setBackground(ConstantesIHM.COULEUR_FOND);
        
        this.pnlOutils              = new JPanel(new GridBagLayout());
        this.pnlOutils              .setOpaque(false);

        this.pnlColonneDroite       = new JPanel(new GridBagLayout());
        this.pnlColonneDroite       .setOpaque(false);

        this.separateurHaut         = new JSeparator(SwingConstants.HORIZONTAL);
        this.separateurHaut         .setForeground(ConstantesIHM.COULEUR_SEPARATEUR);
        this.separateurHaut         .setBackground(ConstantesIHM.COULEUR_FOND_SEPARATEUR);

        this.separateurBas          = new JSeparator(SwingConstants.HORIZONTAL);
        this.separateurBas          .setForeground(ConstantesIHM.COULEUR_SEPARATEUR);
        this.separateurBas          .setBackground(ConstantesIHM.COULEUR_FOND_SEPARATEUR);

        this.dessinerPlateau();   

        this.btnValide              = new JButton("  Valider");
        this.btnValide              .setBackground(Color.WHITE);
        this.btnValide              .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnValide              .setPreferredSize(new Dimension(tailleEcran.width/6, 50));
        this.btnValide              .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/valide.png", 30, 30)));
        this.btnValide              .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnValide              .setFocusPainted(false);

        this.btnReset               = new JButton("  Réintialiser le plateau");
        this.btnReset               .setBackground(Color.WHITE);
        this.btnReset               .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnReset               .setPreferredSize(new Dimension(tailleEcran.width/6, 50));
        this.btnReset               .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/reset.png", 30, 25)));
        this.btnReset               .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnReset               .setFocusPainted(false);

        this.btnRestart             = new JButton("  Recommencer une partie");
        this.btnRestart             .setBackground(Color.WHITE);
        this.btnRestart             .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnRestart             .setPreferredSize(new Dimension(tailleEcran.width/6, 50));
        this.btnRestart             .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/restart.png", 35, 35)));
        this.btnRestart             .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnRestart             .setFocusPainted(false);

        this.btnAnnuler             = new JButton("  Annuler");
        this.btnAnnuler             .setBackground(Color.WHITE);
        this.btnAnnuler             .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnAnnuler             .setPreferredSize(new Dimension(tailleEcran.width/6, 50));
        this.btnAnnuler             .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/annuler.png", 30, 25)));
        this.btnAnnuler             .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnAnnuler             .setFocusPainted(false);
        this.btnAnnuler             .setEnabled(false);

        this.btnAccueil             = new JButton("  Retourner à l'accueil");
        this.btnAccueil             .setBackground(Color.WHITE);
        this.btnAccueil             .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnAccueil             .setPreferredSize(new Dimension(tailleEcran.width/6, 50));
        this.btnAccueil             .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/accueil.png", 30, 25)));
        this.btnAccueil             .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnAccueil             .setFocusPainted(false);

        this.dessinerBoutons();    

        this.btnGomme               = new JButton("");
        this.btnGomme               .setBackground(Color.WHITE);
        this.btnGomme               .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE_GOMME));
        this.btnGomme               .setPreferredSize(new Dimension((this.tailleBoutonOutil - 10) / 2, 50));
        this.btnGomme               .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/gomme.png", 30, 30)));
        this.btnGomme               .setFocusPainted(false);

        this.btnIndice              = new JButton("");
        this.btnIndice              .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnIndice              .setPreferredSize(new Dimension((this.tailleBoutonOutil - 10) / 2, 50));
        this.btnIndice              .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/indice.png", 40, 35)));
        this.btnIndice              .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnIndice              .setFocusPainted(false);

        this.initBoutonIndice();

        this.btnBrouillon           = new JButton("  Mode Brouillon");
        this.btnBrouillon           .setBackground(Color.WHITE);
        this.btnBrouillon           .setFont(new Font("Sans-Serif", Font.PLAIN, 18));
        this.btnBrouillon           .setPreferredSize(new Dimension(this.tailleBoutonOutil, 50));
        this.btnBrouillon           .setIcon(new ImageIcon(this.frame.creerImages("./images/icones/brouillon.png", 30, 25)));
        this.btnBrouillon           .setBorder(BorderFactory.createCompoundBorder(ConstantesIHM.RELIEF, ConstantesIHM.MARGE));
        this.btnBrouillon           .setFocusPainted(false);
    }

    private void positionnerComposants()
    {
        this.pnlBoutons.add(this.btnValide );
        this.pnlBoutons.add(this.btnReset);
        this.pnlBoutons.add(this.btnRestart);
        this.pnlBoutons.add(this.btnAnnuler);
        this.pnlBoutons.add(this.btnAccueil);

        this.pnlBandeau.add(lblValidation);
        
        this.pnlHaut   .add(pnlBandeau     , BorderLayout.CENTER  );
        this.pnlHaut   .add(separateurHaut , BorderLayout.SOUTH );

        GridBagConstraints gbcOutils    = new GridBagConstraints();
        gbcOutils.fill                  = GridBagConstraints.BOTH; 
        gbcOutils.insets                = new Insets(5, 5, 5, 5); 

        gbcOutils.gridx     = 0; gbcOutils.gridy = 0; gbcOutils.gridwidth = 1; 
        gbcOutils.insets    = new Insets(15, 0, 0, 5);
        pnlOutils           .add(this.btnGomme, gbcOutils);

        gbcOutils.gridx     = 1; gbcOutils.gridy = 0;
        gbcOutils.insets    = new Insets(15, 5, 0, 0);
        pnlOutils           .add(this.btnIndice, gbcOutils);

        gbcOutils.gridx     = 0; gbcOutils.gridy = 1; gbcOutils.gridwidth = 2; 
        gbcOutils.insets    = new Insets(10, 0, 0, 0);
        pnlOutils           .add(this.btnBrouillon, gbcOutils);

        GridBagConstraints gbcDroite = new GridBagConstraints();
        gbcDroite.gridx             = 0; 
        gbcDroite.gridy             = 0;
        this.pnlColonneDroite       .add(this.pnlNbres, gbcDroite);
        gbcDroite.gridy         = 1;
        gbcDroite.insets        = new Insets(38, 0, 0, 0); 
        this.pnlColonneDroite   .add(this.pnlOutils, gbcDroite);

        GridBagConstraints gbc  = new GridBagConstraints();
        gbc.anchor              = GridBagConstraints.NORTH;
        gbc.gridx       = 0; gbc.gridy = 0;
        gbc.insets      = new Insets(0, 0, 0, 100); 
        this.pnlCentre  .add(this.pnlPlateau, gbc);

        gbc.gridx       = 1; gbc.gridy = 0;
        gbc.insets      = new Insets(0, 0, 0, 0); 
        this.pnlCentre  .add(this.pnlColonneDroite, gbc);

        pnlBas          .add(separateurBas  , BorderLayout.NORTH );
        pnlBas          .add(pnlBoutons     , BorderLayout.CENTER);

        this.add(pnlHaut        , BorderLayout.NORTH );
        this.add(this.pnlCentre , BorderLayout.CENTER);
        this.add(pnlBas         , BorderLayout.SOUTH );
    }

    private void activerComposants()
    {
        this.btnGomme.addActionListener(this);
        this.btnGomme.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnGomme.setBackground(ConstantesIHM.COULEUR_SURVOL); btnGomme.setForeground(Color.BLACK); }
            public void mouseExited(MouseEvent e)  { btnGomme.setBackground(Color.WHITE); btnGomme.setForeground(Color.BLACK); }
            public void mousePressed(MouseEvent e) { btnGomme.setBackground(Color.WHITE); btnGomme.setForeground(Color.BLACK); }
        });

        this.btnIndice.addActionListener(this);
        this.btnIndice.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (btnIndice.isEnabled()) btnIndice.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { if (btnIndice.isEnabled()) btnIndice.setBackground(ConstantesIHM.COULEUR_INDICE_ACTIVE); }
            public void mousePressed(MouseEvent e) { btnIndice.setBackground(Color.WHITE); btnIndice.setForeground(Color.BLACK); }
        });

        this.btnBrouillon.addActionListener(this);
        this.btnBrouillon.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!PanelJeu.this.modeBrouillon) btnBrouillon.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { if (PanelJeu.this.modeBrouillon) btnBrouillon.setBackground(ConstantesIHM.COULEUR_BROUILLON_ACTIVE); else btnBrouillon.setBackground(Color.WHITE); }
            public void mousePressed(MouseEvent e) { btnBrouillon.setBackground(Color.WHITE); btnBrouillon.setForeground(Color.BLACK); }
        });
        
        this.btnValide.addActionListener(this);
        this.btnValide.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnValide.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { btnValide.setBackground(Color.WHITE); }
        });
        
        this.btnReset.addActionListener(this);
        this.btnReset.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnReset.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { btnReset.setBackground(Color.WHITE); }
        });
        
        this.btnRestart.addActionListener(this);
        this.btnRestart.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnRestart.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { btnRestart.setBackground(Color.WHITE); }
        });
        
        this.btnAnnuler.addActionListener(this);
        this.btnAnnuler.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAnnuler.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { btnAnnuler.setBackground(Color.WHITE); }
        });
        
        this.btnAccueil.addActionListener(this);
        this.btnAccueil.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAccueil.setBackground(ConstantesIHM.COULEUR_SURVOL); }
            public void mouseExited(MouseEvent e)  { btnAccueil.setBackground(Color.WHITE); }
        });
        
        // Pour le clic dans le vide (désélectionne la case en cours)
        MouseAdapter clicDansLeVide = new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                PanelJeu.this.ligBlocSelect = -1;
                PanelJeu.this.colBlocSelect = -1;
                PanelJeu.this.ligCaseSelect = -1;
                PanelJeu.this.colCaseSelect = -1;
                PanelJeu.this.majPlateau();
            }
        };

        this            .addMouseListener(clicDansLeVide);
        this.pnlCentre  .addMouseListener(clicDansLeVide);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "actionAnnuler");
        this.getActionMap().put("actionAnnuler", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { btnAnnuler.doClick(); }
        });
    }

    public void dessinerPlateau()
    {
        this.pnlPlateau.removeAll();
        this.pnlPlateau.setLayout(new GridLayout(3, 3));
        this.tabPnlCases = new JPanel[3][3][3][3];

        for (int lig = 0; lig < 3; lig++)
        {
            for (int col = 0; col < 3; col++)
            {
                JPanel pnlBloc = new JPanel(new GridLayout(3, 3));
                pnlBloc.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                
                for (int lig2 = 0; lig2 < 3; lig2++)
                {
                    for (int col2 = 0; col2 < 3; col2++)
                    {
                        JPanel pnlCellule = new JPanel(new CardLayout());
                        pnlCellule.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));        
                        pnlCellule.setBackground(Color.WHITE);

                        JPanel pnlBrouillon = new JPanel(new GridLayout(3, 3));
                        pnlBrouillon.setOpaque(false);
                        for (int valeurLabel = 1; valeurLabel <= 9; valeurLabel++)
                        {
                            JLabel lblNombre = new JLabel("", SwingConstants.CENTER);
                            lblNombre.setFont(new Font("Sans-Serif", Font.PLAIN, 15)); 
                            lblNombre.setForeground(ConstantesIHM.COULEUR_GRIS_BROUILLON);
                            pnlBrouillon.add(lblNombre);
                        }

                        JLabel lblNombre;
                        int valCase = this.ctrl.getNombre(lig, col, lig2, col2); 

                        if (valCase != 0)
                        {    
                            lblNombre = new JLabel(String.valueOf(valCase), SwingConstants.CENTER);
                            lblNombre.setFont(new Font("Sans-Serif", Font.BOLD, 24));
                        }
                        else
                        {
                            lblNombre = new JLabel("", SwingConstants.CENTER);
                            lblNombre.setFont(new Font("Sans-Serif", Font.PLAIN, 24));
                        }
                        lblNombre.setOpaque(false); 

                        pnlCellule.add(lblNombre, "VALEUR");
                        pnlCellule.add(pnlBrouillon, "BROUILLON");

                        this.tabPnlCases[lig][col][lig2][col2] = pnlCellule;    
                        
                        pnlCellule.addMouseListener(new GereSouris(lig, col, lig2, col2));
                        
                        GereDepot depot = new GereDepot(lig, col, lig2, col2);
                        pnlCellule.setTransferHandler(depot);
                        lblNombre.setTransferHandler(depot);

                        pnlBloc.add(pnlCellule);
                    }
                }
                this.pnlPlateau.add(pnlBloc);
            }
        }
    }

    public void dessinerBoutons()
    {
        this.pnlNbres.removeAll(); 
        this.grpBtn = new ButtonGroup(); 
        
        for (int valeurBouton = 1; valeurBouton <= 9; valeurBouton++)
        {   
            JToggleButton btnTmp = new JToggleButton(String.valueOf(valeurBouton));
            btnTmp.setBackground(Color.WHITE);
            btnTmp.setFont(new Font("Sans-Serif", Font.BOLD, 18));
            btnTmp.setPreferredSize(new Dimension(tailleBoutonPave, tailleBoutonPave));
            btnTmp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            btnTmp.setFocusPainted(false);

            btnTmp.setTransferHandler(new TransferHandler() {
                public int getSourceActions(JComponent c) { return TransferHandler.COPY; }
                protected java.awt.datatransfer.Transferable createTransferable(JComponent c) {
                    return new java.awt.datatransfer.StringSelection(((JToggleButton) c).getText());
                }
            });

            btnTmp.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    JComponent composant = (JComponent) e.getSource();
                    composant.getTransferHandler().exportAsDrag(composant, e, TransferHandler.COPY);
                }
            });

            btnTmp.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (btnTmp.getBackground() != ConstantesIHM.COULEUR_PLACE && !btnTmp.isSelected()) 
                        btnTmp.setBackground(ConstantesIHM.COULEUR_SURVOL);
                }
                public void mouseExited(MouseEvent e) {
                    if (btnTmp.getBackground() != ConstantesIHM.COULEUR_PLACE && !btnTmp.isSelected()) 
                        btnTmp.setBackground(Color.WHITE);
                }
            });

            // Géré par le Controleur
            /*
            if (!this.ctrl.estPresent(valeurBouton)) { btnTmp.setBackground(Color.WHITE); }
            else { btnTmp.setBackground(ConstantesIHM.COULEUR_GRIS_PLACE); }
            */
            
            this.grpBtn.add(btnTmp);
            btnTmp.addActionListener(this);
            this.pnlNbres.add(btnTmp);
        }

        this.pnlNbres.revalidate();
        this.pnlNbres.repaint();
    }

    public void majPlateau()
    {
        for (int ligB = 0; ligB < 3; ligB++)
        {
            for (int colB = 0; colB < 3; colB++)
            {
                for (int ligC = 0; ligC < 3; ligC++)
                {
                    for (int colC = 0; colC < 3; colC++)
                    {
                        Color couleurCase = Color.WHITE;

                        if (ligB == this.ligBlocSelect && colB == this.colBlocSelect && 
                            ligC == this.ligCaseSelect && colC == this.colCaseSelect) 
                        {
                            couleurCase = ConstantesIHM.COULEUR_SELECTION;
                        }

                        this.tabPnlCases[ligB][colB][ligC][colC].setBackground(couleurCase);

                        JLabel lbl = (JLabel) this.tabPnlCases[ligB][colB][ligC][colC].getComponent(0);
                        CardLayout croupier = (CardLayout) this.tabPnlCases[ligB][colB][ligC][colC].getLayout();
                        
                        int vraiChiffre = this.ctrl.getNombre(ligB, colB, ligC, colC);
                        
                        if (vraiChiffre == 0) 
                        {
                            lbl.setText(""); 
                            croupier.show(this.tabPnlCases[ligB][colB][ligC][colC], "BROUILLON");
                        } 
                        else 
                        {
                            lbl.setText(String.valueOf(vraiChiffre)); 
                            
                            // A décommenter quand implémenté
                            // if (!this.ctrl.getCasePlateau(ligB, colB, ligC, colC).estModifiable()) 
                            //    lbl.setFont(new Font("Sans-Serif", Font.BOLD, 24));
                            // else 
                                lbl.setFont(new Font("Sans-Serif", Font.PLAIN, 24));
                            
                            croupier.show(this.tabPnlCases[ligB][colB][ligC][colC], "VALEUR");
                        }
                    }
                }
            }
        }
        
        // this.btnAnnuler.setEnabled(this.ctrl.peutAnnuler());
        this.repaint();
    }

    // Méthode qui met à jour l'affichage des boutons du pavé numérique
    public void majBoutons()
    {
        if (this.grpBtn != null) 
        {
            java.util.Enumeration<AbstractButton> elements = this.grpBtn.getElements();
            while (elements.hasMoreElements()) 
            {
                AbstractButton btn = elements.nextElement();
                
                // --- NOTE POUR LE SUDOKU ---
                // Au Sudoku, on ne grise un bouton que si le joueur a placé ce chiffre 9 fois.
                // À décommenter quand ton collègue aura créé une méthode estComplet(numero) dans le Métier !
                
                /*
                int numero = Integer.parseInt(btn.getText());
                if (this.ctrl.estComplet(numero))
                    btn.setBackground(ConstantesIHM.COULEUR_GRIS_PLACE);
                else
                    btn.setBackground(Color.WHITE);
                */

                // En attendant la méthode du métier, on s'assure que le bouton reprend sa couleur
                // normale (blanc) s'il n'est plus sélectionné, pour corriger ton problème de style !
                if (btn.getBackground() == ConstantesIHM.COULEUR_PLACE && !btn.isSelected()) 
                {
                    btn.setBackground(Color.WHITE);
                }
                
                btn.setEnabled(true);
            }
        }
    }

    private void initBoutonIndice()
    {
        /* À décommenter quand la difficulté sera implémentée dans Sudoku.java
        int difficulte = this.ctrl.getDifficulte();
        if      (difficulte == 0) this.nbIndicesRestants = 2;
        else if (difficulte == 1) this.nbIndicesRestants = 1;
        else                      this.nbIndicesRestants = 0;
        */
        this.nbIndicesRestants = 2; // Valeur par défaut en attendant

        this.btnIndice.setText("  " + this.nbIndicesRestants); 
        if (this.nbIndicesRestants > 0)
        {
            this.btnIndice.setBackground(ConstantesIHM.COULEUR_INDICE_ACTIVE);
            this.btnIndice.setEnabled(true);
        }
        else
        {
            this.btnIndice.setBackground(Color.WHITE);
            this.btnIndice.setEnabled(false); 
        }
    }

    private void initNbValidations()
    {
        this.nbValidationsMax = 3;
        this.nbValidationsRestantes = this.nbValidationsMax;
        this.majAffichageValidation();
    }

    private void majAffichageValidation()
    {
        this.lblValidation.setText("Nombre de validations restantes : " + this.nbValidationsRestantes);

        if (this.nbValidationsRestantes > 2)      { this.lblValidation.setForeground(new Color(0, 150, 0)); } 
        else if (this.nbValidationsRestantes > 1) { this.lblValidation.setForeground(new Color(255, 140, 0)); } 
        else                                      { this.lblValidation.setForeground(Color.RED); }
    }

    public void reset()
    {
        this.ligBlocSelect = -1; this.colBlocSelect = -1;
        this.ligCaseSelect = -1; this.colCaseSelect = -1;
        this.modeValidation = false;
        this.modeBrouillon  = false;

        this.btnBrouillon.setBackground(Color.WHITE);
        this.btnActif = null;
        this.btnGomme.setEnabled(true);
        
        for (int ligB = 0; ligB < 3; ligB++)
        {
            for (int colB = 0; colB < 3; colB++)
            {
                for (int ligC = 0; ligC < 3; ligC++)
                {
                    for (int colC = 0; colC < 3; colC++)
                    {
                        JPanel pnlBrouillon = (JPanel) tabPnlCases[ligB][colB][ligC][colC].getComponent(1);
                        for (int i = 0; i < 9; i++) ((JLabel) pnlBrouillon.getComponent(i)).setText("");
                        
                        // if (this.ctrl.getCasePlateau(ligB, colB, ligC, colC).estModifiable())
                        {
                            JLabel lblContenu = (JLabel) tabPnlCases[ligB][colB][ligC][colC].getComponent(0);
                            String chiffreEfface = lblContenu.getText();

                            if (!chiffreEfface.equals("") && !chiffreEfface.equals("0"))
                            {
                                this.ctrl.effacerNombre(ligB, colB, ligC, colC);
                                        
                                lblContenu.setText(""); 
                                lblContenu.setForeground(null);

                                if (this.grpBtn != null) 
                                {
                                    java.util.Enumeration<AbstractButton> elements = this.grpBtn.getElements();
                                    while (elements.hasMoreElements()) 
                                    {
                                        AbstractButton btn = elements.nextElement();
                                        if (btn.getText().equals(chiffreEfface)) 
                                        {
                                            btn.setBackground(Color.WHITE);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        this.majPlateau();
    }

    public void restart()
    {
        // this.ctrl.resetJeu(); // À décommenter

        this.ligBlocSelect = -1; this.colBlocSelect = -1;
        this.ligCaseSelect = -1; this.colCaseSelect = -1;
        this.modeValidation = false;
        this.modeBrouillon  = false;

        this.btnBrouillon.setBackground(Color.WHITE);
        this.initBoutonIndice();
        this.initNbValidations();
        this.btnActif = null;
        this.btnGomme.setEnabled(true);
        
        this.dessinerPlateau();
        this.dessinerBoutons();
        this.majPlateau();
    }

    private class GereSouris extends MouseAdapter
    {
        private int ligBloc, colBloc, ligCase, colCase; 

        public GereSouris(int lig, int col, int lig2, int col2)
        {
            this.ligBloc = lig; this.colBloc = col;
            this.ligCase = lig2; this.colCase = col2;
        }

        public void mouseClicked(MouseEvent e)
        {
            if (PanelJeu.this.ctrl.estGagne()) { return; }
            
            if (!PanelJeu.this.modeBrouillon) 
            {
                /* 
                // ECHANGE A DECOMMENTER QUAND LA METHODE SERA DANS LE CONTROLEUR
                if (PanelJeu.this.ligBlocSelect != -1 && PanelJeu.this.btnActif == null)
                {
                    int valCliquee = PanelJeu.this.ctrl.getNombre(this.ligBloc, this.colBloc, this.ligCase, this.colCase);
                    int valPremiereCase = PanelJeu.this.ctrl.getNombre(PanelJeu.this.ligBlocSelect, PanelJeu.this.colBlocSelect, PanelJeu.this.ligCaseSelect, PanelJeu.this.colCaseSelect);
                    
                    boolean cibleModifiable = PanelJeu.this.ctrl.getCasePlateau(this.ligBloc, this.colBloc, this.ligCase, this.colCase).estModifiable();
                    boolean sourceModifiable = PanelJeu.this.ctrl.getCasePlateau(PanelJeu.this.ligBlocSelect, PanelJeu.this.colBlocSelect, PanelJeu.this.ligCaseSelect, PanelJeu.this.colCaseSelect).estModifiable();

                    if (valPremiereCase != 0 && cibleModifiable && sourceModifiable && 
                        (PanelJeu.this.ligBlocSelect != this.ligBloc || PanelJeu.this.colBlocSelect != this.colBloc || PanelJeu.this.ligCaseSelect != this.ligCase || PanelJeu.this.colCaseSelect != this.colCase))
                    {
                        PanelJeu.this.ctrl.echangerNbr(PanelJeu.this.ligBlocSelect, PanelJeu.this.colBlocSelect, PanelJeu.this.ligCaseSelect, PanelJeu.this.colCaseSelect, this.ligBloc, this.colBloc, this.ligCase, this.colCase);
                        
                        PanelJeu.this.modeValidation = false;
                        PanelJeu.this.ligBlocSelect = -1; PanelJeu.this.colBlocSelect = -1;
                        PanelJeu.this.ligCaseSelect = -1; PanelJeu.this.colCaseSelect = -1;

                        PanelJeu.this.majPlateau();
                        return; 
                    }
                }
                */
                
                if (PanelJeu.this.ligBlocSelect == this.ligBloc && PanelJeu.this.colBlocSelect == this.colBloc &&
                    PanelJeu.this.ligCaseSelect == this.ligCase && PanelJeu.this.colCaseSelect == this.colCase) 
                {
                    PanelJeu.this.ligBlocSelect = -1; PanelJeu.this.colBlocSelect = -1;
                    PanelJeu.this.ligCaseSelect = -1; PanelJeu.this.colCaseSelect = -1;
                } 
                else 
                {
                    PanelJeu.this.ligBlocSelect = this.ligBloc; PanelJeu.this.colBlocSelect = this.colBloc;
                    PanelJeu.this.ligCaseSelect = this.ligCase; PanelJeu.this.colCaseSelect = this.colCase;
                }

                if (PanelJeu.this.btnActif != null 
                    && PanelJeu.this.ctrl.getNombre(this.ligBloc, this.colBloc, this.ligCase, this.colCase) == 0)
                    // && PanelJeu.this.ctrl.getCasePlateau(this.ligBloc, this.colBloc, this.ligCase, this.colCase).estModifiable())
                {
                    int valeur = Integer.parseInt(PanelJeu.this.btnActif.getText());

                    /* 
                    if (PanelJeu.this.ctrl.estPresent(valeur)) 
                    {
                        PanelJeu.this.btnActif.setSelected(false);
                        PanelJeu.this.btnActif.setBackground(ConstantesIHM.COULEUR_GRIS_PLACE);
                        PanelJeu.this.btnActif = null;
                    }
                    else 
                    */
                    {
                        JLabel lblContenu = (JLabel) tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].getComponent(0);
                        lblContenu.setForeground(Color.BLACK); 

                        PanelJeu.this.ctrl.setNombre(this.ligBloc, this.colBloc, this.ligCase, this.colCase, valeur);
                        
                        PanelJeu.this.btnActif.setBackground(ConstantesIHM.COULEUR_PLACE);
                        PanelJeu.this.grpBtn.clearSelection(); 
                        PanelJeu.this.btnActif = null;

                        PanelJeu.this.ligBlocSelect = -1; PanelJeu.this.colBlocSelect = -1;
                        PanelJeu.this.ligCaseSelect = -1; PanelJeu.this.colCaseSelect = -1;
                        PanelJeu.this.modeValidation = false; 
                    }
                }

                PanelJeu.this.majPlateau();
            }
            else
            {
                if (PanelJeu.this.btnActif != null 
                    // && PanelJeu.this.ctrl.getCasePlateau(this.ligBloc, this.colBloc, this.ligCase, this.colCase).estModifiable()
                    && PanelJeu.this.ctrl.getNombre(this.ligBloc, this.colBloc, this.ligCase, this.colCase) == 0)
                {
                    int valeur = Integer.parseInt(PanelJeu.this.btnActif.getText());

                    /*
                    if (PanelJeu.this.ctrl.estPresent(valeur)) 
                    {
                        PanelJeu.this.btnActif.setSelected(false);
                        PanelJeu.this.btnActif.setBackground(ConstantesIHM.COULEUR_GRIS_PLACE);
                        PanelJeu.this.btnActif = null;
                    }
                    else 
                    */
                    {
                        JPanel pnlBrouillon = (JPanel) tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].getComponent(1);
                        JLabel lblPetitNum = (JLabel) pnlBrouillon.getComponent(valeur - 1);

                        if (lblPetitNum.getText().equals("")) lblPetitNum.setText(String.valueOf(valeur)); 
                        else                                  lblPetitNum.setText(""); 
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            if (PanelJeu.this.ligBlocSelect != this.ligBloc || PanelJeu.this.colBlocSelect != this.colBloc || 
                PanelJeu.this.ligCaseSelect != this.ligCase || PanelJeu.this.colCaseSelect != this.colCase) 
            {
                PanelJeu.this.tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].setBackground(ConstantesIHM.COULEUR_SURVOL);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            if (PanelJeu.this.ligBlocSelect != this.ligBloc || PanelJeu.this.colBlocSelect != this.colBloc || 
                PanelJeu.this.ligCaseSelect != this.ligCase || PanelJeu.this.colCaseSelect != this.colCase) 
            {
                PanelJeu.this.tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].setBackground(Color.WHITE);
            }
        }
    }

    private class GereDepot extends TransferHandler
    {
        private int ligBloc, colBloc, ligCase, colCase; 

        public GereDepot(int lig, int col, int lig2, int col2)
        {
            this.ligBloc = lig; this.colBloc = col;
            this.ligCase = lig2; this.colCase = col2;
        }

        @Override
        public boolean canImport(TransferSupport support)
        {
            if (!support.isDrop()) return false;
            if (!support.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor)) return false;
            return true; // Remplacer par ctrl.getCasePlateau(ligBloc, colBloc, ligCase, colCase).estModifiable() quand implémenté
        }

        @Override
        public boolean importData(TransferSupport support)
        {
            if (!canImport(support)) return false;

            try 
            {
                String donnees = (String) support.getTransferable().getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
                int valeurAjoutee = Integer.parseInt(donnees);

                // if (PanelJeu.this.ctrl.estPresent(valeurAjoutee)) return false;

                if (PanelJeu.this.modeBrouillon) 
                {
                    JPanel pnlBrouillon = (JPanel) tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].getComponent(1);
                    JLabel lblPetitNum = (JLabel) pnlBrouillon.getComponent(valeurAjoutee - 1);
                    
                    if (lblPetitNum.getText().equals("")) lblPetitNum.setText(String.valueOf(valeurAjoutee));
                    else                                  lblPetitNum.setText("");

                    PanelJeu.this.majPlateau();
                    return true; 
                }

                JLabel lblContenu = (JLabel) tabPnlCases[this.ligBloc][this.colBloc][this.ligCase][this.colCase].getComponent(0);
                String chiffreEcrase = lblContenu.getText();
                
                if (!chiffreEcrase.equals("") && !chiffreEcrase.equals("0")) 
                {
                    if (PanelJeu.this.grpBtn != null) 
                    {
                        java.util.Enumeration<AbstractButton> elements = PanelJeu.this.grpBtn.getElements();
                        while (elements.hasMoreElements()) 
                        {
                            AbstractButton btn = elements.nextElement();
                            if (btn.getText().equals(chiffreEcrase)) 
                            {
                                btn.setBackground(Color.WHITE);
                                break;
                            }
                        }
                    }
                }

                PanelJeu.this.ctrl.setNombre(this.ligBloc, this.colBloc, this.ligCase, this.colCase, valeurAjoutee);
                lblContenu.setForeground(Color.BLACK);

                if (PanelJeu.this.grpBtn != null) 
                {
                    java.util.Enumeration<AbstractButton> elements = PanelJeu.this.grpBtn.getElements();
                    while (elements.hasMoreElements()) 
                    {
                        AbstractButton btn = elements.nextElement();
                        if (btn.getText().equals(String.valueOf(valeurAjoutee))) 
                        {
                            btn.setBackground(ConstantesIHM.COULEUR_PLACE);
                            PanelJeu.this.grpBtn.clearSelection();
                            break;
                        }
                    }
                }

                PanelJeu.this.ligBlocSelect = -1; PanelJeu.this.colBlocSelect = -1;
                PanelJeu.this.ligCaseSelect = -1; PanelJeu.this.colCaseSelect = -1;
                PanelJeu.this.modeValidation = false;
                PanelJeu.this.btnActif = null;
                
                PanelJeu.this.majPlateau();
                
                return true;
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                return false;
            }
        }
    }
}