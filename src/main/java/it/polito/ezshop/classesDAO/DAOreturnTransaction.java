package it.polito.ezshop.classesDAO;

import it.polito.ezshop.classes.Credit;
import it.polito.ezshop.classes.Debit;
import it.polito.ezshop.classes.ReturnTransaction;
import it.polito.ezshop.data.BalanceOperation;

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
            pstat.setDouble(3, r.getReturnedValue());
            pstat.setString(4, r.getProdId());
            pstat.setInt(5, r.getAmount());


            pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new DAOexception("error while creating BalanceOperation" + e.getMessage());
        }finally {
            try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + r.getBalanceId()); }
            try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation" + r.getBalanceId()); }
        }
        return generatedKey;
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
                r.setSaleID(rs.getInt("sale_id"));
                r.setReturnedValue(rs.getDouble("returned_value"));
                r.setProdId(rs.getString("returned_product"));
                r.setAmount(rs.getInt("returned_amount"));

                map.put(r.getBalanceId(),r);
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
