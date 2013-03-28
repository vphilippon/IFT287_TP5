package tp3;
import java.sql.*;

// Copié du fichier GestionBibliotheque.java fournit sur le site: http://pages.usherbrooke.ca/vducharme/ift287/
/**
  * Ouvre une connexion avec la BD relationnelle et
  * alloue les gestionnaires de transactions et de tables.
  * <pre>
  * 
  * @param serveur SQL
  * @param bd nom de la bade de données
  * @param user user id pour établir une connexion avec le serveur SQL
  * @param pwd mot de passe pour le user id
  *</pre>
  */
public class GestionTp3 {
    public Connexion cx;
    public Film film;
    public Personne personne;
    public RoleFilm roleFilm;
    public Serie serie;
    public Episode episode;
    public RoleEpisode roleEpisode;
    public GestionFilm gestionFilm;
    public GestionPersonne gestionPersonne;
    public GestionSerie gestionSerie;
    
    public GestionTp3(String serveur, String bd, String user, String pwd) throws Tp3Exception, SQLException{
        cx = new Connexion(serveur,bd,user,pwd);
        film = new Film(cx);
        personne = new Personne(cx);
        roleFilm = new RoleFilm(cx);
        serie = new Serie(cx);
        episode = new Episode(cx);
        roleEpisode = new RoleEpisode(cx);
        gestionFilm = new GestionFilm(film, personne, roleFilm);
        gestionPersonne = new GestionPersonne(personne, film, roleFilm, serie, roleEpisode);
        gestionSerie = new GestionSerie(serie, episode, personne, roleEpisode);
    }
    
    public void fermer() throws SQLException
    {
        // fermeture de la connexion
        cx.fermer();
    }
}
