package it.polito.ezshop.IntegrationTests;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.classesDAO.DAOlocation;
import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;


//bar11: 62910415008
//--
//bar12: 629104150024
//bar13: 6291041500213
//bar14: 62910415002134
//--
//bar15: 629104150021348



public class TestProductTypeAPI {

	ProductType prod1 = null;	
	ProductType prod2 = null;
	EZShopInterface ezShop;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() throws InvalidUsernameException, InvalidPasswordException {   //String customerName, Integer id, String customerCard, Integer points
		DAOproductType.DeleteAll();
		
		ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.login("admin","admin");

		prod1 = new ezProductType("prod1", "629104150024", 1.0, 10, "note1", "1-1-1");
		prod1.setId(DAOproductType.Create(prod1));
		
		prod2 = new ezProductType("prod2", "6291041500213", 2.0, 20, "note2", "2-2-2");
		prod2.setId(DAOproductType.Create(prod2));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    @Test
    public void testCreateProductType() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {  	
    	Integer outcome;	
		outcome = ezShop.createProductType("prod3", "62910415002134", 3.0,"note3");
		Assert.assertEquals((Integer)3, outcome);  //two products already in the DB  	
    }
    
    @Test
    public void testCreateInvalidDescriptionProductTypeEmpty(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType("", "62910415002134", 3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateInvalidDescriptionProductTypeNull(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType(null, "62910415002134", 3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateInvalidProductTypeShort(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType(null, "62910415008", 3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateInvalidProductTypeLong(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType(null, "629104150021348", 3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateInvalidProductTypeWrong(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType(null, "62910415002130", 3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateProductTypeInvalidPricePerUnit(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(InvalidProductDescriptionException.class, ()-> {
			
			ezShop.createProductType(null, "62910415002134", -3.0,"note3");
		
		});
    }
    
    @Test
    public void testCreateUnauthorizedProductType(){  	
    	ezShop.logout();
    	
		Assert.assertThrows(UnauthorizedException.class, ()-> {
			
			ezShop.createProductType("prod3", "62910415002134", 3.0,"note3");
		
		});
    }    
    
    @Test
    public void testCreateProductTypeAlreadyInDB() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {  	
    	ProductType prod3 = new ezProductType("prod3", "62910415002134", 3.0, 30, "note3", "3-3-3");
		prod3.setId(DAOproductType.Create(prod3));
    	
    	Integer outcome;	
		//outcome = ezShop.createProductType("prod3", "62910415002134", 3.0,"note3");
		//Assert.assertEquals((Integer)(-1), outcome);	
    }
    
    
    
    
    
    
    
    
    
    @Test
    public void testUpdatePosition() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	boolean outcome;	
		outcome = ezShop.updatePosition(1, "11-11-11");
		Assert.assertEquals(true, outcome);  //two transactions already in the DB 
		Assert.assertEquals("11-11-11", DAOproductType.read(1).getLocation());
		Assert.assertEquals("11-11-11", DAOlocation.Read(1).getPosition());
    }
    
    @Test
    public void testUpdatePositionInvalid_1() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

    	Assert.assertThrows(InvalidLocationException.class, ()-> {
			
			ezShop.updatePosition(1, "11/11/11");
		
		});
    }
    
    @Test
    public void testUpdatePositionInvalid_2() throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

    	Assert.assertThrows(InvalidLocationException.class, ()-> {
			
			ezShop.updatePosition(1, "11-11-a1");
		
		});
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testUpdateProductType() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	boolean outcome;	
		outcome = ezShop.updateProduct(2,"prod3", "62910415002134", 3.0,"note3");
		Assert.assertEquals(true, outcome);  //two transactions already in the DB 
		Assert.assertEquals("prod3", DAOproductType.read(2).getProductDescription());
    }
    
    @Test
    public void testUpdateMissingProductType() throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	boolean outcome;	
		outcome = ezShop.updateProduct(3,"prod3", "62910415002134", 3.0,"note3");
		Assert.assertEquals(false, outcome);  //two products already in the DB 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testDeleteProductType() throws InvalidProductIdException, UnauthorizedException {
    	boolean outcome;	
		outcome = ezShop.deleteProductType(1);
		Assert.assertEquals(true, outcome);  //two products already in the DB 
    }
    
    @Test
    public void testDeleteProductTypeWithPosition() throws InvalidProductIdException, UnauthorizedException, InvalidLocationException {
    	boolean outcome;
    	ezShop.updatePosition(1, "11-11-11");
		outcome = ezShop.deleteProductType(1);
		Assert.assertEquals(true, outcome);
		Assert.assertEquals(1, DAOproductType.readAll().size());
		Assert.assertNull(DAOproductType.read(1));
		Assert.assertNull(DAOlocation.Read(1));
    }
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testGetAllProductTypes() throws UnauthorizedException {
    	List<ProductType> prods = null;
    	
		prods = ezShop.getAllProductTypes();
		Assert.assertNotNull(prods);  //two products already in the DB
		Assert.assertEquals(2,prods.size());
    }
    
    @Test
    public void testGetAllProductTypesEmpty() throws UnauthorizedException, InvalidProductIdException {
    	List<ProductType> prods = null;
    	
    	ezShop.deleteProductType(1);
    	ezShop.deleteProductType(2);
    	
		prods = ezShop.getAllProductTypes();
		Assert.assertNotNull(prods);
		Assert.assertEquals(0,prods.size());
    }
    
    @Test
    public void testGetProductTypeByBarCode() throws InvalidProductCodeException, UnauthorizedException {
    	ProductType prod = null;
    	
		prod = ezShop.getProductTypeByBarCode("629104150024");
		Assert.assertNotNull(prod);  //two products already in the DB
		Assert.assertEquals("629104150024",prod.getBarCode());
    }
    
    @Test
    public void testGetProductTypeByBarCodeNotInDB() throws InvalidProductCodeException, UnauthorizedException {
    	ProductType prod = null;
    	
		prod = ezShop.getProductTypeByBarCode("62910415002134");
		Assert.assertNull(prod);
    }
    
    @Test
    public void testGetProductTypeByDescription() throws InvalidProductCodeException, UnauthorizedException {
    	List<ProductType> prods = null;
    	
    	ProductType prod3 = new ezProductType("prod1", "62910415002134", 3.0, 30, "note3", "3-3-3");
		prod3.setId(DAOproductType.Create(prod3));
    	
		prods = ezShop.getProductTypesByDescription("prod1");
		Assert.assertNotNull(prods);
		Assert.assertEquals(2,prods.size());
    }
    
    @Test
    public void testGetProductTypeByDescriptionNotInDB() throws InvalidProductCodeException, UnauthorizedException {
    	List<ProductType> prods = null;
    	
		prods = ezShop.getProductTypesByDescription("prod3");
		Assert.assertNotNull(prods);
		Assert.assertEquals(0,prods.size());
    }
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testUpdateQuantityPositive() throws InvalidProductIdException, UnauthorizedException, InvalidLocationException {
    	boolean outcome;
    	
    	ezShop.updatePosition(1, "1-1-1");
    	
		outcome = ezShop.updateQuantity(1, 3);
		Assert.assertEquals(true,outcome);
    }
    
    
    @Test
    public void testUpdateQuantityNegative() throws InvalidProductIdException, UnauthorizedException, InvalidLocationException {
    	boolean outcome;
    	
    	ezShop.updatePosition(1, "1-1-1");
    	
		outcome = ezShop.updateQuantity(1, -3);
		Assert.assertEquals(true,outcome);
    }
    
    @Test
    public void testUpdateQuantityPositiveNoPosition() throws InvalidProductIdException, UnauthorizedException {
    	boolean outcome;
    	
		outcome = ezShop.updateQuantity(1, 3);
		Assert.assertEquals(false,outcome);
    }
    
    @Test
    public void testUpdateQuantityVeryNegative() throws InvalidProductIdException, UnauthorizedException, InvalidLocationException {
    	boolean outcome;
    	
    	ezShop.updatePosition(1, "1-1-1");
    	
		outcome = ezShop.updateQuantity(1, -11);
		Assert.assertEquals(false,outcome);
    }
    
    @Test
    public void testUpdateQuantityNotInDB() throws InvalidProductIdException, UnauthorizedException, InvalidLocationException {
    	boolean outcome;
    	
    	ezShop.updatePosition(1, "1-1-1");
    	
		outcome = ezShop.updateQuantity(3, 1);
		Assert.assertEquals(false,outcome);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}

}
