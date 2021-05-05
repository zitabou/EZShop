package model;

import interfaces.*;
import model.Position;


public class ProductType implements ProdType_interface{
	
	public ProductType() {
		quantity = 0;
		location = "N/A";
		note = "N/A";
		productDescription = "N/A";
		barCode = "N/A";
		pricePerUnit = 0.0;
		id = 0;
		
		position = null;;
		
	}
	
	public ProductType(Integer quantity, String location, String note, String productDescription, String barCode, Double pricePerUnit, Integer id) {
		this.quantity = quantity;
		this.location = location;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
		this.id = id;
		
		this.position = null;
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
	
	

	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;
	
	private Position position;
	
}
