package it.polito.ezshop.classes;

import java.time.LocalDate;


import it.polito.ezshop.data.*;

public class ezOrder extends Debit implements Order{
	
	public ezOrder() {
		this.balanceId = 0;
		this.productCode = "N/A";
		this.pricePerUnit = 0;
		this.quantity = 0; 
		this.status = "N/A";
		this.orderId = 0;
		this.date = LocalDate.now();
		this.money = 0.0;
		this.type = "N/A";
	}
	
	public ezOrder(Integer balanceId, String productCode, double pricePerUnit, int quantity, String status, Integer orderId, LocalDate date, double money, String type) {
		this.balanceId = balanceId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity; 
		this.status = status;
		this.orderId = orderId;
		this.date = date;
		this.money = money;
		this.type = type;
	}
	
	

	//implements
	public Integer getBalanceId() {return balanceId;}

	public void setBalanceId(Integer balanceId) {this.balanceId = balanceId;}

	public String getProductCode() {return productCode;}

	public void setProductCode(String productCode) {this.productCode = productCode;}

	public double getPricePerUnit() {return pricePerUnit;}

	public void setPricePerUnit(double pricePerUnit) {this.pricePerUnit = pricePerUnit;}

	public int getQuantity() {return quantity;}

	public void setQuantity(int quantity) {this.quantity = quantity;}

	public String getStatus() {return status;}

	public void setStatus(String status) {this.status = status;}

	public Integer getOrderId() {return orderId;}

	public void setOrderId(Integer orderId) {this.orderId = orderId;}

	
	//extends
	public LocalDate getDate() {return date;}

	public void setDate(LocalDate date) {this.date = date;}

	public double getMoney() {return money;}

	public void setMoney(double money) {this.money = money;}

	public String getType() {return type;}

	public void setType(String type) {this.type = type;}


	private Integer balanceId;
	private String productCode;
	private double pricePerUnit;
	private int quantity;
	private String status;
	private Integer orderId;
	private LocalDate date;
	private double money;
	private String type;

	@Override
	public String toString() {
		return "ezOrder{" +
				"balanceId=" + balanceId +
				", productCode='" + productCode + '\'' +
				", pricePerUnit=" + pricePerUnit +
				", quantity=" + quantity +
				", status='" + status + '\'' +
				", orderId=" + orderId +
				", date=" + date +
				", money=" + money +
				", type='" + type + '\'' +
				'}';
	}
}
