package it.polito.ezshop.acceptanceTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.ProductType;


public class TestDAOproductType {

	ProductType prod1 = null;
	ProductType prod2 = null;
	ProductType prod3 = null;
	ProductType prod4 = null;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	
	

	//The id doesn't matter it is calculated in the DB
	@Before
	public void populateDB() {
		prod1 = new ezProductType(1, "testProd1", "123456789011", 1.0, 1, "testNote1", "1-1-1");
		prod1.setId(DAOproductType.Create(prod1));
		prod2 = new ezProductType(2, "testProd2", "123456789022", 2.0, 2, "testNote2", "2-2-2");
		prod2.setId(DAOproductType.Create(prod2));
		prod3 = new ezProductType(3, "testProd3", "123456789033", 3.0, 3, "testNote3", "3-3-3");
		prod3.setId(DAOproductType.Create(prod3));
		prod4 = new ezProductType(4, "testProd1", "123456789044", 4.0, 4, "testNote4", "4-4-4");
		prod4.setId(DAOproductType.Create(prod4));
	}
	

//create product
// Test create	
	@Test
	public void testCreateProduct() {
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");
		int outcome;
		
		outcome = DAOproductType.Create(prod5);
		Assert.assertEquals(5, outcome);
	}

// with negative quantity
	@Test
	public void testCreateProductWithNegativeQuantity() {
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, -5, "testNote5", "5-5-5");
		
		try {DAOproductType.Create(prod5); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}
// with duplicate barcode
	@Test
	public void testCreateProductWithDuplicateBarCode() {
		ProductType prod5 = new ezProductType(0, "testProd5", "123456789011", 5.0, 5, "testNote5", "5-5-5");
		
		try {DAOproductType.Create(prod5); Assert.fail("This shouldn't be printed");}
		catch(DAOexception e) {System.out.println(e.getMessage());}
	}

	
// Read product
//ONE BARCODE
	@Test
	public void testReadProductBarCode() {
		
		ProductType prod = DAOproductType.read("123456789011");
		Assert.assertTrue(prod.getProductDescription().equals("testProd1"));
	}

// ONE ID
	@Test
	public void testReadProductId() {
		
		ProductType prod = DAOproductType.read(1);
		Assert.assertTrue(prod.getProductDescription().equals("testProd1"));
	}
// ONE missingProduct ID
	@Test
	public void testReadMissingProductId() {
		
		ProductType prod = DAOproductType.read(10);
		Assert.assertNull(prod);
	}

// ALL	
	@Test
	public void testReadAllProducts() {
		
		List<ProductType> prods = new ArrayList<ProductType>(DAOproductType.readAll().values());
		
		Assert.assertEquals(4, prods.size());
		Assert.assertTrue(prods.get(0).getBarCode().equals("123456789011"));
		Assert.assertTrue(prods.get(1).getBarCode().equals("123456789022"));
	}
	
//ALL missing
	@Test
	public void testReadAllProductsMissing() {
		
		DAOproductType.DeleteAll();
		List<ProductType> prods = new ArrayList<ProductType>(DAOproductType.readAll().values());
		
		Assert.assertEquals(0, prods.size());
	}
	
// ALL BY DESCRIPTION	
	@Test
	public void testReadAllByDescriptionMissing() {
		List<ProductType> prods = new ArrayList<ProductType>(DAOproductType.readAll("testProd10").values());
		
		Assert.assertEquals(0, prods.size());
	}

// ALL BY DESCRIPTION	missing
	@Test
	public void testReadAllByDescription() {
		
		List<ProductType> prods = new ArrayList<ProductType>(DAOproductType.readAll("testProd1").values());
		
		Assert.assertEquals(2, prods.size());
		Assert.assertTrue(prods.get(0).getBarCode().equals("123456789011"));
		Assert.assertTrue(prods.get(1).getBarCode().equals("123456789044"));
	}	
	
	
	
	
	
	
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
	
	
	
// ALL
	@Test
	public void testDeleteAll() {
		
		DAOproductType.DeleteAll();
		Assert.assertEquals(0, DAOproductType.readAll().size());
	}
	
	
	@After
	public void deleteReadProduct() {
		DAOproductType.DeleteAll();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}
}
