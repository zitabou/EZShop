package it.polito.ezshop.acceptanceTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.classesDAO.DAOcustomer;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Customer;


public class TestDAOcustomer {

	Customer cus1 = null;	
	Customer cus2 = null ;
	Customer cus3 = null ;
	Customer cus4 = null ;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		 DAOcustomer.DeleteAll();
		cus1 = new ezCustomer("testcus1", 1, "testcard1", 1);
		cus1.setId(DAOcustomer.Create(cus1));
		cus2 = new ezCustomer("testcus2", 2, "testcard2", 2);
		cus2.setId(DAOcustomer.Create(cus2));
		cus3 = new ezCustomer("testcus3", 3, "testcard3", 3);
		cus3.setId(DAOcustomer.Create(cus3));
		cus4 = new ezCustomer("testcus4", 4, "testcard4", 4);
		cus4.setId(DAOcustomer.Create(cus4));
				
	}
	
    @Test
    public void testCreateCustomer() {  	
    	Customer cus5 = new ezCustomer("testcus5", 5, "testcard5", 5);
    	int outcome;	
		outcome = DAOcustomer.Create(cus5);
		Assert.assertEquals(5, outcome);  	
    }
    

  	@Test
  	public void testReadCustomerById() {
  		
  		Customer cus = DAOcustomer.Read(3);
  		Assert.assertTrue(cus.getCustomerName().equals("testcus3"));
  	}
    
  	
  	@Test
  	public void testReadCustomerByWrongId() {
  		
  		Customer cus = DAOcustomer.Read(10);
  		Assert.assertNull(cus);
  	}
  	
 // ALL	
 	@Test
 	public void testReadAllCustomer() {
 		
 		List<Customer> custs = new ArrayList<Customer>(DAOcustomer.readAll().values());
 		
 		Assert.assertEquals(4, custs.size());
 		Assert.assertTrue(custs.get(0).getCustomerName().equals("testcus1"));
 		Assert.assertTrue(custs.get(1).getCustomerName().equals("testcus2"));
 	}
 	
 	//Update customer	
 // by ID
 	@Test
 	public void testUpdateCustomer() {
 		
 		cus4.setCustomerName("testcus4_Updated");
 		DAOcustomer.Update(cus4);
 		
 		
 		cus4 = DAOcustomer.Read(4);
 		
 		Assert.assertTrue(cus4.getCustomerName().equals("testcus4_Updated"));
 	}
 	
 	@Test
 	public void testDeleteCustomer() {
 		DAOcustomer.Delete(4);
 		Assert.assertEquals(3,DAOcustomer.readAll().size());
 		Assert.assertNull(DAOcustomer.Read(cus4.getId()));
 	}
 	
	@Test
 	public void testDeleteAllCustomer() {
 		DAOcustomer.DeleteAll();
 		Assert.assertEquals(0,DAOcustomer.readAll().size());
 	}


}
