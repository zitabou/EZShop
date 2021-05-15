package it.polito.ezshop.main;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.view.EZShopGUI;

import java.time.LocalDate;

public class TestBalanceOperation {
    public static void main(String[] args) throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {


        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        LocalDate from = LocalDate.now();

        shop.recordBalanceUpdate(2);
        LocalDate to = LocalDate.now();
        System.out.println(shop.getCreditsAndDebits(from, to));
        EZShopGUI gui = new EZShopGUI(shop);


    }
}
