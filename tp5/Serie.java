package tp5;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Serie {

    private Connexion cx;
    private PreparedStatement stmtSerieExiste;
    private PreparedStatement stmtAjouterSerie;
    private PreparedStatement stmtSerieDeRealisateur;
    private PreparedStatement stmtSerieAvecActeur;
    
    public Serie(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        stmtSerieExiste = cx.getConnection().prepareStatement(
                "SELECT * FROM Serie WHERE titre = ? AND anneeSortie = ?");
        stmtAjouterSerie = cx.getConnection().prepareStatement(
                "INSERT INTO Serie (titre, anneeSortie, realisateur, description, nbSaison)" 
                        + " VALUES (?, ?, ?, ?, ?)");
        stmtSerieDeRealisateur = cx.getConnection().prepareStatement(
                "SELECT * FROM Serie WHERE realisateur = ?");
        stmtSerieAvecActeur = cx.getConnection().prepareStatement(
                "SELECT * FROM Serie WHERE titre IN" 
                        + " (SELECT titreSerie FROM RoleEpisode WHERE nomActeur = ?)");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate) throws SQLException {
        boolean retour;
        stmtSerieExiste.setString(1,serieTitre);
        stmtSerieExiste.setDate(2,serieDate);
        ResultSet rs = stmtSerieExiste.executeQuery();
        retour = rs.next();
        rs.close();
        return retour;
    }
    
    // Surcharge de la methode pour garder la possibilité d'ajouter une description
    // pour une serie et le nbSaison
    public void ajouter(String titre, Date dateSortie, String realisateur, 
            String description, int nbSaison) throws SQLException {
        stmtAjouterSerie.setString(1, titre);
        stmtAjouterSerie.setDate(2, dateSortie);
        stmtAjouterSerie.setString(3, realisateur);
        stmtAjouterSerie.setString(4, description);
        stmtAjouterSerie.setInt(5, nbSaison);
        stmtAjouterSerie.executeUpdate();            
    }
    
    // Surcharge de la methode pour respecter les formats d'entree du fichier
    public void ajouter(String titre, Date dateSortie, String realisateur) throws SQLException {
        stmtAjouterSerie.setString(1, titre);
        stmtAjouterSerie.setDate(2, dateSortie);
        stmtAjouterSerie.setString(3, realisateur);
        stmtAjouterSerie.setString(4, ""); //valeur par defaut
        stmtAjouterSerie.setInt(5, 1); //valeur par defaut
        stmtAjouterSerie.executeUpdate();
    }

    public List<TupleSerie> serieDeRealisateur(String nom) throws SQLException {
        List<TupleSerie> listeSerie = new ArrayList<TupleSerie>();
        stmtSerieDeRealisateur.setString(1,nom);
        ResultSet rs = stmtSerieDeRealisateur.executeQuery();
        while(rs.next()){
            listeSerie.add(new TupleSerie(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4), rs.getInt(5)));
        }
        rs.close();
        return listeSerie;
    }
    
    public List<TupleSerie> serieAvecActeur(String nom) throws SQLException {
        List<TupleSerie> listeSerie = new ArrayList<TupleSerie>();
        stmtSerieAvecActeur.setString(1,nom);
        ResultSet rs = stmtSerieAvecActeur.executeQuery();
        while(rs.next()){
            listeSerie.add(new TupleSerie(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4), rs.getInt(5)));
        }
        rs.close();
        return listeSerie;
    }

    public TupleSerie getSerie(String titre, Date anneeSortie) throws SQLException
    {
        TupleSerie laSerie;
        
        stmtSerieExiste.setString(1, titre);
        stmtSerieExiste.setDate(2, anneeSortie);
        
        ResultSet rs = stmtSerieExiste.executeQuery();
        rs.next();
        
        laSerie = new TupleSerie(rs.getString("titre"), rs.getDate("anneeSortie"), rs.getString("realisateur"), 
                                 rs.getString("description"), rs.getInt("nbSaison"));
        rs.close();
        return laSerie;
    }

}
