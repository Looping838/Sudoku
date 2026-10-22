package ihm;

import controleur.Controleur;
import java.awt.*;
import javax.swing.*;

public class FrameJeu extends JFrame
{
    private Controleur ctrl;
    
    private JPanel   panelActif;
    private JPanel[] tabPanel;
    
    public FrameJeu (Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("Sudoku");
		this.setSize(800, 800);
		this.setLayout(new BorderLayout());

        ImageIcon icone = new ImageIcon("./images/icones/sudoku.png");
        this.setIconImage(icone.getImage());

        PanelJeu panel = new PanelJeu(this.ctrl);
        this.add(panel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}