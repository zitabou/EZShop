package it.polito.ezshop.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.polito.ezshop.data.*;


@Entity
@Table(name = "Customer")
public class ezCustomer implements Customer{

	public ezCustomer() {
		customerName = "N/A";
		id = 0;
		card = "N/A";
		points = 0;
	}
	
	public ezCustomer(String customerName, Integer id, String customerCard, Integer points) {
		this.customerName = customerName;
		this.id = id;
		this.card = customerCard;
		this.points = points;
	}
	
	
    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getCustomerCard() {return card;}
    
    public void setCustomerCard(String customerCard) {this.card = customerCard;}
    
    public Integer getPoints() {return this.points;}

    public void setPoints(Integer points) {this.points = points;}
    

    private Integer id;
    
    private String customerName;
    
    private String card;
    
    private Integer points;
}
