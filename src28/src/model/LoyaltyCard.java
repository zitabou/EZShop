package model;

public class LoyaltyCard {
	
	public LoyaltyCard() {
		cardID = "N/A";
		points = 0;
	}
	public LoyaltyCard(String cardID, Integer points) {
		this.cardID = cardID;
		this.points = points;
	}
	
	
	public String getcardID() {return cardID;}
	
	public void setcardID(String cardID) {this.cardID = cardID;}
	
	public Integer getPoints() {return points;}
	
	public void setPoints(Integer points) {this.points = points;}

	
	private String cardID;
	private Integer points;
}
