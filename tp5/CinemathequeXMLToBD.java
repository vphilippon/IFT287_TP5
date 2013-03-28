package tp5;

/*
 * Projet : Tp5
 *
 * Membres :
 * - Guillaume Harvey 12 059 595
 * - Kevin Labrie 12 113 777
 * - Vincent Philippon 12 098 838
 * - Mathieu Larocque 10 129 032
 * 
 */

import java.io.*;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;


public class CinemathequeXMLToBD extends DefaultHandler {

    private SAXParser  saxParser;
    private File       fichier;
    private GestionTp5 gestionTp5;

    // POUR TEST UNIQUEMENT
//    public static void main(String argv[]) throws ParserConfigurationException,
//            SAXException, IOException, SQLException {
//        if (argv.length < 4) {
//            System.err.println("Usage: CinemathequeXMLToBD <nom-bd> <user-id-bd> <mot-de-passe> <fichier-xml-entree>");
//            System.exit(1);
//        }
//
//        CinemathequeXMLToBD handler = new CinemathequeXMLToBD(argv[0], argv[1], argv[2], argv[3]);
//        handler.convert();
//    }

    public CinemathequeXMLToBD(String bd, String user, String mdp, String fich)
            throws SQLException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            saxParser = factory.newSAXParser();
            gestionTp5 = new GestionTp5("postgres", bd, user, mdp);
            fichier = new File(fich);
        } catch (SAXException e) {
            System.out.println("Erreur du parser : " + e);
        } catch (ParserConfigurationException e) {
            System.out.println("Erreur de configuration du parser : " + e);
        } catch (Tp5Exception e) {
            System.out.println("** " + e.toString());
        }
    }

    public void convert() throws SQLException {
        try {
            saxParser.parse(fichier, this);
            gestionTp5.fermer();
        } catch (IOException e) {
            System.out.println("Erreur a la lecture du fichier : " + e);
        } catch (SAXException e) {
            System.out.println("Erreur du parser : " + e);
        }
    }

    /**
    * Cette méthode est appelée par le parser suite à la lecture du marqueur de
    * début d'un élément.
    */
    public void startElement(String namespaceURI, String lName, String qName,
            Attributes attrs) throws SAXException {
        try {
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
        } catch (Tp5Exception e) {
            System.out.println("** " + e.toString());
        } catch (Exception e) {
            System.out.println("ERREUR ANORMALE : " + e.toString());
        }
    }

    private void insertPersonne(Attributes attrs) throws Exception {
        String nom = attrs.getValue("nom");
        Date dateNaissance = new Date(FormatDate.convertirDate(
                attrs.getValue("dateNaissance")).getTime());
        String lieuNaissance = attrs.getValue("lieuNaissance");
        int sexe = Integer.parseInt(attrs.getValue("sexe"));

        gestionTp5.gestionPersonne.ajoutPersonne(nom, dateNaissance, lieuNaissance, sexe);
    }

    private void insertFilm(Attributes attrs) throws Exception {
        String titre = attrs.getValue("titre");
        Date dateSortie = new Date(
                FormatDate.convertirDate(attrs.getValue("dateSortie")).getTime());
        String description = attrs.getValue("description");
        int duree = Integer.parseInt(attrs.getValue("duree"));
        String realisateur = attrs.getValue("realisateur");

        gestionTp5.gestionFilm.ajoutFilm(titre, dateSortie, realisateur);
        gestionTp5.gestionFilm.ajoutDescFilm(titre, dateSortie, description, duree);
    }

    private void insertRoleFilm(Attributes attrs) throws Exception {
        String nomActeur = attrs.getValue("nomActeur");
        String roleActeur = attrs.getValue("roleActeur");
        String filmTitre = attrs.getValue("filmTitre");
        Date anneeSortie = new Date(FormatDate.convertirDate(
                attrs.getValue("anneeSortie")).getTime());

        gestionTp5.gestionFilm.ajoutActeurFilm(filmTitre, anneeSortie, nomActeur,
                roleActeur);
    }

    private void insertSerie(Attributes attrs) throws Exception {
        String titre = attrs.getValue("titre");
        Date anneeSortie = new Date(FormatDate.convertirDate(
                attrs.getValue("anneeSortie")).getTime());
        String realisateur = attrs.getValue("realisateur");
        String description = attrs.getValue("description"); // Jamais utiliser
        int nbSaison = Integer.parseInt(attrs.getValue("nbSaison")); // Jamais utiliser

        gestionTp5.gestionSerie.ajoutSerie(titre, anneeSortie, realisateur);
    }

    private void insertEpisode(Attributes attrs) throws Exception {
        String titre = attrs.getValue("titre");
        String titreSerie = attrs.getValue("titreSerie");
        Date anneeSortieSerie = new Date(FormatDate.convertirDate(
                attrs.getValue("anneeSortieSerie")).getTime());
        int noSaison = Integer.parseInt(attrs.getValue("noSaison"));
        int noEpisode = Integer.parseInt(attrs.getValue("noEpisode"));
        String description = attrs.getValue("description");
        Date dateDiffusion = new Date(FormatDate.convertirDate(
                attrs.getValue("dateDiffusion")).getTime());

        gestionTp5.gestionSerie.ajoutEpisode(titre, titreSerie, anneeSortieSerie,
                noSaison, noEpisode, description, dateDiffusion);
    }

    private void insertRoleEpisode(Attributes attrs) throws Exception {
        String nomActeur = attrs.getValue("nomActeur");
        String roleActeur = attrs.getValue("roleActeur");
        String titreSerie = attrs.getValue("titreSerie");
        int noSaison = Integer.parseInt(attrs.getValue("noSaison"));
        int noEpisode = Integer.parseInt(attrs.getValue("noEpisode"));
        Date anneeSortieSerie = new Date(FormatDate.convertirDate(
                attrs.getValue("anneeSortieSerie")).getTime());

        gestionTp5.gestionSerie.ajoutRoleAEpisode(titreSerie, anneeSortieSerie, noSaison,
                noEpisode, nomActeur, roleActeur);
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
