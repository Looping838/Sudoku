package metier;

import controleur.Controleur;

public class Pile
{
    private String[]   pile;
    private int        numAction;
    private Controleur ctrl;

    public Pile(Controleur ctrl)
    {
        this.pile      = new String[50];
        this.numAction = 1;
        this.ctrl      = ctrl;
    }

    public void empiler(String action)
    {
        if (this.numAction == 50)
        {
            this.numAction = 1;
        }
        this.pile[this.numAction] = action;
        this.numAction++;
    }

    public void depiler()
    {
        if (this.numAction > 1)
        {
            this.numAction--;
            this.decomposeur(this.pile[this.numAction]);
        }
    }

    public void decomposeur(String res)
    {
        if (res == null || res.isBlank())
        {
            return;
        }

        String[] parties = res.replaceAll("[\\[\\],]", " ").trim().split("\\s+");
        String action = parties[0];

        switch (action)
        {
            case "setNbrCase" ->
            {
                int val  = Integer.parseInt(parties[1]);
                int lig1 = Integer.parseInt(parties[2]);
                int col1 = Integer.parseInt(parties[3]);
                int lig2 = Integer.parseInt(parties[4]);
                int col2 = Integer.parseInt(parties[5]);

                if (val == 0)
                {
                    this.ctrl.effacerChiffre(lig1, col1, lig2, col2);
                }
                else
                {
                    this.ctrl.setNbrCase(lig1, col1, lig2, col2, val);
                }
            }

            case "effacerChiffre" ->
            {
                int val  = Integer.parseInt(parties[1]);
                int lig1 = Integer.parseInt(parties[2]);
                int col1 = Integer.parseInt(parties[3]);
                int lig2 = Integer.parseInt(parties[4]);
                int col2 = Integer.parseInt(parties[5]);

                this.ctrl.setNbrCase(lig1, col1, lig2, col2, val);
            }

            case "echangerNbr" ->
            {
                int lig1 = Integer.parseInt(parties[1]);
                int col1 = Integer.parseInt(parties[2]);
                int lig2 = Integer.parseInt(parties[3]);
                int col2 = Integer.parseInt(parties[4]);

                int lig3 = Integer.parseInt(parties[5]);
                int col3 = Integer.parseInt(parties[6]);
                int lig4 = Integer.parseInt(parties[7]);
                int col4 = Integer.parseInt(parties[8]);

                this.ctrl.echangerNbr(lig1, col1, lig2, col2, lig3, col3, lig4, col4);
            }

            default -> System.err.println("Action inconnue : " + action);
        }
    }

    public String afficherPile()
    {
        String res = " [ ";

        for (int cpt = 0; cpt < this.pile.length; cpt++)
        {
            res += this.pile[cpt] + " , ";
        }

        res += " ] ";

        return res;
    }

    public boolean estVide()
    {
        return this.numAction <= 1;
    }
}