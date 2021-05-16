package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.classes.ezReceiptEntry;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.ReceiptEntry;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class DAOsaleEntry {
	
	public static void Create(Integer saleId, TicketEntry entry) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO receipt_entries (sale_id, barcode, product_description, amount, price_per_unit, discount_rate) VALUES (?,?,?,?,?,?)");
			pstat.setInt(1,saleId);
			pstat.setString(2,entry.getBarCode());
			pstat.setString(3,entry.getProductDescription());
			pstat.setInt(4,entry.getAmount());
			pstat.setDouble(5, entry.getPricePerUnit());
			pstat.setDouble(6, entry.getDiscountRate());
			
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception(e.getMessage());
			//throw new DAOexception("error while creating receipt entry for sale " + saleId);
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating receipt entry for sale " + saleId); }
		}
	}
	
	
	
	public static List<TicketEntry> Read(Integer saleId) throws DAOexception{
		
		List<TicketEntry> list= new ArrayList<>();
        TicketEntry entry= null;
        Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	pstat = conn.prepareStatement("SELECT * FROM receipt_entries WHERE sale_id=?");
        	pstat.setInt(1, saleId);
        	rs = pstat.executeQuery();
            while (rs.next()) {
            	entry = new ezReceiptEntry();
                entry.setBarCode(rs.getString("barcode"));
                entry.setProductDescription(rs.getString("product_description"));
                entry.setAmount(rs.getInt("amount"));
                entry.setPricePerUnit(rs.getDouble("price_per_unit"));
                entry.setDiscountRate(rs.getDouble("discount_rate"));
                list.add(entry);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all customers");
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
		}
        return list;

	}
	
/*	
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

	}*/
	
}
