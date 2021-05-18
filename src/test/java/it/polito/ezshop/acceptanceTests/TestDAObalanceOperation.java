package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.Credit;
import it.polito.ezshop.classes.Debit;
import it.polito.ezshop.classes.ezProductType;
import it.polito.ezshop.classesDAO.DAObalanceOperation;
import it.polito.ezshop.classesDAO.DAOproductType;
import it.polito.ezshop.classesDAO.DBManager;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.ProductType;
import net.bytebuddy.asm.Advice;
import org.junit.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class TestDAObalanceOperation {
    @BeforeClass
    public static void establishConnection() {
        DBManager.getConnection();
    }




    //The id doesn't matter it is calculated in the DB
    @Before
    public void populateDB() {
        DAObalanceOperation.DeleteAll();

    }

    @Test
    public void testCreateCredit() {
        int newId = DAObalanceOperation.readAll().size() +1;
        BalanceOperation credit = new Credit(newId , LocalDate.now(), 500);

        int id = DAObalanceOperation.Create(credit);
        Assert.assertTrue(DAObalanceOperation.readAll().containsKey(id));

    }

    @Test
    public void testCreateDebit() {
        int newId = DAObalanceOperation.readAll().size() +1;
        BalanceOperation debit = new Debit(newId , LocalDate.now(), -500);

        int id = DAObalanceOperation.Create(debit);
        Assert.assertTrue(DAObalanceOperation.readAll().containsKey(id));

    }

    @Test
    public void testCreateBOWith0Money() {
        int newId = DAObalanceOperation.readAll().size() +1;
        BalanceOperation debit = new Debit(newId , LocalDate.now(), 0);

        int id = DAObalanceOperation.Create(debit);
        Assert.assertTrue(DAObalanceOperation.readAll().containsKey(id));

    }

    @Test (expected = NullPointerException.class)
    public void testCreateBOWithWrongId() {
        int newId = DAObalanceOperation.readAll().size() +1;
        BalanceOperation debit = new Debit(newId , LocalDate.now(), 0);
        DAObalanceOperation.Create(debit);

        //same id, will throw NullPointerException exception
        BalanceOperation credit = new Credit(newId , LocalDate.now(), 0);
        DAObalanceOperation.Create(credit);


    }

    @Test
    public void testReadAllBalanceOperation() {
        int idCredit = DAObalanceOperation.readAll().size() +1;
        BalanceOperation credit = new Credit(idCredit , LocalDate.now(), 500);
        DAObalanceOperation.Create(credit);
        int idDebit = idCredit +1;
        BalanceOperation debit = new Debit(idDebit, LocalDate.now(), -200);
        DAObalanceOperation.Create(debit);

        Map<Integer, BalanceOperation> BOlist = DAObalanceOperation.readAll();

        Assert.assertTrue(BOlist.containsKey(idCredit));
        Assert.assertTrue(BOlist.containsKey(idDebit));
        Assert.assertEquals(BOlist.size(),2);

        BalanceOperation firstAdded = BOlist.get(1);
        Assert.assertEquals(firstAdded.getDate(), credit.getDate());
        Assert.assertEquals(firstAdded.getMoney(), credit.getMoney(), 0.0001);
        Assert.assertEquals(firstAdded.getType(), credit.getType());

        BalanceOperation secondAdded = BOlist.get(2);
        Assert.assertEquals(secondAdded.getDate(), debit.getDate());
        Assert.assertEquals(secondAdded.getMoney(), debit.getMoney(), 0.0001);
        Assert.assertEquals(secondAdded.getType(), debit.getType());

    }

    @AfterClass
    public static void endConnection() {
        DBManager.closeConnection();
    }
}
