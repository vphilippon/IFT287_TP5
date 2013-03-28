package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class RoleEpisode {

    private Connexion cx;
    private PreparedStatement stmtRoleEpisodeExiste;
    private PreparedStatement stmtAjouterRoleEpisode;
    private PreparedStatement stmtGetRoleEpisodeWithActeur;
    private PreparedStatement stmtRoleEpisodeAutreActeur;
    
    public RoleEpisode(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        stmtRoleEpisodeExiste = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleEpisode WHERE nomActeur = ? AND roleActeur = ? "
                        + "AND titreSerie = ? AND noSaison = ? "
                        + "AND noEpisode = ? AND anneeSortieSerie = ?");
        stmtRoleEpisodeAutreActeur = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleEpisode WHERE roleActeur = ? "
                        + "AND titreSerie = ? AND noSaison = ? "
                        + "AND noEpisode = ? AND anneeSortieSerie = ?");
        stmtAjouterRoleEpisode = cx.getConnection().prepareStatement(
                "INSERT INTO RoleEpisode (nomActeur, roleActeur, titreSerie," 
                        + " noSaison, noEpisode, anneeSortieSerie)" 
                        + " VALUES (?, ?, ?, ?, ?, ?)");
        stmtGetRoleEpisodeWithActeur = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleEpisode WHERE nomActeur = ?");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) throws SQLException {
        boolean retour;
        stmtRoleEpisodeExiste.setString(1,acteur);
        stmtRoleEpisodeExiste.setString(2,roleActeur);
        stmtRoleEpisodeExiste.setString(3,serieTitre);
        stmtRoleEpisodeExiste.setInt(4,noSaison);
        stmtRoleEpisodeExiste.setInt(5,noEpisode);
        stmtRoleEpisodeExiste.setDate(6,serieDate);
        ResultSet rs = stmtRoleEpisodeExiste.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }

    public void ajouter(String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) throws SQLException {
        stmtAjouterRoleEpisode.setString(1,acteur);
        stmtAjouterRoleEpisode.setString(2,roleActeur);
        stmtAjouterRoleEpisode.setString(3,serieTitre);
        stmtAjouterRoleEpisode.setInt(4,noSaison);
        stmtAjouterRoleEpisode.setInt(5,noEpisode);
        stmtAjouterRoleEpisode.setDate(6,serieDate);
        stmtAjouterRoleEpisode.executeUpdate();
    }

    public List<TupleRoleEpisode> rolesDeActeur(String nom) throws SQLException {
        List<TupleRoleEpisode> listeRoleEpisode = new ArrayList<TupleRoleEpisode>();
        stmtGetRoleEpisodeWithActeur.setString(1, nom);
        ResultSet rs = stmtGetRoleEpisodeWithActeur.executeQuery();
        while(rs.next()){
            listeRoleEpisode.add(new TupleRoleEpisode(rs.getString(1),rs.getString(2),rs.getString(3),
                    rs.getString(4),rs.getInt(5),rs.getInt(6),rs.getDate(7)));
        }
        rs.close();
        return listeRoleEpisode;
    }
    
    public boolean existeRole(String serieTitre, Date serieDate, int noSaison, 
            int noEpisode, String roleActeur) throws SQLException {
        boolean retour;
        stmtRoleEpisodeAutreActeur.setString(1,roleActeur);
        stmtRoleEpisodeAutreActeur.setString(2,serieTitre);
        stmtRoleEpisodeAutreActeur.setInt(3,noSaison);
        stmtRoleEpisodeAutreActeur.setInt(4,noEpisode);
        stmtRoleEpisodeAutreActeur.setDate(5,serieDate);
        ResultSet rs = stmtRoleEpisodeAutreActeur.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }
    
}
