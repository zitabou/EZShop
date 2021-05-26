package it.polito.ezshop.IntegrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.classes.ezSaleTransaction;
import it.polito.ezshop.classesDAO.DAOsaleTransaction;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.*;

public class testCreditCard {
	SaleTransaction sale2 = null;
    EZShopInterface ezShop;
    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() throws InvalidPasswordException, InvalidUsernameException {
        ezShop = new it.polito.ezshop.data.EZShop();

        ezShop.login("a","a");
        

    }
    
	@Test
	public void testOKCases() throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidCreditCardException {
		double cashpayment = ezShop.receiveCashPayment(1, 40);
		// Assert.assertEquals(8.75, cashpayment,0.001);
		 
//		double cashpayment1 = ezShop.receiveCashPayment(100, 40);
//		 Assert.assertEquals(-1, cashpayment1,00);
		 
		 boolean x = ezShop.receiveCreditCardPayment(1, "52706279529");		 
		 Assert.assertTrue(x);
		 
		 double returncash = ezShop.returnCashPayment(1);
		 Assert.assertNotEquals(-1, returncash,00); 
		 
		 double returncreditcard = ezShop.returnCreditCardPayment(1, "52706279529");
		 Assert.assertNotEquals(-1, returncreditcard,00);
		 
		 boolean recordbalance = ezShop.recordBalanceUpdate(20.20);
		 Assert.assertTrue(recordbalance);
		 		
	}
	
	@Test
	public void testKOCases() throws InvalidUsernameException, InvalidPasswordException {
		ezShop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.receiveCashPayment(1,40);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.receiveCreditCardPayment(1, "52706279529");
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.returnCashPayment(1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.returnCreditCardPayment(1, "52706279529");
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.recordBalanceUpdate(20.20);
        });
        
        ezShop.login("a", "a");
        
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.receiveCashPayment(0,40);
        });

        Assert.assertThrows(InvalidPaymentException.class, () -> {
        	ezShop.receiveCashPayment(1,0);
        });

        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.receiveCreditCardPayment(0, "52706279529");
        });
        Assert.assertThrows(InvalidCreditCardException.class, () -> {
            ezShop.receiveCreditCardPayment(1,null);
        });
      
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnCashPayment(0);
        });
        Assert.assertThrows(InvalidTransactionIdException.class, () -> {
            ezShop.returnCreditCardPayment(0, "52706279529");
        });
  
        
	}
	
    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
