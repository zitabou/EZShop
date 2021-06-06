package it.polito.ezshop.classes;

public class Product {
	
	public String getRFID() {return RFID;}

	public void setRFID(String rFID) {RFID = rFID;}
	
	public String getBarCode() {return barCode;}

	public void setBarCode(String barCode) {this.barCode = barCode;}


	private String RFID;
	private String barCode;
	
}
