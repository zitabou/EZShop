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
            pstat.setString(4, bo.getType());

            pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new DAOexception("error while creating BalanceOperation" + e.getMessage());
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + bo.getBalanceId()); }
            try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + bo.getBalanceId()); }
        }
        return generatedKey;
    }



    public static BalanceOperation Read(Integer balanceId) throws DAOexception{

        Connection conn = DBManager.getConnection();
        Customer cust = new ezCustomer();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        BalanceOperation op = null;

        try {
            pstat = conn.prepareStatement("SELECT * FROM balance_operation WHERE ID=?");
            pstat.setInt(1, balanceId);
            rs = pstat.executeQuery();
            if (rs.next() == true) {
                op = rs.getString("type").equals("debit") ? new Debit() : new Credit();
                op.setBalanceId(rs.getInt("balance_id"));
                op.setMoney(rs.getDouble("money"));
                op.setType(rs.getString("type"));
            }
            pstat.close();
        }catch(SQLException e){
            throw new DAOexception("error while reading balance operation " + balanceId);
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading balance operation" + balanceId); }
            try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading balance operation" + balanceId); }
        }

        return op;
    }

/*
    public static void Update(Customer cust) throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;

        //get real card_id
        try{
            pstat = conn.prepareStatement("SELECT card_id FROM loyalty_card WHERE ID=?");
            pstat.setString(1, cust.getCustomerCard());
            rs = pstat.executeQuery();

            if (rs.next() == true) {// it should never happen since the update includes the creation of the card
                cust.setCustomerCard(rs.getString("card_id"));
            }
        }catch(SQLException e){
            throw new DAOexception("error while updating Customer"+cust.getCustomerCard());
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
            try {rs.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
        }


        //update customer table
        try{
            pstat = conn.prepareStatement("UPDATE customer SET customer_name=?,customer_card=?, customer_points=? WHERE customer_id=?");
            pstat.setString(1, cust.getCustomerName());
            pstat.setString(2, cust.getCustomerCard());
            pstat.setInt(3, cust.getPoints());
            pstat.setInt(4, cust.getId());
            pstat.executeUpdate();

        }catch(SQLException e){
            throw new DAOexception("error while updating Customer " + cust.getId());
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer" + cust.getId()); }
        }


        //update new loyalty card
        try{
            //reset the previous card. It is detached from the customer
            pstat = conn.prepareStatement("UPDATE loyalty_card SET customer=? WHERE customer=?");
            pstat.setInt(1, 0);
            pstat.setInt(2, cust.getId());
            pstat.executeUpdate();

            //attach new card to customer and update points
            pstat = conn.prepareStatement("UPDATE loyalty_card SET card_points=?,customer=? WHERE card_id=?");
            pstat.setInt(1, cust.getPoints());
            pstat.setInt(2, cust.getId());
            pstat.setString(3, cust.getCustomerCard());
            pstat.executeUpdate();

        }catch(SQLException e){
            throw new DAOexception("error while updating Customer"+cust.getCustomerCard());
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating Customer"+cust.getCustomerCard()); }
        }

    }
*/
    public static void Delete(Integer balance_id) throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat1 = null;
        try{
            pstat1 = conn.prepareStatement("DELETE FROM balance_operation WHERE balance_id=?");
            pstat1.setInt(1,balance_id);
            pstat1.executeUpdate();
        }catch(SQLException e){
            throw new DAOexception("error while deleting Balance Operation " + balance_id);
        }finally {
            try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting Balance Operation " + balance_id); }
        }

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
                op.setMoney(rs.getDouble("money"));
                op.setType(rs.getString("type"));

                map.put(op.getBalanceId(),op);
            }
        }catch(SQLException e){
            throw new DAOexception("error while getting all customers");
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
            try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all customers"); }
        }
        return map;

    }
}
