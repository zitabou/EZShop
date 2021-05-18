package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.classesDAO.DAOcustomer;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Customer;

public class TestDAOcustomer {

	Customer cust1 = null;
	Customer cust2 = null;

	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	

	//The id doesn't matter it is calculated in the DB
	@Before
	public void populateDB() {
		cust1 = new ezCustomer("customer1",1,"0000000001",1);
		cust1.setId(DAOcustomer.Create(cust1));
		cust2 = new ezCustomer("customer2",2,"0000000002",2);
		cust2.setId(DAOcustomer.Create(cust2));
	}


// Test create	
	@Test
	public void testCreateCustomer() {
		Customer cust3= new ezCustomer("customer3",3,"0000000003",3);
		int outcome;
		
		outcome = DAOcustomer.Create(cust3);
		Assert.assertEquals(3, outcome);
	}

// create with negative points
	@Test
	public void testCreateCustomerWithNegativePoints() {
		Customer cust3= new ezCustomer("customer3",3,"0000000003",-3);
		
		try {DAOcustomer.Create(cust3); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}

// read
	@Test
	public void testReadCustomer() {
		
		Customer cust3 = DAOcustomer.Read(1);
		Assert.assertEquals((Integer)1, cust3.getId());
	}
	
// read with negative customer ID
	@Test
	public void testReadCustomerNotInDB() {
		
		Customer cust3 = DAOcustomer.Read(-1);
		Customer cust4 = DAOcustomer.Read(10);
		Assert.assertNull(cust3);
		Assert.assertNull(cust4);
	}
	
	
	
	
	@After
	public void deleteReadProduct() {
		DAOcustomer.DeleteAll();
	}

	
	
	
	
	
	
	
	
	
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}
}
