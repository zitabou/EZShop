package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;
import it.polito.ezshop.classes.Position;


public class ezProductType implements ProductType{
	
	public ezProductType() {
		quantity = 0;
		location = "N/A";
		note = "N/A";
		productDescription = "N/A";
		barCode = "N/A";
		pricePerUnit = 0.0;
		id = 0;
		
		//setPosition(null);;
		
	}
	
	public ezProductType(Integer id, String productDescription, String barCode, Double pricePerUnit, String note) {
		this.quantity = 0;
		this.location = null;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
		this.id = id;
		
		//this.setPosition(null);
	}
	
	
	//standard methods
	public Integer getQuantity() {return quantity;}

	public void setQuantity(Integer quantity) {this.quantity = quantity;}

	public String getLocation() {return location;}

	public void setLocation(String location) {this.location = location;}

	public String getNote() {return note;}

	public void setNote(String note) {this.note = note;}

	public String getProductDescription() {return productDescription;}

	public void setProductDescription(String productDescription) {this.productDescription = productDescription;}

	public String getBarCode() {return barCode;}

	public void setBarCode(String barCode) {this.barCode = barCode;}

	public Double getPricePerUnit() {return pricePerUnit;}

	public void setPricePerUnit(Double pricePerUnit) {this.pricePerUnit = pricePerUnit;}

	public Integer getId() {return id;}

	public void setId(Integer id) {this.id = id;}
	
	
	
	// additional methods 
	
	//public Position getPosition() {return position;}

	//public void setPosition(Position position) {this.position = position;}



	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;
	
	//private Position position;
	
}
