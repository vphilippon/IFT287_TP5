package tp3;

import java.sql.Date;

public class TupleSerie {
    
    private String titre;
    private Date anneeSortie;
    private String realisateur;
    private String description;
    private int nbSaison;

    public TupleSerie(String titre, Date anneeSortie, String realisateur, 
            String description, int nbSaison) {
        this.titre = titre;
        this.anneeSortie = anneeSortie;
        this.realisateur = realisateur;
        this.description = description;
        this.nbSaison = nbSaison;
    }

    public String getTitre() {
        return titre;
    }

    public Date getAnneeSortie() {
        return anneeSortie;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public String getDescription() {
        return description;
    }

    public int getNbSaison() {
        return nbSaison;
    }
}
