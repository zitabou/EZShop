package it.polito.ezshop.IntegrationTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezReceiptEntry;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DAOsaleEntry;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.TicketEntry;

public class TestDAOsaleEntry {
	TicketEntry entry1 = null;	
	TicketEntry entry2 = null ;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		DAOsaleEntry.DeleteAll();
		TicketEntry entry1 = new ezReceiptEntry("barcode1", "productDescr1", 1, 1.0, 0.1);
		DAOsaleEntry.Create(1, entry1);
		TicketEntry entry2= new ezReceiptEntry("barcode2", "productDescr2", 2, 2.0, 0.2);
		DAOsaleEntry.Create(1, entry2);
		TicketEntry entry3 = new ezReceiptEntry("barcode3", "productDescr3", 3, 3.0, 0.3);
		DAOsaleEntry.Create(3, entry3);
	}
	
    @Test
    public void testCreateSaleEntry() {  	
    	TicketEntry entry4 = new ezReceiptEntry("barcode4", "productDescr4", 4, 4.0, 0.4);
    	
    	try{DAOsaleEntry.Create(4, entry4);
    	}catch(DAOexception e){
    		fail();
    	}
    }
    @Test
    public void testCreateSaleEntryNegativeAmount() {  	
    	TicketEntry entry4 = new ezReceiptEntry("barcode4", "productDescr4", -4, 4.0, 0.4);
    	
    	try{DAOsaleEntry.Create(4, entry4);fail();
    	}catch(DAOexception e){
    		e.getMessage();
    	}
    }
    @Test
    public void testCreateSaleEntryNegativePrice() {  	
    	TicketEntry entry4 = new ezReceiptEntry("barcode4", "productDescr4", 4, -4.0, 0.4);
    	
    	try{DAOsaleEntry.Create(4, entry4);fail();
    	}catch(DAOexception e){
    		e.getMessage();
    	}
    }
    @Test
    public void testCreateSaleEntryNegativeDiscount() {  	
    	TicketEntry entry4 = new ezReceiptEntry("barcode4", "productDescr4", 4, 4.0, -0.4);
    	
    	try{DAOsaleEntry.Create(4, entry4);fail();
    	}catch(DAOexception e){
    		e.getMessage();
    	}
    }

  	@Test
  	public void testReadSaleEntries() {
  		
  		List<TicketEntry> entries= DAOsaleEntry.Read(1);
  		Assert.assertEquals(2,entries.size());
  	}
  	
 	@Test
 	public void testDeleteFromSale() {
 		
 		try{ DAOsaleEntry.DeleteFromSale(1);
 		}catch(DAOexception e) {
 			fail();
 		}
 	}
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}

}
