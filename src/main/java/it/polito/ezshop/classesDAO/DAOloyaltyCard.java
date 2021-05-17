package it.polito.ezshop.classesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.data.Customer;

public class DAOloyaltyCard {

	
/*CREATE*/
	
	
	
	public static String Create(LoyaltyCard card) throws DAOexception {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		ResultSet rs = null;
		String generatedKey = "";
		try {
			pstat = conn.prepareStatement("INSERT INTO loyalty_card (card_points,customer) VALUES (?,?)");
			pstat.setInt(1,card.getPoints());
			pstat.setInt(2,card.getCustomer());
					
			pstat.executeUpdate();
			
			//this is the dummy id
			rs = pstat.getGeneratedKeys();
			if (rs.next()) {
			    generatedKey = rs.getString(1);
			}
		}catch(SQLException e){
			throw new DAOexception("product error " + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating loyalty card "+ e.getMessage());}
		}
			
		
		//get the correct id
		try {
			pstat = conn.prepareStatement("SELECT * FROM loyalty_card WHERE id=?");
			pstat.setString(1, generatedKey);
			rs = pstat.executeQuery();
			generatedKey = rs.getString("card_id");
		}catch(SQLException e){
			throw new DAOexception("product error " + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating loyalty card " + e.getMessage());}
		}
		return generatedKey;
	}
	
	
	
	
	
	
/*READ*/	
	
public static LoyaltyCard Read(String cardId) throws DAOexception{
		
		Connection conn = DBManager.getConnection();
		LoyaltyCard card = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			pstat = conn.prepareStatement("SELECT * FROM loyalty_card WHERE card_id=?");
			pstat.setString(1, cardId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				card = new LoyaltyCard();
				card.setCardID(rs.getString("card_id"));
				card.setCustomer(rs.getInt("customer"));
				card.setPoints(rs.getInt("card_points"));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading product" + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
		}
		
		return card;
	}

	public static LoyaltyCard ReadCustomer(Customer cust) throws DAOexception{
	
		Connection conn = DBManager.getConnection();
		LoyaltyCard card = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
	
		try {
			pstat = conn.prepareStatement("SELECT * FROM loyalty_card WHERE customer=?");
			pstat.setInt(1, cust.getId());
			rs = pstat.executeQuery();
		
			if (rs.next() == true) {
				card = new LoyaltyCard();
				card.setCardID(rs.getString("card_id"));
				card.setCustomer(rs.getInt("customer"));
				card.setPoints(rs.getInt("card_points"));
			}
			pstat.close();
		}catch(SQLException e){
			throw new DAOexception("error while reading product " + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
			try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
		}
		
		return card;
	}
	
	
	
	
/*UPDATE*/
	
	
	
	public static void Update(LoyaltyCard card) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		try{
			pstat = conn.prepareStatement("UPDATE loyalty_card SET card_points=?,customer=? WHERE card_id=?");
			pstat.setInt(1, card.getPoints());
			pstat.setInt(2, card.getCustomer());
			pstat.setString(3, card.getCardID());
			pstat.executeUpdate();
			
		}catch(SQLException e){
			throw new DAOexception("error while updating loyalty_card "+card.getCardID() + e.getMessage());
		}finally {
			try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card "+card.getCardID() + e.getMessage()); }
		}
		
		//get the related customer
		card  = DAOloyaltyCard.Read(card.getCardID());
		if(card.getCustomer() != 0) {
			try{
				pstat = conn.prepareStatement("UPDATE customer SET customer_points=? WHERE customer_id=?");
				pstat.setInt(1, card.getPoints());
				pstat.setInt(2, card.getCustomer());
				pstat.executeUpdate();
				
			}catch(SQLException e){
				throw new DAOexception("error while updating loyalty_card "+card.getCardID() + e.getMessage());
			}finally {
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card"+card.getCardID() + e.getMessage()); }
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card"+card.getCardID() + e.getMessage()); }
			}
			
			
		}
		
	}
	
	
	
	
	
	
	
/*DELETE*/
	
	
	public static void Delete(LoyaltyCard card) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		
		try{
			pstat1 = conn.prepareStatement("DELETE FROM loyalty_card WHERE card_id=?");
			pstat1.setString(1,card.getCardID());
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting Customer" + card.getCardID() + e.getMessage());
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting loyalty_card" + card.getCardID() + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat1 = null;
		
		try{
			pstat1 = conn.prepareStatement("DELETE FROM loyalty_card");
			pstat1.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all loyalty_card " + e.getMessage());
		}finally {
			try {pstat1.close();} catch (SQLException e) {throw new DAOexception("error while deleting all loyalty_cards " + e.getMessage()); }
		}
	}
}
