package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.data.ProductType;

public class DAOproductType {

	public static Integer Create(ProductType prod) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = -1;
		try {
			pstat = conn.prepareStatement("INSERT INTO product (quantity, description, barcode, price, location, note) VALUES (?,?,?,?,?,?)");
			pstat.setInt(1,prod.getQuantity());
			pstat.setString(2, prod.getProductDescription());
			pstat.setString(3, prod.getBarCode());
			pstat.setDouble(4, prod.getPricePerUnit());
			//pstat.setString(5,prod.getLocation());
			pstat.setString(6, prod.getNote());
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}

		}catch(SQLException e){
			throw new DAOexception(e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating productType " + prod.getId() + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating productType " + prod.getId() + e.getMessage()); }
		}
		
		return generatedKey;
	}
	

	

/* READ  */
	
	
	
	public static ProductType read(Integer prodId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		ProductType prod = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM product WHERE ID=?");
			pstat.setInt(1, prodId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				prod = new ezProductType();
				prod.setId(rs.getInt("id"));
				prod.setQuantity(rs.getInt("quantity"));
				prod.setProductDescription(rs.getString("description"));
				prod.setBarCode(rs.getString("barcode"));
				prod.setPricePerUnit(rs.getDouble("price"));
				prod.setLocation(rs.getString("location"));
				prod.setNote(rs.getString("note"));
			}
		}catch(SQLException e){
			throw new DAOexception("error while reading product" + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading product" + prodId + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading product" + prodId + e.getMessage()); }
		}
		
		return prod;
	}
	
	
	
	public static ProductType read(String barcode) throws DAOexception {
		Connection conn = DBManager.getConnection();
		ProductType prod= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	pstat = conn.prepareStatement("SELECT * FROM product WHERE barcode=?");
        	pstat.setString(1, barcode);
        	rs = pstat.executeQuery();
        	
        	if (rs.next() == true) {
        		prod = new ezProductType();
        		prod.setId(rs.getInt("id"));
        		prod.setQuantity(rs.getInt("quantity"));
        		prod.setProductDescription(rs.getString("description"));
        		prod.setBarCode(rs.getString("barcode"));
        		prod.setPricePerUnit(rs.getDouble("price"));
        		prod.setLocation(rs.getString("location"));
        		prod.setNote(rs.getString("note"));
        	}
        }catch(SQLException e){
			throw new DAOexception("error while getting all products by barcode" + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all products by barcode " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all products by barcode " + e.getMessage()); }
		}
        return prod;
	}
	
	public static Map<Integer, ProductType> readAll() throws DAOexception {
		Connection conn = DBManager.getConnection();
		Map<Integer, ProductType> map = null;
        ProductType prod= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	pstat = conn.prepareStatement("SELECT * FROM product");
        	rs = pstat.executeQuery();
        	map = new HashMap<>();
        	
            while (rs.next()) {
                prod = new ezProductType();
                prod.setId(rs.getInt("id"));
				prod.setQuantity(rs.getInt("quantity"));
				prod.setProductDescription(rs.getString("description"));
				prod.setBarCode(rs.getString("barcode"));
				prod.setPricePerUnit(rs.getDouble("price"));
				prod.setLocation(rs.getString("location"));
				prod.setNote(rs.getString("note"));
                map.put(prod.getId(),prod);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all products " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all products " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all products " + e.getMessage()); }
		}
        return map;
	}
	
	
	public static Map<Integer, ProductType> readAll(String description) throws DAOexception {
        Connection conn = DBManager.getConnection();
        Map<Integer, ProductType> map = null;
        ProductType prod= null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {

        	if(description == null)
        		pstat = conn.prepareStatement("SELECT * FROM product");
        	else
        		pstat = conn.prepareStatement("SELECT * FROM product WHERE description LIKE '%"+description+"%'");
        	//pstat.setString(1, description);
        	rs = pstat.executeQuery();
        	map = new HashMap<>();
        	
            while (rs.next()) {
                prod = new ezProductType();
                prod.setId(rs.getInt("id"));
				prod.setQuantity(rs.getInt("quantity"));
				prod.setProductDescription(rs.getString("description"));
				prod.setBarCode(rs.getString("barcode"));
				prod.setPricePerUnit(rs.getDouble("price"));
				prod.setLocation(rs.getString("location"));
				prod.setNote(rs.getString("note"));
                map.put(prod.getId(),prod);
            }
        }catch(SQLException e){
			throw new DAOexception("error while getting all products by description "  + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all products by description " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all products by description " + e.getMessage()); }
		}
        return map;
	}
	
	
	
	
	
	
/* UPDATE */	
	
	
	public static void Update(ProductType prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE product SET quantity=?, description = ?, barcode = ?, price = ?, location =?, note =? WHERE ID=?");
			pstat.setInt(1, prod.getQuantity());
			pstat.setString(2, prod.getProductDescription());
			pstat.setString(3, prod.getBarCode());
			pstat.setDouble(4, prod.getPricePerUnit());
			pstat.setString(5, prod.getLocation());
			pstat.setString(6, prod.getNote());
			pstat.setInt(7, prod.getId());
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating product " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating product " + e.getMessage()); }
		}
	}
	
	public static void UpdateByCode(ProductType prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE product SET quantity=?, description = ?, price = ?, location =?, note =? WHERE barcode=?");
			pstat.setInt(1, prod.getQuantity());
			pstat.setString(2, prod.getProductDescription());
			pstat.setDouble(3, prod.getPricePerUnit());
			pstat.setString(4, prod.getLocation());
			pstat.setString(5, prod.getNote());
			pstat.setString(6, prod.getBarCode());
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating product " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating product " + prod.getId() + e.getMessage()); }
		}
	}
	
	
	
	
	
	
	
/* DELETE */	
	
	
	public static void Delete(ProductType prod) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("DELETE FROM product WHERE ID=?");
			pstat.setInt(1,prod.getId());
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
		}catch(SQLException e){
			throw new DAOexception("error while deleting Product" + prod.getId());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Product" + prod.getId()); }
		}
	}
	
	public static void Delete(String barcode) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM product WHERE barcode=?");
			pstat.setString(1,barcode);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting Product with code" + barcode + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Product with code" + barcode + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM product");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all Products " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all Products " + e.getMessage()); }
		}
	}
	
	

}
