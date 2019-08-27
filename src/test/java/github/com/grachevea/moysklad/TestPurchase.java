/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author GrachevEA
 */
public class TestPurchase {

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
    public void TestCreatePurchase() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("01.01.2017");
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
    }

    @Test
    public void TestPurchaseBeforeCreateNewProduct() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("01.01.2017");
        invoker.startPurchase(test);
        //True: because NEWPRODUCT before PURCHASE
        Assert.assertTrue(receiver.purchaseStorage.isEmpty());
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        //Now False that means not Empty
        Assert.assertFalse(receiver.purchaseStorage.isEmpty());
    }

    @Test
    public void TestPurchaseWrongCommandSize() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("01.01.2017");
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        //True! Just check that the product exists.
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //False... Too low command string (No Amount, no Price)
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        //True because uncorrect command size (3)
        Assert.assertNotEquals(test.size(), 5);
    }

    @Test
    public void TestPurchaseNonNegativeAmountOrPrice() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("-1");
        test.add("1000");
        test.add("01.01.2017");
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        //Empty because (AMOUNT < 0 OR PRICE < 0) => True
        Assert.assertTrue(receiver.purchaseStorage.isEmpty());

        test.add("PURCHASE");
        test.add("iphone");
        test.add("1");
        test.add("-1000");
        test.add("01.01.2017");
        invoker.startPurchase(test);
        //False if purchase request return error (AMOUNT < 0 OR PRICE < 0)
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
    }

    @Test
    public void TestPurchaseStrAmountOrPrice() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("one");
        test.add("onethousand");
        test.add("01.01.2017");
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
    }

    @Test
    public void TestPurchaseWrongDate() {
        test.add("PURCHASE");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("01-01-2017");
        receiver.commandsetNP.add("iphone");
        invoker.startPurchase(test);
        //False because wrong delimeter
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        test2.add("PURCHASE");
        test2.add("iphone");
        test2.add("1");
        test2.add("1000");
        test2.add("2017.01.01");
        invoker.startPurchase(test2);
        //False because wrong order
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        test2.add("PURCHASE");
        test3.add("iphone");
        test3.add("1");
        test3.add("1000");
        test3.add("01.01.17");
        invoker.startPurchase(test3);
        //False because wrong YEAR - need YYYY
        Assert.assertFalse(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        test.clear();
        System.out.println("size = " + test.size());
    }
}
