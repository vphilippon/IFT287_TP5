package tp3;

/*
 * Projet : Tp3
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
import java.text.ParseException;
import java.util.StringTokenizer;

public class Main {

    private static GestionTp3 gestionTp3;

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java tp3.Main <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        try {
            gestionTp3 = new GestionTp3("postgres", args[0], args[1], args[2]);
            
            if (args.length > 3)
            {
                BufferedReader reader = ouvrirFichier(args);
                String transaction = lireTransaction(reader);
                while (!finTransaction(transaction)) {
                    executerTransaction(transaction);
                    transaction = lireTransaction(reader);
                }
            }
            else
            {
                String ligneLue = "";
                while(!finTransaction(ligneLue))
                {
                    InputStreamReader lecteur = new InputStreamReader(System.in); 
                    BufferedReader entree = new BufferedReader(lecteur); 
                    ligneLue = entree.readLine(); 
                    
                    if(!finTransaction(ligneLue))
                        executerTransaction(ligneLue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            gestionTp3.fermer();
        }
    }
    
    /**
     * Decodage et traitement d'une transaction :
     * @throws Exception 
     */
    static void executerTransaction(String transaction) throws Exception {
        try {
            System.out.println(transaction);
            // decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens()) {
                String command = tokenizer.nextToken();
                if ("ajoutPersonne".startsWith(command)){
                    gestionTp3.gestionPersonne.ajoutPersonne(readString(tokenizer) /* nom */,
                            readDate(tokenizer) /* dateNaissance */,
                            readString(tokenizer) /* lieuNaissance */,
                            readInt(tokenizer) /* sexe */);
                    
                }else if ("supPersonne".startsWith(command)){
                    gestionTp3.gestionPersonne.supprimerPersonne(readString(tokenizer) /* nom */);
                
                }else if ("ajoutFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* realisateur */);
                
                }else if ("supFilm".startsWith(command)){
                    gestionTp3.gestionFilm.supprimerFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("ajoutDescFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutDescFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* description */,
                            readInt(tokenizer) /* duree */);
                
                }else if ("ajoutActeurFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutActeurFilm(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* nom */,
                            readString(tokenizer) /* role */);
                
                }else if ("ajoutSerie".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutSerie(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* nom realisateur */);
                
                }else if ("ajoutEpisode".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutEpisode(
                            readString(tokenizer) /* titre episode */,
                            readString(tokenizer) /* titre serie */,
                            readDate(tokenizer) /* annee serie */,
                            readInt(tokenizer) /* no saison */,
                            readInt(tokenizer) /* no episode */,
                            readString(tokenizer) /* description */,
                            readDate(tokenizer) /* date episode */);
                
                }else if ("ajoutActeurEpisode".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutRoleAEpisode(
                            readString(tokenizer) /* titre serie */,
                            readDate(tokenizer) /* annee serie */,
                            readInt(tokenizer) /* no saison */,
                            readInt(tokenizer) /* no episode */,
                            readString(tokenizer) /* nom acteur */,
                            readString(tokenizer) /* Role de l'acteur */);
                
                }else if ("listeActeursSerie".startsWith(command)){
                    gestionTp3.gestionSerie.afficherActeursSerie(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("listeSerieActeur".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherSerieAvecActeur(
                            readString(tokenizer) /* nom */);
                
                }else if ("listeRealisateurs".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherRealisateur();
                
                }else if ("listeActeursFilm".startsWith(command)){
                    gestionTp3.gestionFilm.afficherActeurDeFilm(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("listeFilmsActeur".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherFilmDeActeur(
                            readString(tokenizer) /* nom */);
                
                }else{
                    System.out.println(" : Transaction non reconnue");
                }
            }
        } catch (Tp3Exception e) {
            System.out.println("** " + e.toString());
        }
    }

    /** Les methodes suivantes n'ont pas besoin d'etre modifiees */

    public static BufferedReader ouvrirFichier(String[] args)
            throws FileNotFoundException {
        if (args.length < 4)
            // lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream(args[3])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction) {
        // fin de fichier atteinte
        if (transaction == null || transaction.equals("exit"))
            return true;
        else
            return false;
    }

    /** lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("autre parametre attendu");
    }

    /**
     * lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                return Integer.valueOf(token).intValue();
            } catch (NumberFormatException e) {
                throw new Exception("Nombre attendu a la place de \"" + token
                        + "\"");
            }
        } else
            throw new Exception("autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();

            try {
                Date dt = (Date) FormatDate.convertirDate(token);
                return dt;
            } catch (ParseException e) {
                throw new Tp3Exception("Date en format YYYY-MM-DD attendue à la place  de \"" +
                  token + "\"");
            }
            
        } else {
            throw new Exception("autre parametre attendu");
        }
    }

    public static boolean isStringNotEmpty(String s) {
        return (s != null && s.length() > 0);
    }

}