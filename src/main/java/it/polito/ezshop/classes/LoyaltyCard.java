package it.polito.ezshop.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LoyaltyCard")
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

	@Id
	@Column(name="cardID")
	private String cardID;
	
	@Column(name="points")
	private Integer points;

	public boolean getInUse() {
		// TODO Auto-generated method stub
		return false;
	}
}
