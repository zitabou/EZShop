package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

public class ezReceiptEntry implements TicketEntry{
	
	public ezReceiptEntry(){
		this.barcode = null;
		this.productDescription = null;
		this.amount = 0;
		this.pricePerUnit = 0.0;
		this.discountRate = 0.0;
	}
	
	public ezReceiptEntry(String barcode, String productDescription, int amount, double pricePerUnit, double discountRate){
		this.barcode = barcode;
		this.productDescription = productDescription;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.discountRate = discountRate;
	}

	@Override
	public String getBarCode() {return this.barcode;}

	@Override
	public void setBarCode(String barCode) { this.barcode = barCode;}

	@Override
	public String getProductDescription() {return this.productDescription;}

	@Override
	public void setProductDescription(String productDescription) {this.productDescription = productDescription;}
		
	@Override
	public int getAmount() {return this.amount;}

	@Override
	public void setAmount(int amount) {this.amount = amount;}

	@Override
	public double getPricePerUnit() {return this.pricePerUnit;}

	@Override
	public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit;}

	@Override
	public double getDiscountRate() {return this.discountRate;}

	@Override
	public void setDiscountRate(double discountRate) {this.discountRate = discountRate;}
	
	private String barcode;
	private String productDescription;
	private int amount;
	private double pricePerUnit;
	private double discountRate;
	

}
