package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBManager {
	
	
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
/*
	public static Connection getConnectionAndReset() throws DAOexception{
		if (conn == null){
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:Shop.db");
				eraseDB();
				InitialiseDB();
			} catch (SQLException e) {
				throw new DAOexception("DBManager error!", e);
			}
		}
		return conn;
	}
*/
	public static void reset() throws DAOexception{

		if (conn != null){
			eraseDB();
			InitialiseDB();
		}

	}

	private static void InitialiseDB(){ //only used inside getConnection
		
		Statement stat = null;
		try {

			//User
			stat = conn.createStatement();
			if(!existsTable("user")) {
				stat.execute("CREATE TABLE user (user_id integer primary key autoincrement, user_username varchar(30), user_password varchar(30), user_role varchar(30) );");
				stat.execute("INSERT INTO user (user_id, user_username, user_password, user_role) VALUES (1, 'admin', 'admin', 'Administrator');");
				stat.execute("INSERT INTO user (user_id, user_username, user_password, user_role) VALUES (2, 'a', 'a', 'Administrator');");
			}
			stat.close();

			//Customer
			stat = conn.createStatement();
			if(!existsTable("customer")) { //no such table in DB
				stat.execute("CREATE TABLE customer (customer_id integer not null, customer_name varchar (30), customer_card varchar (10), customer_points Integer, primary key(customer_id),CHECK(customer_points >= 0));");
			}
			stat.close();
			//Loyalty Card
			stat = conn.createStatement();
			if(!existsTable("loyalty_card")) { //no such table in DB
				stat.execute("CREATE TABLE loyalty_card (id integer not null, card_id AS (SUBSTR('0000000000' || id,-10,10)), card_points Integer, customer Integer, primary key(id),CHECK(card_points >= 0),CHECK(customer>=0));");
			}
			stat.close();

			//Product
			stat = conn.createStatement();
			if(!existsTable("product")) { //no such table in DB
				stat.execute("CREATE TABLE product (id INTEGER not null, quantity INTEGER, description VARCHAR, barcode VARCHAR(14), price REAL, location VARCHAR(30) DEFAULT NULL, note VARCHAR, primary key(id), CHECK(quantity>=0),UNIQUE(barcode), UNIQUE(location));");
			}
			stat.close();
			
			//location
			stat = conn.createStatement();
			if(!existsTable("location")) { //no such table in DB
				stat.execute("CREATE TABLE location (full_position VARCHAR not null, aisle INTEGER, rack INTEGER, level INTEGER, product INTEGER, primary key(full_position));");
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
				stat.execute("CREATE TABLE receipt_entries (sale_id INTEGER not null, barcode VARCHAR(14), product_description VARCHAR, amount INTEGER, price_per_unit REAL, discount_rate REAL, CHECK(amount>=0), CHECK(price_per_unit>=0), CHECK(discount_rate>=0));");
			}
			stat.close();

			//balance operation
			stat = conn.createStatement();
			if(!existsTable("balance_operation")) { //no such table in DB
				stat.execute("CREATE TABLE balance_operation (balance_id INTEGER not null, date TEXT, money REAL, type varchar (30), primary key(balance_id));");
			}
			stat.close();
			
			//return transaction
			stat = conn.createStatement();
			if(!existsTable("return_transaction")) { //no such table in DB
				stat.execute("CREATE TABLE return_transaction (return_id INTEGER not null, sale_reference INTEGER, returned_value REAL, returned_product VARCHAR(30), returned_amount INTEGER, primary key(return_id));");
			}
			stat.close();
			//entries of sale transaction
			stat = conn.createStatement();
			if(!existsTable("return_entries")) { //no such table in DB
				stat.execute("CREATE TABLE return_entries (return_id INTEGER not null, barcode VARCHAR(14), product_description VARCHAR, amount INTEGER, price_per_unit REAL);");
			}
			stat.close();
			
			//Orders
			stat = conn.createStatement();
			if(!existsTable("orders")) { //no such table in DB
				stat.execute("CREATE TABLE orders (order_id INTEGER not null, product_code varchar(30), quantity INTEGER, price_per_unit REAL, status varchar(30), primary key(order_id));");
			}
			stat.close();
			
			//Credit cards
			stat = conn.createStatement();
			if(!existsTable("credit_card")) { //no such table in DB
				stat.execute("CREATE TABLE credit_card (id INTEGER primary key autoincrement, cc_number varchar(30), owner_name varchar(30), secure_code varchar(30), validity INTEGER);");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(stat != null)
				try {stat.close();} catch (SQLException e) {e.getMessage();}
		}
		
		
		
	}

	private static void eraseDB(){
		Statement stat = null;
		try {

			//User
			stat = conn.createStatement();
			if(existsTable("user")) {
				stat.execute("DROP TABLE user;");
			}
			stat.close();

			//Customer
			stat = conn.createStatement();
			if(existsTable("customer")) { //no such table in DB
				stat.execute("DROP TABLE customer;");
			}
			stat.close();
			//Loyalty Card
			stat = conn.createStatement();
			if(existsTable("loyalty_card")) { //no such table in DB
				stat.execute("DROP TABLE loyalty_card;");
			}
			stat.close();

			//Product
			stat = conn.createStatement();
			if(existsTable("product")) { //no such table in DB
				stat.execute("DROP TABLE product;");
			}
			stat.close();

			//Sale transaction
			stat = conn.createStatement();
			if(existsTable("sale_transaction")) { //no such table in DB
				stat.execute("DROP TABLE sale_transaction;");
			}
			stat.close();
			//entries of sale transaction
			stat = conn.createStatement();
			if(existsTable("receipt_entries")) { //no such table in DB
				stat.execute("DROP TABLE receipt_entries;");
			}
			stat.close();

			//balance operation
			stat = conn.createStatement();
			if(existsTable("balance_operation")) { //no such table in DB
				stat.execute("DROP TABLE balance_operation;");
			}
			stat.close();

			//Orders
			stat = conn.createStatement();
			if(existsTable("orders")) { //no such table in DB
				stat.execute("DROP TABLE orders;");
			}
			stat.close();

			//Return transaction
			stat = conn.createStatement();
			if(existsTable("return_transaction")) { //no such table in DB
				stat.execute("DROP TABLE return_transaction;");
			}
			stat.close();

			//Credit card
			stat = conn.createStatement();
			if(existsTable("credit_card")) { //no such table in DB
				stat.execute("DROP TABLE credit_card;");
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(stat != null)
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
