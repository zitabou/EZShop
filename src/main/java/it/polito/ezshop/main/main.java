package it.polito.ezshop.main;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class main {

	public static void main(String[] args) {
		
		EZShop shop = new EZShop();
	
			
			try {
				
				shop.defineCustomer("Zissis");
				shop.defineCustomer("Mostafa");
				shop.defineCustomer("Marcelo");
				shop.defineCustomer("Tommaso");
				/*for(Customer c : shop.customers)
					System.out.println("p_"+c.getId()+" -> "+c.getCustomerName());*/
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			} catch (InvalidCustomerNameException | UnauthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
	

/*        
        SessionFactory factory = new Configuration()
        						.configure()
        						.addAnnotatedClass(LoyaltyCard.class)
        						.addAnnotatedClass(ezCustomer.class)
        						.buildSessionFactory();
        //Session session = factory.getCurrentSession();  This will close the session once the commit is done
        Session session = factory.openSession();
        
        
        try {
        	
        	System.out.println("new card");
        	LoyaltyCard lc = new LoyaltyCard("card1", 10);
        	ezCustomer c1 = new ezCustomer();
        	c1.setCustomerName("Zissis");
        	c1.setId(1);
        	ezCustomer c2 = new ezCustomer();
        	c1.setCustomerName("Tabouras");
        	c1.setId(2);
        	
        	session.beginTransaction();
        	
        	System.out.println("create tables");
        	session.save(lc);
        	session.save(c1);
        	session.save(c2);
        	
        	session.getTransaction().commit();
        	//session.close();
        	System.out.println("Done");
        	
        	
        	System.out.println("read table Customer");
        	session = factory.openSession();
        	
        	
        	Query query = session.createQuery("from ezCustomer c where c.id like '2' ");
        	List<ezCustomer> readCust = query.list();
        	System.out.println("----------------");
        	for (ezCustomer cus : readCust) {
        		System.out.println(cus.getId() +"  " + cus.getCustomerName() );
        	}
    		System.out.println("----------------");
    		System.out.println("----------------");
    		
    		session.close();
        	
        } finally {
        	factory.close();
        }
        
  */      
	}

}
	
