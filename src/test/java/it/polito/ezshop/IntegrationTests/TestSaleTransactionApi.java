package it.polito.ezshop.IntegrationTests;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.classes.ezSaleTransaction;
import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DAOsaleTransaction;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;


public class TestSaleTransactionApi {
	SaleTransaction sale1 = null;	
	SaleTransaction sale2 = null;
	EZShopInterface ezShop;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() throws InvalidUsernameException, InvalidPasswordException {   //String customerName, Integer id, String customerCard, Integer points
		DAOsaleTransaction.DeleteAll();
		
		ezShop = new it.polito.ezshop.data.EZShop();
        //ezShop.login("admin","admin");
		ezShop.login("a","a");

		sale1 = new ezSaleTransaction(0.1, 1.0);
		sale1.setTicketNumber(DAOsaleTransaction.Create(sale1));
		
		sale2 = new ezSaleTransaction(0.2, 23.0);
		sale2.setTicketNumber(DAOsaleTransaction.Create(sale2));
		
	}
	
    @Test
    public void testStartSaleTransaction() throws UnauthorizedException {  	
    	Integer outcome;	
		outcome = ezShop.startSaleTransaction();
		Assert.assertEquals((Integer)3, outcome);  //two transactions already in the DB  	
    }
    
    @Test
    public void testStartUnauthorizedSaleTransaction(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(UnauthorizedException.class, ()-> {
			
			ezShop.startSaleTransaction();
		
		});
    }
    
    
    
    
    
    //bar12: 629104150024
    //bar13: 6291041500213
    //bar14: 62910415002134
    
    
    
    
    
    
    
    /*PRODUCT TO SALE*/
    
    
    
    @Test
    public void testAddProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 1, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	boolean outcome;
    	ezShop.startSaleTransaction();
		outcome = ezShop.addProductToSale(3, "629104150024", 1);
		Assert.assertEquals(true, outcome);  //two transactions already in the DB  	
    }
    
    @Test
    public void testAddMissingProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	boolean outcome;
    	ezShop.startSaleTransaction();
		outcome = ezShop.addProductToSale(3, "6291041500213", 1);
		Assert.assertEquals(false, outcome);  	
    }
    
    @Test
    public void testAddProductToInvalidSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		Assert.assertThrows(InvalidTransactionIdException.class, ()-> {
			
			ezShop.addProductToSale(0, "629104150024", 1);
		});
    }
    
    @Test
    public void testAddProductToOldSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
    	boolean outcome;
    	outcome = ezShop.addProductToSale(1, "629104150024", 1);
    	Assert.assertEquals(false, outcome);
    }
    
    @Test
    public void testAddInvalidProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		Assert.assertThrows(InvalidProductCodeException.class, ()-> {
			
			ezShop.addProductToSale(3, "6291041500218", 1);
		});
    }
    
    @Test
    public void testAddProductToSaleTransactionInvalidQuantity() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		Assert.assertThrows(InvalidQuantityException.class, ()-> {
			
			ezShop.addProductToSale(3, "629104150024", 11);
		});
    }
    
    @Test
    public void testAddProductToSaleTransactionInvalidNegativeQuantity() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		Assert.assertThrows(InvalidQuantityException.class, ()-> {
			
			ezShop.addProductToSale(3, "629104150024", -1);
		});
    }
    
    @Test
    public void testUnauthorizedAddProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	
    	ezShop.startSaleTransaction();
    	ezShop.logout();
		Assert.assertThrows(UnauthorizedException.class, ()-> {
			
			ezShop.addProductToSale(3, "629104150024", 1);
		});
    }
    

    
    
    @Test
    public void testDeleteProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 2);
		
		boolean outcome;
		outcome = ezShop.deleteProductFromSale(3, "629104150024", 1);
		Assert.assertEquals(true, outcome); 
    }
    
    @Test
    public void testDeleteMissingProductToSaleTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException{  	
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 2);
		
		boolean outcome;
		outcome = ezShop.deleteProductFromSale(3, "6291041500213", 1);
		Assert.assertEquals(false, outcome); 
    }
    
    
    
    
    
    
    
    
	
    
    
    
    
    
    
    
    /*APPLY DISCOUNT*/
    
    
    
    
    @Test
    public void testApplyDiscountRateToProduct() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		boolean outcome;
		outcome = ezShop.applyDiscountRateToProduct(3, "629104150024", 0.1);
		Assert.assertEquals(true, outcome);
    }
    
    @Test
    public void testApplyDiscountRateToProductOldTransaction() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		boolean outcome;
		outcome = ezShop.applyDiscountRateToProduct(1, "629104150024", 0.1);
		Assert.assertEquals(false, outcome);
    }
    
    @Test
    public void testApplyInvalidDiscountRateToProduct() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		Assert.assertThrows(InvalidDiscountRateException.class, ()-> {
			
			ezShop.applyDiscountRateToProduct(3, "629104150024", 1.1);
		});
    }
    
    @Test
    public void testApplyDiscountRateToSale() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
		DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		boolean outcome;
		outcome = ezShop.applyDiscountRateToSale(3, 0.1);
		Assert.assertEquals(true, outcome);
    }
    
    @Test
    public void testApplyDiscountRateToInvalidSale() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
		DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		Assert.assertThrows(InvalidTransactionIdException.class ,()-> {
			
			ezShop.applyDiscountRateToSale(-1, 0.1);
		});
    }
    
    @Test
    public void testApplyDiscountRateToOldSale() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
		DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		boolean outcome;
		outcome = ezShop.applyDiscountRateToSale(1, 0.1);
		Assert.assertEquals(false, outcome);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*END SALE TRANSACTION*/
    
    
    
    
    
    @Test
    public void testEndSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		
		boolean outcome;
		outcome = ezShop.endSaleTransaction(3);
		Assert.assertEquals(true, outcome);
    }
    
    @Test
    public void testEndWrongSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
    	ezShop.startSaleTransaction();
		ezShop.addProductToSale(3, "629104150024", 1);
		
		
		boolean outcome;
		outcome = ezShop.endSaleTransaction(1);
		Assert.assertEquals(false, outcome);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*DELETE sale Transaction*/
    
    
    @Test
    public void testDeleteSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidQuantityException {
    	DAOproductType.DeleteAll();
    	ProductType prod = new ezProductType("product_1", "629104150024", 1.0, 10, "note_1", "1-1-1");
    	DAOproductType.Create(prod);
    	
		
		boolean outcome;
		outcome = ezShop.deleteSaleTransaction(2);
		Assert.assertEquals(true, outcome);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*GET SALE TRANSACTION*/
    

    @Test
    public void testGetSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException{
    	
		SaleTransaction sale = null;
		
		sale = ezShop.getSaleTransaction(1);
		Assert.assertNotNull(sale);
		Assert.assertEquals((Integer)1, sale.getTicketNumber());
    }
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
    
    
    
    
    
    /*COMPUTE POINTS FOR SALE*/
    
    
    
    
    
    
    
    

    @Test
    public void testComputePointsForSale() throws InvalidTransactionIdException, UnauthorizedException{
    	
		int outcome;
		outcome = ezShop.computePointsForSale(2);
		Assert.assertEquals(2, outcome);
    }
    
    
    
    
    
    
    
    
    
    
    
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}
}
