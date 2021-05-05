package model;

import interfaces.*;

public class Customer implements Customer_interface{

	public Customer() {
		this.customerCard = "N/A";
		this.customerName = "N/A"; 
		this.id = 0; 
		this.points = 0; 
	}
	
	public Customer(String customerName, String customerCard, Integer id, Integer points) {
		this.customerCard = customerCard;
		this.customerName = customerName; 
		this.id = id; 
		this.points = points; 	
	}
	
	
    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public String getCustomerCard() {return customerCard;}

    public void setCustomerCard(String customerCard) {this.customerCard = customerCard;}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPoints() {return points;}

    public void setPoints(Integer points) {this.points = points;}
    
    
    private String customerName;
    private String customerCard;
    private Integer id;
    private Integer points;
    
	
}
