package it.polito.ezshop.data;

import java.util.List;

public interface SaleTransaction {

    Integer getReceiptNumber();

    void setReceiptNumber(Integer receiptNumber);

    //List<ReceiptEntry> getEntries();

    //void setEntries(List<ReceiptEntry> entries);

    double getDiscountRate();

    void setDiscountRate(double discountRate);

    double getPrice();

    void setPrice(double price);
}
