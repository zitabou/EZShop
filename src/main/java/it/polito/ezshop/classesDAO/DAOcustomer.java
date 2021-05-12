package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.data.Customer;


public class DAOcustomer {
	
	public static void Create(ezCustomer cust) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try {
			pstat = conn.prepareStatement("INSERT INTO customer (name,card) VALUES (?,?);");
			pstat.setString(1,cust.getCustomerName());
			pstat.setString(2,cust.getCustomerCard());
						
			pstat.executeUpdate();

		}catch(SQLException e){
			throw new DAOexception("product error" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
	}
	
	
	
	public static ezCustomer Read(Integer customerId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		ezCustomer cust = new ezCustomer();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM customer WHERE ID=?");
			pstat.setInt(1, customerId);
			rs = pstat.executeQuery();
			if (rs.next() == true) {
				
				cust.setId(rs.getInt("id"));
				cust.setCustomerName(rs.getString("name"));
				cust.setCustomerCard(rs.getString("card"));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading product" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
		
		return cust;
	}
	
	
	public static void Update(ezCustomer cust) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("UPDATE customer SET name=?,card=? WHERE ID=?");
			pstat.setString(1, cust.getCustomerName());
			pstat.setString(2, cust.getCustomerCard());
			pstat.setInt(3, cust.getId());
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while updating Customer " + cust.getId());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
	}
	
	public static void Delete(ezCustomer cust) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		PreparedStatement pstat2 = null;
		try{
			pstat1 = conn.prepareStatement("DELETE FROM customer WHERE ID=?");
			pstat1.setInt(1,cust.getId());
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting Customer" + cust.getId());
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
		
		try {
			//delete also the associated card
			pstat2 = conn.prepareStatement("DELETE FROM loyalty_card WHERE id = ? ");
			pstat2.setString(1,cust.getCustomerCard());
			pstat2.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while deleting Card" + cust.getCustomerCard());
		}finally {
			try {pstat2.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
		
	}
	
	public static List<Customer> readAll() throws DAOexception {
        List<Customer> list = new ArrayList<Customer>();
        ezCustomer cust= null;
        
        Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
        try {
        	
        	pstat = conn.prepareStatement("SELECT * FROM customer");
        	rs = pstat.executeQuery();
            while (rs.next()) {
                cust = new ezCustomer();
                cust.setId(rs.getInt("id"));
                cust.setCustomerName(rs.getString("name"));
                cust.setCustomerCard(rs.getString("card"));
 
                list.add(cust);
            }
        }catch(SQLException e){
			throw new DAOexception("error while deleting Card" + cust.getCustomerCard());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer" + cust.getId()); }
		}
        return list;
	
	}
}