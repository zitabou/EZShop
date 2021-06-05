package it.polito.ezshop.classesDAO;

import it.polito.ezshop.classes.Credit;
import it.polito.ezshop.classes.Debit;
import it.polito.ezshop.classes.ReturnTransaction;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.SaleTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DAOreturnTransaction {

    public static int Create(ReturnTransaction r) throws DAOexception {
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int generatedKey = 0;

        try {

            pstat = conn.prepareStatement("INSERT INTO return_transaction (return_id, sale_reference, returned_value, returned_product, returned_amount) VALUES (?,?,?,?,?)");
            pstat.setInt(1, r.getBalanceId());
            pstat.setInt(2, r.getSaleID());
            pstat.setDouble(3, r.getReturnedValue()==null? 0 : r.getReturnedValue());
            pstat.setString(4, r.getProdId()==null? "" : r.getProdId());
            pstat.setInt(5, r.getAmount());


            pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new DAOexception("error while creating BalanceOperation" + e.getMessage());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + r.getBalanceId()); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + r.getBalanceId()); }
        }
        return generatedKey;
    }

    public static ReturnTransaction Read(Integer returnId) throws DAOexception{

        Connection conn = DBManager.getConnection();
        ReturnTransaction r = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        BalanceOperation op = null;

        try {
            pstat = conn.prepareStatement("SELECT * FROM return_transaction WHERE return_id=?");
            pstat.setInt(1, returnId);
            rs = pstat.executeQuery();
            if (rs.next() == true) {
                r = new ReturnTransaction();
                r.setBalanceId(rs.getInt("return_id"));
                r.setSaleID(rs.getInt("sale_reference"));
                r.setReturnedValue(rs.getDouble("returned_value"));
                r.setMoney(rs.getDouble("returned_value"));//the field is duplicated
                r.setProdId(rs.getString("returned_product"));
                r.setAmount(rs.getInt("returned_amount"));

            }
            pstat.close();
        }catch(SQLException e){
            throw new DAOexception("error while reading balance operation " + returnId);
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading balance operation" + returnId); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading balance operation" + returnId); }
        }

        return r;
    }

    public static Map<Integer, ReturnTransaction> readAll() throws DAOexception {
        Map<Integer, ReturnTransaction> map = new HashMap<>();
        ReturnTransaction r= null;
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {

            pstat = conn.prepareStatement("SELECT * FROM return_transaction");
            rs = pstat.executeQuery();
            while (rs.next()) {

                r = new ReturnTransaction();
                r.setBalanceId(rs.getInt("return_id"));
                r.setSaleID(rs.getInt("sale_reference"));
                r.setReturnedValue(rs.getDouble("returned_value"));
                r.setProdId(rs.getString("returned_product"));
                r.setAmount(rs.getInt("returned_amount"));

                map.put(r.getBalanceId(),r);
            }
        }catch(SQLException e){
            throw new DAOexception("error while getting all return transactions");
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all return transactions"); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all return transactions"); }
        }
        return map;

    }

    public static void Update(ReturnTransaction r) throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;

        try{
            pstat = conn.prepareStatement("UPDATE return_transaction SET returned_amount=?, returned_product =?, returned_value=? WHERE return_id=?");
            pstat.setDouble(1, r.getAmount());
            pstat.setString(2, r.getProdId());
            pstat.setDouble(3, r.getMoney());
            pstat.setInt(4, r.getBalanceId());
            int result = pstat.executeUpdate();
            if(result == 0)
				throw new SQLException("entry not found");
        }catch(SQLException e){
            throw new DAOexception("error while updating return transaction " +  r.getBalanceId());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating return transaction " +  r.getBalanceId()); }
        }


    }

    public static void Delete(Integer return_id) throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        try{
            pstat = conn.prepareStatement("DELETE FROM return_transaction WHERE return_id=?");
            pstat.setInt(1,return_id);
            pstat.executeUpdate();
        }catch(SQLException e){
            throw new DAOexception("error while deleting Return Transaction " + return_id + e.getMessage());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting Customer " + return_id + e.getMessage()); }
        }
    }
    
    
    
    
    
    
    public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		DAOsaleEntry.DeleteAll();
		
		try{
			pstat = conn.prepareStatement("DELETE FROM return_transaction");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all return transactions " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all return transactions  " + e.getMessage()); }
		}
	}
    
    
    
    
}
