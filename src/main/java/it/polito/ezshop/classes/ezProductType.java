package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;

public class ezProductType implements ProductType{
	
	public ezProductType() {
		this.id = 0;
		this.productDescription = "N/A";
		this.barCode = "N/A";
		this.pricePerUnit = 0.0;
		this.quantity = 0;
		this.note = "N/A";
		this.location = "";
		
	}
	
	public ezProductType(Integer id, String productDescription, String barCode, Double pricePerUnit, Integer quantity, String note, String location) {
		this.id = id;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.note = note;
		this.location = location;
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
	
	
	
	private Integer id;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer quantity;
	private String note;
	private String location;


	//private Position position;
	
}
