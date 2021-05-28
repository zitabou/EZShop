package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.Location;
import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.data.ProductType;

public class DAOlocation {

	
	public static Integer Create(Location loc) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Integer generatedKey = null;
		try {
			pstat = conn.prepareStatement("INSERT INTO location (full_position, aisle, rack, level, product) VALUES (?,?,?,?,?)");
			pstat.setString(1, loc.getPosition());
			pstat.setInt(2,loc.getAisleID());
			pstat.setInt(3, loc.getRackID());
			pstat.setInt(4, loc.getLevelID());
			pstat.setInt(5, loc.getProduct());
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}

		}catch(SQLException e){
			throw new DAOexception(e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while craeting location" + loc.getPosition() + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating location" + loc.getPosition() + e.getMessage()); }
		}
		
		return generatedKey;
	}
	

	

/* READ  */
	
	
	
public static Location Read(Integer prodId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		Location loc = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM location WHERE product=?");
			pstat.setInt(1, prodId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				loc = new Location();
				loc.setAisleID(rs.getInt("aisle"));
				loc.setRackID(rs.getInt("rack"));
				loc.setLevelID(rs.getInt("level"));
				loc.setProduct(rs.getInt("product"));
			}
		}catch(SQLException e){
			throw new DAOexception("error while reading location" + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading location of product " + prodId+ e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading location of product " + prodId + e.getMessage()); }
		}
		
		return loc;
	}

public static Location Read(String position) throws DAOexception{
	
	Connection conn = DBManager.getConnection();
	Location loc = null;
	PreparedStatement pstat = null;
	ResultSet rs = null;
	
	try {
		pstat = conn.prepareStatement("SELECT * FROM location WHERE full_position=?");
		pstat.setString(1, position);
		rs = pstat.executeQuery();
		
		if (rs.next() == true) {
			loc = new Location();
			loc.setAisleID(rs.getInt("aisle"));
			loc.setRackID(rs.getInt("rack"));
			loc.setLevelID(rs.getInt("level"));
			loc.setProduct(rs.getInt("product"));
		}
	}catch(SQLException e){
		throw new DAOexception("error while reading location" + e.getMessage());
	}finally {
		if(pstat != null)
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading location of position " + position + e.getMessage()); }
		if(rs != null)
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading location of position " + position + e.getMessage()); }
	}
	
	return loc;
}

	


	
	
	
	
	
	
	
	
	
	
/* UPDATE */	
	
	public static void Update(Location loc) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE location SET aisle= ?, rack = ?, level = ?, product =? WHERE full_position=?");
			pstat.setInt(1, loc.getAisleID());
			pstat.setInt(2, loc.getRackID());
			pstat.setInt(3, loc.getLevelID());
			pstat.setInt(4, loc.getProduct());
			pstat.setString(5, loc.getPosition());
			
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("[error while updating location] " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating location " + e.getMessage()); }
		}
	}
	
	
	
	
	
	
	
/* DELETE */	
	
	public static void Delete(Location loc) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM location WHERE full_position=?");
			pstat.setString(1,loc.getPosition());
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting location with position " + loc.getPosition() + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting location with position " + loc.getPosition()+ e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM location");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all location " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all location " + e.getMessage()); }
		}
	}
	
	
}
