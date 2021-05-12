package it.polito.ezshop.main;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class Test_db {

	public static void main(String[] args) {

		EZShop shop = new EZShop();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		try {
			shop.defineCustomer("test");
		} catch (InvalidCustomerNameException | UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
