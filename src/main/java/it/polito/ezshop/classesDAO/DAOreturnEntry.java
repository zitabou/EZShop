package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.classes.ezReceiptEntry;
import it.polito.ezshop.data.TicketEntry;

public class DAOreturnEntry {
	
	//ticket entry is the same as a return ticket entry
	public static void Create(Integer returnId, TicketEntry entry) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO return_entries (return_id, barcode, product_description, amount, price_per_unit) VALUES (?,?,?,?,?)");
			pstat.setInt(1,returnId);
			pstat.setString(2,entry.getBarCode());
			pstat.setString(3,entry.getProductDescription());
			pstat.setInt(4,entry.getAmount());
			pstat.setDouble(5, entry.getPricePerUnit());
			
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while creating return entry for return transaction " + returnId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating return entry for return transaction " + returnId + e.getMessage()); }
		}
	}
	
	
	
	public static List<TicketEntry> Read(Integer returnId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		List<TicketEntry> list= null;
        TicketEntry entry= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	pstat = conn.prepareStatement("SELECT * FROM return_entries WHERE return_id=?");
        	pstat.setInt(1, returnId);
        	rs = pstat.executeQuery();
        	list= new ArrayList<>();
            while (rs.next()) {
            	entry = new ezReceiptEntry();
                entry.setBarCode(rs.getString("barcode"));
                entry.setProductDescription(rs.getString("product_description"));
                entry.setAmount(rs.getInt("amount"));
                entry.setPricePerUnit(rs.getDouble("price_per_unit"));
                list.add(entry);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting return entries " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting return entries " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting return entries " + e.getMessage()); }
		}
        return list;

	}
	

	/*No Updates can happen to an entry. They are bound to a returnTransaction*/
	

	public static void DeleteFromSale(Integer returnId) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM return_entries WHERE return_id=?");
			pstat.setInt(1,returnId);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting entry from return transaction " + returnId + e.getMessage() );
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting entry from return transaction " + returnId + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM return_entries");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all entries " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all entries " + e.getMessage()); }
		}
	}

}
