/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */

import java.sql.*;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import org.w3c.dom.*;

public class CinemathequeBDToXML {
    
    private Connexion cx;
    private PreparedStatement stmSelectPersonne;
    private PreparedStatement stmSelectFilm;
    private PreparedStatement stmSelectRoleFilm;
    private PreparedStatement stmSelectSerie;
    private PreparedStatement stmSelectEpisode;
    private PreparedStatement stmSelectRoleEpisode;
    
    
    public CinemathequeBDToXML(String bd, String  user, String pwd, String nomFichier) throws Exception{
        try 
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = (Document) factory.newDocumentBuilder().newDocument();
            
            cx = new Connexion("postgres", bd, user, pwd);   
            init();
            
            Element racine = document.createElement("cinematheque");
            document.appendChild(racine);
            
            addPersonne(document, racine);
            addFilm(document, racine);
            addRoleFilm(document, racine);
            addSerie(document, racine);
            addEpisode(document, racine);
            addRoleEpisode(document, racine);
            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "GestionCinematheque.dtd");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);          
            StreamResult result = new StreamResult(nomFichier);
            transformer.transform(source, result);

        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void init() throws Exception{
        
        stmSelectPersonne = cx.getConnection().prepareStatement(
                "SELECT * FROM personne");
        
        stmSelectFilm = cx.getConnection().prepareStatement(
                "SELECT * FROM film");
        
        stmSelectRoleFilm = cx.getConnection().prepareStatement(
                "SELECT * FROM roleFilm");
        
        stmSelectSerie = cx.getConnection().prepareStatement(
                "SELECT * FROM serie");
        
        stmSelectEpisode = cx.getConnection().prepareStatement(
                "SELECT * FROM episode");
        
        stmSelectRoleEpisode = cx.getConnection().prepareStatement(
                "SELECT * FROM roleEpisode");
        
    }
    
    public void addPersonne(Document doc, Element racine) throws Exception{
        
        // TODO: ecrire personne dans xml
        ResultSet rs = stmSelectPersonne.executeQuery();
        
        while(rs.next())
        {
            Element personne = (Element) doc.createElement("personne");
            personne.setAttribute("nom", rs.getString("nom"));
            personne.setAttribute("dateNaissance", rs.getDate("dateNaissance").toString());
            personne.setAttribute("lieuNaissance", rs.getString("lieuNaissance"));
            personne.setAttribute("sexe", Integer.toString(rs.getInt("sexe")));     
            
            racine.appendChild(personne);
        }
    }
    
    public void addFilm(Document doc, Element racine) throws Exception{
        ResultSet rs = stmSelectFilm.executeQuery();
        
        while(rs.next())
        {
            Element film = (Element) doc.createElement("film");
            film.setAttribute("titre", rs.getString("titre"));
            film.setAttribute("dateSortie", rs.getDate("dateSortie").toString());
            film.setAttribute("duree", Integer.toString(rs.getInt("duree")));
            film.setAttribute("realisateur", rs.getString("realisateur"));
            
            if(rs.getString("description") != null)
                film.setAttribute("description", rs.getString("description"));
            
            racine.appendChild(film);
        }
    }
    
    public void addRoleFilm(Document doc, Element racine) throws Exception{
        ResultSet rs = stmSelectRoleFilm.executeQuery();
        
        while(rs.next())
        {
            Element roleFilm = (Element) doc.createElement("roleFilm");
            roleFilm.setAttribute("nomActeur", rs.getString("nomActeur"));
            roleFilm.setAttribute("roleActeur", rs.getString("roleActeur"));
            roleFilm.setAttribute("filmTitre", rs.getString("filmTitre"));
            roleFilm.setAttribute("anneeSortie", rs.getDate("anneeSortie").toString());
            
            racine.appendChild(roleFilm);
        }
    }
    
    public void addSerie(Document doc, Element racine) throws Exception{
        ResultSet rs = stmSelectSerie.executeQuery();
        
        while(rs.next())
        {
            Element serie = (Element) doc.createElement("serie");
            serie.setAttribute("titre", rs.getString("titre"));
            serie.setAttribute("anneeSortie", rs.getDate("anneeSortie").toString());
            serie.setAttribute("realisateur", rs.getString("realisateur"));
            serie.setAttribute("duree", Integer.toString(rs.getInt("nbSaison")));
            
            if(rs.getString("description") != null)
                serie.setAttribute("description", rs.getString("description"));
            
            racine.appendChild(serie);
        }
    }
    
    public void addEpisode(Document doc, Element racine) throws Exception{
        ResultSet rs = stmSelectEpisode.executeQuery();
        
        while(rs.next())
        {
            Element episode = (Element) doc.createElement("episode");
            episode.setAttribute("titre", rs.getString("titre"));
            episode.setAttribute("titreSerie", rs.getString("titreSerie"));
            episode.setAttribute("anneeSortieSerie", rs.getDate("anneeSortieSerie").toString());
            episode.setAttribute("noSaison", Integer.toString(rs.getInt("noSaison")));
            episode.setAttribute("noEpisode", Integer.toString(rs.getInt("noEpisode")));
            episode.setAttribute("dateDiffusion", rs.getDate("dateDiffusion").toString());
            
            if(rs.getString("description") != null)
                episode.setAttribute("description", rs.getString("description"));
            
            racine.appendChild(episode);
        }
    }
    
    public void addRoleEpisode(Document doc, Element racine) throws Exception{
        ResultSet rs = stmSelectRoleEpisode.executeQuery();
        
        while(rs.next())
        {
            Element roleEpisode = (Element) doc.createElement("roleEpisode");
            roleEpisode.setAttribute("nomActeur", rs.getString("nomActeur"));
            roleEpisode.setAttribute("roleActeur", rs.getString("roleActeur"));
            roleEpisode.setAttribute("titreSerie", rs.getString("titreSerie"));
            roleEpisode.setAttribute("noSaison", Integer.toString(rs.getInt("noSaison")));
            roleEpisode.setAttribute("noEpisode", Integer.toString(rs.getInt("noEpisode")));
            roleEpisode.setAttribute("anneeSortieSerie", rs.getDate("anneeSortieSerie").toString());
            
            racine.appendChild(roleEpisode);
        }
    }
     
    public void fermer() throws SQLException
    {
        // fermeture de la connexion
        cx.fermer();
    }
}
