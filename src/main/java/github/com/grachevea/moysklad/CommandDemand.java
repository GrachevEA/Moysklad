/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author GrachevEA
 */
public class CommandDemand implements Command {

    private Receiver receiver;
    final static Logger logger = Logger.getLogger(CommandDemand.class);

    CommandDemand(Receiver rec) {
        receiver = rec;
    }

    @Override
    public void execute(List<String> strList) {
        List<String> stringList = new ArrayList<String>(3);
        if (strList.size() != 5) {
            logger.error("ERROR! Wrong command(DEMAND) size, != 5\n");
            receiver.resultMessage("ERROR");
        } else {
            String product_name = strList.get(1);
            boolean checkProduct = receiver.commandsetNP.contains(product_name);
            if (checkProduct != true) {
                logger.error("ERROR! Need to add this product before DEMAND\n");
                receiver.resultMessage("ERROR");
            } else {
                String strDate = strList.get(4);
                String delimeter = "\\.";
                for (String retval : strDate.split(delimeter)) {
                    stringList.add(retval);
                }
                if (stringList.size() != 3) {
                    logger.error("ERROR! Wrong date.size, != 3, command (DEMAND)\n");
                    receiver.resultMessage("ERROR");
                } else {
                    try {
                        int day = Integer.parseInt(stringList.get(0));
                        int month = Integer.parseInt(stringList.get(1));
                        int year = Integer.parseInt(stringList.get(2));
                        int amount = Integer.parseInt(strList.get(2));
                        int price = Integer.parseInt(strList.get(3));
                        if (price < 0 || amount < 0) {
                            logger.error("ERROR! price or amount < 0\n");
                            receiver.resultMessage("ERROR");
                        } else {
                            Operation getPurchaseOperation
                                    = new Operation("PURCHASE", product_name, LocalDate.of(year, month, day));
                            int forEach_i = 0;
                            int productCounter = 0;
                            boolean chechPurchaseKey = false;
                            for (Operation key : receiver.purchaseStorage.keySet()) {
                                forEach_i = key.date.compareTo(getPurchaseOperation.date);
                                chechPurchaseKey = (key.product.equals(getPurchaseOperation.product));
                                if (forEach_i <= 0 && chechPurchaseKey) {
                                    productCounter += receiver.purchaseStorage.get(key).amount;
                                }
                            }
                            Operation getDemandOperation
                                    = new Operation("DEMAND", product_name, LocalDate.of(year, month, day));
                            int demandBeforeproductCounter = 0;
                            boolean checkDemandKey = false;
                            for (Operation key : receiver.demandStorage.keySet()) {
                                forEach_i = key.date.compareTo(getDemandOperation.date);
                                checkDemandKey = (key.product.equals(getDemandOperation.product));
                                if (forEach_i <= 0 && checkDemandKey) {
                                    demandBeforeproductCounter += receiver.demandStorage.get(key).amount;
                                }
                            }
                            if (demandBeforeproductCounter >= productCounter) {
                                logger.error("ERROR! not enough product to demand\n");
                                receiver.resultMessage("ERROR");
                            } else {
                                productCounter -= demandBeforeproductCounter;
                                if (productCounter < amount) {
                                    logger.error("ERROR! not enough product to demand\n");
                                    receiver.resultMessage("ERROR");
                                } else {
                                    receiver.demand(product_name, amount, price, LocalDate.of(year, month, day));
                                }
                            }
                        }
                    } catch (NumberFormatException | DateTimeException ex) {
                        logger.error("Failed!", ex);
                        receiver.resultMessage("ERROR");
                    }
                }
            }
        }
    }
}
