/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 *
 * @author GrachevEA
 */
public class CommandManager_Invoker {

    private Command add;
    private Command demand;
    private Command purchase;
    private Command report;
    private Command info;
    final static Logger logger = Logger.getLogger(CommandManager_Invoker.class);

    public CommandManager_Invoker(Command passed_add, Command passed_demand, Command passed_purchase, Command passed_report, Command passed_info) {
        add = passed_add;
        demand = passed_demand;
        purchase = passed_purchase;
        report = passed_report;
        info = passed_info;
    }

    public void startAdd(List<String> strList) {
        add.execute(strList);
    }

    public void startDemand(List<String> strList) {
        demand.execute(strList);
    }

    public void startPurchase(List<String> strList) {
        purchase.execute(strList);
    }

    public void startReport(List<String> strList) {
        report.execute(strList);
    }

    public void startInfo(List<String> strList) {
        info.execute(strList);
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        CommandManager_Invoker invoker = new CommandManager_Invoker(new CommandAddNewProduct(receiver),
                new CommandDemand(receiver), new CommandPurchase(receiver), new CommandSalesReport(receiver), new CommandInfo(receiver));
        Scanner sc = new Scanner(System.in);
        int firstScan = 0;
        do {
            List<String> strList = new ArrayList<String>();
            String str = sc.nextLine();
            String delimeter = "\\s";
            for (String retval : str.split(delimeter)) {
                strList.add(retval);
            }
            try {
                switch (EnumCommand.valueOf(strList.get(0))) {
                    case PURCHASE: {
                        invoker.startPurchase(strList);
                        break;
                    }
                    case DEMAND: {
                        invoker.startDemand(strList);
                        break;
                    }
                    case SALESREPORT: {
                        invoker.startReport(strList);
                        break;
                    }
                    case NEWPRODUCT: {
                        invoker.startAdd(strList);
                        break;
                    }
                    case INFO: {
                        invoker.startInfo(strList);
                        break;
                    }
                }
            } catch (IllegalArgumentException ex) {
                logger.error("ERROR! Wrong command.", ex);
                if (firstScan == 0) {
                    firstScan++;
                    List<String> firstList = new ArrayList<String>();
                    firstList.add("INFO");
                    invoker.startInfo(firstList);
                } else {
                    logger.error("ERROR! Wrong command.", ex);
                    receiver.resultMessage("ERROR");
                }
            }
        } while (sc.hasNext());
    }
}
