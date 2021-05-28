package it.polito.ezshop.IntegrationTests;

import it.polito.ezshop.classesDAO.DAOuser;
import it.polito.ezshop.classes.ezUser;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TestUserApi{
	/*	EZShop.java api functions tests
	//createUser
	//deleteUser
	//getAllUsers
	//getUser
	//updateUserRights
	//login
	//logout
	*/

	EZShopInterface ezShop;
	User admin_test = null;
	User cashier_test = null;
	User manager_test = null;
	User active_user = null;
	@Before
	public void populateDB(){
		ezShop = new it.polito.ezshop.data.EZShop();
		admin_test = new ezUser("admintest", "1234", "Administrator");
		admin_test.setId(DAOuser.Create(admin_test));
		cashier_test = new ezUser("cashiertest", "1234", "Cashier");
		cashier_test.setId(DAOuser.Create(cashier_test));
		manager_test = new ezUser("mantest", "1234", "ShopManager");
		manager_test.setId(DAOuser.Create(manager_test));
	}

	@Test
	public void testLogin() throws InvalidUsernameException, InvalidPasswordException{
		System.out.println("Testing login()");
		// Login with existing user
		String username = "admintest";
		String password = "1234";
		User usr = null;
		usr = ezShop.login(username, password);
		Assert.assertNotNull(usr);
			
		// Login with nonexisting user
		// Expected exception InvalidUsernameException
		Assert.assertThrows(InvalidUsernameException.class, () -> {
			ezShop.login("RANDOM_USER_12335345","RANDOM_PASSWORD_1834713746");
		});

		// Login with user with wrong password
		// Expected exception InvalidPasswordException
		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.login("admintest","WRONG_PASSWORD");
		});

		// Login with empty username or null username
		// Expected exception InvalidUsernameException
		Assert.assertThrows(InvalidUsernameException.class, () -> {
			ezShop.login("","RANDOM_PASSWORD_1834713746");
		});

		// Login with empty password or null password
		// Expected exception InvalidPasswordException
		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.login("RANDOM_USER_12335345","");
		});

	}
	@Test
	public void testLogout() throws InvalidUsernameException, InvalidPasswordException{
		System.out.println("Testing logout()");
		// Current state: User is logged in
		User usr = null;
		usr = ezShop.login("admintest", "1234");
		// Assert
		Assert.assertTrue(ezShop.logout());
	}

	@Test
	public void testListUsers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException{
		System.out.println("Testing getAllUsers()");
		//Current state: User is logged in
		User usr = ezShop.login("admintest","1234");
		//ezShop getAllUsers 
		Assert.assertNotNull(ezShop.getAllUsers());
		ezShop.logout();
		
		//Try to list as non-admin
		User usr_cashier = ezShop.login("cashiertest","1234");
		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getAllUsers();
		});
	}

	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidUserIdException{
		System.out.println("Testing getUser().");
		//Current state: User logged in.
		User usr = ezShop.login("admintest", "1234");
		// Test normal working
		Assert.assertNotNull(ezShop.getUser(cashier_test.getId()));
		// Test when invalid user Id is given
		Assert.assertThrows(InvalidUserIdException.class, () -> {
			ezShop.getUser(-1);
		});
		ezShop.logout();
		// Test when the logged user is not authorized.
		User usr_cashier  = ezShop.login("cashiertest", "1234");
		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getUser(usr_cashier.getId());
		});
	}
	@Test
	public void testDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException{
		System.out.println("Testing deleteUser().");
		//Current state: User logged in.
		User usr = ezShop.login("admintest", "1234");
		// Test normal working
		Assert.assertTrue(ezShop.deleteUser(manager_test.getId()));
		// Test when invalid user Id is given
		Assert.assertThrows(InvalidUserIdException.class, () -> {
			ezShop.deleteUser(-1);
		});
		ezShop.logout();
		// Test when the logged user is not authorized.
		User usr_cashier  = ezShop.login("cashiertest", "1234");
		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteUser(admin_test.getId());
		});
	}
	
	@Test
	public void testCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		System.out.println("Testing createUser().");
		//Current state: User logged in.
		User usr = ezShop.login("admintest", "1234");
		// Test normal working
		User new_usr = null;
		new_usr = new ezUser("newusr1", "1234", "Cashier");
		new_usr.setId(ezShop.createUser(new_usr.getUsername(), new_usr.getPassword(), new_usr.getRole()));
		Assert.assertTrue(new_usr.getId() > 0);
		// Test when invalid Username is given
		Assert.assertThrows(InvalidUsernameException.class, () -> {
			ezShop.createUser("", "1234","Cashier");
		});
		// Test when invalid Password is given
		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.createUser("newusr2", "", "Cashier");
		});
		// Test when invalid Role is given
		Assert.assertThrows(InvalidRoleException.class, () -> {
			ezShop.createUser("newusr3", "1234", "");
		});
		ezShop.logout();
	}
    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
