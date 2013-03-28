package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personne {

    private PreparedStatement stmGetPersonne;
    private PreparedStatement stmInsertPersonne;
    private PreparedStatement stmDeletePersonne;
    private PreparedStatement stmtGetRealisateur;
    private PreparedStatement stmtGetActeurDeSerie;
    private Connexion cx;

    public Personne(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        stmGetPersonne = cx.getConnection().prepareStatement(
                "SELECT * FROM Personne WHERE nom = ?");
        stmInsertPersonne = cx.getConnection().prepareStatement(
                "INSERT INTO Personne (nom, datenaissance, lieunaissance, sexe) VALUES(?, ?, ?, ?)");
        stmDeletePersonne = cx.getConnection().prepareStatement(
                "DELETE FROM Personne WHERE nom = ?");
        stmtGetRealisateur = cx.getConnection().prepareStatement(
                "SELECT * FROM personne WHERE nom IN (SELECT DISTINCT realisateur FROM Film)");
        stmtGetActeurDeSerie = cx.getConnection().prepareStatement(
                "SELECT * FROM personne WHERE nom IN (SELECT DISTINCT nomActeur FROM RoleEpisode WHERE titreSerie = ? AND anneeSortieSerie = ?)");
    }

    public Connexion getConnexion() {
        return cx;
    }
    
    public boolean existe(String nom) throws SQLException {
        boolean personneExiste;
        stmGetPersonne.setString(1, nom);
        ResultSet rs = stmGetPersonne.executeQuery();
        personneExiste = rs.next();
        rs.close();
        return personneExiste;
    }
    
    public TuplePersonne getPersonne(String nom) throws SQLException {
        stmGetPersonne.setString(1, nom);
        ResultSet rs = stmGetPersonne.executeQuery();
        rs.next();

        TuplePersonne tuplePer = new TuplePersonne(rs.getString("nom"), rs.getDate("dateNaissance"), 
                                                   rs.getString("lieuNaissance"), rs.getInt("sexe"));
        rs.close();
        return tuplePer;
    }
    
    public void ajouter(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws SQLException {
        stmInsertPersonne.setString(1, nom);
        stmInsertPersonne.setDate(2, dateNaissance);
        stmInsertPersonne.setString(3, lieuNaissance);
        stmInsertPersonne.setInt(4, sexe);
        stmInsertPersonne.executeUpdate();
    }
    
    public int enlever(String nom) throws SQLException {
        stmDeletePersonne.setString(1, nom);
        return stmDeletePersonne.executeUpdate();
    }
    
    public List<TuplePersonne> realisateurDeFilms() throws SQLException {
        List<TuplePersonne> listeRealisateur = new ArrayList<TuplePersonne>();
        ResultSet rs = stmtGetRealisateur.executeQuery();
        while(rs.next()){
            listeRealisateur.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
        }
        rs.close();
        return listeRealisateur;
    }

    public List<TuplePersonne> acteursDeSerie(String serieTitre, Date serieDate) throws SQLException {
        List<TuplePersonne> listeActeur = new ArrayList<TuplePersonne>();
        stmtGetActeurDeSerie.setString(1, serieTitre);
        stmtGetActeurDeSerie.setDate(2, serieDate);
        ResultSet rs = stmtGetActeurDeSerie.executeQuery();
        while(rs.next()){
            listeActeur.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
        }
        rs.close();
        return listeActeur;
    }

}
