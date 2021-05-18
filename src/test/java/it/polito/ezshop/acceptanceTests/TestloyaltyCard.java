package it.polito.ezshop.acceptanceTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.classesDAO.DAOcustomer;
import it.polito.ezshop.classesDAO.DAOloyaltyCard;
import it.polito.ezshop.classesDAO.DBManager;


public class TestloyaltyCard {

	LoyaltyCard loyalcard1 = null;
	LoyaltyCard loyalcard2 = null;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	@Before
	public void populateDB(){
		DAOloyaltyCard.DeleteAll();
		loyalcard1 = new LoyaltyCard("4485370086510891",150);
		loyalcard1.setCardID(DAOloyaltyCard.Create(loyalcard1));
		loyalcard2 = new LoyaltyCard("5100293991053009",10);
		loyalcard2.setCardID(DAOloyaltyCard.Create(loyalcard2)) ;
	}
	@Test
	public void testCreateLoyaltyCard() {
		LoyaltyCard loyalcard = new LoyaltyCard("4716258050958645",10);
		
		String outcome = DAOloyaltyCard.Create(loyalcard);
		Assert.assertEquals("card_00003", outcome); 
	}
	
	
	@Test
	public void testReadLoyaltyCard() {
					
		LoyaltyCard lcard1 = DAOloyaltyCard.Read("card_00001");
  		Assert.assertTrue(lcard1.getPoints().equals(150));
	}
	
 	//Update loyalty card	
	// by ID
 	@Test
 	public void testUpdateLoyaltyCard() {
 		
 		loyalcard1.setPoints(200);
 		DAOloyaltyCard.Update(loyalcard1);
 		
 		
 		loyalcard1 = DAOloyaltyCard.Read("card_00001");
 		
 		Assert.assertTrue(loyalcard1.getPoints().equals(200));
 	}
 	
	//Delete by card id
 	@Test
 	public void testDeleteLoyaltyCard() {
 		DAOloyaltyCard.Delete(loyalcard1);
 		Assert.assertNull(DAOloyaltyCard.Read(loyalcard1.getCardID()));
 	}
 	
 	
 	//Delete All
	@Test
 	public void testDeleteAllLoyaltyCard() {
		DAOloyaltyCard.DeleteAll();
		Assert.assertNull(DAOloyaltyCard.Read(loyalcard1.getCardID()));
		Assert.assertNull(DAOloyaltyCard.Read(loyalcard2.getCardID()));
 	}
	
}
