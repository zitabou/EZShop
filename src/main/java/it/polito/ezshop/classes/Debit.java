package it.polito.ezshop.classes;

import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;

public class Debit implements BalanceOperation {
    @Override
    public String toString() {
        return "Debit{" +
                "balanceId=" + balanceId +
                ", date=" + date +
                ", money=" + money +
                ", type='" + type + '\'' +
                '}';
    }

    private Integer balanceId;
    private LocalDate date;
    private double money;
    private String type;
    @Override
    public Integer getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date=date;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money=money;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
