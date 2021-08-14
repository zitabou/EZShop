package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classes.Product;
import it.polito.ezshop.classesDAO.*;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

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
        if( !DAOproduct.readByCode("629104150024").contains("1000") ){
            Product p = new Product();
            p.setRFID("1000");
            p.setBarCode("629104150024");
        }




    }

    @Test
    public void testOkCases() throws UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidRFIDException, InvalidLocationException, InvalidOrderIdException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException {
        ezShop.login("a","a");
        int qty = DAOproductType.read("629104150024").getQuantity();
        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ezShop.recordOrderArrivalRFID(orderId, "000000002000");
        Assert.assertEquals(qty + 3, (int) DAOproductType.read("629104150024").getQuantity());
        Assert.assertNotNull(DAOproduct.readByRFID("02002"));

        //order is in COMPLETED state, no effect
        ezShop.recordOrderArrivalRFID(orderId, "000000002000");
        Assert.assertEquals(qty + 3, (int) DAOproductType.read("629104150024").getQuantity());

        saleId = ezShop.startSaleTransaction();
        ezShop.addProductToSaleRFID(saleId, "000000002000");
        ezShop.addProductToSaleRFID(saleId, "000000002001");
        ezShop.endSaleTransaction(saleId);

        List<TicketEntry> items = DAOsaleEntry.Read(saleId);
        int quantity = items.get(0).getAmount();
        Assert.assertEquals(2, quantity);

        Integer retId = ezShop.startReturnTransaction(saleId);
        ezShop.returnProductRFID(retId, "000000002000");
        ezShop.endReturnTransaction(retId, true);

        items = DAOsaleEntry.Read(saleId);
        quantity = items.get(0).getAmount();
        Assert.assertEquals(1, quantity);

    }
  
    

    
    @Test
    public void ProductToSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidRFIDException, InvalidTransactionIdException {
        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ezShop.recordOrderArrivalRFID(orderId, "000000002000");
        
        saleId = ezShop.startSaleTransaction();
        Assert.assertTrue(ezShop.addProductToSaleRFID(saleId, "000000002000"));
        Assert.assertTrue(ezShop.addProductToSaleRFID(saleId, "000000002001"));
        Assert.assertTrue(ezShop.deleteProductFromSaleRFID(saleId, "000000002002"));
        Assert.assertFalse(ezShop.deleteProductFromSaleRFID(saleId, "000000002008"));
        ezShop.endSaleTransaction(saleId);

    }
    

    @Test
    public void testKOCases() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidRFIDException {
        ezShop.logout();

        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "000000002000");
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.returnProductRFID(1, "000000002000");
        });

        ezShop.login("admin","admin");
        Assert.assertThrows(InvalidOrderIdException.class, () -> {
            ezShop.recordOrderArrivalRFID(0, "000000002000");
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnProductRFID(-1, "000000002000");
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnProductRFID(0, "000000002000");
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnProductRFID(null, "000000002000");
        });

        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "2000");
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, null);
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "00000000000002000");
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.recordOrderArrivalRFID(1, "RFIDcode12ch");
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.returnProductRFID(1, "2000");
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.returnProductRFID(1, null);
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.returnProductRFID(1, "00000000000002000");
        });
        Assert.assertThrows(InvalidRFIDException.class, () -> {
            ezShop.returnProductRFID(1, "RFIDcode12ch");
        });

        int orderId = ezShop.payOrderFor("629104150024", 3, 1);
        ProductType p = DAOproductType.read("629104150024");
        p.setLocation(null);
        DAOproductType.Update(p);
        Assert.assertThrows(InvalidLocationException.class, () -> {
            ezShop.recordOrderArrivalRFID(orderId, "000000002000");
        });

        saleId = ezShop.startSaleTransaction();
        ezShop.addProductToSaleRFID(saleId, "000000002000");
        ezShop.addProductToSaleRFID(saleId, "000000002001");
        ezShop.endSaleTransaction(saleId);

        Integer retId = ezShop.startReturnTransaction(saleId);
        Assert.assertFalse(ezShop.returnProductRFID(retId, "000000002002"));
        Assert.assertFalse(ezShop.returnProductRFID(10000, "000000002000"));
        Assert.assertFalse(ezShop.returnProductRFID(10000, "000000004000"));

    }







    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
