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
public class TestSalesReport {

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
    public void TestCreateSalesReport() {
        //NEWPRODUCT + with check
        receiver.commandsetNP.add("iphone");
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //PURCHASE + with check
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        //DEMAND + with check
        receiver.demand("iphone", 2, 5000, LocalDate.of(2017, 01, 02));
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 1, 2))));
        //SALESREPORT    
        test.add("SALESREPORT");
        test.add("iphone");
        test.add("02.01.2017");
        invoker.startReport(test);
        Assert.assertTrue(receiver.reportStorage.containsKey(new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 1, 2))));
        Assert.assertNotEquals(receiver.reportStorage.size(), 0);
    }

    @Test
    public void TestSalesReportWithWrongCommandSize() {
        //NEWPRODUCT + check
        receiver.commandsetNP.add("iphone");
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //PURCHASE + check
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        //DEMAND + check
        receiver.demand("iphone", 2, 5000, LocalDate.of(2017, 01, 02));
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 1, 2))));
        //SALESREPORT
        test.add("SALESREPORT");
        test.add("iphone");
        invoker.startReport(test);
        Assert.assertFalse(receiver.reportStorage.containsKey(new Operation("SALESREPORTT", "iphone", LocalDate.of(2017, 1, 2))));
        Assert.assertEquals(receiver.reportStorage.size(), 0);
    }

    @Test
    public void TestSalesReportWithWrongDate() {

        //NEWPRODUCT + check
        receiver.commandsetNP.add("iphone");
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //PURCHASE + check
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        //DEMAND + check
        receiver.demand("iphone", 2, 5000, LocalDate.of(2017, 01, 02));
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 1, 2))));
        //SALESREPORT
        //Date must be DD.MM.YYYY
        test.add("SALESREPORT");
        test.add("iphone");
        test.add("Today");//for example today is 02.01.2017
        invoker.startReport(test);
        //False: because wrong Date - must be DD.MM.YYYY, 
        /*for example 02.01.2017(int). Cannot convert this String (Today) to int in commands:
        *int day = Integer.parseInt(stringList.get(0));
        *int month = Integer.parseInt(stringList.get(1));
        *int year = Integer.parseInt(stringList.get(2));
         */
        Assert.assertFalse(receiver.reportStorage.containsKey(new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 1, 2))));
        Assert.assertEquals(receiver.reportStorage.size(), 0);

        test2.add("SALESREPORT");
        test2.add("iphone");
        test2.add("02-01-2017");
        invoker.startReport(test2);
        //False because wtong delimeter = "-", must be "."
        Assert.assertFalse(receiver.reportStorage.containsKey(new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 1, 2))));
        Assert.assertEquals(receiver.reportStorage.size(), 0);
    }

    @Test
    public void TestCreateSalesReportBeforeDemand() {
        //NEWPRODUCT + check
        receiver.commandsetNP.add("iphone");
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //PURCHASE + check
        receiver.purchase("iphone", 5, 2000, LocalDate.of(2017, 01, 01));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        //DEMAND + check
        receiver.demand("iphone", 2, 5000, LocalDate.of(2017, 01, 02));
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 1, 2))));
        //SALESREPORT
        test.add("SALESREPORT");
        test.add("iphone");
        test.add("01.01.2017");
        invoker.startReport(test);
        Assert.assertFalse(receiver.reportStorage.containsKey(new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 1, 1))));
        Assert.assertEquals(receiver.reportStorage.size(), 0);
        test2.add("SALESREPORT");
        test2.add("iphone");
        test2.add("02.01.2017");
        invoker.startReport(test2);
        //Now true: because demand(02.01.2017) same day report (02.01.2017)
        Assert.assertTrue(receiver.reportStorage.containsKey(new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 1, 2))));
        Assert.assertNotEquals(receiver.reportStorage.size(), 0);

    }

    @Test
    public void TestSalesReportProfitCheck() {
        //NEWPRODUCT + check
        receiver.commandsetNP.add("iphone");
        Assert.assertTrue(receiver.commandsetNP.contains("iphone"));
        //PURCHASE + check
        receiver.purchase("iphone", 1, 1000, LocalDate.of(2017, 01, 01));
        receiver.purchase("iphone", 2, 2000, LocalDate.of(2017, 02, 01));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 1, 1))));
        Assert.assertTrue(receiver.purchaseStorage.containsKey(new Operation("PURCHASE", "iphone", LocalDate.of(2017, 2, 1))));
        //DEMAND + check
        receiver.demand("iphone", 2, 5000, LocalDate.of(2017, 03, 01));
        Assert.assertTrue(receiver.demandStorage.containsKey(new Operation("DEMAND", "iphone", LocalDate.of(2017, 3, 1))));
        //SALESREPORT
        test.add("SALESREPORT");
        test.add("iphone");
        test.add("02.03.2017");
        invoker.startReport(test);
        Integer profit = 7000;
        Operation salesreportOperation = new Operation("SALESREPORT", "iphone", LocalDate.of(2017, 3, 2));
        Assert.assertTrue(receiver.reportStorage.containsKey(salesreportOperation));
        Assert.assertEquals(profit, receiver.getValue(salesreportOperation));
    }
}
