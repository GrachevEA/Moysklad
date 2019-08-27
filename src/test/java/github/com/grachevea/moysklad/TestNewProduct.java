/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author GrachevEA
 */
public class TestNewProduct {

    Receiver receiver;
    CommandManager_Invoker invoker;
    List<String> test;

    @Before
    public void initialize() {
        receiver = new Receiver();
        invoker = new CommandManager_Invoker(new CommandAddNewProduct(receiver),
                new CommandDemand(receiver), new CommandPurchase(receiver), new CommandSalesReport(receiver), new CommandInfo(receiver));
        test = new ArrayList<String>();
    }

    @Test
    public void TestAddNewPoduct() {
        test.add("NEWPRODUCT");
        test.add("iphone");
        invoker.startAdd(test);
        //true if successful product adding
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //True because correct command
        Assert.assertEquals(test.size(), 2);
    }

    @Test
    public void TestAddDuplicatePoduct() {
        test.add("NEWPRODUCT");
        test.add("iphone");
        invoker.startAdd(test);
        //After FirstAdd
        Assert.assertEquals(receiver.commandsetNP.size(), 1);
        invoker.startAdd(test);
        //With another adding of the same product, the size didn't change
        Assert.assertEquals(receiver.commandsetNP.size(), 1);
    }

    @Test
    public void TestNewProductWrongCommandSize() {
        test.add("NEWPRODUCT");
        test.add("iphone");
        test.add("1");
        test.add("1000");
        test.add("01.01.2017");
        invoker.startAdd(test);
        //False... Too big command string
        Assert.assertFalse(receiver.commandsetNP.contains("iphone"));
        //True because uncorrect command size (5)
        Assert.assertTrue(test.size() != 2);
    }
}
