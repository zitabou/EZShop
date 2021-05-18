package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezCustomer;
import it.polito.ezshop.classesDAO.DAObalanceOperation;
import it.polito.ezshop.classesDAO.DAOcustomer;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Customer;


public class TestDAOcustomer {

	Customer cus1 = null;	
	Customer cus2 = null ;
	Customer cus3 = null ;
	Customer cus4 = null ;
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		 DAOcustomer.DeleteAll();
		cus1 = new ezCustomer("testcus1", 1, "testcard1", 1);
		cus1.setId(DAOcustomer.Create(cus1));
		cus2 = new ezCustomer("testcus2", 2, "testcard2", 2);
		cus2.setId(DAOcustomer.Create(cus2));
		cus3 = new ezCustomer("testcus3", 3, "testcard3", 3);
		cus3.setId(DAOcustomer.Create(cus3));
		cus4 = new ezCustomer("testcus4", 4, "testcard4", 4);
		cus4.setId(DAOcustomer.Create(cus4));
		
		
	}
	
    @Test
    public void testCreateCustomer() {
    	
    	Customer cus5 = new ezCustomer("testcus5", 5, "testcard5", 5);
    	int outcome;
    	
		outcome = DAOcustomer.Create(cus5);
		Assert.assertEquals(5, outcome);
    	
    }


}
