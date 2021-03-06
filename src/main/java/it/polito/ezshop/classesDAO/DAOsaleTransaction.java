package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import it.polito.ezshop.classes.ezSaleTransaction;
import it.polito.ezshop.data.SaleTransaction;

public class DAOsaleTransaction {

	
	//get Id
	public static Integer getId() throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Integer generatedKey = 0;
		try {
			pstat = conn.prepareStatement("SELECT * FROM sale_transaction ORDER BY id DESC LIMIT 1");
			rs = pstat.executeQuery();
			if (rs.next() == false)
				generatedKey = 0;
			else {
				generatedKey = rs.getInt("id");
			}
		}catch(SQLException e){
			throw new DAOexception("error while getting sale transaction id " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting sale transaction id " + generatedKey + e.getMessage()); }
		}
		return generatedKey;
		
	}
	
	
	public static String getStatus(Integer saleId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		String sale_status = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT status FROM sale_transaction WHERE ID=?");
			pstat.setInt(1, saleId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				sale_status = (rs.getString("status"));
			}
		}catch(SQLException e){
			throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage()); }
		}
		
		return sale_status;
	}
	
	
	public static void Open(Integer saleId) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		
		try{
			pstat = conn.prepareStatement("UPDATE sale_transaction SET status =? WHERE id=?");
			pstat.setString(1, "open");
			pstat.setInt(2, saleId);
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating sale transaction " +  saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating sale transaction " +  saleId + e.getMessage()); }
		}
	}
	
	public static void Close(Integer saleId) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		
		try{
			pstat = conn.prepareStatement("UPDATE sale_transaction SET status =? WHERE id=?");
			pstat.setString(1, "close");
			pstat.setInt(2, saleId);
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating sale transaction " +  saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating sale transaction " +  saleId + e.getMessage()); }
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int Create(SaleTransaction sale) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		try {
			pstat = conn.prepareStatement("INSERT INTO sale_transaction (discount_rate, price) VALUES (?,?)");
			pstat.setDouble(1,sale.getDiscountRate());
			pstat.setDouble(2,sale.getPrice());
			pstat.executeUpdate();
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new DAOexception("error while creating sale transaction " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating sale transaction " + generatedKey + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating sale transaction " + generatedKey + e.getMessage()); }
		}
		return generatedKey;
	}
	

	
	public static SaleTransaction Read(Integer saleId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		SaleTransaction sale = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM sale_transaction WHERE ID=?");
			pstat.setInt(1, saleId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				sale = new ezSaleTransaction();
				sale.setTicketNumber(rs.getInt("id"));
				sale.setDiscountRate(rs.getDouble("discount_rate"));
				sale.setPrice(rs.getDouble("price"));
				sale.setEntries(DAOsaleEntry.Read(saleId));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId + e.getMessage()); }
		}
		
		return sale;
	}
	

	public static void Update(SaleTransaction sale) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		
		try{
			pstat = conn.prepareStatement("UPDATE sale_transaction SET discount_rate=?, price =? WHERE id=?");
			pstat.setDouble(1, sale.getDiscountRate());
			pstat.setDouble(2, sale.getPrice());
			pstat.setInt(3, sale.getTicketNumber());
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating sale transaction " +  sale.getTicketNumber() + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating sale transaction " +  sale.getTicketNumber() + e.getMessage()); }
		}
	}


	
	
// DELETE
	public static void Delete(Integer saleId) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		DAOsaleEntry.DeleteFromSale(saleId);
		
		try{
			pstat = conn.prepareStatement("DELETE FROM sale_transaction WHERE id=?");
			pstat.setInt(1,saleId);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting sale transaction " + saleId + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting sale transaction  " + saleId + e.getMessage()); }
		}
	}
	
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		DAOsaleEntry.DeleteAll();
		
		try{
			pstat = conn.prepareStatement("DELETE FROM sale_transaction");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all sale transactions " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all sale transactions  " + e.getMessage()); }
		}
	}
	public static Map<Integer, SaleTransaction> ReadAll() throws DAOexception{
		Map<Integer, SaleTransaction> map = new HashMap<>();
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		SaleTransaction st = null;
		try{
			pstat = conn.prepareStatement("SELECT id, discount_rate, price FROM sale_transaction");
			rs = pstat.executeQuery();
			while (rs.next()) {
				st = new ezSaleTransaction();
				st.setTicketNumber(rs.getInt("id"));
				st.setDiscountRate(rs.getDouble("discount_rate"));
				st.setPrice(rs.getDouble("price"));
				map.put(st.getTicketNumber(), st);
			}
			return map;
		}
		catch (SQLException e) {
			throw new DAOexception("Error while ReadAll() Sale transactions.");
		} finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("Error while ReadAll() Sale transactions");}
			if(pstat != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("Error while ReadAll() Sale transactions");}
		}
	}	
	

}
