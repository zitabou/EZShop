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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import org.apache.commons.lang3.StringUtils;


public class EZShop implements EZShopInterface {

    private Map<Integer, User> users;
    private Map<Integer, ProductType> products;
    private Map<Integer, Customer> customers;
    private Map<Integer, ReturnTransaction> returns;
    private Map<Integer, Order> orders;
    private AccountBook accountBook;
    private ezUser activeUser;

    //	private Integer user_id = 1; //'Admin' user is created with user_id=1, if the table does not exist.
    private Integer cust_id = 555;
    private Integer ret_id = 0;
    private Integer balance_id = 0;
    private Integer order_id = 0;

    private Integer last_sale_id = 0;
    private SaleTransaction openSale = null;
    private Map<String, ProductType> prodsToUpdate = null;

    private List<TicketEntry> retEntries = null;
    private Map<String, ProductType> prodsToReturn = null;


    public EZShop() {

        accountBook = new AccountBook();
        //users = new HashMap<>();
        products = new HashMap<>();
        customers = new HashMap<>();
        returns = new HashMap<>();
        orders = new HashMap<>();
        activeUser = null;
        //	users.put(0, new ezUser(0, "admin", "admin", "Administrator"));
        prodsToUpdate = new HashMap<>();
        prodsToReturn = new HashMap<>();

        DBManager.getConnection();
        DBManager.closeConnection();

    }


    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
	if (username.equalsIgnoreCase("") || username == null) {
		throw new InvalidUsernameException("Username is empty or null at createUser(...).");
	}
	if (password.equalsIgnoreCase("") || password == null) {
		throw new InvalidPasswordException("Password is empty or null at createUser(...).");
	}
	if ((role.equals("Cashier") == true || role.equals("ShopManager") == true || role.equals("Administrator") == true)==false || role.equalsIgnoreCase("")){
		throw new InvalidRoleException("The role parameter is not Cashier, ShopManager or Administrator; or is empty. createUser(...)");
	}
	Integer user_id = 0;
	try {
		// Check if the username is not being used already
		for (User u : getAllUsers()) {
			if ( u.getUsername().equals(username) ) {
				throw new InvalidUsernameException("The username is already in use.");
			}
		}
		User usr = new ezUser(username, password, role);
		user_id = DAOuser.Create(usr);	
	} catch (DAOexception e){
		return -1;
	} finally {
		return user_id;
	}
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
       	if (activeUser == null || activeUser.getRole().equals("Administrator") == false) {
	throw new UnauthorizedException("The active user is not authorized to deleteUser(id), or there is no logged user.");
	}
	if (id <= 0 || id == null) {
	throw new InvalidUserIdException("Invalid User ID. deleteUser(id)");
	}
       	try {
		DAOuser.Delete(id);
	} catch ( DAOexception e ) {
		return false;
	} finally {
		return true;
	}	
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
	users = null;
	if (activeUser == null || activeUser.getRole().equals("Administrator") == false) {
	throw new UnauthorizedException("The active user is not authorized to getAllUsers(), or there is no logged user.");
	}

    	try{
		users= DAOuser.readAll();
    	}catch (DAOexception e) {
    		return null;
    	} finally {
        	return new ArrayList<User>(users.values());
	}
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
	User usr = null;
	try {
		usr = DAOuser.Read(id);
		if (usr==null || id <= 0 || id == null) {
			throw new InvalidUserIdException("Invalid User ID. getUser(id)");
		}
		if (activeUser == null || activeUser.getRole().equals("Administrator") == false) {
			throw new UnauthorizedException("The active user is not authorized to getUser(id), or there is no logged user.");
		}
	} catch (DAOexception e) {
		return null;
	} finally {
		return usr;
	}
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
	User usr = getUser(id);
	if (usr==null || id <= 0 || id == null) {
		throw new InvalidUserIdException("Invalid User ID. updateUserRights(,)");
	}
	if ((role.equals("Cashier") == true || role.equals("ShopManager") == true || role.equals("Administrator") == true)==false || role.equalsIgnoreCase("")){
		throw new InvalidRoleException("The role parameter is not Cashier, ShopManager or Administrator; or is empty. updateUserRights(,)");
	}
	if (activeUser == null || (activeUser.getRole().equals("Administrator") == false)) {
		throw new UnauthorizedException("The active user is not authorized to updateUserRights(,) or there is no logged user.");
	}

	usr.setRole(role);
	try {
		DAOuser.Update(usr);
	} catch (DAOexception e) {
		return false;	
	} finally {
		return true;
	}
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
	//Exceptions
        if (username.equalsIgnoreCase("") || username == null) {
		throw new InvalidUsernameException("Username is empty or null at login.");
	}
	if (password.equalsIgnoreCase("") || password == null) {
		throw new InvalidPasswordException("Password is empty or null at login.");
	}	
	//
        activeUser = null;	
    	try {
		users = DAOuser.readAll();
	} catch (DAOexception e) {
		return null;		
	} 
	boolean validate_username = false;
	boolean validate_password = false;
	User aux_usr = null;
    	for (User u : users.values()) {
		//System.out.println(u.getId() + "-" + u.getUsername() + "-" + u.getPassword());
		validate_username = validate_username || u.getUsername().equals(username);
		if (validate_username) {
			aux_usr = u;
			break;
		}	
    	}
	if (validate_username == false) {
		throw new InvalidUsernameException("Username does not exist.");
	}	
	try {
    		if(aux_usr.getPassword().equals(password)) {
    			activeUser = (ezUser) aux_usr;
			return aux_usr;
	    	} else {
			throw new InvalidPasswordException("Password is incorrect.");
		}
	}catch (Exception e) {
		return null;
	}
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

        if (!StringUtils.isNumeric(productCode) || productCode.length() < 12 || productCode.length() > 14)
            return false;

        char[] char_digits = productCode.toCharArray();
        int check_digit = 0;
        int mul = 0;
        long round_int = 0;
        double round = 0;

        //get the sum of products
        int pair = (char_digits.length - 1) % 2;
        for (int i = 0; i < char_digits.length - 1; i++) {
            mul = (2 * ((i + pair) % 2)) + 1;
            check_digit = (Character.getNumericValue(char_digits[i]) * mul) + check_digit;
        }

        //calculate the nearest equal or higher multiple of ten
        round = (double) check_digit / 10.0;
        round_int = (long) round;
        if (round - round_int != 0)  //fractional part /=0
            round = (round_int + 1) * 10;
        else                      //fractional part  =0
            round = check_digit;
        //check if the last digit is indeed what it should
        if (round - check_digit == Character.getNumericValue(char_digits[char_digits.length - 1]))
            return true;
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (description == null || description.equals(""))
            throw new InvalidProductDescriptionException("product description is null or empty");
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(productCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("price per unit cannot be <=0");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        Integer prod_id = null;
        try {
            ezProductType prod = new ezProductType(0, description, productCode, pricePerUnit, 0, note, "N/A");
            prod_id = DAOproductType.Create(prod);
        } catch (DAOexception e) {
            return -1;
        }

        return prod_id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (id <= 0 || id == null)
            throw new InvalidProductIdException("product id is null or <=0");
        if (newDescription == null || newDescription.equals(""))
            throw new InvalidProductDescriptionException("product description is null or empty");
        if (newCode == null || newCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(newCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (newPrice <= 0)
            throw new InvalidProductCodeException("Invalid price value (<=0)");

        ProductType prod = null;                    // new product
        try {
            prod = DAOproductType.read(id);                // read necessary to leave other fields unchanged
            prod.setProductDescription(newDescription);
            prod.setBarCode(newCode);
            prod.setPricePerUnit(newPrice);
            prod.setNote(newNote);

            DAOproductType.Update(prod);                //update

        } catch (DAOexception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if (id <= 0 || id == null)
            throw new InvalidProductIdException("product id is null or <=0");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

         Location loc = DAOlocation.Read(id);
        if(loc !=null) {
        	loc.setProduct(0);
        	DAOlocation.Update(loc);
        }

        ProductType prod = new ezProductType();
        prod.setId(id);
        try {
            DAOproductType.Delete((ezProductType) prod);
        } catch (DAOexception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();
        try {
            products = DAOproductType.readAll();
        } catch (DAOexception e) {
            return null;
        }

        return new ArrayList<ProductType>(products.values());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if (barCode == null || barCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(barCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        ProductType prod = null;
        try {
            prod = DAOproductType.read(barCode);
        } catch (DAOexception e) {
            e.printStackTrace();
            return null;
        }

        return prod;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        try {
            products = DAOproductType.readAll(description);
        } catch (DAOexception e) {
            return null;
        }

        return new ArrayList<ProductType>(products.values());
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if (productId <= 0 || productId == null)
            throw new InvalidProductIdException("product id is null or <=0");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        ProductType prod = null;                        // new product
        try {
            prod = DAOproductType.read(productId);        // read necessary to leave other fields unchanged
            if ((prod.getQuantity() + toBeAdded < 0)) {
                System.out.println("Unacceptable quantity");
                return false;
            }
            if (prod.getLocation() == null) {
                System.out.println("No location specified for product: " + prod.getId());
                return false;
            }
            prod.setQuantity(prod.getQuantity() + toBeAdded);    //assign new value

            DAOproductType.Update(prod);                //update
        } catch (DAOexception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;

    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {


        if (productId <= 0 || productId == null)
            throw new InvalidProductIdException("product id is null or <=0");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        String pattern = "^\\d+\\-\\d+\\-\\d+$";
        Pattern regex = Pattern.compile(pattern);
        Matcher check = regex.matcher(newPos);
        if (!check.matches())
            throw new InvalidLocationException();


		 
        //define position
        //reset old position if oldPos exists
        Location oldLoc = DAOlocation.Read(productId);
        if(oldLoc != null ) {
        	oldLoc.setProduct(0);
        	DAOlocation.Update(oldLoc);
        }
        
        //check if new position exists and if so is it available
        if(!newPos.equals("0-0-0")){
        	Location newLoc = DAOlocation.Read(newPos);
        	if(newLoc == null){                           //if it doesn't exist create it
        		newLoc = new Location();
        		newLoc.setAisleByExtract(newPos);
        		newLoc.setRackByExtract(newPos);
        		newLoc.setLevelByExtract(newPos);
        		newLoc.setProduct(productId);
        		DAOlocation.Create(newLoc);
        	}
        	else if(newLoc.getProduct() == 0) {  //if the position exists and it is not occupied update it
        		newLoc.setProduct(productId);
        		DAOlocation.Update(newLoc);
        	}
        }




        // real operations
        ProductType prod = null;                        // old product
        try {
            prod = DAOproductType.read(productId);        // read necessary to leave other fields unchanged
            prod.setLocation(newPos);            //assign new value
            DAOproductType.Update(prod);    //update
        } catch (DAOexception e) {
            return false;
        }

        return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (productCode == null || productCode.equals("") || !validBarCode(productCode))
            throw new InvalidProductCodeException();
        if (quantity <= 0) throw new InvalidQuantityException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();

        //product does not exist
        if (DAOproductType.read(productCode) == null) return -1;
        order_id = 0;
        DAOorder.readAll().values().stream().map(o -> o.getOrderId()).forEach(id -> {
            if (id > order_id)
                order_id = id;
        });
        order_id++;
        ezOrder o = new ezOrder();
        o.setProductCode(productCode);
        o.setQuantity(quantity);
        o.setPricePerUnit(pricePerUnit);
        o.setOrderId(order_id);
        o.setStatus("ISSUED");

        DAOorder.Create(o);

        orders.put(o.getOrderId(), o);

        return o.getOrderId();
    }


    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        //exceptions are raised by the called methods

        Integer orderId = issueOrder(productCode, quantity, pricePerUnit);
        if (orderId == -1) return -1;

        try {
            if (!payOrder(orderId))
                return -1;
        } catch (InvalidOrderIdException | UnauthorizedException e) {
            e.printStackTrace();
        }

        return orderId;

    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (orderId <= 0) throw new InvalidOrderIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();

        ezOrder o = (ezOrder) orders.get(orderId);
        if (o == null || !(o.getStatus().equals("ISSUED") || o.getStatus().equals("ORDERED")))
            return false;

        double amountToPay = o.getQuantity() * o.getPricePerUnit();

        if (accountBook.computeBalance() < amountToPay)
            return false;

        recordBalanceUpdate(-amountToPay);
        o.setStatus("PAYED");
        DAOorder.Update(o);
        return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if (orderId <= 0) throw new InvalidOrderIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();

        ezOrder o = (ezOrder) orders.get(orderId);
        ezProductType prod = (ezProductType) DAOproductType.readAll().values().stream().filter(p -> p.getBarCode().equals(o.getProductCode()))
                .collect(Collectors.toList()).get(0);


        if (prod.getLocation() == null) throw new InvalidLocationException();

        if (!(o.getStatus().equals("PAYED") || o.getStatus().equals("COMPLETED"))) return false;

        if (o.getStatus().equals("PAYED")) {


            prod.setQuantity(prod.getQuantity() + o.getQuantity());
            DAOproductType.Update(prod);
            o.setStatus("COMPLETED");
            DAOorder.Update(o);
        }


        return true;

    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();
        //

        orders = DAOorder.readAll();
        List<Order> ordersInIssuedOrderedOrCompletedState = orders.values().stream()
                .filter(o -> (o.getStatus().equals("ISSUED") || o.getStatus().equals("ORDERED") || o.getStatus().equals("COMPLETED") || o.getStatus().equals("PAYED")))
                .collect(Collectors.toList());

        return ordersInIssuedOrderedOrCompletedState;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (customerName == null)
            throw new InvalidCustomerNameException("customer name is null ");
        if (customerName.equals(""))
            throw new InvalidCustomerNameException("customer name is empty ");


        int id = 0;
        try {
            id = DAOcustomer.Create(new ezCustomer(customerName, cust_id, "N/A", 0));
        } catch (DAOexception e) {
            return -1;
        }

        return id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        if (newCustomerName == null)
            throw new InvalidCustomerNameException("customer name is null ");
        if (newCustomerName.equals(""))
            throw new InvalidCustomerNameException("customer name is empty ");
        if (id == null)
            throw new InvalidCustomerIdException("customer id is null");
        if (id <= 0)
            throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
        if (newCustomerCard == null)
            throw new InvalidCustomerCardException("customer card is null");
        if (newCustomerCard.equals(""))
            throw new InvalidCustomerCardException("customer card is empty");
        if (newCustomerCard.length() > 10)
            throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        try {
            DAOcustomer.Update(new ezCustomer(newCustomerName, id, newCustomerCard, 0));
        } catch (DAOexception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        if (id == null)
            throw new InvalidCustomerIdException("customer id is null");
        if (id <= 0)
            throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        try {
            Customer cust = DAOcustomer.Read(id);
        	LoyaltyCard card = DAOloyaltyCard.ReadCustomer(cust);
        	
        	DAOloyaltyCard.Delete(card);
            DAOcustomer.Delete(id);
        } catch (DAOexception e) {
            return false;
        }
        return true;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (id == null)
            throw new InvalidCustomerIdException("customer id is null");
        if (id <= 0)
            throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        ezCustomer cust;
        try {
            cust = (ezCustomer) DAOcustomer.Read(id);
        } catch (DAOexception e) {
            return null;
        }
        return cust;

    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();
        try {
            customers = DAOcustomer.readAll();
        } catch (DAOexception e) {
            return null;
        }
        return new ArrayList<Customer>(customers.values());
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        LoyaltyCard card = new LoyaltyCard();
        String id;
        try {
            id = DAOloyaltyCard.Create(card);
        } catch (DAOexception e) {
            return null;
        }
        return id;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

        if (customerId == null)
            throw new InvalidCustomerIdException("customer id is null");
        if (customerId <= 0)
            throw new InvalidCustomerIdException("customer id is less or equal to zero");     // some clients may have a zero id if the constructor used is the one without parameter. Those customers cannot be modified, searched or deleted
        if (customerCard == null)
            throw new InvalidCustomerCardException("customer card is null");
        if (customerCard.equals(""))
            throw new InvalidCustomerCardException("customer card is empty");
        if (customerCard.length() > 10)
            throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        Customer cust = null;
        cust = DAOcustomer.Read(customerId);   //get customer from DB
        cust.setCustomerCard(customerCard);     //change the card
        DAOcustomer.Update(cust);               //update the customer(this will update the card info as well)
        /*
        if DB is unreachable return false
         */
        return true;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (customerCard == null)
            throw new InvalidCustomerCardException("customer card is null");
        if (customerCard.equals(""))
            throw new InvalidCustomerCardException("customer card is empty");
        if (customerCard.length() > 10)
            throw new InvalidCustomerCardException("Wrong customer card format (must be 10 digits string)");
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        //we get the card so that we will increase the already accumulated points
        LoyaltyCard card;//old card points
        try {
            	card = DAOloyaltyCard.Read(customerCard); //old card points
		if (card == null) {
			return false;
		}
            	card.setPoints(card.getPoints() + pointsToBeAdded); //new card points
            	DAOloyaltyCard.Update(card);
        } catch (DAOexception e) {
            return false;
        }


        return true;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        last_sale_id = DAOsaleTransaction.getId() + 1; // it will be updated once the transaction is closed
        openSale = new ezSaleTransaction(last_sale_id);
        List<TicketEntry> entry = new ArrayList<>();
        openSale.setEntries(entry);

        return last_sale_id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(productCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (amount < 0)
            throw new InvalidQuantityException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        ProductType prod = null;
        TicketEntry entry = new ezReceiptEntry();
        try {
            prod = DAOproductType.read(productCode);
            if (amount > prod.getQuantity()) return false;

            entry.setAmount(amount);
            entry.setDiscountRate(0.0);
            entry.setBarCode(productCode);
            entry.setPricePerUnit(prod.getPricePerUnit());
            entry.setProductDescription(prod.getProductDescription());

            //products
            if (prodsToUpdate.containsKey(productCode))
                prod = prodsToUpdate.get(productCode);
            prod.setQuantity(prod.getQuantity() - amount);
            prodsToUpdate.put(productCode, prod);
            //entries
            for (int i = 0; i < openSale.getEntries().size(); i++) {
                if (openSale.getEntries().get(i).getBarCode().equals(productCode)) {                                            // get entry if exists
                    openSale.getEntries().get(i).setAmount(amount + openSale.getEntries().get(i).getAmount());                // set amount in entries
                    openSale.setPrice(openSale.getPrice() + openSale.getEntries().get(i).getPricePerUnit() * amount);        // pricePerUnit is the discounted price
                    return true;
                }
            }
            openSale.setPrice(openSale.getPrice() + entry.getPricePerUnit() * amount);
            openSale.getEntries().add(entry);                                                                   // or create a new one if it doesn't,  pricePerUnit is the product's price per unit with 0% discount
        } catch (DAOexception e) {
            e.getMessage();
            return false;
        }
        return true;

    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(productCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (amount < 0)
            throw new InvalidQuantityException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        /* IT DOESNT INFLUENCE THE GUI VALUES !!!!  */
        /* IT IS ONLY USED FOR KEEPING TRACK INTERNALLY */

        for (int i = 0; i < openSale.getEntries().size(); i++)
            if (openSale.getEntries().get(i).getBarCode().equals(productCode))
                if (openSale.getEntries().get(i).getAmount() < amount) {
                    return false;
                } else {
                    openSale.setPrice(openSale.getPrice() - openSale.getEntries().get(i).getPricePerUnit() * amount);    //update price
                    if (openSale.getEntries().get(i).getAmount() == amount) {                                            //if the amount is equal to the one requested then remove
                        openSale.getEntries().remove(i);
                        prodsToUpdate.remove(productCode);

                    } else {                                                                                                //else increase quantity
                        openSale.getEntries().get(i).setAmount(openSale.getEntries().get(i).getAmount() - amount);
                        prodsToUpdate.get(productCode).setQuantity(prodsToUpdate.get(productCode).getQuantity() + amount);
                    }

                    return true;
                }

        return true;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException("product barcode is null or empty");
        if (!validBarCode(productCode))
            throw new InvalidProductCodeException("Wrong product barcode format");
        if (discountRate < 0 || discountRate >= 1)
            throw new InvalidDiscountRateException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        if (transactionId == last_sale_id) {
            try {
                DAOproductType.read(productCode);
            } catch (DAOexception e) {
                e.getMessage();
                return false;
            }
            //TODO check the price change
            for (int i = 0; i < openSale.getEntries().size(); i++) {
                if (openSale.getEntries().get(i).getBarCode().equals(productCode)) {

                    openSale.setPrice(openSale.getPrice() - openSale.getEntries().get(i).getPricePerUnit() * openSale.getEntries().get(i).getAmount());            //reset price

                    openSale.getEntries().get(i).setDiscountRate(discountRate);
                    openSale.getEntries().get(i).setPricePerUnit(prodsToUpdate.get(productCode).getPricePerUnit() * (1 - discountRate));                            //update discounted price per unit
                    openSale.setPrice(openSale.getPrice() + openSale.getEntries().get(i).getPricePerUnit() * openSale.getEntries().get(i).getAmount());            //compute new price
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (discountRate < 0 || discountRate >= 1)
            throw new InvalidDiscountRateException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        if (transactionId != last_sale_id)
            return false;
        if (openSale.getDiscountRate() != 0)
            openSale.setPrice(openSale.getPrice() / (1 - openSale.getDiscountRate()));

        openSale.setDiscountRate(discountRate);
        openSale.setPrice(openSale.getPrice() * (1 - discountRate));

        return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
	if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

	//Check if transaction exists
	
	Map<Integer, SaleTransaction> sts = null;
	int computed_points = 0;
	try {
		sts= DAOsaleTransaction.ReadAll();
		for (SaleTransaction st : sts.values()) {
			if(st.getTicketNumber() == transactionId){
				System.out.println("SaleTransction found, computing points...");
				System.out.println(st.getTicketNumber() + "|" +  st.getPrice() + "|" + st.getDiscountRate());
				computed_points = (int)(st.getPrice()/10.0);
				System.out.println("Computed points: " + computed_points);
				return computed_points;
			}
		}
		return -1;
	}catch (DAOexception e) {
		return -1;
	}
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        try {

            if (transactionId != DAOsaleTransaction.getId() + 1)
                return false;


            //create the transaction
            Integer saleId = DAOsaleTransaction.Create(openSale);
            for (int i = 0; i < openSale.getEntries().size(); i++) {
                System.out.println("entry(" + i + ") ");
                DAOsaleEntry.Create(saleId, openSale.getEntries().get(i));
                System.out.println(openSale.getEntries().get(i));
            }

            //update the products
            for (ProductType prod : prodsToUpdate.values()) {
                System.out.println("product ");
                DAOproductType.UpdateByCode(prod);
                System.out.println(prod.getBarCode());
            }

            //get ready for next sale
            last_sale_id = 0;
            openSale = null;
            prodsToUpdate.clear();


        } catch (DAOexception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        if (saleNumber <= 0 || saleNumber == null)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();


        List<TicketEntry> sale_entries = null;
        ProductType prod = null;


        try {

            //restore product availability
            sale_entries = DAOsaleEntry.Read(saleNumber);
            for (TicketEntry entry : sale_entries) {
                prod = DAOproductType.read(entry.getBarCode());
                prod.setQuantity(prod.getQuantity() + entry.getAmount());
                DAOproductType.Update(prod);
            }

            //remove sale transaction that removes the entries as well
            DAOsaleTransaction.Delete(saleNumber);
        } catch (DAOexception ex) {
            ex.getMessage();
            return false;
        }


        return true;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        return DAOsaleTransaction.Read(transactionId);
    }


    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        if (saleNumber <= 0 || saleNumber == null) throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();

        ret_id = 0;
        DAOreturnTransaction.readAll().values().stream().map(r -> r.getBalanceId()).forEach(id -> {
            if (id > ret_id)
                ret_id = id;
        });
        ret_id++;
        ReturnTransaction ret = new ReturnTransaction();
        ret.setBalanceId(ret_id);
        returns.put(ret_id, ret);
        ret.setSaleID(saleNumber);
        SaleTransaction referingSale = getSaleTransaction(saleNumber);
        retEntries = new ArrayList<>();

        DAOreturnTransaction.Create(ret);
        //referingSale.add(ReturnTransaction);
        return ret_id;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager"))) throw new UnauthorizedException();
    	int i=0;



    	try {
    		//retrieve return transaction and referring sale transaction
	    	ReturnTransaction ret =DAOreturnTransaction.Read(returnId);
	    	
	    	SaleTransaction referingSale = (SaleTransaction) DAOsaleTransaction.Read(ret.getSaleID());
	    	
	    	//return false if amount to be returned > amount bought

			List<TicketEntry> saleEntries = DAOsaleEntry.Read(ret.getSaleID());


            /*entry*/
			/**/
			ProductType prod = null;
			TicketEntry entry = new ezReceiptEntry();
	    	for(TicketEntry te : saleEntries) {
	    		if(te.getBarCode().equals(productCode)) {
	    			entry.setProductDescription(te.getProductDescription());
	    			entry.setBarCode(productCode);
	    			entry.setAmount(amount);
	    			entry.setPricePerUnit(te.getPricePerUnit());
	    			System.out.println(entry.getProductDescription());
	    			retEntries.add(entry);
	    		}
		    	
	    	}
	    	prod = DAOproductType.read(productCode);
	    	if (prodsToReturn.containsKey(productCode)) {
	    		prod = prodsToReturn.get(productCode);
	    	}
            prod.setQuantity(prod.getQuantity() + amount);
            System.out.println(prod.getBarCode());
            prodsToUpdate.put(productCode, prod);
	    	
	    	/**/
			/*entry*/
			AtomicBoolean amountReturnedIsHigherThanAmountBought = new AtomicBoolean(false);
			AtomicReference<Double> money= new AtomicReference<>((double) 0);
			saleEntries.forEach( e -> {
				if (e.getBarCode().equals(productCode) )
					if (e.getAmount() < amount)
						amountReturnedIsHigherThanAmountBought.set(true);
					else
						money.set(e.getPricePerUnit() * amount * (1-e.getDiscountRate())); // is already considered in e.getPricePerUnit
						//money.set(e.getPricePerUnit() * e.getAmount());

			});
			if(amountReturnedIsHigherThanAmountBought.get())
				return false;

	        ret.setAmount(amount);
	        ret.setProdId(productCode);
	        //ret.setMoney(money.get());
	        ret.setMoney(ret.getMoney() + money.get()*(1-referingSale.getDiscountRate()));
		DAOreturnTransaction.Update(ret);

    	}catch(Exception e) {
    		//return false if the transaction do not exist
    		return false;
    	}

        return true;
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId <= 0 || returnId == null)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();
        int i;

        try {

            ReturnTransaction ret = DAOreturnTransaction.Read(returnId);

            ezSaleTransaction referingSale = (ezSaleTransaction) DAOsaleTransaction.Read(ret.getSaleID());

            if (commit) {

                /*entry*/
            	/**/
                for (int j = 0; j< retEntries.size(); j++) {
                    System.out.println("entry(" + j + ") ");
                    DAOreturnEntry.Create(returnId, retEntries.get(j));
                    System.out.println(retEntries.get(j));
                }

                //update the products
                for (ProductType prod : prodsToReturn.values()) {
                    System.out.println("product ");
                    DAOproductType.UpdateByCode(prod);
                    System.out.println(prod.getBarCode());
                }

                //get ready for next sale
                last_sale_id = 0;
                retEntries = null;
                prodsToReturn.clear();
            	/**/
                /*entry*/

                List<TicketEntry> returnedProducts = DAOreturnEntry.Read(returnId);
                
                //increase quantity available :TODO for all products
                for (TicketEntry rp : returnedProducts){
                    ProductType prodType = DAOproductType.read(rp.getBarCode());
                    prodType.setQuantity(prodType.getQuantity() + rp.getAmount());
                    DAOproductType.Update(prodType);
                }


                //decrease amount spent on the Sale
                referingSale.setPrice(referingSale.getPrice() - ret.getReturnedValue());
                DAOsaleTransaction.Update(referingSale);

                //remove returned items from sale transaction
                List<TicketEntry> saleEntries = DAOsaleEntry.Read(ret.getSaleID());
                for (TicketEntry rp : returnedProducts) {
                    saleEntries.forEach(e -> {
                        if (e.getBarCode().equals(rp.getBarCode())) //TODO: not ret.getProdId ma il prod id delle ret entries
                            e.setAmount(e.getAmount() - rp.getAmount());
                    });
                }
                //update (remove old entries and recreate new entries)
                DAOsaleEntry.DeleteFromSale(ret.getSaleID());
                saleEntries.forEach(e -> {
                    if (e.getAmount() > 0)
                        DAOsaleEntry.Create(ret.getSaleID(), e);
                });


            }else{
                DAOreturnTransaction.Delete(returnId);

            }
        } catch (Exception e) {
            //return false if the returnTransaction is not found or if problem with DB
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager|Cashier")))
            throw new UnauthorizedException();

        try {
            ReturnTransaction ret = DAOreturnTransaction.Read(returnId);
            if (ret == null) return false;
            DAOreturnTransaction.Delete(returnId);

            //decrease quantity available
            ProductType prodType = DAOproductType.read(ret.getProdId());
            prodType.setQuantity(prodType.getQuantity() - ret.getAmount());
            DAOproductType.Update(prodType);

            //add previously returned items to sale transaction
            List<TicketEntry> saleEntries = DAOsaleEntry.Read(ret.getSaleID());
            saleEntries.forEach(e -> {
                if (e.getBarCode().equals(ret.getProdId()))
                    e.setAmount(e.getAmount() + ret.getAmount());
            });
            //update (remove old entries and recreate new entries)
            DAOsaleEntry.DeleteFromSale(ret.getSaleID());
            saleEntries.forEach(e -> {
                if (e.getAmount() > 0)
                    DAOsaleEntry.Create(ret.getSaleID(), e);
            });
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
	System.out.println("Receiving Cash Payment...");
	// TODO NumberFormatException: empty String
	// when the textbox for cash is left empty.
	// Exceptions
	if (ticketNumber <= 0) {
		throw new InvalidTransactionIdException("The ticketNumber (or id) is less or equal to 0. receiveCashPayment(,).");
	}
       if (cash <= 0) {
		throw new InvalidPaymentException("The cash is less than or equal to 0. receiveCashPayment(,)");
	}
	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))){
	       	throw new UnauthorizedException("The active user is not authorized. receiveCashPayment(,)");
	}
	double change = 0;
	try {
	SaleTransaction st = getSaleTransaction(ticketNumber);
	if (st == null) { return -1;}
	if (cash < st.getPrice()) { return -1;}
       
	change = cash - st.getPrice();
	//Record balance operation
	accountBook.getCreditsAndDebits().stream().map(bo -> bo.getBalanceId() ).forEach(id -> {
			if(id > balance_id)
				balance_id = id;
		});
		
    	balance_id++;
        accountBook.recordBalanceUpdate(st.getPrice(), balance_id);
       	} catch (DAOexception e) {
	return -1;
       	}
	return change;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        
	if (ticketNumber <= 0 || ticketNumber == null) {
		throw new InvalidTransactionIdException("The ticketNumber (or id) is less or equal to 0. receiveCashPayment(,).");
	}
	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))){
	       	throw new UnauthorizedException("The active user is not authorized. receiveCashPayment(,)");
	}
	// Check if credit card is in the system
	ezCreditCard cc = null;
	boolean ccFound = false;
	try {
		for (ezCreditCard ccc : DAOcc.readAll().values()){
			if (ccc.getCCNumber().equals(creditCard)){
				ccFound = true;
				cc = ccc;
				break;
			}
		}
	} catch (DAOexception e) {
		return false;
	}
	// If cc not found create credit card and Create record in database 
	if (!ccFound) {
		cc = new ezCreditCard();
		cc.setCCNumber(creditCard);
		if ( creditCard == null || creditCard.equals("")|| !cc.checkLuhn(creditCard)){
			throw new InvalidCreditCardException("The credit card is invalid!");
		}
		System.out.println("Validez: " + cc.getValidity());

		//Record on the DB a new CC if the number is valid
		if (cc.getValidity()) {
			System.out.println("CC is valid!");
			try{
			cc.setValidity(true);
			cc.setId(DAOcc.Create(cc));
			} catch(DAOexception e) {
			return false;
			}
		}
	} else{
		//Check if CC is valid using Luhn's algorithm
		if ( creditCard == null || creditCard.equals("")|| !cc.checkLuhn(creditCard)){
			throw new InvalidCreditCardException("The credit card is invalid!");
		}
		System.out.println("Validez: " + cc.getValidity());
	}

	if (cc.getValidity()){
		try {
		SaleTransaction st = getSaleTransaction(ticketNumber);
		if (st == null) { return false;}
		//Record balance operation
		accountBook.getCreditsAndDebits().stream().map(bo -> bo.getBalanceId() ).forEach(id -> {
				if(id > balance_id)
					balance_id = id;
			});
			
		balance_id++;
		accountBook.recordBalanceUpdate(st.getPrice(), balance_id);
		} catch (DAOexception e) {
		return false;
		}
		return true;
	} else {
		return false;
	}
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
	// Exceptions
	if (returnId <= 0){
		throw new InvalidTransactionIdException("The returnId is not valid. returnCashPayment(id)");
	}
	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))){
	       	throw new UnauthorizedException("The active user is not authorized. returnCashPayment(id)");
	}
	
	double money_returned = 0; 
	try {
	ReturnTransaction rt = DAOreturnTransaction.Read(returnId);
	if (rt == null) { return -1;}

	money_returned = rt.getReturnedValue();
	//Record balance operation
	accountBook.getCreditsAndDebits().stream().map(bo -> bo.getBalanceId() ).forEach(id -> {
			if(id > balance_id)
				balance_id = id;
		});
    	balance_id++;
        accountBook.recordBalanceUpdate(0-money_returned, balance_id);
	} catch(DAOexception e) {
		return -1;
	}
	System.out.println("Money returned: " + money_returned);
        return money_returned;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
	//Exceptions
	if (returnId <= 0 || returnId== null) {
		throw new InvalidTransactionIdException("The ticketNumber (or id) is less or equal to 0. receiveCashPayment(,).");
	}
	if (activeUser == null || ! (activeUser.getRole().matches("Administrator|ShopManager|Cashier"))){
	       	throw new UnauthorizedException("The active user is not authorized. receiveCashPayment(,)");
	}

	ezCreditCard cc = DAOcc.Read(creditCard);
	if (cc == null) {
		return -1;
	}
	if ( creditCard == null || creditCard.equals("")|| !cc.checkLuhn(creditCard)){
		throw new InvalidCreditCardException("The credit card is invalid!");
	}
	
	double money_returned = 0; 
	try {
		ReturnTransaction rt = DAOreturnTransaction.Read(returnId);
		if (rt == null) { return -1;}

		money_returned = rt.getReturnedValue();
		//Record balance operation
		accountBook.getCreditsAndDebits().stream().map(bo -> bo.getBalanceId() ).forEach(id -> {
				if(id > balance_id)
					balance_id = id;
			});
		balance_id++;
		accountBook.recordBalanceUpdate(0-money_returned, balance_id);
	} catch(DAOexception e) {
		return -1;
	}
	System.out.println("Money returned: " + money_returned);
        return money_returned;
    }

    @Override
    public boolean recordBalanceUpdate(double qty) throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();

        //get last balance_id on DB
        accountBook.getCreditsAndDebits().stream().map(bo -> bo.getBalanceId()).forEach(id -> {
            if (id > balance_id)
                balance_id = id;
        });

        balance_id++;
        return accountBook.recordBalanceUpdate(qty, balance_id);

    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();
        //TODO check if only Administrator and ShopManager can call this method

        if (from == null || to == null) return null;

        if (from.isAfter(to)) {
            Map<String, LocalDate> swappedDates = swapDates(from, to);
            from = swappedDates.get("from");
            to = swappedDates.get("to");
        }

        LocalDate finalFrom = from;
        LocalDate finalTo = to;
        List<BalanceOperation> credsAndDebtsFiltered = accountBook.getCreditsAndDebits().stream()
                .filter(balanceOperation -> (balanceOperation.getDate().isAfter(finalFrom) && balanceOperation.getDate().isBefore(finalTo)))
                .collect(Collectors.toList());

        return credsAndDebtsFiltered;
    }

    private Map<String, LocalDate> swapDates(LocalDate from, LocalDate to) {

        Map<String, LocalDate> swapped = new HashMap<>();
        swapped.put("from", to);
        swapped.put("to", from);
        return swapped;
    }


    @Override
    public double computeBalance() throws UnauthorizedException {
        if (activeUser == null || !(activeUser.getRole().matches("Administrator|ShopManager")))
            throw new UnauthorizedException();
        //TODO check if only Administrator and ShopManager can call this method

        return accountBook.computeBalance();
    }
}
