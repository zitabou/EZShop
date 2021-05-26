package it.polito.ezshop.acceptanceTests;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.Location;
import it.polito.ezshop.classesDAO.DAOlocation;
import it.polito.ezshop.classesDAO.DBManager;

public class TestDAOlocation {

	Location loc1 = null;	
	Location loc2 = null ;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		DAOlocation.DeleteAll();
		
		loc1 = new Location(1, 1, 1, 11);
		DAOlocation.Create(loc1);
		loc2 = new Location(2, 2, 2, 22);
		DAOlocation.Create(loc2);
	}
	
    @Test
    public void testCreateLocation() {  	
    	Location loc3 = new Location(3, 3, 3, 33);
    	Integer outcome;	
		outcome = DAOlocation.Create(loc3);
		Assert.assertEquals((Integer)3, outcome);  	
    }
    

  	@Test
  	public void testReadLocation() {
  		
  		Location loc = DAOlocation.Read(22);
  		Assert.assertNotNull(loc);
  		Assert.assertEquals("2-2-2", loc.getPosition());
  	}
  	
  	@Test
  	public void testReadMissingLocation() {
  		
  		Location loc = DAOlocation.Read(4);
  		Assert.assertNull(loc);
  	}
  	
  	@Test
  	public void testReadLocationByPosition() {
  		
  		Location loc = DAOlocation.Read("1-1-1");
  		Assert.assertNotNull(loc);
  		Assert.assertEquals((Integer)11, loc.getProduct());
  	}
  	
  	@Test
  	public void testReadMissingLocationByPosition_1() {
  		
  		Location loc = DAOlocation.Read("4-4-4");
  		Assert.assertNull(loc);
  	}
  	
  	@Test
  	public void testReadMissingLocationByPosition_2() {
  		
  		Location loc = DAOlocation.Read("4/4-a");
  		Assert.assertNull(loc);
  	}
    
  	
  	@Test
  	public void testUpdateLocation() {
  		
  		loc1.setProduct(5);
  		
  		DAOlocation.Update(loc1);
  		
  		Assert.assertEquals((Integer)5, DAOlocation.Read("1-1-1").getProduct());
  	}
 	
 	
 	@Test
 	public void testDeleteLocation() {
 		DAOlocation.Delete(loc1);
 		
 		Assert.assertNull(DAOlocation.Read("1-1-1"));
 	}
 	
 	
 	
 	
 	
 	
 	
 	
 	
	
	
	
	
	
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}

}
