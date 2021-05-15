package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.classes.*;
import it.polito.ezshop.classesDAO.*;
import it.polito.ezshop.classesDAO.DBManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/*used only with DB impl*/
/*
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
*/

public class EZShop implements EZShopInterface {

	private Map<Integer, User> users;
	private Map<Integer, ProductType> products;
	private Map<Integer, SaleTransaction> sales;
	private Map<Integer, Customer> customers;
	private Map<String, LoyaltyCard> cards;
	private Map<Integer, ReturnTransaction> returns;
	private Map<Integer, Order> orders;
	private AccountBook accountBook;
	private ezUser activeUser;
	
	private Integer user_id = 0;
	private Integer cust_id =555;
	private Integer ret_id = 0;
	
	private Integer last_sale_id = 0;
	private SaleTransaction openSale = null;
	private Map<String, ProductType> openProds = null;
	
	
	public EZShop() {
		
		accountBook = new AccountBook();
		users = new HashMap<>();
		products = new HashMap<>();
		sales = new HashMap<>();
		customers = new HashMap<>();
		returns = new HashMap<>();
		orders = new HashMap<>();
		activeUser = null;
		
		openProds = new HashMap<>();
		
		users.put(0, new ezUser(0, "admin", "admin", "Administrator"));
		DBManager.getConnection();
		DBManager.closeConnection();
		
	}
	
	

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	
    	user_id++;
    	return user_id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return true;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	
        return new ArrayList<User>( users.values());
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
        
    	for (User u : users.values()) {
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
    
    
    private boolean validBarCode(String productCode) {
		//bar12: 629104150024
    	//bar13: 6291041500213
    	//bar14: 62910415002134
    	
    	if(!StringUtils.isNumeric(productCode)  || productCode.length()<12 || productCode.length()>14)
    		return false;
    	
    	char[] char_digits = productCode.toCharArray();
    	int check_digit = 0 ;
    	int mul = 0;
    	long round_int= 0;
    	double round= 0;
    	
    	//get the sum of products
    	int pair = (char_digits.length-1)%2;
    	for(int i=0; i<char_digits.length-1; i++) {
    		mul = (2*((i+pair)%2))+1;
    		check_digit = (Character.getNumericValue(char_digits[i]) * mul) + check_digit;
    	}
    	
    	//calculate the nearest equal or higher multiple of ten
    	round = (double)check_digit/10.0;
    	round_int = (long)round;
    	if(round-round_int != 0)  //fractional part /=0
    		round = (round_int+1)*10;
    	else                      //fractional part  =0
    		round = check_digit;
    	//check if the last digit is indeed what it should
    	if(round - check_digit == Character.getNumericValue(char_digits[char_digits.length-1]) )
    		return true;
		return false;
	}

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(description == null || description.equals(""))
    		throw new InvalidProductDescriptionException("product description is null or empty");
    	if(productCode == null || productCode.equals(""))
    		throw new InvalidProductCodeException("product barcode is null or empty");   
    	if(!validBarCode(productCode))
    		throw new InvalidProductCodeException("Wrong product barcode format");
    	if(pricePerUnit <= 0 )
    		throw new InvalidPricePerUnitException("price per unit cannot be <=0");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();

    	
    	
    	Integer prod_id = null;
    	ezProductType prod = new ezProductType(0, description, productCode, pricePerUnit, 0, note, "N/A");
    	try{prod_id = DAOproductType.Create(prod);
    	}catch (DAOexception e) {
    		return -1;
    	}
    	
    	return prod_id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(id <= 0 || id == null)
    		throw new InvalidProductIdException("product id is null or <=0");
    	if(newDescription == null || newDescription.equals(""))
    		throw new InvalidProductDescriptionException("product description is null or empty");
    	if(newCode == null || newCode.equals(""))
    		throw new InvalidProductCodeException("product barcode is null or empty");   
    	if(!validBarCode(newCode))
    		throw new InvalidProductCodeException("Wrong product barcode format");
    	if(newPrice <= 0)
    		throw new InvalidProductCodeException("Invalid price value (<=0)");
    	
    	
    	ProductType prod = null; 					// new product 
    	try {
    		prod = DAOproductType.Read(id);				// read necessary to leave other fields unchanged
    		prod.setProductDescription(newDescription);
    		prod.setBarCode(newCode);
    		prod.setPricePerUnit(newPrice);
    		prod.setNote(newNote);

    		DAOproductType.Update(prod);				//update
    		
    	}catch (DAOexception e) {
    		return false;
    	}
		return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	if(id <= 0 || id == null)
    		throw new InvalidProductIdException("product id is null or <=0");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	ProductType prod = new ezProductType();
    	prod.setId(id);
    	try{DAOproductType.Delete((ezProductType)prod);
    	}catch (DAOexception e) {
    		return false;
    	}
        return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	try{products = DAOproductType.readAll();
    	}catch (DAOexception e) {
    		return null;
    	}
    	
        return new ArrayList<ProductType>( products.values());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	if(barCode == null || barCode.equals(""))
    		throw new InvalidProductCodeException("product barcode is null or empty");   
    	if(!validBarCode(barCode))
    		throw new InvalidProductCodeException("Wrong product barcode format");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	
System.out.println("1");
    	ProductType prod = null;
    	try{prod = DAOproductType.read(barCode);
System.out.println("prod: " +prod.getBarCode());
    	}catch (DAOexception e) {
    		e.printStackTrace();
    		return null;
    	}
    	
        return prod;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	
    	try{products = DAOproductType.readAll(description);
    	}catch (DAOexception e) {
    		return null;
    	}
    	
        return new ArrayList<ProductType>( products.values());
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
    	if(productId <= 0 || productId == null)
    		throw new InvalidProductIdException("product id is null or <=0");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	ProductType prod = null; 						// new product 
    	try {
    		prod = DAOproductType.Read(productId);		// read necessary to leave other fields unchanged
    		if((prod.getQuantity() + toBeAdded < 0) || (prod.getLocation()==null)) {
    			System.out.println("Unacceptable quantity");
    			return false;
    		}
    		prod.setQuantity(prod.getQuantity() + toBeAdded);	//assign new value

    		DAOproductType.Update(prod);				//update
    	}catch (DAOexception e) {
    		System.out.println(e.getMessage());
    		return false;
    	}    	
		return true;
    	
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	

    	
    	if(productId <= 0 || productId == null)
    		throw new InvalidProductIdException("product id is null or <=0");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	String pattern = "^\\d+\\-\\d+\\-\\d+$";  // TODO be sure about the regex
        Pattern regex = Pattern.compile(pattern);
        Matcher check = regex.matcher(newPos);
    	if( !check.matches())
    		throw new InvalidLocationException();
    	/*
    	//TODO see if it can be done by the table properties
    	//check that no other product has the same position
    	List<ProductType> t_prod = null; 
    	Map<Integer, ProductType>map;
    	
    	map = DAOproductType.readAll();
    	t_prod = (List<ProductType>) map.values();
    	for(ProductType p : t_prod) {
    		if(p.getLocation().equals(newPos))
    			return false;
    	}
    		*/
    	
    	// real operations
    	ProductType prod = null; 						// old product 
    	try {prod = DAOproductType.Read(productId);		// read necessary to leave other fields unchanged
    	}catch (DAOexception e) {
    		return false;
    	}
    	
    	prod.setLocation(newPos);			//assign new value
    	try{DAOproductType.Update(prod);	//update
    	}catch (DAOexception e) {
    		return false;
    	}
    	
		return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(productCode == null || productCode.equals("") || !validBarCode(productCode))    	throw new InvalidProductCodeException();
        if( quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
        	
        //product does not exist
      	if(products.get(productCode) == null ) return -1;
    	
    	ezOrder o = new ezOrder();
        o.setProductCode(productCode);
        o.setQuantity(quantity);
        o.setPricePerUnit(pricePerUnit);
        
        orders.put(o.getOrderId(), o);
        
        return o.getOrderId();
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
    	ezProductType prod = (ezProductType) products.get(o.getProductCode());
        
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
    	
    	
    	int id = 0;
    	try {id = DAOcustomer.Create(new ezCustomer(customerName, cust_id, "N/A", 0));
    	}catch(DAOexception e) {
    		return -1;
    	}
    	
    	return id;
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
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();

    	
    	try{DAOcustomer.Update(new ezCustomer(newCustomerName, id, newCustomerCard,0));
    	}catch(DAOexception e) {
    		return false;
    	}
    	return true;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

    	if(id == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(id <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();
 
    	
    	try{DAOcustomer.Delete(id);
    	}catch(DAOexception e) {
    		return false;
    	}
    	return true;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id == null)
    		throw new InvalidCustomerIdException("customer id is null");
    	if(id <=0)
    		throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();

    	/*for (Customer c : customers) {
    		if(c.getId() == id) {
    			return c; 
    		}
    	} */
    	
    	ezCustomer cust;
    	try{cust = (ezCustomer) DAOcustomer.Read(id);
    	}catch(DAOexception e) {
    		return null;
    	}
    	return cust;
    	
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();

    	try{customers = DAOcustomer.readAll();
    	}catch(DAOexception e) {
    		return null;
    	}
    	return new ArrayList<Customer>( customers.values());
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();
    	//card_id++;
    	//cards.put("card_"+card_id, new LoyaltyCard("card_"+card_id, 0));
    	
    	LoyaltyCard card = new LoyaltyCard();
    	String id;
    	try{id = DAOloyaltyCard.Create(card);
    	}catch(DAOexception e) {
    		return null;
    	}
        return id;
    }

    @Override
 // TODO WHEN IS THIS FUNCTION CALLED
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	System.out.println("ATTACH CARD TO CUSTOMER is called");
    	
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
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();

    	
    	
    	Customer cust = null;
    	cust  = DAOcustomer.Read(customerId);   //get customer from DB
    	cust.setCustomerCard(customerCard);     //change the card
    	DAOcustomer.Update(cust);               //update the customer(this will update the card info as well)
    	/*
   	 	if DB is unreachable return false
    	 */
    	return true;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	if(customerCard == null)
    		throw new InvalidCustomerCardException("customer card is null");
    	if(customerCard.equals(""))
    		throw new InvalidCustomerCardException("customer card is empty");
    	if(customerCard.length() > 10)
    		throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();

    	/*
    	LoyaltyCard lc = cards.get(customerCard);
    	if( lc != null && (pointsToBeAdded >0 || lc.getPoints() >= pointsToBeAdded) ) {  //check that the points are positive and if they are negative check that the card points are not less
			lc.setPoints(pointsToBeAdded);
			return true;
		}
    	
    	 * TODO Not sure I respected the logic, check if right. Here is the previous code
    	for(LoyaltyCard lc : cards)						
			if(lc.getcardID().equals(customerCard)) {		 					   //get the card
				if(pointsToBeAdded >0 || lc.getPoints() >= pointsToBeAdded) {  //check that the points are positive and if they are negative check that the card points are not less
					lc.setPoints(pointsToBeAdded);
					return true;
				}
		} */ 
    	
    	//we get the card so that we will increase the already accumulated points
    	LoyaltyCard card;//old card points
    	try{
    		card = DAOloyaltyCard.Read(customerCard);
    	
    		card.setPoints(card.getPoints() + pointsToBeAdded); //new card points
    		DAOloyaltyCard.Update(card);
    	
    	}catch(DAOexception e) {
    		return false;
    	}
    	
    	
    	return true;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))) throw new UnauthorizedException();
   	
    	last_sale_id = DAOsaleTransaction.readId()+1; // it will be updated once the transaction is closed
    	openSale = new ezSaleTransaction(last_sale_id);
	
        return last_sale_id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	System.out.println("1111");       	
    	if(transactionId <=0 || transactionId ==null)
    		throw new InvalidTransactionIdException();
    	if(productCode == null || productCode.equals(""))
    		throw new InvalidProductCodeException("product barcode is null or empty");   
    	if(!validBarCode(productCode))
    		throw new InvalidProductCodeException("Wrong product barcode format");
    	if(amount < 0)
    		throw new InvalidQuantityException();
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	
    	
System.out.println("transactionId: " + transactionId + "\nproductCode: " + productCode + "\namount: " + amount);
    	ProductType prod = null;
    	ReceiptEntry entry = new ezReceiptEntry();
    	
    	try {
    		prod = DAOproductType.read(productCode);
    		if(prod == null) return false;
    	
    		entry.setAmount(amount);
    		entry.setDiscountRate(0.1);
    		entry.setBarCode(productCode);
    		entry.setPricePerUnit(prod.getPricePerUnit());
    		entry.setProductDescription(prod.getProductDescription());
    	
    		prod.setQuantity(prod.getQuantity() - amount);
    		openProds.put(productCode,prod);
    		openSale.getEntries().add(entry);
    		
    		openSale.setPrice(openSale.getPrice() + entry.getPricePerUnit()*entry.getAmount());
    	
    	
    	}catch(DAOexception e) {
    		return false;
    	}
    	return true;
    	
    }

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
    	if(transactionId <=0 || transactionId ==null)
    		throw new InvalidTransactionIdException();
    	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
    		throw new UnauthorizedException();
    	
    	last_sale_id++;
    	DAOsaleTransaction.Create(openSale);
    	
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
System.out.println("getSaleTransaction");
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
        returns.put(ret_id, ret);
        ret.setSaleReference(saleNumber);
        SaleTransaction referingSale = getSaleTransaction(saleNumber);
        //referingSale.add(ReturnTransaction);
        return ret_id;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
/*        
    	int i=0;
    	
    	try {
    		//retrieve return transaction and referring sale transaction
	    	ReturnTransaction ret =returns.get(returnId);
	    	
	    	SaleTransaction referingSale = (SaleTransaction) sales.get(ret.getSaleReference());
	    	
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
        */
        return true;
    }
    
    

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
    	if(returnId <= 0 || returnId == null)
    		throw new InvalidTransactionIdException();
    	//TODO throw exception if user has no rights
    	int i;
    	/*
    	try {
    	
	    	ReturnTransaction ret = returns.get(returnId);
	    	
	    	SaleTrans referingSale = (SaleTrans) sales.get(ret.getSaleReference());
	    	
	    	if(commit) {
	    		//increase quantity available
	    		ProductType prodType = products.get(ret.getProdId());
	    		
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
    	*/
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
