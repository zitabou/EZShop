package model;

import java.time.LocalDate;

public class ReturnTransaction extends Debit{
	
	public ReturnTransaction() {
		balanceId = 0;
		date = LocalDate.now();
		money = 0.0;
		type = "N/A";
	}
	
	public ReturnTransaction(int balanceId, LocalDate date, double money, String type) {
		this.balanceId = balanceId;
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
    
    
    private	int balanceId;
    private	LocalDate date;
    private	double money;
    private String type;
    

}
