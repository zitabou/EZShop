package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.ezshop.classes.ezSaleTransaction;
import it.polito.ezshop.data.SaleTransaction;

public class DAOsaleTransaction {
	
	public static Integer readId() throws DAOexception {
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
System.out.println("id: " + generatedKey);	
		}catch(SQLException e){
			e.printStackTrace();
			//throw new DAOexception("sale transaction error");
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating sale transaction " + generatedKey); }
		}
		return generatedKey;
	
	}
	
	public static int Create(SaleTransaction sale) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		int generatedKey = 0;
		
		try {
			pstat = conn.prepareStatement("INSERT INTO sale_transaction (discount, price, status) VALUES (?,?,?)");
			pstat.setDouble(1,sale.getDiscountRate());
			pstat.setDouble(2,sale.getPrice());
			pstat.setString(2,"Open");
			pstat.executeUpdate();
			
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new DAOexception("error while creating sale transaction " + generatedKey);
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating sale transaction " + generatedKey); }
		}
		return generatedKey;
	}
	

	
	public static SaleTransaction Read(Integer saleId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		SaleTransaction sale = new ezSaleTransaction();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM sale_transaction WHERE ID=?");
			pstat.setInt(1, saleId);
			rs = pstat.executeQuery();
			if (rs.next() == true) {
				sale.setReceiptNumber(rs.getInt("id"));
				sale.setDiscountRate(rs.getDouble("discount_rate"));
				sale.setPrice(0);
				//entries still left it will be done later
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading sale transaction " +  saleId);
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading sale transaction " +  saleId); }
		}
		
		sale.setEntries(DAOsaleEntry.Read(saleId));
		
		return sale;
	}
	

	public static void Update(SaleTransaction sale) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		try{
			pstat = conn.prepareStatement("UPDATE sale_transaction SET discount=?, price =?, WHERE id=?");
			pstat.setDouble(1, sale.getDiscountRate());
			pstat.setDouble(2, sale.getPrice());
			pstat.setInt(3, sale.getReceiptNumber());
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while updating sale transaction " +  sale.getReceiptNumber());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating sale transaction " +  sale.getReceiptNumber()); }
		}
		
		
	}

	/*
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
