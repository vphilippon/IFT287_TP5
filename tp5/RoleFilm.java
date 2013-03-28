package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class RoleFilm {
    
    private Connexion cx;
    private PreparedStatement stmtRoleFilmExiste;
    private PreparedStatement stmtRoleFilmExistePourAutreActeur;
    private PreparedStatement stmtAjouteoRleFilm;
    private PreparedStatement stmtGetActeurOfFilm;
    private PreparedStatement stmtGetRoleOfActeur;

    public RoleFilm(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        stmtRoleFilmExiste = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE nomActeur = ? AND filmTitre = ?" 
                        + " AND anneeSortie = ? AND roleActeur = ?");
        stmtRoleFilmExistePourAutreActeur = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE filmTitre = ? AND anneeSortie = ? AND roleActeur = ?");
        stmtAjouteoRleFilm = cx.getConnection().prepareStatement(
                "INSERT INTO RoleFilm (nomActeur, roleActeur, filmTitre, anneeSortie) VALUES (?, ?, ?, ?)");
        stmtGetActeurOfFilm = cx.getConnection().prepareStatement(
                "SELECT * FROM Personne WHERE nom IN (SELECT nomActeur FROM RoleFilm" 
                        + " WHERE filmTitre = ? AND anneeSortie = ?)");
        stmtGetRoleOfActeur = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE nomActeur = ?");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String nomActeur, String filmTitre, Date anneeSortie, 
            String role) throws SQLException {
        boolean retour;
        stmtRoleFilmExiste.setString(1,nomActeur);
        stmtRoleFilmExiste.setString(2,filmTitre);
        stmtRoleFilmExiste.setDate(3,anneeSortie);
        stmtRoleFilmExiste.setString(4,role);
        ResultSet rs = stmtRoleFilmExiste.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }
    
    public boolean existe(String filmTitre, Date anneeSortie, String role) throws SQLException {
        boolean retour;
        stmtRoleFilmExistePourAutreActeur.setString(1,filmTitre);
        stmtRoleFilmExistePourAutreActeur.setDate(2,anneeSortie);
        stmtRoleFilmExistePourAutreActeur.setString(3,role);
        ResultSet rs = stmtRoleFilmExistePourAutreActeur.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }

    public void ajouter(String nomActeur, String filmTitre, Date anneeSortie, String role) throws SQLException {
        stmtAjouteoRleFilm.setString(1,nomActeur);
        stmtAjouteoRleFilm.setString(2,role);
        stmtAjouteoRleFilm.setString(3,filmTitre);
        stmtAjouteoRleFilm.setDate(4,anneeSortie);
        stmtAjouteoRleFilm.executeUpdate();
    }

    public boolean aDesRoles(String titre, Date anneeSortie) throws SQLException {
        boolean retour;
        stmtGetActeurOfFilm.setString(1,titre);
        stmtGetActeurOfFilm.setDate(2,anneeSortie);
        ResultSet rs = stmtGetActeurOfFilm.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }
    
    public List<TuplePersonne> getActeurs(String titre, Date anneeSortie) throws SQLException {
        List<TuplePersonne> listePersonne = new ArrayList<TuplePersonne>();
        stmtGetActeurOfFilm.setString(1,titre);
        stmtGetActeurOfFilm.setDate(2,anneeSortie);
        ResultSet rs = stmtGetActeurOfFilm.executeQuery();
        while(rs.next()){
            listePersonne.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
        }
        rs.close();
        return listePersonne;
    }

    public List<TupleRoleFilm> rolesDeActeur(String nom) throws SQLException {
        List<TupleRoleFilm> listeRole = new ArrayList<TupleRoleFilm>();
        stmtGetRoleOfActeur.setString(1,nom);
        ResultSet rs = stmtGetRoleOfActeur.executeQuery();
        while(rs.next()){
            listeRole.add(new TupleRoleFilm(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDate(4)));
        }
        rs.close();
        return listeRole;
    }
}
