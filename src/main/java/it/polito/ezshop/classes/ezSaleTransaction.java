package it.polito.ezshop.classes;

import java.util.*;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class ezSaleTransaction implements SaleTransaction{  //two methods are commented in SaleTransaction interface
	
	
	public ezSaleTransaction() {
		saleID = 0;
		discountRate = 0.0;
		price = 0.0;
		//customer = new ezCustomer();
	    
	}
	
	public ezSaleTransaction(Integer saleID) {
		this.saleID = saleID;
		discountRate = 0.0;
		price = 0.0;
		//customer = new ezCustomer();
	    
	}
	
	public ezSaleTransaction(int saleId, double discountRate, double price) {
		this.saleID = saleId;
		this.discountRate = discountRate;
		this.price = price;
		//this.customer = new ezCustomer();
		
	}
	

	//implements
    
    public Integer getTicketNumber() {return this.saleID;}
    
    public void setTicketNumber(Integer receiptNumber) { this.saleID = receiptNumber;}
    
    public List<TicketEntry> getEntries(){return this.entries;}
    
    public void setEntries(List<TicketEntry> entries) {this.entries = entries;}
    
    public double getDiscountRate() {return discountRate;}

    public void setDiscountRate(double discountRate) {this.discountRate = discountRate;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}
    
    
	
	private Integer saleID;
	private List<TicketEntry> entries;
    private double discountRate;
    private double price;
    
}
