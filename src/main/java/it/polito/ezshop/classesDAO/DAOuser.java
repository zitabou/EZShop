package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.ezUser;
import it.polito.ezshop.data.User;


public class DAOuser {
	
	public static int Create(User usr) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO user (user_username, user_password, user_role) VALUES (?,?,?)");
			//pstat.setInt(1,usr.getId());
			pstat.setString(1,usr.getUsername());
			pstat.setString(2,usr.getPassword());
			pstat.setString(3,usr.getRole());
			
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			    //System.out.println("Autoincrement key is: " + generatedKey );
			}
		}catch(SQLException e){
			throw new DAOexception("error while creating User " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating User " + usr.getId()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating User " + usr.getId()); }
		}

		return generatedKey;
	}
	
	public static User Read(Integer userId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		User usr = new ezUser();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM user WHERE user_id=?");
			pstat.setInt(1, userId);
			rs = pstat.executeQuery();
			if (rs.next() == true) {
				
				usr.setId(rs.getInt("user_id"));
				usr.setUsername(rs.getString("user_username"));
				usr.setPassword(rs.getString("user_password"));
				usr.setRole(rs.getString("user_role"));
			}
			//pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading User" + userId);
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading User " + usr.getId()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading User " + usr.getId()); }
		}
		
		return usr;
	}
	
	
	public static void Update(User usr) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int result = 0;
		
		//update user table
		try{
			pstat = conn.prepareStatement("UPDATE user SET user_username=?, user_password=?, user_role=? WHERE user_id=?");
			pstat.setString(1, usr.getUsername());
			pstat.setString(2, usr.getPassword());
			pstat.setString(3, usr.getRole());
			pstat.setInt(4, usr.getId());
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating User" + usr.getId());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating User" + usr.getId()); }
		}
		
		
	}
	
	public static void Delete(Integer user_id) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("DELETE FROM user WHERE user_id=?");
			pstat.setInt(1,user_id);
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting User " + user_id);
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting User " + user_id); }
		}

	}
	
	public static Map<Integer, User> readAll() throws DAOexception {
		Map<Integer, User> map = new HashMap<>();
        User usr= null;
        Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	pstat = conn.prepareStatement("SELECT user_id, user_username, user_password, user_role FROM user");
        	rs = pstat.executeQuery();
            while (rs.next()) {
		//System.out.println(rs.getInt("user_id") + "-" + rs.getString("user_username") + "-" + rs.getString("user_password") + "-" +  rs.getString("user_role"));    
                usr = new ezUser();
                usr.setId(rs.getInt("user_id"));
                usr.setUsername(rs.getString("user_username"));
                usr.setPassword(rs.getString("user_password"));
                usr.setRole(rs.getString("user_role"));
                map.put(usr.getId(),usr);
            }
		return map;		
        }catch(SQLException e){
			throw new DAOexception("[error while getting all user] ");
	}finally {
		if(pstat != null)
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all users  "); }
		if(rs != null)
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all users "); }
		return map;
	}

	}
}
