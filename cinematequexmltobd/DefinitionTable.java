package cinematequexmltobd;
import java.sql.*;
import java.util.*;
/**
 *<pre>
 *Cette classe permet d'obtenir les attributs formant une table.
 *Le type d'un attribut est défini dans la classe Constantes.
 *
 * Marc Frappier - 22 janvier 2002
 * Université de Sherbrooke
 *</pre>
 */
public class DefinitionTable {

Map attMap;

/**
 *Création d'une définition de table (liste d'attributs avec leurs types)
 */
public DefinitionTable(Connection conn, String nomTable)
  throws Exception, SQLException
{
boolean tableExiste = false;
attMap = new HashMap();
ResultSet rset = conn.getMetaData().getColumns(null,null,nomTable,null);
while (rset.next ())
    {
    attMap.put(rset.getString(4).toUpperCase(),decodeSQLType(rset.getInt(5)));
    tableExiste = true;
    }
if (!tableExiste)
  throw new Exception("Table inexistante");
}

    public DefinitionTable(Object connection, String qName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
/**
 *Retourne le type d'un attribut. La valeur du type est
 *une des 5 constantes de la classes Constantes.
 */
public int getTypeAttribut(String nomAttribut)
{
return ((Integer) attMap.get(nomAttribut.toUpperCase())).intValue();
}

private static Integer decodeSQLType(int SQLType)
{
switch (SQLType)
  {
  case java.sql.Types.BIGINT : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.BINARY : return new Integer(Constantes.INCONNU);
  case java.sql.Types.BIT : return new Integer(Constantes.INCONNU);
  case java.sql.Types.CHAR : return new Integer(Constantes.CHAINE);
  case java.sql.Types.DATE : return new Integer(Constantes.DATE);
  case java.sql.Types.DECIMAL : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.DOUBLE : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.FLOAT : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.INTEGER : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.LONGVARBINARY : return new Integer(Constantes.INCONNU);
  case java.sql.Types.LONGVARCHAR : return new Integer(Constantes.CHAINE);
  case java.sql.Types.NULL : return new Integer(Constantes.INCONNU);
  case java.sql.Types.NUMERIC : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.OTHER : return new Integer(Constantes.INCONNU);
  case java.sql.Types.REAL : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.SMALLINT : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.TIME : return new Integer(Constantes.DATE);
  case java.sql.Types.TIMESTAMP : return new Integer(Constantes.DATE);
  case java.sql.Types.TINYINT : return new Integer(Constantes.NOMBRE);
  case java.sql.Types.VARBINARY : return new Integer(Constantes.INCONNU);
  case java.sql.Types.VARCHAR : return new Integer(Constantes.CHAINE);
  case 11 : return new Integer(Constantes.DATE); // oracle Lite 4.0 retourne 11 pour une date
  }
return new Integer(Constantes.INCONNU);
}
}