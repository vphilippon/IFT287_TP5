import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class CinematequeXMLToBD {
    
    public static final String OPERATEUR = "operateur";
    public static final String SOMME = "+";
    public static final String DIFFERENCE = "-";
    public static final String NOMBRE = "nombre";
    private List<String> pile;
    
    public CinematequeXMLToBD(String file) throws ParserConfigurationException, SAXException, IOException {
        if ("".equals(file)) {
            System.err.println("Usage: ParserSimple <fichier>");
            System.exit(1);
        }
        DefaultHandler handler = new DefaultHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new File(file), handler);
    }
    
     public static void main(String argv[]) throws ParserConfigurationException, SAXException, IOException {
        if (argv.length < 1) {
            System.err.println("Usage: ParserSimple <fichier>");
            System.exit(1);
        }
        DefaultHandler handler = new DefaultHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new File(argv[0]), handler);
     }
    
     public void startDocument() throws SAXException {
        System.out.println("Début du document");
        pile = new LinkedList<String>();
    }
     
     public void endDocument() throws SAXException {
        System.out.println("Fin du document");
        System.out.println("Valeur expression est " + pile.get(0));
    }
     
    public void startElement(String namespaceURI, String lName, String qName,
                                Attributes attrs) throws SAXException {
        pile.add(0, attrs.getValue(0));
    }
    
    public void endElement(String namespaceURI, String lName, String qName)
			throws SAXException {
        if (qName.equals("operateur")) {
            int v1 = Integer.parseInt(pile.remove(0));
            int v2 = Integer.parseInt(pile.remove(0));
            String op = pile.remove(0);
            if (op.equals(SOMME)) {
                pile.add(0, Integer.toString(v1 + v2));
            }
            else {
                pile.add(0, Integer.toString(v2 - v1));
            }
        }
    }
    
    public void characters(char buf[], int offset, int len) throws SAXException {
        System.out.print(new String(buf, offset, len));
    }
    
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("Warning : " + e);
    }

    public void error(SAXParseException e) throws SAXException {
        System.out.println("Error : " + e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("Fatal error : " + e);
    }
    
    public void convert() {
        
    }
}
