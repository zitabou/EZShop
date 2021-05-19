package it.polito.ezshop.acceptanceTests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezOrder;
import it.polito.ezshop.classesDAO.DAOcustomer;
import it.polito.ezshop.classesDAO.DAOorder;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.Order;
public class TestDAOorder {

	
	Order order1 =null;
	Order order2 =null;
	Order order3 =null;
	Order order4 =null;
	Integer orderId= 0;
	Random rand = new Random();
	
	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {  
	
		order1 = new ezOrder(10, "1", 100.0, 10, "deactive", rand.nextInt(5000), LocalDate.now(), 1000, "Game");
		order1.setOrderId(DAOorder.Create(order1));
		order2 = new ezOrder(10, "2", 100.0, 10, "active", rand.nextInt(5000), LocalDate.now(), 1000, "Game");
		order2.setOrderId(DAOorder.Create(order2));
		order3 = new ezOrder(10, "3", 100.0, 10, "deactive", rand.nextInt(5000), LocalDate.now(), 1000, "Game");
		order3.setOrderId(DAOorder.Create(order3));
		order4 = new ezOrder(10, "4", 100.0, 10, "active", rand.nextInt(5000), LocalDate.now(), 1000, "Game");
		order4.setOrderId(DAOorder.Create(order4));
		orderId = order1.getOrderId();
		
	}
	
	//Create
	@Test
	public void TestCreateOrder() {
		Integer orderId1 = rand.nextInt(5000);
		Order order = new ezOrder(10, "5", 100.0, 10, "active", orderId1, LocalDate.now(), 1000, "Game");
		
		Integer outcome = DAOorder.Create(order);
		
		Assert.assertEquals(order.getOrderId(), outcome); 
	}
	
	//Read
  	@Test
  	public void testReadOrder() {
  		
  		Order order = DAOorder.Read(orderId);
  		Assert.assertTrue(order.getStatus().equals("deactive"));
  	}
	
	//Read ALL
	@Test
	public void TestReadAllOrder() {
 		List<Order> orders = new ArrayList<Order>(DAOorder.readAll().values());
// 		Integer  id=0;
// 		for (Order order: orders) 
// 		{ 
// 		    if(orderId == order.getOrderId())
// 		    {
// 		    	id = order.getOrderId();
// 		    }
// 		}
 	//	Assert.assertEquals(4, orders.size());
 	//	Assert.assertEquals(id,orderId);
	}
	
	//update
	@Test
	public void TestUpdateOrder() {
		order1.setQuantity(8888);
 		DAOorder.Update(order1);
 		
 		
 		order1 = DAOorder.Read(orderId);
 		
 		Assert.assertEquals(order1.getQuantity(),8888);
	}
	
	
	
	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}
}
