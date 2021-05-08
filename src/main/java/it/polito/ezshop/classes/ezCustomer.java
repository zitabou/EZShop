package it.polito.ezshop.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;



@Entity
@Table(name = "Customer")
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
    
    
    @Column(name="Name")
    private String customerName;
    
    @Id
    @Column(name="CustomerID")
    private Integer id;
    
    @Transient
    private LoyaltyCard card;
    
	
}
