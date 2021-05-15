package it.polito.ezshop.classes;

import it.polito.ezshop.data.ReceiptEntry;

public class ezReceiptEntry implements ReceiptEntry{

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
