package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.data.Customer;

public class DAOsaleTransaction {
	
	public static int Create() throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO sale_transaction (cost, discount, date, status ) VALUES (?,?,date('now'),?)");
			pstat.setDouble(1,0.0);
			pstat.setDouble(2,0.0);
			pstat.setString(3,"Open");
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new DAOexception("sale transaction error" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating sale transaction"); }
		}
		return generatedKey;
	}
	
/*	
	
	public static Customer Read(Integer customerId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		Customer cust = new ezCustomer();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM customer WHERE ID=?");
			pstat.setInt(1, customerId);
			rs = pstat.executeQuery();
			if (rs.next() == true) {
				
				cust.setId(rs.getInt("id"));
				cust.setCustomerName(rs.getString("customer_name"));
				cust.setCustomerCard(rs.getString("customer_card"));
				cust.setPoints(rs.getInt("customer_points"));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading customer " + customerId);
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
		
		return cust;
	}
	
	
	public static void Update(Customer cust) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		//get real card_id
		try{
			pstat = conn.prepareStatement("SELECT card_id FROM loyalty_card WHERE ID=?");
			pstat.setString(1, cust.getCustomerCard());
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {// it should never happen since the update includes the creation of the card
				cust.setCustomerCard(rs.getString("card_id"));
			}							
		}catch(SQLException e){
			throw new DAOexception("error while updating loyalty_card"+cust.getCustomerCard());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
		}
		
		
		//update customer table
		try{
			pstat = conn.prepareStatement("UPDATE customer SET customer_name=?,customer_card=?, customer_points=? WHERE customer_id=?");
			pstat.setString(1, cust.getCustomerName());
			pstat.setString(2, cust.getCustomerCard());
			pstat.setInt(3, cust.getPoints());
			pstat.setInt(4, cust.getId());
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while updating Customer " + cust.getId());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
		}
		
		
		//update new loyalty card
		try{
			//reset the previous card. It is detached from the customer
			pstat = conn.prepareStatement("UPDATE loyalty_card SET customer=? WHERE customer=?");
			pstat.setInt(1, 0);
			pstat.setInt(2, cust.getId());
			pstat.executeUpdate();
			
			//attach new card to customer and update points
			pstat = conn.prepareStatement("UPDATE loyalty_card SET card_points=?,customer=? WHERE card_id=?");
			pstat.setInt(1, cust.getPoints());
			pstat.setInt(2, cust.getId());
			pstat.setString(3, cust.getCustomerCard());
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while updating loyalty_card"+cust.getCustomerCard());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card"+cust.getCustomerCard()); }
		}
		
	}
	
	public static void Delete(Integer cust_id) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		try{
			pstat1 = conn.prepareStatement("DELETE FROM customer WHERE customer_id=?");
			pstat1.setInt(1,cust_id);
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting Customer " + cust_id);
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer " + cust_id); }
		}

	}
	
	public static Map<Integer, Customer> readAll() throws DAOexception {
		Map<Integer, Customer> map = new HashMap<>();
        Customer cust= null;
        Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
       // "SELECT customer_id,customer_name,customer_card,card_points FROM [customer] JOIN loyalty_card ON [customer].customer_id = card_id ");

        	pstat = conn.prepareStatement("SELECT * FROM customer");
        	rs = pstat.executeQuery();
            while (rs.next()) {
                cust = new ezCustomer();
                cust.setId(rs.getInt("customer_id"));
                cust.setCustomerName(rs.getString("customer_name"));
                cust.setCustomerCard(rs.getString("customer_card"));
                cust.setPoints(rs.getInt("customer_points"));
                map.put(cust.getId(),cust);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all customers");
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
		}
        return map;
	}
	*/
}
