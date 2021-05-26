package it.polito.ezshop.IntegrationTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.classes.ezUser;
import it.polito.ezshop.classesDAO.DAOexception;
import it.polito.ezshop.classesDAO.DAOuser;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.User;
import java.sql.SQLException;

public class TestDAOuser{
	User usr1 = null;
	User usr2 = null;
	User usr3 = null;

	@BeforeClass
	public static void establishConnection() {
		DBManager.getConnection();
	}
	
	@Before
	public void populateDB() {   //String customerName, Integer id, String customerCard, Integer points
		DAOuser.DeleteAll();
		usr1 = new ezUser("cash1","1234","Cashier");
		usr1.setId(DAOuser.Create(usr1));
		usr2 = new ezUser("admin", "1234", "Administrator");
		usr2.setId(DAOuser.Create(usr2));
		usr3 = new ezUser("man1", "1234", "ShopManager");
		usr3.setId(DAOuser.Create(usr3));
	}
	@Test
	public void testCreateUser() {
	System.out.println("Testing Create()");
	User usr_test1 = new ezUser("cashtest","1234","Cashier");
	User usr_test2 = new ezUser("mantest", "1234", "ShopManageer");
	User usr_test3 = new ezUser("admintest", "1234", "Administrator");
	User usr_test4 = new ezUser("noroletest", "1234", "");
	User usr_test5 = new ezUser("nopasswordtest", "","Cashier");
	User usr_test6 = new ezUser("", "noadmintest", "Cashier");
	User usr_test7 = null;
	try{
		DAOuser.Create(usr_test1);
		DAOuser.Create(usr_test2);
		DAOuser.Create(usr_test3);
		DAOuser.Create(usr_test4);
		DAOuser.Create(usr_test5);
		DAOuser.Create(usr_test6);
		// Send null user as parameter
		Assert.assertThrows(NullPointerException.class, () -> {
			DAOuser.Create(usr_test7);
		});
	}catch(DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}
	@Test
	public void testReadUser(){
	System.out.println("Testing Read()");
	Integer userId = usr3.getId();
	User usr_test = null;
	try{
		usr_test = DAOuser.Read(userId);
		Assert.assertEquals(userId, usr_test.getId());
	}catch (DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}

	@Test
	public void testUpdateUser(){
	System.out.println("Testing Update()");
    	User usr_test1 = new ezUser("cashtest","1234","Cashier");
	User usr_test2 = null;
	Integer userId = usr3.getId();
	usr_test1.setId(userId);

	try{
		DAOuser.Update(usr_test1);
		// Send user null as parameter
		Assert.assertThrows(NullPointerException.class, () -> {
			DAOuser.Create(usr_test2);
		});

	}catch(DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}
	
	@Test
	public void testDeleteUser(){
	System.out.println("Testing Delete()");
	Integer userId = usr3.getId();
	try{
		DAOuser.Delete(userId);
	}catch(DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}
	
	@Test
	public void testReadAllUsers(){
	System.out.println("Testing ReadAll()");
	try{
		Assert.assertNotNull(DAOuser.readAll());
	}catch(DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}
	
	@Test
	public void testSQLInjection(){
	System.out.println("Testing SQL Injection.");
	User usr_test1 = new ezUser("; DROP TABLE user;","1234","Administrator");
	User usr_test2 = new ezUser("; DROP TABLE; ","1234","Administrator");
	usr_test2.setId(usr2.getId());
	// Prepared statement is doing its job
	//Assert.assertThrows(SQLException.class, () -> {
	//DAOuser.Create(usr_test1);
	//});
	Integer userId = 0;
	try{
		DAOuser.Create(usr_test1);
		DAOuser.Update(usr_test2);
	}catch (DAOexception e){
		System.out.println(e.getMessage());
		fail();
	}
	}

	@AfterClass
	public static void endConnection() {
		DBManager.closeConnection();
	}

}
