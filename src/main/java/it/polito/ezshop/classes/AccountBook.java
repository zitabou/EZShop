package it.polito.ezshop.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.classesDAO.DAObalanceOperation;
import it.polito.ezshop.data.BalanceOperation;

public class AccountBook {
	private double balance;
	private List<BalanceOperation> creditsAndDebts;
	public AccountBook(){
		balance = 0;
		creditsAndDebts = new ArrayList<>();
	}
	
	public boolean recordBalanceUpdate(double money, Integer balance_id) {
		if ( (money<0) && balance < (money * -1)) {
			return false;
		}
		BalanceOperation newBO = money < 0 ? new Debit() : new Credit();
		newBO.setBalanceId(balance_id);
		newBO.setDate(LocalDate.now());
		newBO.setMoney(money);
		newBO.setType(money < 0 ? "debit" : "credit");
		DAObalanceOperation.Create(newBO);
		balance += money;
		return true;
		
	}
	
	public List<BalanceOperation> getCreditsAndDebits(){
		creditsAndDebts = new ArrayList<BalanceOperation> ( DAObalanceOperation.readAll().values() );

		return creditsAndDebts;
	}
	
	//updates balance by recomputing it from scratch and returns its value
	public double computeBalance() {

		balance = 0;
		getCreditsAndDebits().forEach(balanceOp ->
			

				balance += balanceOp.getMoney()

		);
		
		
		return balance;
	}
	
	
	
	
}
