package model;

import java.time.LocalDate;

// returns product of a completed saleTransaction
public class ReturnTransaction extends Debit{
	
	public ReturnTransaction() {
		balanceId = 0;
		saleReference = 0;
		date = LocalDate.now();
		money = 0.0;
		type = "N/A";
	}
	
	public ReturnTransaction(int balanceId, int saleReference, LocalDate date, double money, String type) {
		this.balanceId = balanceId;
		this.saleReference = saleReference;
		this.date = date;
		this.money = money;
		this.type = type;
	}
	
	
	//extends
    public Integer getBalanceId() {return balanceId;}

    public void setBalanceId(Integer balanceId) {this.balanceId = balanceId;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public double getMoney() {return money;}

    public void setMoney(double money) {this.money = money;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
    
    
    //additional methods
    
    public int getSaleReference() {return saleReference;}

	public void setSaleReference(int saleReference) {this.saleReference = saleReference;}
	
	public int getAmount() {return amount;}

	public void setAmount(int amount) {this.amount = amount;}

	public Double getReturnedValue() {return returnedValue;}

	public void setReturnedValue(Double returnedValue) {this.returnedValue = returnedValue;}

	public Integer getSaleID() {return saleID;}

	public void setSaleID(Integer saleID) {this.saleID = saleID;}
    
    public boolean returnProduct(ProductType product, int amount) {
    	setReturnedValue(product.getPricePerUnit() * amount);
    	this.setAmount(amount);
    	return true;
    }
    
    


	private	int balanceId;
    private	LocalDate date;
    private	double money; // is it the return value?
    private String type;
    
    private int saleReference;
    private int amount;
    private Double returnedValue;
    private Integer saleID;

}
