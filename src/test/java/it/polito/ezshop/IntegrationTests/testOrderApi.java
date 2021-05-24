package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class testOrderApi {

    //issueOrder, payOrder, payOrderFor, recordOrderArrival, getAllOrders

    EZShopInterface ezShop;
    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        ezShop = new it.polito.ezshop.data.EZShop();

        ezShop.login("a","a");

        if(ezShop.getAllProductTypes().size() == 0 || DAOproductType.read("629104150024") == null){
            Integer prodId = ezShop.createProductType("prova", "629104150024", 1, "");
            ezShop.updatePosition(prodId, "45-45-45");
            ezShop.updateQuantity(prodId, 1);
        }



    }

    @Test
    public void testOkCases() throws UnauthorizedException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException, InvalidLocationException {

        int previousQuantityOfOrders = ezShop.getAllOrders().size();
        ProductType prod = ezShop.getAllProductTypes().get(0);
        int ordId = ezShop.issueOrder(prod.getBarCode(), 1, 1.0);
        Assert.assertEquals(previousQuantityOfOrders +1 , ezShop.getAllOrders().size());

        ezShop.payOrder(ordId);
        int finalOrdId = ordId;
        Order order = ezShop.getAllOrders().stream().filter(o -> o.getOrderId().equals(finalOrdId)).collect(Collectors.toList()).get(0);
        Assert.assertEquals("PAYED", order.getStatus());

        previousQuantityOfOrders = ezShop.getAllOrders().size();
        ordId = ezShop.payOrderFor(prod.getBarCode(), 1,1.0);
        int finalOrdId1 = ordId;
        order = ezShop.getAllOrders().stream().filter(o -> o.getOrderId().equals(finalOrdId1)).collect(Collectors.toList()).get(0);
        Assert.assertEquals(previousQuantityOfOrders +1 , ezShop.getAllOrders().size());
        Assert.assertEquals("PAYED", order.getStatus());

        ezShop.recordOrderArrival(ordId);
        order = ezShop.getAllOrders().stream().filter(o -> o.getOrderId().equals(finalOrdId1)).collect(Collectors.toList()).get(0);
        Assert.assertEquals("COMPLETED", order.getStatus());

    }

    @Test
    public void testKOCases() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidOrderIdException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException {
        ezShop.logout();

        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.getAllOrders();
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.payOrder(1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.issueOrder("629104150024", 1, 1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.payOrderFor("629104150024", 1, 1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.recordOrderArrival(1);
        });

        ezShop.login("a", "a");
        Assert.assertThrows(InvalidOrderIdException.class, () -> {
            ezShop.payOrder(-1);
        });
        Assert.assertThrows(InvalidOrderIdException.class, () -> {
            ezShop.recordOrderArrival(-1);
        });

        Assert.assertThrows(InvalidProductCodeException.class, () -> {
            ezShop.issueOrder("", 1, 1);
        });
        Assert.assertThrows(InvalidProductCodeException.class, () -> {
            ezShop.payOrderFor("", 1, 1);
        });

        Assert.assertThrows(InvalidQuantityException.class, () -> {
            ezShop.issueOrder("629104150024", -1, 1);
        });
        Assert.assertThrows(InvalidQuantityException.class, () -> {
            ezShop.payOrderFor("629104150024", -1, 1);
        });

        Assert.assertThrows(InvalidPricePerUnitException.class, () -> {
            ezShop.issueOrder("629104150024", 1, -1);
        });
        Assert.assertThrows(InvalidPricePerUnitException.class, () -> {
            ezShop.payOrderFor("629104150024", 1, -1);
        });

        //not existing order
        Assert.assertFalse( ezShop.payOrder(10000));

        DAOproductType.Delete("629104150024");
        int ret = ezShop.issueOrder("629104150024", 1, 1);
       Assert.assertEquals(-1, ret );
    }





    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
