package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classes.AccountBook;
import it.polito.ezshop.classesDAO.DAObalanceOperation;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class testBalanceOperationApi {

    //computeBalance, recordBalanceUpdate, getCreditsAndDebits

    EZShopInterface ezShop;
    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() throws InvalidPasswordException, InvalidUsernameException {
        ezShop = new it.polito.ezshop.data.EZShop();

        ezShop.login("a","a");

    }

    @Test
    public void testOkCases() throws UnauthorizedException {
        double prevBalance =ezShop.computeBalance();
        List<BalanceOperation> list = ezShop.getCreditsAndDebits(LocalDate.MIN, LocalDate.MAX);
        double qty = 2;
        ezShop.recordBalanceUpdate(qty);
        Assert.assertEquals(prevBalance + 2, ezShop.computeBalance(), 0.0001);

        prevBalance =ezShop.computeBalance();
        qty = -1;
        ezShop.recordBalanceUpdate(qty);
        Assert.assertEquals(prevBalance -1, ezShop.computeBalance(), 0.0001);

        prevBalance =ezShop.computeBalance();
        qty = 0;
        ezShop.recordBalanceUpdate(qty);
        Assert.assertEquals(prevBalance, ezShop.computeBalance(), 0.0001);

        int actualOperations = ezShop.getCreditsAndDebits(LocalDate.MIN, LocalDate.MAX).size();
        Assert.assertEquals(list.size() + 3, actualOperations);

    }

    @Test
    public void testKOCases() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        ezShop.logout();
        
       // List<BalanceOperation> list = ezShop.getCreditsAndDebits(LocalDate.MIN, LocalDate.MAX);
        double qty = 2;

        Assert.assertThrows(UnauthorizedException.class, () -> {
            double prevBalance =ezShop.computeBalance();

        });

        Assert.assertThrows(UnauthorizedException.class, () -> {

            List<BalanceOperation> list = ezShop.getCreditsAndDebits(LocalDate.MIN, LocalDate.MAX);

        });

        double finalQty = qty;
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.recordBalanceUpdate(finalQty);
        });

        qty = -100000;
        ezShop.login("a", "a");
        Assert.assertFalse(ezShop.recordBalanceUpdate(qty));

    }





    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
