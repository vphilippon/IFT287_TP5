package tp3;

import java.sql.*;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 * 
 * <pre>
 * Marc Frappier - 83 427 378
 * Universite de Sherbrooke
 * version 2.0 - 13 novembre 2004
 * ift287 - exploitation de bases de donnees
 * 
 * Modifie par Vincent Ducharme - 30 janvier 2013
 * 
 * Ce programme permet d'ouvrir une connexion avec une BD via JDBC.
 * La methode serveursSupportes() indique les serveurs supportes.
 * 
 * Pre-condition
 *   le driver JDBC approprie doit etre accessible.
 * 
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et serialisable, 
 *   (s'il est supporte par le serveur).
 * </pre>
 */
public class Connexion {

	private Connection conn;

	/**
	 * Ouverture d'une connexion en mode autocommit false et serialisable (si
	 * supporte)
	 * 
	 * @param serveur SQL de la BD
	 * @bd nom de la base de donnees
	 * @user userid sur le serveur SQL
	 * @pass mot de passe sur le serveur SQL
	 */
	public Connexion(String serveur, String bd, String user, String pass)
			throws SQLException {
		Driver d;
		try {
			if (serveur.equals("local")) {
				d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@127.0.0.1:1521:" + bd, user, pass);
			}
			if (serveur.equals("sti")) {
				d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@io.usherbrooke.ca:1521:" + bd, user,
						pass);
			} else if (serveur.equals("postgres")) {
				d = (Driver) Class.forName("org.postgresql.Driver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection("jdbc:postgresql:" + bd,
						user, pass);
			} else
			{
				throw new Tp3Exception("Serveur non reconnu");
			}

			// mettre en mode de commit manuel
			conn.setAutoCommit(false);

			// mettre en mode serialisable si possible
			// (plus haut niveau d'integrite l'acces concurrent aux donnees)
			DatabaseMetaData dbmd = conn.getMetaData();
			if (dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				System.out.println("Ouverture de la connexion en mode s�rialisable :\n"
								+ "Estampille "
								+ System.currentTimeMillis()
								+ " " + conn);
			} else
				System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
								+ "Heure "
								+ System.currentTimeMillis()
								+ " "
								+ conn);
		}// try
		catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new SQLException("JDBC Driver non instanci�");
		}
	}

	/**
	 * fermeture d'une connexion
	 */
	public void fermer() throws SQLException {
		conn.rollback();
		conn.close();
		System.out.println("Connexion fermee" + " " + conn);
	}

	/**
	 * commit
	 */
	public void commit() throws SQLException {
		conn.commit();
	}

	public void setIsolationReadCommited() throws SQLException {
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	/**
	 * rollback
	 */
	public void rollback() throws SQLException {
		conn.rollback();
	}

	/**
	 * retourne la Connection jdbc
	 */
	public Connection getConnection() {
		return conn;
	}

	public void setAutoCommit(boolean m) throws SQLException {
		conn.setAutoCommit(m);
	}

	/**
	 * Retourne la liste des serveurs supportes par ce gestionnaire de
	 * connexions
	 */
	public static String serveursSupportes() {
		return "local : Oracle installe localement 127.0.0.1\n"
				+ "sti   : Oracle installe au Service des technologies de l'information\n"
				+ "postgres : Postgres installe localement\n";
	}
}// Classe Connexion
