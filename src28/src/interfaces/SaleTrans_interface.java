package interfaces;

import java.util.List;

public interface SaleTrans_interface {
	
    Integer getTicketNumber();

    void setTicketNumber(Integer ticketNumber);

    //List<TicketEntry> getEntries();

    //void setEntries(List<TicketEntry> entries);

    double getDiscountRate();

    void setDiscountRate(double discountRate);

    double getPrice();

    void setPrice(double price);
}
