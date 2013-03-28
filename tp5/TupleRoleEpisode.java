package tp3;

import java.sql.Date;

public class TupleRoleEpisode {
    private String nomActeur;
    private String roleActeur;
    private String titreSerie;
    private String titreEpisode;
    private int noSaison;
    private int noEpisode;
    private Date anneeSortieSerie;
    
    public TupleRoleEpisode(String nomActeur, String roleActeur, String titreSerie, 
            String titreEpisode, int noSaison, int noEpisode, Date anneeSortieSerie) {
        this.nomActeur = nomActeur;
        this.roleActeur = roleActeur;
        this.titreSerie = titreSerie;
        this.titreEpisode = titreEpisode;
        this.noSaison = noSaison;
        this.noEpisode = noEpisode;
        this.anneeSortieSerie = anneeSortieSerie;
    }

    public String getNomActeur() {
        return nomActeur;
    }

    public String getRoleActeur() {
        return roleActeur;
    }

    public String getTitreSerie() {
        return titreSerie;
    }

    public String getTitreEpisode() {
        return titreEpisode;
    }

    public int getNoSaison() {
        return noSaison;
    }

    public int getNoEpisode() {
        return noEpisode;
    }

    public Date getAnneeSortieSerie() {
        return anneeSortieSerie;
    }
}
