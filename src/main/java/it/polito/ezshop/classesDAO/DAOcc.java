package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.classes.ezCreditCard;


public class DAOcc{
	
	public static int Create(ezCreditCard cc) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO credit_card (cc_number, owner_name, secure_code, validity) VALUES (?,?,?,?)");

			//pstat.setInt(1,usr.getId());
			pstat.setString(1,cc.getCCNumber());
			pstat.setString(2,cc.getOwnerName());
			pstat.setString(3,cc.getSecureCode());
			if ( cc.getValidity() ) {pstat.setInt(4, 1);} else {pstat.setInt(4, 0);}
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			    //System.out.println("Autoincrement key is: " + generatedKey );
			}
			cc.setId(generatedKey);
		}catch(SQLException e){
			throw new DAOexception("error while creating Credit Card" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating Credit Card" + cc.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating Credit Card" + cc.getId()); }
		}

		return generatedKey;
	}
	
	public static ezCreditCard Read(String cc_number) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		ezCreditCard cc= new ezCreditCard();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM credit_card WHERE cc_number=?");
			pstat.setString(1, cc_number);
			rs = pstat.executeQuery();
			if (rs.next() == true) {
				
				cc.setId(rs.getInt("id"));
				cc.setCCNumber(rs.getString("cc_number"));
				//cc.setOwnerName(rs.getString("owner_name"));
				//cc.setSecureCode(rs.getString("secure_code"));
				if (rs.getInt("validity") == 0) { cc.setValidity(false);} else 
				{cc.setValidity(true);}
			}
			
		}catch(SQLException e){
			throw new DAOexception("error while reading Credit Card"  + cc_number );
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading Credit Card" + cc_number); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading Credit Card" + cc_number); }

			return cc;
		}
		
	}
	
/*	
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
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating User" + usr.getId()); }
		}
		
		
	}
	
	public static void Delete(Integer user_id) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		try{
			pstat1 = conn.prepareStatement("DELETE FROM user WHERE user_id=?");
			pstat1.setInt(1,user_id);
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting User " + user_id);
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting User " + user_id); }
		}

	}
*/	
	public static Map<Integer, ezCreditCard> readAll() throws DAOexception {
		Map<Integer, ezCreditCard> map = new HashMap<>();
        ezCreditCard cc= null;
        Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	pstat = conn.prepareStatement("SELECT id, cc_number, validity FROM credit_card");
        	rs = pstat.executeQuery();
            while (rs.next()) {
		//System.out.println(rs.getInt("user_id") + "-" + rs.getString("user_username") + "-" + rs.getString("user_password") + "-" +  rs.getString("user_role"));    
                cc= new ezCreditCard();
                cc.setId(rs.getInt("id"));
                cc.setCCNumber(rs.getString("cc_number"));
                if (rs.getInt("validity") == 0) { cc.setValidity(false);} else {cc.setValidity(true) ;};
                map.put(cc.getId(),cc);
            }
		return map;		
        }catch(SQLException e){
			throw new DAOexception("[error while getting all credit cards] ");
	}finally {
		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all credit cards"); }
		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all credit cards"); }
		return map;
	}

	}
}
