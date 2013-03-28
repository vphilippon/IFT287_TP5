package tp3;

import java.sql.Date;

public class TupleRoleFilm {
    private String nomActeur;
    private String roleActeur;
    private String filmTitre;
    private Date anneeSortie;
    
    TupleRoleFilm(String nomActeur, String roleActeur, String filmTitre, Date anneeSortie) {
        this.nomActeur = nomActeur;
        this.roleActeur = roleActeur;
        this.filmTitre = filmTitre;
        this.anneeSortie = anneeSortie;
    }
    
    public String getFilmTitre() {
        return this.filmTitre;
    }

    public String getNomActeur() {
        return this.nomActeur;
    }
    
    public String getRoleActeur() {
        return this.roleActeur;
    }
    
    public Date getAnneeSortie() {
        return this.anneeSortie;
    }
}
