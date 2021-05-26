package it.polito.ezshop.IntegrationTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezReceiptEntry;
import it.polito.ezshop.classes.ezSaleTransaction;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DAOsaleTransaction;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class TestDAOsaleTransaction {
	SaleTransaction sale1 = null;	
	SaleTransaction sale2 = null ;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		DAOsaleTransaction.DeleteAll();
		sale1 = new ezSaleTransaction();
		sale1.setEntries(new ArrayList<TicketEntry>());
		sale1.getEntries().add(new ezReceiptEntry("barcode1", "productDescr1", 1, 1.0, 0.1));
		sale1.getEntries().add(new ezReceiptEntry("barcode2", "productDescr2", 1, 1.0, 0.1));
		sale1.setTicketNumber(DAOsaleTransaction.Create(sale1));
		
		sale2 = new ezSaleTransaction();
		sale2.setEntries(new ArrayList<TicketEntry>());
		sale2.getEntries().add(new ezReceiptEntry("barcode2", "productDescr2", 2, 2.0, 0.2));
		sale2.setTicketNumber(DAOsaleTransaction.Create(sale2));
				
	}
	
    @Test
    public void testCreateSaleTransaction() {  	
    	SaleTransaction sale3 = new ezSaleTransaction(0.0,0);
    	sale3.setEntries(new ArrayList<TicketEntry>());
		sale3.getEntries().add(new ezReceiptEntry("barcode3", "productDescr3", 3, 3.0, 0.3));
    	int outcome;	
		outcome = DAOsaleTransaction.Create(sale3);
		Assert.assertEquals(3, outcome);  	
    }
    
    @Test
    public void testCreateSaleTransactionNegativePrice() {  	
    	SaleTransaction sale3 = new ezSaleTransaction(-0.1, 1.0);
    	sale3.setEntries(new ArrayList<TicketEntry>());
		sale3.getEntries().add(new ezReceiptEntry("barcode3", "productDescr3", 3, 3.0, 0.3));
		Assert.assertThrows(DAOexception.class, () -> {

			DAOsaleTransaction.Create(sale3);

        });
    }
    
    @Test
    public void testCreateSaleTransactionNegativeDiscount() {  	
    	SaleTransaction sale3 = new ezSaleTransaction(0.1, -1.0);
    	sale3.setEntries(new ArrayList<TicketEntry>());
		sale3.getEntries().add(new ezReceiptEntry("barcode3", "productDescr3", 3, 3.0, 0.3));
		Assert.assertThrows(DAOexception.class, () -> {

			DAOsaleTransaction.Create(sale3);

        });
    }
    

  	@Test
  	public void testReadSaleTransaction() {
  		
  		SaleTransaction sale = DAOsaleTransaction.Read(1);
  		Assert.assertEquals((Integer)1,sale.getTicketNumber());
  	}
  	
 // ALL	
 	@Test
 	public void testReadAllSalesTransaction() {
 		
 		Map<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>(DAOsaleTransaction.ReadAll());
 		
 		Assert.assertEquals(2, sales.size());
 	}
 	
 	@Test
 	public void testDeleteSaleTransaction() {
 		DAOsaleTransaction.Delete(1);
 		Assert.assertEquals(1,DAOsaleTransaction.ReadAll().size());
 	}
 	
	@Test
 	public void testReadSaleTransactionEntries() {
		SaleTransaction sale = DAOsaleTransaction.Read(1);
  		Assert.assertEquals(0,sale.getEntries().size());  // sale DAOsaleTransaction does not create entries
 	}
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}

}
