package it.polito.ezshop.classes;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.BalanceOperation;

public class AccountBook {
	private double balance;
	private List<BalanceOperation> creditsAndDebts;
	public AccountBook(){
		balance = 0;
		creditsAndDebts = new ArrayList<>();
	}
	
	public boolean recordBalanceUpdate(double quantity) {
		balance += quantity;
		return true;
		
	}
	
	public List<BalanceOperation> getCreditsAndDebits(){
		return creditsAndDebts;
	}
	
	//updates balance by recomputing it from scratch and returns its value
	public double computeBalance() {
		/*
		balance = 0;
		creditsAndDebts.forEach(balanceOp -> {
			
			if (balanceOp.getClass().equals(Debit.class)) {
				balance -= balanceOp.getMoney();
			} else if (balanceOp.getClass().equals(Credit.class)) {
				balance += balanceOp.getMoney();
			}
		});*/
		
		
		return balance;
	}
	
	
	
	
}
