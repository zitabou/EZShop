package model;

public class Position {
	
	public Position() {
		aisleID = null;
		rackID = null;
		levelID = null;
	}
	
	public Position(Integer aisleID, Integer rackID, Integer levelID) {
		this.aisleID = aisleID;
		this.rackID = rackID;
		this.levelID = levelID;
	}

	
	public Integer getAisleID() {return aisleID;}
	
	public void setAisleID(Integer aisleID) {this.aisleID = aisleID;}

	public Integer getRackID() {return rackID;}
	
	public void setRackID(Integer rackID) { this.rackID = rackID;}

	public Integer getLevelID() { return levelID;}

	public void setLevelID(Integer levelID) { this.levelID = levelID;}




	private Integer aisleID;
	private Integer rackID;
	private Integer levelID;
}
