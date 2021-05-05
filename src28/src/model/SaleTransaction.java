package model;

import java.time.LocalDate;
import java.util.*;

import interfaces.*;

public class SaleTransaction extends Credit implements SaleTrans_interface{
	
	
	public SaleTransaction() {
		balanceId = 0;
		date = LocalDate.now();
		money = 0.0;
		type = "N/A";
		discountRate = 0.0;
		price = 0.0;
		
		quantityPerProduct = new ArrayList<>();
		products = new ArrayList<ProductType>();
		
	}
	
	public SaleTransaction(int balanceId, LocalDate date, double money, String type, double discountRate, double price) {
		this.balanceId = balanceId;
		this.date = date;
		this.money = money;
		this.type = type;
		this.discountRate = discountRate;
		this.price = price;
		
		quantityPerProduct = new ArrayList<>();
		products = new ArrayList<ProductType>();
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
	
	//implements
    
    public Integer getReceiptNumber() {return receipt.getReceiptNumber();}
    
    public void setReceiptNumber(Integer receiptNumber) { receipt.setReceiptNumber(receiptNumber);}
    
  //--the following methods use ticket that is not present.
    //List<ReceiptEntry> getEntries();
    //void setEntries(List<ReceiptEntry> entries);
    
    public double getDiscountRate() {return discountRate;}

    public void setDiscountRate(double discountRate) {}

    public double getPrice() {return price;}

    public void setPrice(double price) {}
    
    
    //additional methods
    
    
    public Customer getCustomer() {return customer;}
    
	public void setCustomer(Customer customer) {this.customer = customer;}
	
	public List<Integer> getAllQuantities() {return quantityPerProduct;}

	// maybe it could be done through the object and prevent the loop
	public boolean updateQuantityPerProduct(ProductType product, Integer quantity) {
		int prodId = product.getId();
		for(int i =0; i<products.size(); i++) {
			if(prodId == products.get(i).getId()) {
				price = price + products.get(i).getPricePerUnit()*(quantity - quantityPerProduct.get(i));
				quantityPerProduct.set(i, quantity);
				return true;
			}
		}
		return false; // product was not found
	}

	public List<ProductType> getAllProducts() {return products;}

	public boolean addProductToSale(ProductType product, Integer quantity) {
		products.add(product);
		quantityPerProduct.add(quantity);
		return true;
	}
	
	// maybe it could be done through the object and prevent the loop
	public boolean deleteProductFromSale(ProductType product) {
		int prodId = product.getId();
		for(int i =0; i<products.size(); i++) {
			if(prodId == products.get(i).getId()) {
				price = price - products.get(i).getPricePerUnit()*quantityPerProduct.get(i);
				products.remove(i);
				return true;
			}
		}
		return false; // product was not found
	}
	
	//points are calculated based on the cost of the price. one point per 10 money
	public int computePointsForSale() {
		int points = (int) (price/10);
		if(points > 0) 
			return points;
		return 0;
	}
	
	public void selectPaymentType(String payType) {paymentType = payType;}
	public String getPaymentType() {return paymentType;}
	
	//boolean printSaleReceipt() {}
	

	private int balanceId;
    private LocalDate date;
    private double money; //?
    private String type;
    private double discountRate;
    private double price;
    
    private String paymentType;
    private Customer customer;
    private List<Integer> quantityPerProduct;
    private List<ProductType> products;
    private Receipt receipt;
	
}
