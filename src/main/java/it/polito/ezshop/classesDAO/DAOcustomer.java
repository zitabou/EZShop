package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.data.Customer;


public class DAOcustomer {
	
	
	
	
	
/*CREATE*/	
	
	
	
	public static int Create(Customer cust) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO customer (customer_name, customer_card, customer_points) VALUES (?,?,?)");
			pstat.setString(1,cust.getCustomerName());
			pstat.setString(2,cust.getCustomerCard());
			pstat.setInt(3,cust.getPoints());
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new DAOexception("error while creating Customer" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating Customer" + cust.getId() + e.getMessage()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating Customer" + cust.getId() + e.getMessage()); }
		}
		return generatedKey;
	}
	
	
	
	
	
	
	
/*READ*/
	
	
	public static Customer Read(Integer customerId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		Customer cust = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM customer WHERE customer_id=?");
			pstat.setInt(1, customerId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				cust = new ezCustomer();
				cust.setId(rs.getInt("id"));
				cust.setCustomerName(rs.getString("customer_name"));
				cust.setCustomerCard(rs.getString("customer_card"));
				cust.setPoints(rs.getInt("customer_points"));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading customer " + customerId + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading customer" + cust.getId() + e.getMessage()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading customer" + cust.getId() + e.getMessage()); }
		}
		
		return cust;
	}
	
	public static Map<Integer, Customer> readAll() throws DAOexception {
		Connection conn = DBManager.getConnection();
		Map<Integer, Customer> map = null;
        Customer cust= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	pstat = conn.prepareStatement("SELECT * FROM customer");
        	rs = pstat.executeQuery();
        	map = new HashMap<>();
            while (rs.next()) {
                cust = new ezCustomer();
                cust.setId(rs.getInt("customer_id"));
                cust.setCustomerName(rs.getString("customer_name"));
                cust.setCustomerCard(rs.getString("customer_card"));
                cust.setPoints(rs.getInt("customer_points"));
                map.put(cust.getId(),cust);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all customers " + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers " + e.getMessage()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers " + e.getMessage()); }
		}
        return map;

	}
	
	
	
	
	
	
	
	
/*UPDATE*/
	
	
	public static void Update(Customer cust) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		LoyaltyCard card = null;
		int result = 0;
		
		//get the old card and transfer the points
		card = DAOloyaltyCard.ReadCustomer(cust);
		/////
		if (card == null) {
			card = new LoyaltyCard();
			card.setCardID(cust.getCustomerCard());
		}
		/////
		cust.setPoints(card.getPoints());  			//take points from old card
		//update customer table with new card and same points
		try{
			pstat = conn.prepareStatement("UPDATE customer SET customer_name=?,customer_card=?, customer_points=? WHERE customer_id=?");
			pstat.setString(1, cust.getCustomerName());
			pstat.setString(2, cust.getCustomerCard());
			pstat.setInt(3, cust.getPoints());
			pstat.setInt(4, cust.getId());
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating Customer " + cust.getId() + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer " + cust.getId() + e.getMessage()); }
		}
		
		//detach old card without reseting the points
		card.setCustomer(0);
		DAOloyaltyCard.Update(card);

		//attach new card to customer and update points
		card.setCardID(cust.getCustomerCard());
		card.setPoints(cust.getPoints());
		card.setCustomer(cust.getId());
		
		DAOloyaltyCard.Update(card);
	}
	
	
	
	
	
/*DELETE*/
	
	
	public static void Delete(Integer cust_id) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		try{
			pstat1 = conn.prepareStatement("DELETE FROM customer WHERE customer_id=?");
			pstat1.setInt(1,cust_id);
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting Customer " + cust_id + e.getMessage());
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer " + cust_id + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		
		DAOloyaltyCard.DeleteAll();
		
		try{
			pstat1 = conn.prepareStatement("DELETE FROM customer");
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all Customers " + e.getMessage());
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting all Customers " + e.getMessage()); }
		}
	}
	
	
}
