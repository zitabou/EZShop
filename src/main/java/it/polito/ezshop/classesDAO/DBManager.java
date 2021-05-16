package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import it.polito.ezshop.classes.*;

public class DBManager {
	/*
	public static SessionFactory factory;  //singleton
	
	public static Session getSession() {
		if(factory == null) {
			factory = new Configuration()
					.configure()
					.addAnnotatedClass(LoyaltyCard.class)
					.addAnnotatedClass(ezCustomer.class)
					.buildSessionFactory();
		}
		return factory.openSession();

	}
	
	public static void closeDB() {
		//if factory exists and no sessions are open then close
		if(factory != null && factory.getCurrentSession() != null) {
			factory.close();
			factory = null;
		}
	}
	*/
	
	
	
	

	protected static Connection conn;  //Connection singleton
	
	public static Connection getConnection() throws DAOexception{
		if (conn == null){
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:Shop.db");
				InitialiseDB();
			} catch (SQLException e) {
				throw new DAOexception("DBManager error!", e);
			}
		}
		return conn;
	}
	
	public static void closeConnection() throws DAOexception{
		try {
			if (conn != null && conn.isClosed()){
				conn.close();
				conn = null;
			}
		}catch(SQLException ex) {
			throw new DAOexception("unable to close connection");
		}
	}
	
	private static void InitialiseDB(){ //only used inside getConnection
		
		Statement stat = null;
		try {

			//Customer
			stat = conn.createStatement();
			if(!existsTable("customer")) { //no such table in DB
				stat.execute("CREATE TABLE customer (customer_id integer not null, customer_name varchar (30), customer_card varchar (10), customer_points Integer, primary key(customer_id));");
			}
			stat.close();
			//Loyalty Card
			stat = conn.createStatement();
			if(!existsTable("loyalty_card")) { //no such table in DB
				stat.execute("CREATE TABLE loyalty_card (id integer not null, card_id AS ('card_' || SUBSTR('00000' || id,-5,5)), card_points Integer, customer Integer, primary key(id));");
			}
			stat.close();

			//Product
			stat = conn.createStatement();
			if(!existsTable("product")) { //no such table in DB
				stat.execute("CREATE TABLE product (id INTEGER not null, quantity INTEGER, description VARCHAR, barcode VARCHAR(14), price REAL, location VARCHAR(30) DEFAULT NULL, note VARCHAR, primary key(id), UNIQUE(location),UNIQUE(barcode));");
			}
			stat.close();
			
			
			//Sale transaction
			stat = conn.createStatement();
			if(!existsTable("sale_transaction")) { //no such table in DB
				stat.execute("CREATE TABLE sale_transaction (id INTEGER not null, discount_rate REAL, price REAL, primary key(id));");
				
			}
			stat.close();
			//entries of sale transaction
			stat = conn.createStatement();
			if(!existsTable("receipt_entries")) { //no such table in DB
				stat.execute("CREATE TABLE receipt_entries (sale_id INTEGER not null, barcode VARCHAR(14), product_description VARCHAR, amount INTEGER, price_per_unit REAL, discount_rate REAL );");
			}
			stat.close();

			//balance operation
			stat = conn.createStatement();
			if(!existsTable("balance_operation")) { //no such table in DB
				stat.execute("CREATE TABLE balance_operation (balance_id INTEGER not null, date TEXT, money REAL, type VARCHAR);");
			}
			stat.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {stat.close();} catch (SQLException e) {e.getMessage();}
		}
		
		
		
	}
	
	
	private static boolean existsTable(String tableName) {   //only used inside initialise
		
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			ResultSet rs = metadata.getTables(null, null, tableName, null);
			if(rs.next())
				return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
}
