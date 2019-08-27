/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 *
 * @author GrachevEA
 */
public class TestInfo {

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
    public void TestInfoCommand() {
        test.add("INFO");
        invoker.startInfo(test);
        Assert.assertEquals(test.size(), 1);
    }
    @Test
    public void TestInfoCommandWithWrongSize(){
        test.add("INFO");
        test.add("iphone");
        invoker.startInfo(test);
        Assert.assertNotEquals(test.size(), 1);
    }
}
