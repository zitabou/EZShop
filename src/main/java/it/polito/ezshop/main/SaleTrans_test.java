package it.polito.ezshop.main;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.*;

public class SaleTrans_test {

	
	public static void main(String[] args) {
		

		EZShop shop = new EZShop();
		
		try {
			shop.createUser("zissis", "zissis", "Administrator");
			
			shop.createProductType("prod1", "sda", 20, "test");
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		} catch (InvalidUsernameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRoleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductDescriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPricePerUnitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
