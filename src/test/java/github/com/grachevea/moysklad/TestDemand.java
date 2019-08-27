/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author GrachevEA
 */
public class TestDemand {

    Receiver receiver;
    CommandManager_Invoker invoker;
    List<String> test, test2, test3;

    @Before
    public void initialize() {
        receiver = new Receiver();
        invoker = new CommandManager_Invoker(new CommandAddNewProduct(receiver),
                new CommandDemand(receiver), new CommandPurchase(receiver), new CommandSalesReport(receiver), new CommandInfo(receiver));
        test = new ArrayList<String>();
        test2 = new ArrayList<String>();
        test3 = new ArrayList<String>();
    }

    @Test
    public void TestCreateDemand() {
        //Right command
        test.add("DEMAND");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("02.01.2017");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        //PURCHASE
        receiver.purchase("iphone", 5, 2000,  LocalDate.of(2017, 01, 02));
        invoker.startDemand(test);
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 02))));
        Assert.assertEquals(receiver.demandStorage.size(), 1);
    }

    @Test
    public void TestCreateDemandBeforePurchase() {
        //Not: NEWPRODUCT before PURCHASE before DEMAND
        test.add("DEMAND");
        test.add("iphone");
        test.add("2");
        test.add("5000");
        test.add("04.01.2017");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        invoker.startDemand(test);
        //Empty because: PURCHASE before DEMAND
        Assert.assertTrue(receiver.demandStorage.isEmpty());
        receiver.purchase("iphone", 2, 1000, LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        //Well done!
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 04))));
    }

    @Test
    public void TestDemandWithWrongCommandSize() {
        //Right command
        test.add("DEMAND");
        test.add("iphone");
        test.add("1");
        test.add("02.01.2017");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        //PURCHASE
        receiver.purchase("iphone", 5, 2000,LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone",LocalDate.of(2017, 1, 1))));
        Assert.assertNotEquals(receiver.demandStorage.size(), 1);
    }

    @Test
    public void TestDemandWithNonNegativeAmountOrPrice() {
        //Right command
        test.add("DEMAND");
        test.add("iphone");
        test.add("-1");
        test.add("1000");
        test.add("02.01.2017");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        //PURCHASE
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        //Empty because (AMOUNT < 0) => True
        Assert.assertTrue(receiver.demandStorage.isEmpty());
        test2.add("PURCHASE");
        test2.add("iphone");
        test2.add("1");
        test2.add("-1000");
        test2.add("01.01.2017");
        invoker.startDemand(test2);
        //False because now (PRICE < 0)
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 01, 01))));
    }

    @Test
    public void TestDemandAndPurchaseAmount() {
        test.add("DEMAND");
        test.add("iphone");
        test.add("2");
        test.add("5000");
        test.add("04.01.2017");
        receiver.commandsetNP.add("iphone");
        receiver.purchase("iphone", 1, 2000, LocalDate.of(2017, 01, 02));
        invoker.startDemand(test);
        //Empty because: PURCHASE.amount< DEMAND.amount
        Assert.assertTrue(receiver.demandStorage.isEmpty());
        //Need one more iphone
        receiver.purchase("iphone", 1, 1000, LocalDate.of(2017, 01, 03));
        invoker.startDemand(test);
        //Well done!
        Assert.assertFalse(receiver.demandStorage.isEmpty());
    }

    @Test
    public void TestCheckDemandAndPurchaseDate() {
        test.add("DEMAND");
        test.add("iphone");
        test.add("2");
        test.add("5000");
        test.add("04.01.2017");
        receiver.commandsetNP.add("iphone");
        receiver.purchase("iphone", 1, 2000, LocalDate.of(2017, 01, 02));
        invoker.startDemand(test);
        //Empty because: PURCHASE.amount< DEMAND.amount
        Assert.assertTrue(receiver.demandStorage.isEmpty());
        //Need one more iphone
        receiver.purchase("iphone", 1, 1000, LocalDate.of(2017, 01, 05));
        invoker.startDemand(test);
        //Oops! Too late!
        Assert.assertTrue(receiver.demandStorage.isEmpty());
    }

    @Test
    public void TestDemandWrongDate() {
        //Date must be DD.MM.YYYY
        test.add("DEMAND");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("01.01.2017");
        receiver.commandsetNP.add("iphone");
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        //True - everything is correct! That means we can sell iphone at the same day 
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 01))));
        test2.add("DEMAND");
        test2.add("iphone");
        test2.add("1");
        test2.add("1000");
        test2.add("02022017");
        invoker.startDemand(test2);
        //False because no delimeter = "."
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 02, 02))));
        test2.add("DEMAND");
        test3.add("iphone");
        test3.add("1");
        test3.add("1000");
        test3.add("Today");//for Example 03.03.2017
        invoker.startDemand(test3);
        //False because wrong Date - need DD.MM.YYYY, for example 2017(int). Cannot convert this String (Today) to int in commands:
        /*
        *int day = Integer.parseInt(stringList.get(0));
        *int month = Integer.parseInt(stringList.get(1));
        *int year = Integer.parseInt(stringList.get(2));
         */
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 03, 03))));
    }

    @Test
    public void TestDemandStrAmountOrPrice() {
        test.add("DEMAND");
        test.add("iphone");
        test.add("one");
        test.add("onethousand");
        test.add("01.01.2017");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        //PURCHASE
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        //False because wrong Amount and Price. Cannot convert this Strings (one/onethousand) to int in commands:
        /*
        *int day = Integer.parseInt(stringList.get(0));
        *int month = Integer.parseInt(stringList.get(1));
        *int year = Integer.parseInt(stringList.get(2));
         */
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 01))));
        //Right command
        test2.add("DEMAND");
        test2.add("iphone");
        test2.add("1");
        test2.add("1000");
        test2.add("01.01.2017");
        invoker.startDemand(test2);
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 01))));
    }

    @Test
    public void TestDemandWrongDateSize() {
        //Right command
        test.add("DEMAND");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("02.01");
        //NEWPRODUCT
        receiver.commandsetNP.add("iphone");
        //PURCHASE
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        invoker.startDemand(test);
        //False: because Date size !=3
        Assert.assertFalse(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 01, 02))));
        Assert.assertEquals(receiver.demandStorage.size(), 0);
    }
}
