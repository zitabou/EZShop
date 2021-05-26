package it.polito.ezshop.IntegrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class testCustomerApi {
    EZShopInterface ezShop;
    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() throws InvalidPasswordException, InvalidUsernameException {
        ezShop = new it.polito.ezshop.data.EZShop();

        ezShop.login("a","a");

    }
    
    @Test
	public void testOKCases() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
    	
        int previousQuantityOfCustomers = ezShop.getAllCustomers().size();
        Integer id = ezShop.defineCustomer("mostafa");
        Assert.assertEquals(previousQuantityOfCustomers +1 , ezShop.getAllCustomers().size());


        String card = ezShop.createCard();
		boolean isModified = ezShop.modifyCustomer(id, "mostafa asad", card);
		
		Customer customer = ezShop.getAllCustomers().stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList()).get(0);
	    assertEquals("mostafa asad", customer.getCustomerName());
	    assertEquals(card, customer.getCustomerCard());
		assertTrue(isModified);
		
		customer = ezShop.getCustomer(id);
		
		assertEquals(customer.getCustomerCard(), card);
		assertEquals(customer.getId(), id);
		assertEquals(customer.getCustomerName(), "mostafa asad");
		
		boolean isDeleted = ezShop.deleteCustomer(id);
		assertTrue(isDeleted);
		
		 Assert.assertEquals(previousQuantityOfCustomers  , ezShop.getAllCustomers().size());	
		
	}
    
    @Test
    public void testKOCases() throws InvalidCustomerIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
     
    	ezShop.logout();
        
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.getAllCustomers();
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.getCustomer(1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.deleteCustomer(1);
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.defineCustomer("mostafa");
        });
        Assert.assertThrows(UnauthorizedException.class, () -> {
            ezShop.modifyCustomer(1, "mostafa","0000000001");
        });

  
        ezShop.login("a","a");
        
        Assert.assertThrows(InvalidCustomerIdException.class, () -> {
            ezShop.getCustomer(0);
        });

        Assert.assertThrows(InvalidCustomerNameException.class, () -> {
            ezShop.defineCustomer(null);
        });

        Assert.assertThrows(InvalidCustomerNameException.class, () -> {
            ezShop.modifyCustomer(1, null, "0000000001");
        });
        Assert.assertThrows(InvalidCustomerCardException.class, () -> {
            ezShop.modifyCustomer(1, "mostafa", null);
        });
      
        Assert.assertThrows(InvalidCustomerIdException.class, () -> {
            ezShop.deleteCustomer(0);
        });
    }
    

    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
	

}
