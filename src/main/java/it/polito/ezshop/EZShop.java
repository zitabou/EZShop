package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args){
	try{
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);
	} catch (NumberFormatException nfe) {
		// Exception at the level of the GUI
		nfe.printStackTrace();
	}
    }

}
