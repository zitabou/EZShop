package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.classes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*used only with DB impl*/
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class EZShop implements EZShopInterface {

	private List<User> users;
	private List<ProductType> products;
	private List<SaleTransaction> sales;
	private List<Customer> customers;
	private List<LoyaltyCard> cards;
	private List<ReturnTransaction> returns;
	private Map<Integer, Order> orders;
	private AccountBook accountBook;
	private ezUser activeUser;
	
	private Integer user_id = 1;
	private Integer prod_id = 0;
	private Integer cust_id = 0;
	private Integer card_id = 0;
	private Integer sale_id = 0;
	private Integer ret_id = 0;
	
	public EZShop() {
		
		accountBook = new AccountBook();
		users = new ArrayList<>();
		products = new ArrayList<>();
		sales = new ArrayList<>();
		customers = new ArrayList<>();
		returns = new ArrayList<>();
		orders = new HashMap<>();
		activeUser = null;
		
		users.add(new ezUser(0, "admin", "admin", "Administrator"));
	}
	
	

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	user_id++;
    	users.add(new ezUser(user_id, username, password, role));
    	for (User u : users) {
    		System.out.println("User: " + u.getId() + ", " + u.getUsername()+ ", "+ u.getRole() + ", "+ u.getPassword());
    	}
    	
    	return 1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return users;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        
    	for (User u : users) {
    		if(u.getUsername().equals(username) && u.getPassword().equals(password)) {
    			activeUser = (ezUser) u;	
    			return u; 
    		}
    	}
    	
    	return null;
    }

    @Override
    public boolean logout() {
    	activeUser = null;
        return true;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	prod_id++;
    	products.add(new ezProductType(prod_id, description, productCode, pricePerUnit, note));		
    	return prod_id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	for (ProductType p : products) {
    		if(p.getId() == id) {
    			p.setProductDescription(newDescription);
    			p.setBarCode(newNote);
    			p.setPricePerUnit(newPrice);
    			p.setNote(newNote);
    			return true; 
    		}
    	}
    	
    	
    	return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	
        return products;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(productCode == null || productCode.equals("") || !validBarCode(productCode))    	throw new InvalidProductCodeException();
        if( quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
        	
        //product does not exist
      	if(products.stream().filter( p -> p.getBarCode().equals(productCode)).count() <= 0 ) return -1;
    	
    	ezOrder o = new ezOrder();
        o.setProductCode(productCode);
        o.setQuantity(quantity);
        o.setPricePerUnit(pricePerUnit);
        
        orders.put(o.getOrderId(), o);
        
        return o.getOrderId();
    }

    private boolean validBarCode(String productCode) {
		// TODO validation of bar code
		return true;
	}



	@Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        //exceptions are raised by the called methods
		
		Integer orderId = issueOrder(productCode, quantity, pricePerUnit);
		if(orderId == -1) return -1;
        
		try {
			if(!payOrder(orderId)) 
				return -1;
		} catch (InvalidOrderIdException | UnauthorizedException e) {
			e.printStackTrace();
		}	
        
        return orderId;
        
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if(orderId <= 0) throw new InvalidOrderIdException();
        if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
        
    	ezOrder o = (ezOrder) orders.get(orderId);
    	if(o == null || !(o.getStatus().equals("ISSUED") || o.getStatus().equals("ORDERED") ) ) 
    		return false;
        
    	double amountToPay = o.getQuantity() * o.getPricePerUnit();
    	
    	if(accountBook.computeBalance() < amountToPay) 
    		return false;
    	
    	accountBook.recordBalanceUpdate(- amountToPay);
        o.setStatus("PAYED");
        return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
    	if(orderId <= 0) throw new InvalidOrderIdException();
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
    	
    	ezOrder o = (ezOrder) orders.get(orderId);
    	ezProductType prod = (ezProductType) products.stream().filter( p -> p.getBarCode().equals(o.getProductCode())).collect(Collectors.toList()).get(0);
        
    	if(prod.getLocation() == null) throw new InvalidLocationException();
    	
        if( ! (o.getStatus().equals("PAYED") || o.getStatus().equals("COMPLETED")) ) return false;
        
        if(o.getStatus().equals("PAYED")) {
	        
	        
	        prod.setQuantity(prod.getQuantity() + o.getQuantity());
	        o.setStatus("COMPLETED");
        }
        
        
        return true;
        
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
    	//
    	
    	List<Order> ordersInIssuedOrderedOrCompletedState = orders.values().stream()
    			.filter(o -> (o.getStatus().equals("ISSUED") || o.getStatus().equals("ORDERED") || o.getStatus().equals("COMPLETED")))
        		.collect(Collectors.toList());
    	
    	return ordersInIssuedOrderedOrCompletedState;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	if(customerName == null)
    		throw new InvalidCustomerNameException("customer name is null ");
    	if(customerName.equals(""))
    		throw new InvalidCustomerNameException("customer name is empty ");
    	/*
    	 if(if there is no logged user or if it has not the rights to perform the operation)
    	 	throw new UnauthorizedException();
    	 */
    	cust_id++;
    	customers.add(new ezCustomer(customerName, cust_id, "N/A", 0));

    	
    	
    	
    	
  /*impl with DB*/ 
    	/*
    	 * 
    	Transaction tx = null;
    	Session s = null;
    	try {
    		s = DBManager.getSession();  //it opens a session
    		tx = s.beginTransaction();
	
    		s.save(new ezCustomer(customerName, cust_id, "N/A", 0));
		
    		tx.commit();
    		
    		System.out.println("session -<"+DBManager.factory.getCurrentSession()+">");
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		tx.rollback();
    	}
    	finally {s.close();}
        
        *
        */
 /**/       
    	return cust_id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	if(newCustomerName == null)
    		throw new InvalidCustomerNameException("customer name is null ");
    	if(newCustomerName.equals(""))
    		throw new InvalidCustomerNameException("customer name is empty ");
    	if(id == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(id <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	if(newCustomerCard == null)
    		throw new InvalidCustomerCardException("customer card is null");
    	if(newCustomerCard.equals(""))
    		throw new InvalidCustomerCardException("customer card is empty");
    	if(newCustomerCard.length() > 10)
    		throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	
    	for (Customer c : customers) {
    		if(c.getId() == id) {
    			c.setCustomerName(newCustomerName);
    			c.setCustomerCard(newCustomerCard);
    			return true; 
    		}
    	}
    	return false;  //not found
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

    	if(id == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(id <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	
    	
    	for (Customer c : customers) {
    		if(c.getId() == id) {
    			customers.remove(c);
    			return true; 
    		}
    	}    	
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(id <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	for (Customer c : customers) {
    		if(c.getId() == id) {
    			return c; 
    		}
    	}    	
    	return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	
    	return customers;
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	card_id++;
    	cards.add(new LoyaltyCard("card_"+card_id, 0));
    	
    	/*
    	 if DB is unreachable return empty string
    	 */
        return cards.get(cards.size()-1).getcardID();
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	if(customerId == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(customerId <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	if(customerCard == null)
    		throw new InvalidCustomerCardException("customer card is null");
    	if(customerCard.equals(""))
    		throw new InvalidCustomerCardException("customer card is empty");
    	if(customerCard.length() > 10)
    		throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	
    	int index = -1;
    	for(Customer c : customers)                 				 			//get the customer
    		if(c.getId() == customerId)
    			index = customers.indexOf(c);
    	if(index > 0)															//if the customer exists search for card availability
    		for(LoyaltyCard lc : cards)
    			if(lc.getcardID().equals(customerCard) && lc.getInUse() == false) {  //if card exists and it is available assign it to customer and exit successfully
    				customers.get(index).setCustomerCard(customerCard);
    				return true;												
    			}    	
    	/*
   	 	if DB is unreachable return false
    	 */
    	return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	if(customerCard == null)
    		throw new InvalidCustomerCardException("customer card is null");
    	if(customerCard.equals(""))
    		throw new InvalidCustomerCardException("customer card is empty");
    	if(customerCard.length() > 10)
    		throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
    	/*
   	 	if(if there is no logged user or if it has not the rights to perform the operation)
   	 		throw new UnauthorizedException();
    	 */
    	for(LoyaltyCard lc : cards)						
			if(lc.getcardID().equals(customerCard)) {		 					   //get the card
				if(pointsToBeAdded >0 || lc.getPoints() >= pointsToBeAdded) {  //check that the points are positive and if they are negative check that the card points are not less
					lc.setPoints(pointsToBeAdded);
					return true;
				}
		}  
    	
    	/*
   	 	if DB is unreachable return false
    	 */
    	return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	sale_id++;
    	sales.add(new ezSaleTransaction(sale_id,0.0,0.0,0.0));
        return sale_id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	
    	for(ProductType prod : products)
    		if(prod.getBarCode().equals(productCode)) {
    			//if(pro.)
    			prod.setQuantity(prod.getQuantity()-amount);
    		
    	
    	
    	return false;
    	
    }
    	return true;}

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
    	if(saleNumber <= 0 || saleNumber == null)
    		throw new InvalidTransactionIdException();
    	//TODO throw exception if user has no rights
    	ret_id++;
        ReturnTransaction ret = new ReturnTransaction();
        ret.setBalanceId(ret_id);
        returns.add(ret);
        ret.setSaleReference(saleNumber);
        SaleTransaction referingSale = getSaleTransaction(saleNumber);
        //referingSale.add(ReturnTransaction);
        return ret_id;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        
    	int i=0;
    	
    	try {
    		//retrieve return transaction and refering sale transaction
	    	ReturnTransaction ret = returns.stream().filter( 
	        		r -> r.getBalanceId() == returnId 
	        		)
	        		.collect(Collectors.toList()).get(0);
	    	
	    	SaleTrans referingSale = (SaleTrans) sales.stream().filter( 
	        		s -> s.getReceiptNumber() == ret.getSaleReference() 
	        		)
	        		.collect(Collectors.toList()).get(0);
	    	
	    	//return false if amount to be returned > amount bought
	    	List<ezProductType> saleProducts = referingSale.getAllProducts();
	    	List<Integer> saleQuantities = referingSale.getAllQuantities();
	    	for (i=0; i<saleProducts.size(); i++) {
	    		if(saleProducts.get(i).getBarCode().equals(productCode)) {
	    			if (amount > saleQuantities.get(i))
	    				return false;
	    		}
	    	}
	        ret.setAmount(amount);
	        ret.setProdId(productCode);
    	}catch(Exception e) {
    		//return false if the transaction do not exist
    		return false;
    	}
        
        return true;
    }
    
    

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
    	if(returnId <= 0 || returnId == null)
    		throw new InvalidTransactionIdException();
    	//TODO throw exception if user has no rights
    	int i;
    	try {
    	
	    	ReturnTransaction ret = returns.stream().filter( 
	        		r -> r.getBalanceId() == returnId 
	        		)
	        		.collect(Collectors.toList()).get(0);
	    	
	    	SaleTrans referingSale = (SaleTrans) sales.stream().filter( 
	        		s -> s.getReceiptNumber() == ret.getSaleReference() 
	        		)
	        		.collect(Collectors.toList()).get(0);
	    	
	    	if(commit) {
	    		//increase quantity available
	    		ProductType prodType = products.stream().filter(
	    				p -> p.getBarCode().equals(ret.getProdId())
	    				).collect(Collectors.toList()).get(0);
	    		
	    		prodType.setQuantity(prodType.getQuantity() + ret.getAmount());
	    		
	    		//decrease amount spent on the Sale
	    		referingSale.setPrice(referingSale.getPrice() - ret.getMoney());
	    		
	    		List<ezProductType> saleProducts = referingSale.getAllProducts();
		    	List<Integer> saleQuantities = referingSale.getAllQuantities();
		    	for (i=0; i<saleProducts.size(); i++) {
		    		if(saleProducts.get(i).getBarCode().equals(ret.getProdId())) {
		    			//TODO update quantità
		    			//saleQuantities.get(i) -= ret.getAmount();
		    		}
		    	}
	    	}
    	}catch(Exception e) {
    		//return false if the returnTransaction is not found or if problem with DB
    		return false;
    	}
    	
    	return true;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double qty) throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
    	
    	//TODO: insert Debit or Credit inside accountBook.creditsAndDebits
    	
        return accountBook.recordBalanceUpdate(qty);
        
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
    	//TODO check if only Administrator and ShopManager can call this method
    	
    	if(from == null || to == null) return null;
    	
    	if(from.isAfter(to)) {
    		swapDates(from, to);
    	}
    	
    	List<BalanceOperation> credsAndDebtsFiltered = accountBook.getCreditsAndDebits().stream()
    			.filter( balanceOperation -> (balanceOperation.getDate().isAfter(from) && balanceOperation.getDate().isBefore(to) ))
    			.collect(Collectors.toList());
    	
        return credsAndDebtsFiltered;
    }

    private void swapDates(LocalDate from, LocalDate to) {
		LocalDate tmp = from;
		from = to;
		to = tmp;
	}



	@Override
    public double computeBalance() throws UnauthorizedException {
		if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
		//TODO check if only Administrator and ShopManager can call this method
		
        return accountBook.computeBalance();
    }
}
