package it.polito.ezshop.classes;

import java.util.*;

import it.polito.ezshop.data.ReceiptEntry;
import it.polito.ezshop.data.SaleTransaction;

public class ezSaleTransaction implements SaleTransaction{  //two methods are commented in SaleTransaction interface
	
	
	public ezSaleTransaction() {
		saleID = 0;;
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
		this.setReceiptNumber(saleId);
		this.discountRate = discountRate;
		this.price = price;
		//this.customer = new ezCustomer();
		
	}
	

	//implements
    
    public Integer getReceiptNumber() {return this.saleID;}
    
    public void setReceiptNumber(Integer receiptNumber) { this.saleID = receiptNumber;}
    
    public List<ReceiptEntry> getEntries(){return this.entries;}
    
    public void setEntries(List<ReceiptEntry> entries) {this.entries = entries;}
    
    public double getDiscountRate() {return discountRate;}

    public void setDiscountRate(double discountRate) {this.discountRate = discountRate;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}
    
    
    //additional methods
    
    //public ezCustomer getCustomer() {return customer;}
    
	//public void setCustomer(ezCustomer customer) {this.customer = customer;}
	
	
	//points are calculated based on the cost of the price. one point per 10 money
	public int computePointsForSale() {
		int points = (int) (price/10);
		if(points > 0) 
			return points;
		return 0;
	}
	
	//public void selectPaymentType(String payType) {paymentType = payType;}
	//public String getPaymentType() {return paymentType;}
	
	
	
	private Integer saleID;
	private List<ReceiptEntry> entries;
    private double discountRate;
    private double price;
    
    //private String paymentType;
    //private ezCustomer customer;

}
