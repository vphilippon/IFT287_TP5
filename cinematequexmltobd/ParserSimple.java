package cinematequexmltobd;

/** *ParserSimple.java
 *
 * Marc Frappier
 * Université de Sherbrooke
 *
 *Permet de lire un document XML et d'afficher ses éléments et
 *les attributs des éléments
 *Les méthodes de cette classes sont appelées par le SAXParser.
 *Paramètres:0- nom du document XML
 *           1- validation du fichier XML (optionnel, valeur = -v)
 */

import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class ParserSimple extends DefaultHandler {
	public static void main(String argv[]) throws ParserConfigurationException,
			SAXException, IOException {
		if (argv.length < 1) {
			System.err.println("Usage: ParserSimple <fichier>");
			System.exit(1);
		}

		DefaultHandler handler = new DefaultHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		if (argv.length == 2 && argv[1].equals("-v"))
			factory.setValidating(true);
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(new File(argv[0]), handler);
	}

	/**
	 * Cette méthode est appelée par le parser après la lecture du début du
	 * document.
	 */
	public void startDocument() throws SAXException {
		System.out.println("Début du document");
	}

	/**
	 * Cette méthode est appelée par le parser à la fin du document.
	 */
	public void endDocument() throws SAXException {
		System.out.println("Fin du document");
	}

	/**
	 * Cette méthode est appelée par le parser suite à la lecture de la balise
	 * de début d'un élément.
	 */
	public void startElement(String namespaceURI, String lName, String qName,
			Attributes attrs) throws SAXException {
		System.out.print("<" + qName + ">");
		if (attrs.getLength() > 0) {
			System.out.println("Liste des attributs");
			for (int i = 0; i < attrs.getLength(); i++) {
				System.out.print("attribut " + attrs.getQName(i) + " = \"");
				System.out.println(attrs.getValue(i) + "\"");
			}
			System.out.println("Fin de la liste des attributs");
		}
	}

	/**
	 * Cette méthode est appelée par le parser suite à la lecture de la balise
	 * de la fin d'un élément.
	 */
	public void endElement(String namespaceURI, String lName, String qName)
			throws SAXException {
		System.out.println("</" + qName + ">");
	}

	/**
	 * Cette méthode est appelée par le parser suite à la lecture des caractères
	 * entre le balise de début et le balise de fin d'un élément
	 */
	public void characters(char buf[], int offset, int len) throws SAXException {
		System.out.print(new String(buf, offset, len));
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
