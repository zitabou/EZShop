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


import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
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
//		Integer id = ezShop.defineCustomer("mostafa");
//		assertTrue(id == -1 ? false : true);
//    	boolean isDeleted3 =ezShop.deleteCustomer(4);
//    	boolean isDeleted4 =ezShop.deleteCustomer(8);
    	
        int previousQuantityOfCustomers = ezShop.getAllCustomers().size();
        Integer id = ezShop.defineCustomer("mostafa");
        Assert.assertEquals(previousQuantityOfCustomers +1 , ezShop.getAllCustomers().size());


        String card = ezShop.createCard();
		boolean isModified = ezShop.modifyCustomer(id, "mostafa asad", card);
		
		Customer customer = ezShop.getAllCustomers().stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList()).get(0);
	    assertEquals("mostafa asad", customer.getCustomerName());
	    assertEquals(card, customer.getCustomerCard());
		assertTrue(isModified);
		
		boolean isDeleted = ezShop.deleteCustomer(id);
		assertTrue(isDeleted);
//		
//		List<Customer> custs =  ezShop.getAllCustomers();
		
		
	}
	

}
