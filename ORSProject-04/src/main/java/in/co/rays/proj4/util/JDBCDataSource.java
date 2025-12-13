package in.co.rays.proj4.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBCDataSource is a singleton utility class that manages JDBC connections
 * using C3P0 connection pooling.
 * <p>
 * Configuration is loaded from:
 * <br>
 * <b>in.co.rays.proj4.bundle.system</b> resource bundle.
 * <p>
 * Expected keys:
 * <ul>
 *   <li>driver</li>
 *   <li>url</li>
 *   <li>username</li>
 *   <li>password</li>
 *   <li>initialpoolsize</li>
 *   <li>acquireincrement</li>
 *   <li>maxpoolsize</li>
 * </ul>
 *
 * Example:
 * <pre>
 *   Connection conn = null;
 *   try {
 *       conn = JDBCDataSource.getConnection();
 *       // use connection
 *   } finally {
 *       JDBCDataSource.closeConnection(conn);
 *   }
 * </pre>
 *
 * @author Deepak Verma
 * @version 1.0
 */
public final class JDBCDataSource {

	private static JDBCDataSource jds = null;

	private ComboPooledDataSource cpds = null;

	private static final ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.proj4.bundle.system");

	/**
	 * Private constructor to initialize C3P0 connection pool.
	 */
	private JDBCDataSource() {

        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(rb.getString("driver"));

            String env = System.getProperty("env");

            if ("docker".equals(env)) {
                cpds.setJdbcUrl(rb.getString("url.docker"));
            } else {
                cpds.setJdbcUrl(rb.getString("url.local"));
            }

           
            
            cpds.setUser(rb.getString("username"));
            cpds.setPassword(rb.getString("password"));
            cpds.setInitialPoolSize(Integer.parseInt(rb.getString("initialpoolsize")));
            cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireincrement")));
            cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxpoolsize")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Returns the singleton instance of JDBCDataSource.
	 *
	 * @return instance of JDBCDataSource
	 */
	public static JDBCDataSource getInstance() {
		if (jds == null) {
			jds = new JDBCDataSource();
		}
		return jds;
	}

	/**
	 * Returns a database connection from the C3P0 connection pool.
	 *
	 * @return {@link Connection} or null if connection could not be obtained
	 */
	public static Connection getConnection() {
		try {
			return getInstance().cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Closes ResultSet, Statement and Connection in safe order.
	 *
	 * @param conn connection to close
	 * @param stmt statement to close
	 * @param rs   result set to close
	 */
	public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes Statement and Connection.
	 *
	 * @param conn connection to close
	 * @param stmt statement to close
	 */
	public static void closeConnection(Connection conn, Statement stmt) {
		closeConnection(conn, stmt, null);
	}

	/**
	 * Closes Connection.
	 *
	 * @param conn connection to close
	 */
	public static void closeConnection(Connection conn) {
		closeConnection(conn, null);
	}
}
