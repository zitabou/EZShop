package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class ezCustomer implements Customer{

	public ezCustomer() {
		customerName = "N/A";
		id = 0;
		card = null;
	}
	
	public ezCustomer(String customerName, Integer id, String customerCard, Integer points) {
		this.customerName = customerName;
		this.id = id;
		card = new LoyaltyCard(customerCard, points);
	}
	
	
    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public String getCustomerCard() {return card.getcardID();}

    public void setCustomerCard(String customerCard) {this.card.setcardID(customerCard);}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPoints() {return this.card.getPoints();}

    public void setPoints(Integer points) {this.card.setPoints(points);}
    
    
    private String customerName;
    private Integer id;
    private LoyaltyCard card;
    
	
}
