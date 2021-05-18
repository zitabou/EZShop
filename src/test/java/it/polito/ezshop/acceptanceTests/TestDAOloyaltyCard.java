package it.polito.ezshop.acceptanceTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.LoyaltyCard;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DAOloyaltyCard;
import it.polito.ezshop.classesDAO.DBManager;


public class TestDAOloyaltyCard {

	LoyaltyCard card1 = null;
	LoyaltyCard card2 = null;
	LoyaltyCard card3 = null;
	LoyaltyCard card4 = null;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	

	//The id doesn't matter it is calculated in the DB
	@Before
	public void populateDB() {
		card1 = new LoyaltyCard(1,1);
		card1.setCardID(DAOloyaltyCard.Create(card1));
		card2 = new LoyaltyCard(2,2);
		card2.setCardID(DAOloyaltyCard.Create(card2));
	}
	

//create product
// Test create	
	@Test
	public void testCreateProduct() {
		LoyaltyCard card3= new LoyaltyCard("0000000003",3,3);
		String outcome;
		
		outcome = DAOloyaltyCard.Create(card3);
		Assert.assertEquals("0000000003", outcome);
	}

// with negative points
	@Test
	public void testCreateLoyaltyaCardWithNegativePoints() {
		LoyaltyCard card3= new LoyaltyCard("0000000003",-3,3);
		
		try {DAOloyaltyCard.Create(card3); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}

// with negative customer ID
	@Test
	public void testCreateLoyaltyaCardWithNegativeCustomerId() {
		LoyaltyCard card3= new LoyaltyCard("0000000003",3,-3);
		LoyaltyCard card4= new LoyaltyCard("0000000004",4,0);
		
		try {DAOloyaltyCard.Create(card3); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
		try {DAOloyaltyCard.Create(card4); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}


// Read loyalty card
	@Test
	public void testReadLoyaltyCard() {
		LoyaltyCard card3= null;
		
		card3 = DAOloyaltyCard.Read(card1.getCardID());
		Assert.assertEquals("0000000001", card3.getCardID());
	}
	
	
// Read loyalty card not in DB
	@Test
	public void testReadMissingLoyaltyCard() {
		LoyaltyCard card3= null;
		
		card3 = DAOloyaltyCard.Read("0000000003");
		Assert.assertNull(card3);
	}
	
// Update loyalty card not in DB
	@Test
	public void testUpdateLoyaltyCard() {
		card1.setPoints(100);
		
		DAOloyaltyCard.Update(card1);
		
		LoyaltyCard card3 = DAOloyaltyCard.Read(card1.getCardID());
		Assert.assertEquals((Integer)100, card3.getPoints());
	}

	
// Update loyalty card not in DB
	@Test
	public void testUpdateMissingLoyaltyCard() {
		LoyaltyCard card3 = new LoyaltyCard("0000000003",3,3);
		card3.setPoints(100);
		
		try{DAOloyaltyCard.Update(card3);Assert.fail("this shouldn't be printed");}
		catch(DAOexception e) {e.getMessage();}
	}
	
// Delete loyalty card from DB
	@Test
	public void testDeleteLoyaltyCard() {
		
		LoyaltyCard card = DAOloyaltyCard.Read("0000000001");
		
		DAOloyaltyCard.Delete(card1);
		
		card = DAOloyaltyCard.Read("0000000001");
		Assert.assertNull(card);
	}

	
	
	

	/*
	
	
//Update product	
// by ID
	@Test
	public void testUpdateProd() {
		
		prod4.setProductDescription("description_update");
		DAOproductType.Update(prod4);
		
		prod4 = DAOproductType.read("123456789044");
		
		Assert.assertTrue(prod4.getProductDescription().equals("description_update"));
	}

// by missing ID
	@Test
	public void testUpdateProdMissing() {
		
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");
		try{DAOproductType.Update(prod5);Assert.fail("this shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}
	

//  BY CODE
	@Test
	public void testUpdateProdByCode() {
		
		prod4.setProductDescription("description_update");
		DAOproductType.UpdateByCode(prod4);
		prod4 = DAOproductType.read("123456789044");
		
		Assert.assertTrue(prod4.getProductDescription().equals("description_update"));
	}
	
	
// by missing CODE
	@Test
	public void testUpdateProdMissingCode() {
		
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");
		try{DAOproductType.UpdateByCode(prod5);Assert.fail("this shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}

	
	

// Delete product
// by ID
	@Test
	public void testDeleteProductById() {
		DAOproductType.Delete(prod4);
		
		Assert.assertEquals(3, DAOproductType.readAll().size());
		Assert.assertNull(DAOproductType.read(prod4.getId()));
	}

// by missing ID
	@Test
	public void testDeleteProductByMissingId() {
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");
		DAOproductType.Delete(prod5);
		Assert.assertEquals(4, DAOproductType.readAll().size());
	}
	
// BY CODE
	@Test
	public void testDeleteProductByCode() {
		
		DAOproductType.Delete(prod4.getBarCode());
		
		Assert.assertEquals(3, DAOproductType.readAll().size());
		Assert.assertNull(DAOproductType.read(prod4.getBarCode()));
	}

//By missing Code@Test
	
	public void testDeleteProductByMissingCode() {
		
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");
		DAOproductType.Delete(prod5.getBarCode());
		
		Assert.assertEquals(4, DAOproductType.readAll().size());
	}
	
*/	
	@After
	public void deleteReadProduct() {
		DAOloyaltyCard.DeleteAll();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}
}
