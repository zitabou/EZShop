package it.polito.ezshop.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Location {
	
	public Location() {
		aisleID = null;
		rackID = null;
		levelID = null;
		product = 0;
	}
	
	public Location(Integer aisleID, Integer rackID, Integer levelID, Integer product) {
		this.aisleID = aisleID;
		this.rackID = rackID;
		this.levelID = levelID;
		this.product = product;
	}

	
	public Integer getAisleID() {return aisleID;}
	
	public void setAisleID(Integer aisleID) {this.aisleID = aisleID;}

	public Integer getRackID() {return rackID;}
	
	public void setRackID(Integer rackID) { this.rackID = rackID;}

	public Integer getLevelID() { return levelID;}

	public void setLevelID(Integer levelID) { this.levelID = levelID;}

	public String getPosition() {return Integer.toString(aisleID) + "-"+ Integer.toString(rackID) + "-" + Integer.toString(levelID); }

	public Integer getProduct() {return product;}

	public void setProduct(Integer product) {this.product = product;}
	
	
	public boolean setAisleByExtract(String position) {
		Pattern pattern = Pattern.compile("^\\d+\\-");
		Matcher m = pattern.matcher(position);
		
		boolean found = m.find();
		this.aisleID = Integer.parseInt(m.group(0).substring(0, m.group(0).length() -1));
		return found;
	}
	
	public boolean setRackByExtract(String position) {
		
		Pattern pattern = Pattern.compile("\\-\\d+\\-");
		Matcher m = pattern.matcher(position);
		
		boolean found = m.find();
		this.rackID = Integer.parseInt(m.group(0).substring(1, m.group(0).length() -1));
		return found;
	}
	
	public boolean setLevelByExtract(String position) {
		Pattern pattern = Pattern.compile("\\-\\d+$");
		Matcher m = pattern.matcher(position);
		
		boolean found = m.find();
		this.levelID = Integer.parseInt(m.group(0).substring(1, m.group(0).length()));
		return found;
	}
	




	private Integer aisleID;
	private Integer rackID;
	private Integer levelID;
	private Integer product;
}
