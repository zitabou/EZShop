package it.polito.ezshop.classesDAO;


public class DAOexception extends RuntimeException { 

    public DAOexception() {
    }

    public DAOexception(String descr) {
        super(descr);
    }

    public DAOexception(String descr, Throwable msg) {
        super(descr, msg);
    }

    public DAOexception(Throwable msg) {
        super(msg);
    }
    
	
}
