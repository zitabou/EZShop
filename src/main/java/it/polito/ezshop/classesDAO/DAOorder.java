package it.polito.ezshop.classesDAO;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DAOorder {

    public static int Create(Order o) throws DAOexception {
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int generatedKey = 0;

        try {

            pstat = conn.prepareStatement("INSERT INTO orders (order_id, product_code, quantity, price_per_unit, status) VALUES (?,?,?,?,?)");

            pstat.setInt(1, o.getOrderId());
            pstat.setString(2, o.getProductCode());
            pstat.setInt(3, o.getQuantity());
            pstat.setDouble(4, o.getPricePerUnit());
            pstat.setString(5, o.getStatus());

            pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        }catch(SQLException e){
            throw new DAOexception("error while creating BalanceOperation " + e.getMessage());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation " + o.getOrderId()); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while creating BalanceOperation " + o.getOrderId()); }
        }
        return generatedKey;
    }

    public static Map<Integer, Order> readAll() throws DAOexception {
        Map<Integer, Order> map = new HashMap<>();
        Order o= null;
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            // "SELECT customer_id,customer_name,customer_card,card_points FROM [customer] JOIN loyalty_card ON [customer].customer_id = card_id ");

            pstat = conn.prepareStatement("SELECT * FROM orders");
            rs = pstat.executeQuery();
            while (rs.next()) {
                o= new ezOrder();
                o.setOrderId(rs.getInt("order_id"));
                o.setProductCode(rs.getString("product_code"));
                o.setQuantity(rs.getInt("quantity"));
                o.setPricePerUnit(rs.getDouble("price_per_unit"));
                o.setStatus(rs.getString("status"));


                map.put(o.getOrderId(),o);
            }
        }catch(SQLException e){
            throw new DAOexception("error while getting all orders ");
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while getting all order"); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while getting all orders"); }
        }
        return map;

    }

    public static Order Read(Integer ordedId) throws DAOexception{

        Connection conn = DBManager.getConnection();
        Order o= null;
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            pstat = conn.prepareStatement("SELECT * FROM orders WHERE ID=?");
            pstat.setInt(1, ordedId);
            rs = pstat.executeQuery();
            if (rs.next() == true) {

                o= new ezOrder();
                o.setOrderId(rs.getInt("order_id"));
                o.setProductCode(rs.getString("product_code"));
                o.setQuantity(rs.getInt("quantity"));
                o.setPricePerUnit(rs.getDouble("price_per_unit"));
                o.setStatus(rs.getString("status"));
            }
            pstat.close();
        }catch(SQLException e){
            throw new DAOexception("error while reading order " + ordedId);
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading order" + o.getOrderId()); }
        	if(rs != null)
        		try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading order" + o.getOrderId()); }
        }

        return o;
    }

    public static void Update(Order o) throws DAOexception{
        Connection conn = DBManager.getConnection();
        PreparedStatement pstat = null;
        int result =0;

        //update customer table with new card and same points
        try{
            pstat = conn.prepareStatement("UPDATE orders SET product_code=?,quantity=?, price_per_unit=?, status=? WHERE order_id=?");
            pstat.setString(1, o.getProductCode());
            pstat.setInt(2, o.getQuantity());
            pstat.setDouble(3, o.getPricePerUnit());
            pstat.setString(4, o.getStatus());
            pstat.setInt(5, o.getOrderId());
            
            result = pstat.executeUpdate();
            if(result == 0)
				throw new SQLException("[entry not found]");

        }catch(SQLException e){
            throw new DAOexception("[error while updating order] " + o.getOrderId());
        }finally {
        	if(pstat != null)
        		try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating order" + o.getOrderId()); }
        }

    }
}
