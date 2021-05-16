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
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating Customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating Customer" + cust.getId()); }
		}
		return generatedKey;
	}
	
	
	
	public static Customer Read(Integer customerId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		Customer cust = new ezCustomer();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM customer WHERE customer_id=?");
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
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading customer" + cust.getId()); }
		}
		
		return cust;
	}
	
	
	public static void Update(Customer cust) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		LoyaltyCard card = null;
		
		//get the old card and transfer the points
		card = DAOloyaltyCard.ReadCustomer(cust);
		cust.setPoints(card.getPoints());  			//take points from old card
		
		//update customer table with new card and same points
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
		
		//detach old card without reseting the points
		card.setCustomer(0);
		DAOloyaltyCard.Update(card);

		//attach new card to customer and update points
		card.setCardID(cust.getCustomerCard());
		card.setPoints(cust.getPoints());
		card.setCustomer(cust.getId());
		
		DAOloyaltyCard.Update(card);
			
		
		
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
}
