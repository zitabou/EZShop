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
			throw new DAOexception("loyalty card error " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while creating loyalty card "+ e.getMessage());}
		}
			
		
		//get the correct id
		try {
			pstat = conn.prepareStatement("SELECT * FROM loyalty_card WHERE id=?");
			pstat.setString(1, generatedKey);
			rs = pstat.executeQuery();
			generatedKey = rs.getString("card_id");
		}catch(SQLException e){
			throw new DAOexception("loyalty card error" + e.getMessage());
		}finally {
			if(pstat != null)
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
			pstat = conn.prepareStatement("SELECT card_id, customer, card_points FROM loyalty_card WHERE card_id=?");
			pstat.setString(1, cardId);
			rs = pstat.executeQuery();
			
			if (rs.next() == true) {
				card = new LoyaltyCard();
				card.setCardID(rs.getString("card_id"));
				card.setCustomer(rs.getInt("customer"));
				card.setPoints(rs.getInt("card_points"));
			}
			
		}catch(SQLException e){
			throw new DAOexception("error while reading loyalty card" + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
			if(rs != null)
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

		}catch(SQLException e){
			throw new DAOexception("error while reading customer from loyalty card " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
			if(rs != null)
				try {rs.close();} catch (SQLException e) {throw new DAOexception("error while reading loyalty_card " + e.getMessage()); }
		}
		return card;
	}
	
	
	
	
/*UPDATE*/
	
	
	
	public static void Update(LoyaltyCard card) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		int result = 0;
		try{
			pstat = conn.prepareStatement("UPDATE loyalty_card SET card_points=?,customer=? WHERE card_id=?");
			pstat.setInt(1, card.getPoints());
			pstat.setInt(2, card.getCustomer());
			pstat.setString(3, card.getCardID());
			result = pstat.executeUpdate();
			if(result == 0)
				throw new SQLException("entry not found");
			
		}catch(SQLException e){
			throw new DAOexception("error while updating loyalty_card "+card.getCardID() + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card "+ card.getCardID() + e.getMessage()); }
		}
		
		//get the related customer
		card  = DAOloyaltyCard.Read(card.getCardID());
		if(card.getCustomer() != 0) {
			try{
				pstat = conn.prepareStatement("UPDATE customer SET customer_points=? WHERE customer_id=?");
				pstat.setInt(1, card.getPoints());
				pstat.setInt(2, card.getCustomer());
				result = pstat.executeUpdate();
				if(result == 0)
					throw new SQLException("entry not found");
				
			}catch(SQLException e){
				throw new DAOexception("error while updating loyalty_card "+card.getCardID() + e.getMessage());
			}finally {
				if(pstat != null)
					try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while updating loyalty_card"+card.getCardID() + e.getMessage()); }
			}
		}
	}
	
	
	
	
	
	
	
/*DELETE*/
	
	
	public static void Delete(LoyaltyCard card) throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		try{
			pstat = conn.prepareStatement("DELETE FROM loyalty_card WHERE card_id=?");
			pstat.setString(1,card.getCardID());
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting loyalty card" + card.getCardID() + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting loyalty_card" + card.getCardID() + e.getMessage()); }
		}
	}
	
	public static void DeleteAll() throws DAOexception{
		Connection conn = DBManager.getConnection();
		PreparedStatement pstat = null;
		
		try{
			pstat = conn.prepareStatement("DELETE FROM loyalty_card");
			pstat.executeUpdate();
		}catch(SQLException e){
			throw new DAOexception("error while deleting all loyalty_card " + e.getMessage());
		}finally {
			if(pstat != null)
				try {pstat.close();} catch (SQLException e) {throw new DAOexception("error while deleting all loyalty_cards " + e.getMessage()); }
		}
	}
}
