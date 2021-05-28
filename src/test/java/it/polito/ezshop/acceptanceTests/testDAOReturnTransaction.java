package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.classesDAO.*;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class testDAOReturnTransaction {

    AccountBook acc;
    @BeforeClass
    public static void establishConnection() {
        DBManager.getConnection();
    }


    int idSale= 1500;
    int ret_id = 0;

    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() {
        //create return transaction for the update method

    }

    @Test
    public void testCreateReturnTransaction() {
        ret_id = DAOreturnTransaction.readAll().values().stream().map(ReturnTransaction::getBalanceId).mapToInt(id -> id).filter(id -> id >= 0).max().orElse(0);
        ret_id++;
        ReturnTransaction ret = new ReturnTransaction();
        ret.setBalanceId(ret_id);
        ret.setSaleID(idSale);
        ret.setMoney(15);
        ret.setProdId("629104150024");
        ret.setAmount(1);

        DAOreturnTransaction.Create(ret);

        ReturnTransaction r = DAOreturnTransaction.Read(ret_id);

        Assert.assertNotNull(r);
    }

    @Test
    public void testUpdateReturnTransaction() {
        ReturnTransaction ret = new ReturnTransaction();
        ret.setBalanceId(ret_id);
        ret.setSaleID(idSale);
        ret.setMoney(15);
        ret.setProdId("629104150024");
        ret.setAmount(1);

        DAOreturnTransaction.Create(ret);

        ReturnTransaction r = DAOreturnTransaction.Read(ret_id);
        r.setAmount(2);

        DAOreturnTransaction.Update(r);


        r = DAOreturnTransaction.Read(ret_id);

        Assert.assertEquals(r.getAmount(), 2);
    }

    @Test
    public void testDeleteReturnTransaction() {

        DAOreturnTransaction.Delete(ret_id);


        ReturnTransaction r = DAOreturnTransaction.Read(ret_id);

        Assert.assertNull(r);
    }






    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
