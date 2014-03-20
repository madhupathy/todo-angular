package todo.persistence;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


//import org.apache.log4j.Logger;

public class DBConnPool {
	static Logger log = Logger.getLogger(DBConnPool.class);
	
	public static void main(String[] args) {
		Connection con = getConnection();
	}

	/**
	 * Gets a Pooled Connection, based on the JNDI lookup entry.
	 * 
	 * @param none
	 * @return Returns Pooled Connection based on the JNDI Lookup entry. Entry
	 *         name is obtained from the properties file.
	 */
	public static Connection getConnection() {
		log.debug("getConnection - Begin");
		Connection con = null;
		try {
			DataSource ds;
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext
				.lookup("java:/comp/env");
			InitialContext context = new InitialContext();
			ds = (DataSource) envContext.lookup("jdbc/Todo");
			con = ds.getConnection();
			if (con == null)
				log.error("ds.getConnection() returned null");

		} catch (SQLException sqle) {
			log.error("getPooledConnection: SQL Exception - "
					+ sqle.getMessage());
			sqle.printStackTrace();
		} catch (NamingException ne) {
			log.error("getPooledConnection: Naming Exception - "
					+ ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			log.error("getPooledConnection: Exception - " + e.getMessage());
			e.printStackTrace();
		}
		return con;
	}

	public static void release(Connection con) throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}
	}

}