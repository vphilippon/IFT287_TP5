package tp5;

import java.io.*;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import tp5.Connexion;

public class CinematequeXMLToBD extends DefaultHandler {

    private static Connexion         conn;

    private static PreparedStatement stmtAjouterFilm;
    private static PreparedStatement stmtAjouterEpisode;
    private static PreparedStatement stmtAjouterPersonne;
    private static PreparedStatement stmtAjouterRoleEpisode;
    private static PreparedStatement stmtAjouterRoleFilm;
    private static PreparedStatement stmtAjouterSerie;

    public static void main(String argv[]) throws ParserConfigurationException,
            SAXException, IOException, SQLException {
        if (argv.length < 5) {
            System.err.println("Usage: ChargeurTableAttribut <nom-bd> <user-id-bd> <mot-de-passe> <fichier-xml-entree>");
            System.exit(1);
        }

        DefaultHandler handler = new CinematequeXMLToBD();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        conn = new Connexion("postgres", argv[1], argv[2], argv[3]);
        init();
        conn.setAutoCommit(true);
        saxParser.parse(new File(argv[4]), handler);
        conn.fermer();
    }

    private static void init() throws SQLException {
        stmtAjouterFilm = conn.getConnection().prepareStatement(
                "INSERT INTO Film (titre, dateSortie, description, duree, realisateur) VALUES (?, ?, ?, ?, ?)");
        stmtAjouterEpisode = conn.getConnection().prepareStatement(
                "INSERT INTO Episode (titre, titreSerie, anneeSortieSerie,"
                        + " noSaison, noEpisode, description, dateDiffusion)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?)");
        stmtAjouterPersonne = conn.getConnection().prepareStatement(
                "INSERT INTO Personne (nom, datenaissance, lieunaissance, sexe) VALUES(?, ?, ?, ?)");
        stmtAjouterRoleEpisode = conn.getConnection().prepareStatement(
                "INSERT INTO RoleEpisode (nomActeur, roleActeur, titreSerie,"
                        + " noSaison, noEpisode, anneeSortieSerie)"
                        + " VALUES (?, ?, ?, ?, ?, ?)");
        stmtAjouterRoleFilm = conn.getConnection().prepareStatement(
                "INSERT INTO RoleFilm (nomActeur, roleActeur, filmTitre, anneeSortie) VALUES (?, ?, ?, ?)");
        stmtAjouterSerie = conn.getConnection().prepareStatement(
                "INSERT INTO Serie (titre, anneeSortie, realisateur, description, nbSaison)"
                        + " VALUES (?, ?, ?, ?, ?)");

    }

    /**
    * Cette méthode est appelée par le parser suite à la lecture du marqueur de
    * début d'un élément.
    */
    public void startElement(String namespaceURI, String lName, String qName,
            Attributes attrs) throws SAXException {

        if (qName.equals("personne")) {
            insertPersonne(attrs);
        } else if (qName.equals("film")) {
            insertFilm(attrs);
        } else if (qName.equals("roleFilm")) {
            insertRoleFilm(attrs);
        } else if (qName.equals("serie")) {
            insertSerie(attrs);
        } else if (qName.equals("episode")) {
            insertEpisode(attrs);
        } else if (qName.equals("roleEpisode")) {
            insertRoleEpisode(attrs);
        }
    }

    private void insertPersonne(Attributes attrs) throws SQLException {
        String nom = attrs.getValue("nom");
        Date dateNaissance = Date.valueOf(attrs.getValue("dateNaissance"));
        String lieuNaissance = attrs.getValue("lieuNaissance");
        int sexe = Integer.parseInt(attrs.getValue("sexe"));

        stmtAjouterPersonne.setString(1, nom);
        stmtAjouterPersonne.setDate(2, dateNaissance);
        stmtAjouterPersonne.setString(3, lieuNaissance);
        stmtAjouterPersonne.setInt(4, sexe);
        stmtAjouterPersonne.executeUpdate();
    }

    private void insertFilm(Attributes attrs) throws SQLException {
        String titre = attrs.getValue("titre");
        Date dateSortie = Date.valueOf(attrs.getValue("dateSortie"));
        String description = attrs.getValue("description");
        int duree = Integer.parseInt(attrs.getValue("duree"));
        String realisateur = attrs.getValue("realisateur");

        stmtAjouterFilm.setString(1, titre);
        stmtAjouterFilm.setDate(2, dateSortie);
        stmtAjouterFilm.setString(3, description);
        stmtAjouterFilm.setInt(4, duree);
        stmtAjouterFilm.setString(5, realisateur);
        stmtAjouterFilm.executeUpdate();
    }

    private void insertRoleFilm(Attributes attrs) throws SQLException {
        String nomActeur = attrs.getValue("nomActeur");
        String roleActeur = attrs.getValue("roleActeur");
        String filmTitre = attrs.getValue("filmTitre");
        Date anneeSortie = Date.valueOf(attrs.getValue("anneeSortie"));

        stmtAjouterRoleFilm.setString(1, nomActeur);
        stmtAjouterRoleFilm.setString(2, roleActeur);
        stmtAjouterRoleFilm.setString(3, filmTitre);
        stmtAjouterRoleFilm.setDate(4, anneeSortie);
        stmtAjouterRoleFilm.executeUpdate();
    }

    private void insertSerie(Attributes attrs) throws SQLException {
        String titre = attrs.getValue("titre");
        Date anneeSortie = Date.valueOf(attrs.getValue("anneeSortie"));
        String realisateur = attrs.getValue("realisateur");
        String description = attrs.getValue("description");
        int nbSaison = Integer.parseInt(attrs.getValue("nbSaison"));

        stmtAjouterSerie.setString(1, titre);
        stmtAjouterSerie.setDate(2, anneeSortie);
        stmtAjouterSerie.setString(3, realisateur);
        stmtAjouterSerie.setString(4, description);
        stmtAjouterSerie.setInt(5, nbSaison);
        stmtAjouterSerie.executeUpdate();
    }

    private void insertEpisode(Attributes attrs) throws SQLException {
        String titre = attrs.getValue("titre");
        String titreSerie = attrs.getValue("titreSerie");
        Date anneeSortieSerie = Date.valueOf(attrs.getValue("anneeSortieSerie"));
        int noSaison = Integer.parseInt(attrs.getValue("noSaison"));
        int noEpisode = Integer.parseInt(attrs.getValue("noEpisode"));
        String description = attrs.getValue("description");
        Date dateDiffusion = Date.valueOf(attrs.getValue("dateDiffusion"));

        stmtAjouterEpisode.setString(1, titre);
        stmtAjouterEpisode.setString(2, titreSerie);
        stmtAjouterEpisode.setDate(3, anneeSortieSerie);
        stmtAjouterEpisode.setInt(4, noSaison);
        stmtAjouterEpisode.setInt(5, noEpisode);
        stmtAjouterEpisode.setString(6, description);
        stmtAjouterEpisode.setDate(7, dateDiffusion);
        stmtAjouterEpisode.executeUpdate();
    }

    private void insertRoleEpisode(Attributes attrs) throws SQLException {
        String nomActeur = attrs.getValue("nomActeur");
        String roleActeur = attrs.getValue("roleActeur");
        String titreSerie = attrs.getValue("titreSerie");
        int noSaison = Integer.parseInt(attrs.getValue("noSaison"));
        int noEpisode = Integer.parseInt(attrs.getValue("noEpisode"));
        Date anneeSortieSerie = Date.valueOf(attrs.getValue("anneeSortieSerie"));

        stmtAjouterRoleEpisode.setString(1, nomActeur);
        stmtAjouterRoleEpisode.setString(2, roleActeur);
        stmtAjouterRoleEpisode.setString(3, titreSerie);
        stmtAjouterRoleEpisode.setInt(4, noSaison);
        stmtAjouterRoleEpisode.setInt(5, noEpisode);
        stmtAjouterRoleEpisode.setDate(6, anneeSortieSerie);
        stmtAjouterRoleEpisode.executeUpdate();
    }

    /**
     * Cette méthode est appelée par le parser pour les avertissements
     */
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("Warning : " + e);
    }

    /**
     * Cette méthode est appelée par le parser pour les erreur non fatales
     */
    public void error(SAXParseException e) throws SAXException {
        System.out.println("Error : " + e);
    }

    /**
     * Cette méthode est appelée par le parser pour les erreur fatales
     */
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("Fatal error : " + e);
    }
}
