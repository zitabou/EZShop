package it.polito.ezshop.acceptanceTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.classesDAO.DAOloyaltyCard;
import it.polito.ezshop.classesDAO.DBManager;

public class TestloyaltyCard {

	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	@Before
	public void populateDB(){
		DAOloyaltyCard.DeleteAll();
	}
	@Test
	public void testCreateLoyaltyCard() {
		LoyaltyCard loyalcard = new LoyaltyCard("4485370086510891",150);
		
		String outcome = DAOloyaltyCard.Create(loyalcard);
		Assert.assertEquals("card_00001", outcome); 
	}
}
