package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;

public class ezCreditCard {
	
	public ezCreditCard() {
		id = 0;
		cc_number  = "";
		owner_name= "";
		secure_code= "";
		valid = false;
	}
	

	//luhn algorithm
	public boolean checkLuhn(String cardNo)
	{
		System.out.println("Validating credit card: " + cardNo);
	    int nDigits = cardNo.length();
	 
	    int nSum = 0;
	    boolean isSecond = false;
	    for (int i = nDigits - 1; i >= 0; i--)
	    {
	 
		int d = cardNo.charAt(i) - '0';
	 
		if (isSecond == true)
		    d = d * 2;
	 
		// We add two digits to handle
		// cases that make two digits
		// after doubling
		nSum += d / 10;
		nSum += d % 10;
	 
		isSecond = !isSecond;
	    }
	    boolean isValid = false;
	    isValid = (nSum % 10 == 0);
	    this.valid = isValid;
	    return isValid;

	} 

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id= id;}
    
    public String getCCNumber() {return cc_number;}

    public void setCCNumber(String cc_number) {this.cc_number = cc_number;}

    public String getOwnerName() {return owner_name;}

    public void setOwnerName(String owner_name) {this.owner_name = owner_name;}

    public String getSecureCode() {return secure_code;}

    public void setSecureCode(String role) {this.secure_code = secure_code;}

    public boolean getValidity() {return valid;}

    public void setValidity(boolean valid ) {this.valid= valid;}

    private Integer id;
    private String cc_number;
    private String owner_name;
    private String secure_code;
    private boolean valid;
}
