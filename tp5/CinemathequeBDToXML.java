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

import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class CinemathequeBDToXML {

    private Connexion         cx;
    private Document          document;
    private StreamResult      result;
    private PreparedStatement stmSelectPersonne;
    private PreparedStatement stmSelectFilm;
    private PreparedStatement stmSelectRoleFilm;
    private PreparedStatement stmSelectSerie;
    private PreparedStatement stmSelectEpisode;
    private PreparedStatement stmSelectRoleEpisode;

    // POUR TEST UNIQUEMENT
//    public static void main(String argv[]) throws ParserConfigurationException,
//            SAXException, IOException, SQLException {
//        if (argv.length < 4) {
//            System.err.println("Usage: CinemathequeBDToXML <nom-bd> <user-id-bd> <mot-de-passe> <fichier-xml-sortie>");
//            System.exit(1);
//        }
//
//        CinemathequeBDToXML handler = new CinemathequeBDToXML(argv[0], argv[1], argv[2],
//                argv[3]);
//        handler.convert();
//    }

    public CinemathequeBDToXML(String bd, String user, String pwd, String nomFichier)
            throws SQLException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            document = (Document) factory.newDocumentBuilder().newDocument();

            cx = new Connexion("postgres", bd, user, pwd);
            init();
            result = new StreamResult(nomFichier);

        } catch (ParserConfigurationException e) {
            System.out.println("Erreur de configuration du parser : " + e);
        }
    }

    public void convert() throws SQLException {
        try {
            Element racine = document.createElement("cinematheque");
            document.appendChild(racine);

            try {
                addPersonne(document, racine);
                addFilm(document, racine);
                addRoleFilm(document, racine);
                addSerie(document, racine);
                addEpisode(document, racine);
                addRoleEpisode(document, racine);
            } catch (SQLException e) {
                System.out.println("** Erreur lors de l'extraction : " + e.toString());
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                    "GestionCinematheque.dtd");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            System.out.println("Erreur de transformer : " + e);
        } finally {
            cx.fermer();
        }
    }

    private void init() throws SQLException {

        stmSelectPersonne = cx.getConnection().prepareStatement("SELECT * FROM personne");
        stmSelectFilm = cx.getConnection().prepareStatement("SELECT * FROM film");
        stmSelectRoleFilm = cx.getConnection().prepareStatement("SELECT * FROM roleFilm");
        stmSelectSerie = cx.getConnection().prepareStatement("SELECT * FROM serie");
        stmSelectEpisode = cx.getConnection().prepareStatement("SELECT * FROM episode");
        stmSelectRoleEpisode = cx.getConnection().prepareStatement(
                "SELECT * FROM roleEpisode");
    }

    public void addPersonne(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectPersonne.executeQuery();

        while (rs.next()) {
            Element personne = (Element) doc.createElement("personne");
            personne.setAttribute("nom", rs.getString("nom"));
            personne.setAttribute("dateNaissance", rs.getDate("dateNaissance").toString());
            personne.setAttribute("lieuNaissance", rs.getString("lieuNaissance"));
            personne.setAttribute("sexe", Integer.toString(rs.getInt("sexe")));

            racine.appendChild(personne);
        }
    }

    public void addFilm(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectFilm.executeQuery();

        while (rs.next()) {
            Element film = (Element) doc.createElement("film");
            film.setAttribute("titre", rs.getString("titre"));
            film.setAttribute("dateSortie", rs.getDate("dateSortie").toString());
            film.setAttribute("duree", Integer.toString(rs.getInt("duree")));
            film.setAttribute("realisateur", rs.getString("realisateur"));

            if (rs.getString("description") != null) {
                film.setAttribute("description", rs.getString("description"));
            }

            racine.appendChild(film);
        }
    }

    public void addRoleFilm(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectRoleFilm.executeQuery();

        while (rs.next()) {
            Element roleFilm = (Element) doc.createElement("roleFilm");
            roleFilm.setAttribute("nomActeur", rs.getString("nomActeur"));
            roleFilm.setAttribute("roleActeur", rs.getString("roleActeur"));
            roleFilm.setAttribute("filmTitre", rs.getString("filmTitre"));
            roleFilm.setAttribute("anneeSortie", rs.getDate("anneeSortie").toString());

            racine.appendChild(roleFilm);
        }
    }

    public void addSerie(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectSerie.executeQuery();

        while (rs.next()) {
            Element serie = (Element) doc.createElement("serie");
            serie.setAttribute("titre", rs.getString("titre"));
            serie.setAttribute("anneeSortie", rs.getDate("anneeSortie").toString());
            serie.setAttribute("realisateur", rs.getString("realisateur"));
            serie.setAttribute("nbSaison", Integer.toString(rs.getInt("nbSaison")));

            if (rs.getString("description") != null)
                serie.setAttribute("description", rs.getString("description"));

            racine.appendChild(serie);
        }
    }

    public void addEpisode(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectEpisode.executeQuery();

        while (rs.next()) {
            Element episode = (Element) doc.createElement("episode");
            episode.setAttribute("titre", rs.getString("titre"));
            episode.setAttribute("titreSerie", rs.getString("titreSerie"));
            episode.setAttribute("anneeSortieSerie",
                    rs.getDate("anneeSortieSerie").toString());
            episode.setAttribute("noSaison", Integer.toString(rs.getInt("noSaison")));
            episode.setAttribute("noEpisode", Integer.toString(rs.getInt("noEpisode")));
            episode.setAttribute("dateDiffusion", rs.getDate("dateDiffusion").toString());

            if (rs.getString("description") != null)
                episode.setAttribute("description", rs.getString("description"));

            racine.appendChild(episode);
        }
    }

    public void addRoleEpisode(Document doc, Element racine) throws SQLException {
        ResultSet rs = stmSelectRoleEpisode.executeQuery();

        while (rs.next()) {
            Element roleEpisode = (Element) doc.createElement("roleEpisode");
            roleEpisode.setAttribute("nomActeur", rs.getString("nomActeur"));
            roleEpisode.setAttribute("roleActeur", rs.getString("roleActeur"));
            roleEpisode.setAttribute("titreSerie", rs.getString("titreSerie"));
            roleEpisode.setAttribute("noSaison", Integer.toString(rs.getInt("noSaison")));
            roleEpisode.setAttribute("noEpisode",
                    Integer.toString(rs.getInt("noEpisode")));
            roleEpisode.setAttribute("anneeSortieSerie",
                    rs.getDate("anneeSortieSerie").toString());

            racine.appendChild(roleEpisode);
        }
    }

    public void fermer() throws SQLException {
        // fermeture de la connexion
        cx.fermer();
    }
}
