package it.polito.ezshop.classes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBManager {
	
	public static SessionFactory factory;  //singleton
	
	public static Session getSession() {
		if(factory == null) {
			factory = new Configuration()
					.configure()
					.addAnnotatedClass(LoyaltyCard.class)
					.addAnnotatedClass(ezCustomer.class)
					.buildSessionFactory();
		}
		return factory.openSession();

	}
	
	public static void closeDB() {
		//if factory exists and no sessions are open then close
		if(factory != null && factory.getCurrentSession() != null) {
			factory.close();
			factory = null;
		}
	}
}
