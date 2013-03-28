package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

class GestionFilm {
    
    private Film film;
    private Personne personne;
    private RoleFilm roleFilm;
    private Connexion cx;

    public GestionFilm(Film film, Personne personne, RoleFilm roleFilm) throws Tp3Exception{
        if (film.getConnexion() != personne.getConnexion()){
            throw new Tp3Exception("Connexions différentes dans GestionFilm");
        }
        this.cx = film.getConnexion();
        this.film = film;
        this.roleFilm = roleFilm;
        this.personne = personne;
    }

    public void ajoutFilm(String titre, Date dateSortie, String realisateur) throws Exception {
        try {
            // Vérifie si le film existe déja 
            if (film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Impossible d'ajouter, le film " + titre + " paru le " + dateSortie + " existe deja.");
            }
            // S'assure que le réalisateur existe
            if (!personne.existe(realisateur)){
                throw new Tp3Exception("Impossible d'ajouter, le réalisateur " + realisateur + " n'existe pas.");
            }
            Date realisateurNaissance = personne.getPersonne(realisateur).getDateNaissance();
            // S'assure que le réalisateur est né avant la sortie du film
            if (realisateurNaissance.after(dateSortie)){
                throw new Tp3Exception("Le réalisateur " + realisateur + " est né le: " + realisateurNaissance + 
                        " et ne peut pas avoir réaliser un film créé le: " + dateSortie);
            }  
            // Ajout du film dans la table des films
            film.ajouter(titre, dateSortie, realisateur);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    public void supprimerFilm(String titre, Date dateSortie) throws Exception {
        try {
            // Vérifie si le film existe
            if (!film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Impossible de supprimer, le film " + titre + " paru le " + dateSortie + " n'existe pas.");
            }
            //Verifie si un role est relier au film
            if(roleFilm.aDesRoles(titre, dateSortie)){
                throw new Tp3Exception("Impossible de supprimer, le film " + titre + ", un/des role(s) y sont encore rattache.");
            }
            // Supression du film dans la table Film
            System.out.println(film.enlever(titre, dateSortie) + " film supprimé");
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * @param titre
     * @param anneeSortie
     * @param description
     * @param duree 
     */
    public void ajoutDescFilm(String titre, Date anneeSortie, String description, int duree) throws Exception {
        try{
            //si le film n'existe pas
            if(!film.existe(titre, anneeSortie)){
                throw new Tp3Exception("Impossible d'ajouter la description, le film " + titre + " paru le " + anneeSortie + " n'existe pas.");
            }
            film.ajouterDescription(titre, anneeSortie, description, duree);
            cx.commit();
        }
        catch(Exception e){
            cx.rollback();
            throw e;
        }
    }
    
    
    public void ajoutActeurFilm(String titre, Date anneeSortie, String nomActeur, String role) throws Exception {
        try {
            //verifie si l acteur existe
            if (!personne.existe(nomActeur)) {
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film, l'acteur " + nomActeur + " n'existe pas.");
            }
            
            //verifie si le film existe
            if (!film.existe(titre, anneeSortie)) {
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film, le film " + titre + " paru le " + anneeSortie + " n'existe pas.");
            }
            
            //verifie que l acteur est nee avant la sortie du film
            TuplePersonne acteur = personne.getPersonne(nomActeur);
            if(acteur.getDateNaissance().after(anneeSortie)){
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film, l'acteur " + nomActeur + " est nee avant la date de sortie du film.");
            }
            
            //verifie que le role n existe pas deja pour cette acteur
            if (roleFilm.existe(nomActeur, titre, anneeSortie, role)) {
                throw new Tp3Exception("L'acteur " + nomActeur + " joue deja le role " + role + " dans le film " + titre + ".");
            }
            
            //@TODO voir si cette verification devrait etre la ou pas!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //verifie que le role n'existe pas deja pour un autre acteur
            if (roleFilm.existe(titre, anneeSortie, role)) {
                throw new Tp3Exception("Un autre acteur joue deja le role " + role + " dans le film " + titre + ".");
            }

            roleFilm.ajouter(nomActeur, titre, anneeSortie, role);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    public void afficherActeurDeFilm(String titre, Date anneeSortie) throws Tp3Exception, SQLException {
        if(!film.existe(titre, anneeSortie)){
            throw new Tp3Exception("Le film " + titre + " paru en " + anneeSortie + " n'existe pas.");
        }
        
        List <TuplePersonne> tuples = roleFilm.getActeurs(titre, anneeSortie);
        StringBuilder output = new StringBuilder();
        Iterator<TuplePersonne> it = tuples.iterator();
        while(it.hasNext()){
            output.append(it.next().getNom()).append(it.hasNext() ?", ":".");
        }
        System.out.println(output.toString());
    }
}
