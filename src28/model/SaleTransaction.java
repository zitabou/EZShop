package model;

import java.time.LocalDate;

import interfaces.*;

public class SaleTransaction extends Credit implements SaleTrans_interface{

	//extends
    public Integer getBalanceId() {}

    public void setBalanceId(int balanceId) {}

    public LocalDate getDate() {}

    public void setDate(LocalDate date) {}

    public double getMoney() {}

    public void setMoney(double money) {}

    public String getType() {}

    public void setType(String type) {}
	
	//implements
    public Integer getTicketNumber() {}

    public void setTicketNumber(Integer ticketNumber) {}

    //List<TicketEntry> getEntries();

    //void setEntries(List<TicketEntry> entries);

    public double getDiscountRate() {}

    public void setDiscountRate(double discountRate) {}

    public double getPrice() {}

    public void setPrice(double price) {}
	
}
