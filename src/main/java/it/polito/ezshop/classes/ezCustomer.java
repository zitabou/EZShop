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
		card = "N/A";
	}
	
	
    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getCustomerCardId() {return card;}
    
    public void setCustomerCard(String customerCard) {this.card = customerCard;}
    
    public LoyaltyCard getLoyaltyCard() {return lCard;}
    
    public void setLoyaltyCard(LoyaltyCard lCard) {
    	this.lCard = lCard;
    	this.card = lCard.getcardID();
    }

    public Integer getPoints() {return this.lCard.getPoints();}

    public void setPoints(Integer points) {this.lCard.setPoints(points);}
    
    
    @Column(name="Name")
    private String customerName;
    
    @Id
    @Column(name="CustomerID")
    private Integer id;
    
    @Column(name="Card_Id")
    private String card;
    
    @Transient
    private LoyaltyCard lCard;

	@Override
	public String getCustomerCard() {
		// TODO Auto-generated method stub
		return null;
	}

	
    
	
}
