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
public class CommandSalesReport implements Command {

    private Receiver receiver;
    final static Logger logger = Logger.getLogger(CommandSalesReport.class);

    CommandSalesReport(Receiver rec) {
        receiver = rec;
    }

    @Override
    public void execute(List<String> strList) {
        if (strList.size() != 3) {
            logger.error("ERROR! Wrong command(SALESREPORT) size, != 3\n");
            receiver.resultMessage("ERROR");
        } else {
            List<String> stringList = new ArrayList<String>(3);
            String product_name = strList.get(1);
            String strDate = strList.get(2);
            String delimeter = "\\.";
            for (String retval : strDate.split(delimeter)) {
                stringList.add(retval);
            }
            if (stringList.size() != 3) {
                logger.error("ERROR! Wrong date.size, != 3, command (SALESREPORT)\n");
                receiver.resultMessage("ERROR");
            } else {
                try {
                    if (receiver.demandStorage.isEmpty()) {
                        logger.error("ERROR! Demand history is empty, command (SALESREPORT)\n");
                        receiver.resultMessage("ERROR");
                    } else {
                        int day = Integer.parseInt(stringList.get(0));
                        int month = Integer.parseInt(stringList.get(1));
                        int year = Integer.parseInt(stringList.get(2));
                        Operation getDemandOperation
                                = new Operation("DEMAND", product_name, LocalDate.of(year, month, day));
                        int forEach_i = 0;
                        int checkDemandOperationProductCounter = 0;
                        boolean chechKey = false;
                        for (Operation key : receiver.demandStorage.keySet()) {
                            forEach_i = key.date.compareTo(getDemandOperation.date);
                            chechKey = (key.product.equals(getDemandOperation.product));
                            if (forEach_i <= 0 && chechKey) {
                                checkDemandOperationProductCounter++;
                            }
                        }
                        if (checkDemandOperationProductCounter == 0) {
                            logger.error("ERROR! Demand history before " + LocalDate.of(year, month, day) + " is empty, command (SALESREPORT)\n");
                            receiver.resultMessage("ERROR");
                        } else {
                            receiver.report(product_name, LocalDate.of(year, month, day));
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
