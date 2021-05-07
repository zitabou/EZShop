package it.polito.ezshop.classes;

import it.polito.ezshop.data.*;

public class ezUser implements User{
	
	public ezUser() {
		id = 0;
		username = null;
		password = null;
		role = "N/A";
	}
	
	public ezUser(Integer id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	

    public Integer getId() {return id;}

    public void setId(Integer id) {}

    public String getUsername() {return username;}

    public void setUsername(String username) {}

    public String getPassword() {return password;}

    public void setPassword(String password) {}

    public String getRole() {return role;}

    public void setRole(String role) {}
	
    private Integer id;
    private String username;
    private String password;
    private String role;
	
}
