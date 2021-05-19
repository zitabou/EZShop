package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.classes.ezReceiptEntry;
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
			throw new DAOexception("error while creating receipt entry for sale " + saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating receipt entry for sale " + saleId + e.getMessage()); }
		}
	}
	
	
	
	public static List<TicketEntry> Read(Integer saleId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		List<TicketEntry> list= null;
        TicketEntry entry= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	pstat = conn.prepareStatement("SELECT * FROM receipt_entries WHERE sale_id=?");
        	pstat.setInt(1, saleId);
        	rs = pstat.executeQuery();
        	list= new ArrayList<>();
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
			throw new DAOexception("error while getting all customers " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers " + e.getMessage()); }
		}
        return list;

	}
	

	/*No Updates can happen to an entry. They are bound to a saleTransaction*/
	

	public static void DeleteFromSale(Integer saleId) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM receipt_entries WHERE sale_id=?");
			pstat.setInt(1,saleId);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting entry from sale " + saleId + e.getMessage() );
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting entry from sale " + saleId + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM receipt_entries");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all entries " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all entrie " + e.getMessage()); }
		}
	}
	
}
