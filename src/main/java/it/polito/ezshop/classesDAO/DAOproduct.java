package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.polito.ezshop.classes.Product;

public class DAOproduct {
	
	public static Integer Create(Product prod) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Integer generatedKey = -1;
		try {
			pstat = conn.prepareStatement("INSERT INTO product_RFID (RFID, barcode) VALUES (?, ?)");
			//pstat.setString(1,prod.getRFID());
			pstat.setString(1,StringUtils.leftPad(prod.getRFID(), 12, '0'));
			pstat.setString(2,prod.getBarCode());
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}

		}catch(SQLException e){
			throw new DAOexception(e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating product " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating product " + e.getMessage()); }
		}
		
		
		return generatedKey;
	}
	

	

/* READ  */
	
	public static Product read(Integer id) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		Product prod = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM product_RFID WHERE id=?");
			pstat.setInt(1, id);
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				prod = new Product();
				prod.setRFID(rs.getString("RFID"));
				prod.setBarCode(rs.getString("barcode"));
            }
		}catch(SQLException e){
			throw new DAOexception("error while reading product_RFID "+ id+ e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading product_RFID "+ id + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading product_RFID " + id + e.getMessage()); }
		}
		
		return prod;
	}
	
	
	
	//get all RFIDs for a certain barcode
	public static List<String> readByCode(String barcode) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		List<String> RFIDs = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT RFID FROM product_RFID WHERE barcode=?");
			pstat.setString(1, barcode);
			rs = pstat.executeQuery();
			RFIDs = new ArrayList<String>();
			
			while (rs.next()) {
				RFIDs.add(rs.getString("RFID"));
            }
		}catch(SQLException e){
			throw new DAOexception("error while reading product_RFIDs of "+ barcode + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading product_RFIDs of"+ barcode + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading product_RFIDs of " + barcode + e.getMessage()); }
		}
		
		return RFIDs;
	}
	
	
	//get barcode of certain RFID
	public static String readByRFID(String RFID) throws DAOexception {
		Connection conn = DBManager.getConnection();
		String barcode = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	pstat = conn.prepareStatement("SELECT barcode FROM product_RFID WHERE RFID=?");
        	//pstat.setString(1, RFID);
        	pstat.setString(1,StringUtils.leftPad(RFID, 12, '0'));
        	rs = pstat.executeQuery();
			
			if (rs.next()) {
                barcode = rs.getString("barcode");
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting barcodes for"+ RFID + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting barcodes for "+ RFID + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting barcodes for "+ RFID + e.getMessage()); }
		}
        return barcode;
	}
	
	
	public static Map<Integer, Product> readAll() throws DAOexception {
		Connection conn = DBManager.getConnection();
		Map<Integer, Product> map = null;
		Product prod = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	pstat = conn.prepareStatement("SELECT * FROM product_RFID");
        	rs = pstat.executeQuery();
        	map = new HashMap<>();
        	
            while (rs.next()) {
            	prod = new Product();
            	prod.setBarCode(rs.getString("barcode"));
            	prod.setRFID(rs.getString("RFID"));
                map.put(rs.getInt("id"),prod);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all product_RFID " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all product_RFID " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all product_RFID " + e.getMessage()); }
		}
        return map;
	}
	
	
	
	
	
	
	
	
/* UPDATE */	
	
	
	public static void UpdateRFID(Product prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE product_RFID SET RFID = ? WHERE barcode=?");
			//pstat.setString(1, prod.getRFID());
			pstat.setString(1,StringUtils.leftPad(prod.getRFID(), 12, '0'));
			pstat.setString(2, prod.getBarCode());
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating product_RFID " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating product_RFID " + e.getMessage()); }
		}
	}
	
	public static void UpdateBarcode(Product prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE product_RFID SET barcode = ? WHERE RFID=?");
			pstat.setString(1, prod.getBarCode());
			//pstat.setString(2, prod.getRFID());
			pstat.setString(2,StringUtils.leftPad(prod.getRFID(), 12, '0'));
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating product_RFID " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating product_RFID " + e.getMessage()); }
		}
	}
	
	
	
	
	
	
/* DELETE */	
	
	
	public static void Delete(Product prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("DELETE FROM product_RFID WHERE RFID=?");
			//pstat.setString(1,prod.getRFID());
			pstat.setString(1,StringUtils.leftPad(prod.getRFID(), 12, '0'));
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
		}catch(SQLException e){
			throw new DAOexception("error while deleting product_RFID" + prod.getRFID());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting product_RFID" + prod.getRFID()); }
		}
	}
	
	public static void Delete(String barcode) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM product_RFID WHERE barcode=?");
			pstat.setString(1,barcode);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting product_RFID with code" + barcode + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting product_RFID with code" + barcode + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM product_RFID");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all product_RFID " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all product_RFID " + e.getMessage()); }
		}
	}
	

}
