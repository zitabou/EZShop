package model;

import java.time.LocalDate;

import interfaces.*;

public class Order extends Debit implements Order_interface{

	//extends
	public void setBalanceId(int balanceId) {}

	public LocalDate getDate() {}

	public void setDate(LocalDate date) {}

	public double getMoney() {}

	public void setMoney(double money) {}

	public String getType() {}

	public void setType(String type) {}

	//implements
	public Integer getBalanceId() {}

	public void setBalanceId(Integer balanceId) {}

	public String getProductCode() {}

	public void setProductCode(String productCode) {}

	public double getPricePerUnit() {}

	public void setPricePerUnit(double pricePerUnit) {}

	public int getQuantity();

	public void setQuantity(int quantity) {}

	public String getStatus() {}

	public void setStatus(String status) {}

	public Integer getOrderId() {}

	public void setOrderId(Integer orderId) {}
	
}
