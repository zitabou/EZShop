package it.polito.ezshop.classesDAO;

import it.polito.ezshop.classes.Credit;
import it.polito.ezshop.classes.Debit;
import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class DAObalanceOperation {

    public static int Create(BalanceOperation bo) throws DAOexception {
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int generatedKey = 0;

        try {

            pstat = conn.prepareStatement("INSERT INTO balance_operation (balance_id, date, money, type) VALUES (?,?,?,?)");
            pstat.setInt(1, bo.getBalanceId());
            pstat.setString(2, bo.getDate().toString());
            pstat.setDouble(3, bo.getMoney());
            pstat.setString(4, bo.getMoney() >= 0 ? "credit" : "debit");

            pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new DAOexception("error while creating BalanceOperation" + e.getMessage());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + bo.getBalanceId()); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + bo.getBalanceId()); }
        }
        return generatedKey;
    }


    public static Map<Integer, BalanceOperation> readAll() throws DAOexception {
        Map<Integer, BalanceOperation> map = new HashMap<>();
        BalanceOperation op= null;
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            // "SELECT customer_id,customer_name,customer_card,card_points FROM [customer] JOIN loyalty_card ON [customer].customer_id = card_id ");

            pstat = conn.prepareStatement("SELECT * FROM balance_operation");
            rs = pstat.executeQuery();
            while (rs.next()) {
                op = rs.getString("type").equals("debit") ? new Debit() : new Credit();
                op.setBalanceId(rs.getInt("balance_id"));
                op.setDate(LocalDate.parse(rs.getString("date")));
                op.setMoney(rs.getDouble("money"));
                op.setType(rs.getString("type"));


                map.put(op.getBalanceId(),op);
            }
        }catch(SQLException e){
            throw new DAOexception("error while getting all customers");
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
        }
        return map;

    }

    public static void DeleteAll() throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        try{
            pstat = conn.prepareStatement("DELETE FROM balance_operation");
            pstat.executeUpdate();
        }catch(SQLException e){
            throw new DAOexception("error while deleting all Balance Operation ");
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all Balance Operation "); }
        }

    }






}
