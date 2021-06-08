package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classes.Product;
import it.polito.ezshop.classesDAO.*;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testRFIDApi {

    //issueOrder, payOrder, payOrderFor, recordOrderArrival, getAllOrders

    EZShopInterface ezShop;
    Integer saleId = 0;
    //The id doesn't matter it is calculated in the DB
    
    @Before
    public void populateDB() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPasswordException, InvalidUsernameException {
        
    	
    	ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.login("admin","admin");
    	DAOcustomer.DeleteAll();
    	DAOloyaltyCard.DeleteAll();
    	DAOproductType.DeleteAll();
    	DAOproduct.DeleteAll();
    	DAOlocation.DeleteAll();
    	DAOsaleTransaction.DeleteAll();
    	DAOsaleEntry.DeleteAll();
    	DAObalanceOperation.DeleteAll();
    	DAOreturnTransaction.DeleteAll();
    	DAOreturnEntry.DeleteAll();
    	DAOorder.DeleteAll();
    	DAOcc.DeleteAll();
        
    	
        ezShop.recordBalanceUpdate(1000);

        if(ezShop.getAllProductTypes().size() == 0 || DAOproductType.read("629104150024") == null){
            Integer prodId = ezShop.createProductType("prova", "629104150024", 1, "");
            ezShop.updatePosition(prodId, "4-A-4");
            ezShop.updateQuantity(prodId, 4000);
        }else if (DAOproductType.read("629104150024").getQuantity() < 100){
            ProductType r = DAOproductType.read("629104150024");
            r.setQuantity(4000);
            DAOproductType.Update(r);
        }
        if(DAOproductType.read("629104150024").getLocation() == null || DAOproductType.read("629104150024").getLocation() != "4-A-4"){
            Integer prodId = DAOproductType.read("629104150024").getId();
            ezShop.updatePosition(prodId, "4-A-4");
        }
        if( !DAOproduct.readByCode("629104150024").contains("0000001000") ){
            Product p = new Product();
            p.setRFID("0000001000");
            p.setBarCode("629104150024");
        }

        saleId = ezShop.startSaleTransaction();
        ProductType prod = DAOproductType.read("629104150024");
        ezShop.addProductToSale(saleId, prod.getBarCode(), 5);
        ezShop.endSaleTransaction(saleId);

    }

    @Test
    public void testOkCases() throws UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidRFIDException, InvalidLocationException, InvalidOrderIdException, InvalidPasswordException, InvalidUsernameException {
        ezShop.login("a","a");
        int qty = DAOproductType.read("629104150024").getQuantity();
        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ezShop.recordOrderArrivalRFID(orderId, "0000002000");
        Assert.assertEquals(qty + 3, (int) DAOproductType.read("629104150024").getQuantity());
        Assert.assertNotNull(DAOproduct.readByRFID("0000002002"));

        //order is in COMPLETED state, no effect
        ezShop.recordOrderArrivalRFID(orderId, "0000002000");
        Assert.assertEquals(qty + 3, (int) DAOproductType.read("629104150024").getQuantity());



    }
  
    

    
    @Test
    public void addProductToSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidRFIDException, InvalidTransactionIdException {
    	//ezShop.login("a","a");
        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ezShop.recordOrderArrivalRFID(orderId, "0000002000");
        
        saleId = ezShop.startSaleTransaction();
        Assert.assertTrue(ezShop.addProductToSaleRFID(saleId, "0000002000"));
        ezShop.endSaleTransaction(saleId);

    }
    

    @Test
    public void testKOCases() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        ezShop.logout();

        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "0000002000");
        });

        ezShop.login("admin","admin");
        Assert.assertThrows(InvalidOrderIdException.class, () -> {
            ezShop.recordOrderArrivalRFID(0, "0000002000");
        });

        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "2000");
        });

        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ProductType p = DAOproductType.read("629104150024");
        p.setLocation(null);
        DAOproductType.Update(p);
        Assert.assertThrows(InvalidLocationException.class, () -> {
            ezShop.recordOrderArrivalRFID(orderId, "0000002000");
        });



    }







    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
