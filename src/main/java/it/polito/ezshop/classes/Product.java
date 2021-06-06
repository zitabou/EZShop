package it.polito.ezshop.classes;

public class Product {
	
	public Product() {
		RFID = "";
		barCode = "";
	}

	public Product(String RFID, String barCode) {
		this.RFID = RFID;
		this.barCode = barCode;
	}
	
	public String getRFID() {return RFID;}

	public void setRFID(String RFID) {this.RFID = RFID;}
	
	public String getBarCode() {return barCode;}

	public void setBarCode(String barCode) {this.barCode = barCode;}


	private String RFID;
	private String barCode;
	
}
