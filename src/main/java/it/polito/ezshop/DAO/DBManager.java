package it.polito.ezshop.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

	protected final static String dbPath = "jdbc:sqlite:ezshopDB.db";
	protected static Connection conn;  //instance for singleton Connection
	
	public static Connection getConnection(){
		if (conn == null){
			try {
				conn = DriverManager.getConnection(dbPath);
			} catch (SQLException e) {
				throw new RuntimeException("instantiate DBManager error", e);
			}
		}
		return conn;
	}
	
	public static void closeConnection() throws SQLException{
		if (conn != null && conn.isClosed()){
			conn.close();
			conn = null;
		}
	}

	
}
