package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DAOreturnEntry;
import it.polito.ezshop.classesDAO.DAOsaleEntry;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class testReturnTransactionApi {

    //issueOrder, payOrder, payOrderFor, recordOrderArrival, getAllOrders

    EZShopInterface ezShop;
    Integer saleId = 0;
    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPasswordException, InvalidUsernameException {
        ezShop = new it.polito.ezshop.data.EZShop();

        ezShop.login("a","a");

        if(ezShop.getAllProductTypes().size() == 0 || DAOproductType.read("629104150024") == null){
            Integer prodId = ezShop.createProductType("prova", "629104150024", 1, "");
            ezShop.updatePosition(prodId, "45-45-45");
            ezShop.updateQuantity(prodId, 4000);
    }else if (DAOproductType.read("629104150024").getQuantity() < 100){
            ProductType r = DAOproductType.read("629104150024");
            r.setQuantity(4000);
            DAOproductType.Update(r);
        }

        saleId = ezShop.startSaleTransaction();
        ProductType prod = DAOproductType.read("629104150024");
        ezShop.addProductToSale(saleId, prod.getBarCode(), 5);
        ezShop.endSaleTransaction(saleId);


    }

    @Test
    public void testOkCases() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {

        Integer retId = ezShop.startReturnTransaction(saleId);
        ezShop.returnProduct(retId, "629104150024", 1);
        ezShop.endReturnTransaction(retId, true);

        List<TicketEntry> items = DAOsaleEntry.Read(saleId);
        int quantity = items.get(0).getAmount();
        Assert.assertEquals(4, quantity);

        int retQty = DAOreturnEntry.Read(retId).get(0).getAmount();
        Assert.assertEquals(1, retQty);

        retId = ezShop.startReturnTransaction(saleId);
        ezShop.returnProduct(retId, "629104150024", 1);
        ezShop.endReturnTransaction(retId, false);

        items = DAOsaleEntry.Read(saleId);
        quantity = items.get(0).getAmount();
        Assert.assertEquals(4, quantity);


    }

    @Test
    public void testKOCases() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        ezShop.logout();

        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.startReturnTransaction(saleId);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.returnProduct(1, "629104150024", 1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.endReturnTransaction(1, false);
        });

        ezShop.login("a", "a");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.startReturnTransaction(-1);
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnProduct(-1, "629104150024", 1);
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.endReturnTransaction(-1, false);
        });

        Assert.assertThrows(InvalidProductCodeException.class, () -> {
            ezShop.returnProduct(1, "", 1);
        });

        Assert.assertThrows(InvalidQuantityException.class, () -> {
            ezShop.returnProduct(1, "629104150024", -1);
        });

    }







    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
