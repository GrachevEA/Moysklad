/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.LocalDate;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author GrachevEA
 */
public class Receiver {

    Set<String> commandsetNP = new HashSet<String>();

    Map<Operation, OperationСost> purchaseStorage = new HashMap<Operation, OperationСost>();
    Map<Operation, OperationСost> demandStorage = new HashMap<Operation, OperationСost>();
    Map<Operation, Integer> reportStorage = new HashMap<Operation, Integer>();

    final static Logger logger = Logger.getLogger(Receiver.class);

    public Integer getValue(Operation key) {
        return reportStorage.get(key);
    }

    void resultMessage(String resultMessage) {
        System.out.println(resultMessage);
    }

    void info() {
        logger.info("Command Info");
        resultMessage("OK");
    }

    void add(String product_name) {
        logger.info("NEWPRODUCT " + product_name + "\n");
        commandsetNP.add(product_name);
        resultMessage("OK");
    }

    void purchase(String product_name, int amount, int price, LocalDate localdate) {
        logger.info("PURCHASE " + product_name + " " + amount + " " + price + " " + localdate + "\n");
        purchaseStorage.put(new Operation("PURCHASE",
                product_name,
                localdate),
                new OperationСost(amount, price));
        resultMessage("OK");
    }

    void demand(String product_name, int amount, int price, LocalDate localdate) {
        demandStorage.put(new Operation("DEMAND",
                product_name,
                localdate),
                new OperationСost(amount, price));
        resultMessage("OK");
    }

    void report(String product_name, LocalDate localdate) {
        Operation getDemandOperation
                = new Operation("DEMAND", product_name, localdate);
        int forEach_i = 0;
        int forEach_j = 0;
        int i = 0;
        int minAmount = 0; 
        int minCost = 0; 
        int productCounter = 0;
        int primeCost = 0;
        int totalSum = 0;
        boolean chechKey = false;

        for (Operation key : demandStorage.keySet()) {
            forEach_i = key.date.compareTo(getDemandOperation.date);
            chechKey = (key.product.equals(getDemandOperation.product));
            if (forEach_i <= 0 && chechKey) {
                productCounter += demandStorage.get(key).amount;
                totalSum += demandStorage.get(key).operationCost;
            }
        }
        Operation EarliestOperation = new Operation("PURCHASE", product_name, localdate);
        Operation EarliestOperation2 = new Operation("PURCHASE", product_name, localdate);
        chechKey = false;
        
        Operation getPurchaseOperation
                = new Operation("PURCHASE", product_name, localdate);
        while (productCounter != 0) {
            //Get min key
            if (i == 0) {
                for (Operation key : purchaseStorage.keySet()) {
                    if (i == 0) {
                        forEach_i = key.date.compareTo(getPurchaseOperation.date);
                        chechKey = (key.product.equals(getPurchaseOperation.product));
                        if (forEach_i <= 0 && chechKey) {
                            EarliestOperation = key;
                            i++;
                        }
                    } else {
                        forEach_i = key.date.compareTo(EarliestOperation.date);
                        chechKey = (key.product.equals(getPurchaseOperation.product));
                        if (forEach_i <= 0 && chechKey) {
                            EarliestOperation = key;
                        }
                    }
                }
            } else {
                for (Operation key : purchaseStorage.keySet()) {
                    forEach_i = key.date.compareTo(getPurchaseOperation.date);
                    forEach_j = key.date.compareTo(EarliestOperation.date);
                    chechKey = (key.product.equals(getPurchaseOperation.product));
                    if (forEach_i <= 0 && chechKey && forEach_j > 0) {
                        EarliestOperation2 = key;
                        for (Operation key2 : purchaseStorage.keySet()) {
                            forEach_i = key2.date.compareTo(EarliestOperation.date);
                            forEach_j = key2.date.compareTo(EarliestOperation2.date);
                            chechKey = (key2.product.equals(getPurchaseOperation.product));
                            if (forEach_i > 0 && chechKey && forEach_j < 0) {
                                EarliestOperation2 = key2;
                            }
                        }
                    }
                }
                EarliestOperation = EarliestOperation2;
            }
            minAmount = purchaseStorage.get(EarliestOperation).amount;
            minCost = purchaseStorage.get(EarliestOperation).price;
            logger.info("\tEarliestOperationDate: " + EarliestOperation.date + "\n"+
                        "\tEarliestOperationAmount: " + minAmount + "\n"+
                        "\tEarliestOperationCost: " + minCost + "\n"+
                        "\tProductCounter: " + productCounter + "\n");
            //if enough
            if (productCounter <= minAmount) {
                primeCost += productCounter * minCost;
                productCounter = 0;
            } //if not enough
            else {
                primeCost += minAmount * minCost;
                productCounter -= minAmount;
            }
            logger.info("\tPrimeCost: " + primeCost + "\n");
        }
        logger.info("\tProductCounterBeforeProfit: " + productCounter + "\n");
        Integer profit = 0;
        profit = totalSum - primeCost;
        logger.info("\tProfit: " + profit + " = " + totalSum + " - " + primeCost + "\n");
        reportStorage.put(new Operation("SALESREPORT",
                product_name,
                localdate),
                profit);
        resultMessage(profit.toString());
    }
}
