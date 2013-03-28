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
    
    private static Connexion conn;
    
    private static PreparedStatement stmtAjouterFilm;
    private static PreparedStatement stmtAjouterEpisode;
    private static PreparedStatement stmtAjouterPersonne;
    private static PreparedStatement stmtAjouterRoleEpisode;
    private static PreparedStatement stmtAjouterRoleFilm;
    private static PreparedStatement stmtAjouterSerie;

    private static Statement stmt;

    private static int niveau = -1;
    
    public static void main(String argv[]) throws ParserConfigurationException,
			SAXException, IOException, SQLException {
		if (argv.length < 5) {
			System.err
					.println("Usage: ChargeurTableAttribut <fichier> <serveur> <bd> <userid> <pw> [-v]");
			System.exit(1);
		}

		DefaultHandler handler = new CinematequeXMLToBD();
		// DefaultHandler handler = new DefaultHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		if (argv.length == 6 && argv[5].equals("-v"))
			factory.setValidating(true);
		SAXParser saxParser = factory.newSAXParser();
		conn = new Connexion(argv[1], argv[2], argv[3], argv[4]);
                init();
		conn.setAutoCommit(true);
		stmt = conn.getConnection().createStatement();
		saxParser.parse(new File(argv[0]), handler);
		conn.fermer();
	}
    
    private static void init() throws SQLException{
        stmtAjouterFilm = conn.getConnection().prepareStatement(
                "INSERT INTO Film (titre, dateSortie, realisateur) VALUES (?, ?, ?)");
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
		             
		if (qName.equals("personne")){
                    insertPersonne(attrs);
                } else if(qName.equals("film")) {
                    insertFilm(attrs);
                } else if(qName.equals("roleFilm")) {
                   insertRoleFilm(attrs);
                } else if(qName.equals("serie")) {
                    insertSerie(attrs);
                } else if(qName.equals("episode")) {
                   insertEpisode(attrs);
                } else if(qName.equals("roleEpisode")) {
                    insertRoleEpisode(attrs);
                }

	}

	/**
	 * Cette méthode formatte un énoncé SQL insert pour insérer un
	 * enregistrement dans la table.
	 */
	private static String formatterInsert(String nomTable,
			DefinitionTable defTable, Attributes attrs) {
		StringBuffer enonceInsert = new StringBuffer("insert into " + nomTable
				+ " (");
		enonceInsert.append(attrs.getQName(0));
		for (int i = 1; i < attrs.getLength(); i++)
			enonceInsert.append("," + attrs.getQName(i));
		enonceInsert.append(") values (");
		enonceInsert.append(formatterValeur(attrs.getQName(0), attrs
				.getValue(0), defTable));
		for (int i = 1; i < attrs.getLength(); i++)
			enonceInsert.append(","
					+ formatterValeur(attrs.getQName(i), attrs.getValue(i),
							defTable));
		enonceInsert.append(")");
		return enonceInsert.toString();
	}

	/**
	 * Cette méthode formatte la valeur d'un attribut dans l'énoncé insert. Elle
	 * utilise la définition de la table pour obtenir le type de l'attribut.
	 */
	private static String formatterValeur(String nomAttribut,
			String valeurAttribut, DefinitionTable defTable) {
		int typeAttribut = defTable.getTypeAttribut(nomAttribut);
		if (typeAttribut == Constantes.CHAINE)
			return "'" + valeurAttribut + "'";
		else if (typeAttribut == Constantes.NOMBRE)
			return valeurAttribut;
		else if (typeAttribut == Constantes.DATE)
			return "to_date('" + valeurAttribut + "','YYYY-MM-DD')";
		else
			return "";
	}

	/**
	 * Cette méthode est appelée par le parser suite à la lecture du marqueur de
	 * la fin d'un élément.
	 */
	public void endElement(String namespaceURI, String lName, String qName)
			throws SAXException {
		niveau = niveau - 1;
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