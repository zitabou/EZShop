package it.polito.ezshop.data;

import java.time.LocalDate;

public interface BalanceOperation {

    Integer getBalanceId();

    void setBalanceId(Integer balanceId);

    LocalDate getDate();

    void setDate(LocalDate date);

    double getMoney();

    void setMoney(double money);

    String getType();

    void setType(String type);
}
