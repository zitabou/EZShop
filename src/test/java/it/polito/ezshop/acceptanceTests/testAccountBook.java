package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.AccountBook;
import it.polito.ezshop.classes.Credit;
import it.polito.ezshop.classes.Debit;
import it.polito.ezshop.classesDAO.DAObalanceOperation;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.BalanceOperation;
import org.junit.*;

import java.time.LocalDate;
import java.util.Map;

public class testAccountBook {
    AccountBook acc;
    @BeforeClass
    public static void establishConnection() {
        DBManager.getConnection();
    }




    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() {
        DAObalanceOperation.DeleteAll();
         acc = new AccountBook();
         acc.recordBalanceUpdate(100, 1);

    }

    @Test
    public void testRecordBalanceUpdate() {
        acc.recordBalanceUpdate(10, acc.getCreditsAndDebits().size() +1);
        Assert.assertEquals(110, acc.computeBalance(), 0.0001);
    }
    @Test
    public void testRecordBalanceUpdateNeg() {
        acc.recordBalanceUpdate(-10, acc.getCreditsAndDebits().size() +1);
        Assert.assertEquals( 90,acc.computeBalance(), 0.0001);
    }

    @Test
    public void testRecordBalanceUpdateZero() {
        acc.recordBalanceUpdate(0, acc.getCreditsAndDebits().size() +1);
        Assert.assertEquals( 100,acc.computeBalance(), 0.0001);
    }

    @Test
    public void testGetCreditsAndDebits() {
        Assert.assertEquals(1, acc.getCreditsAndDebits().size());
    }



    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
