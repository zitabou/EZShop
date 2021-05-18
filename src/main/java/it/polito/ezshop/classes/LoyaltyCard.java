package it.polito.ezshop.classes;

public class LoyaltyCard {
	
	public LoyaltyCard() {
		this.cardID = "N/A";
		this.points = 0;
		this.customer = 0;
	}
	public LoyaltyCard(String cardID, Integer points) {
		this.cardID = cardID;
		this.points = points;
		this.customer = 0;
	}
	
	public LoyaltyCard(String cardID, Integer points, Integer customer) {
		this.cardID = cardID;
		this.points = points;
		this.customer = customer;
	}
	
	
	public String getCardID() {return cardID;}
	
	public void setCardID(String cardID) {this.cardID = cardID;}
	
	public Integer getPoints() {return points;}
	
	public void setPoints(Integer points) {this.points = points;}
	
	public Integer getCustomer() {return this.customer;}
	
	public void setCustomer(Integer customer) {this.customer = customer;}
	
	

	private String cardID;
	
	private Integer points;
	
	private Integer customer;
}
