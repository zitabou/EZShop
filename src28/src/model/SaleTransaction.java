package model;

import java.time.LocalDate;
import java.util.*;

import interfaces.*;
import model.Quantity;
import model.ProductType;

public class SaleTransaction extends Credit implements SaleTrans_interface{
	
	
	public SaleTransaction() {
		balanceId = 0;
		date = LocalDate.now();
		money = 0.0;
		type = "N/A";
		discountRate = 0.0;
		price = 0.0;
		
		qunatityPerProduct = new ArrayList<Quantity>();
		products = new ArrayList<ProductType>();
		
	}
	
	public SaleTransaction(int balanceId, LocalDate date, double money, String type, double discountRate, double price) {
		this.balanceId = balanceId;
		this.date = date;
		this.money = money;
		this.type = type;
		this.discountRate = discountRate;
		this.price = price;
		
		qunatityPerProduct = new ArrayList<Quantity>();
		products = new ArrayList<ProductType>();
	}
	

	//extends
    public Integer getBalanceId() {return balanceId;}

    public void setBalanceId(Integer balanceId) {this.balanceId = balanceId;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public double getMoney() {return money;}

    public void setMoney(double money) {this.money = money;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
	
	//implements
    
    //--the following methods use ticket that is not present.
    public Integer getTicketNumber() {return 0;}
    public void setTicketNumber(Integer ticketNumber) {}
    //List<TicketEntry> getEntries();
    //void setEntries(List<TicketEntry> entries);
    //
    
    public double getDiscountRate() {return discountRate;}

    public void setDiscountRate(double discountRate) {}

    public double getPrice() {return price;}

    public void setPrice(double price) {}
    
    
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;
    private double discountRate;
    private double price;
    
    private List<Quantity> qunatityPerProduct;
    private List<ProductType> products;
	
}
