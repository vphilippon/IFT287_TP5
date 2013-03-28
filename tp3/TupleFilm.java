package tp3;

import java.sql.Date;

class TupleFilm 
{
    
    private String titre;
    private Date dateSortie;
    private String description;
    private int duree;
    private String realisateur;

    public TupleFilm(String titre, Date dateSortie, String description, int duree, String realisateur) {
        this.titre = titre;
        this.dateSortie = dateSortie;
        this.description = description;
        this.duree = duree;
        this.realisateur = realisateur;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public String getDescription() {
        return description;
    }

    public int getDuree() {
        return duree;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public String getTitre() {
        return titre;
    }
}
