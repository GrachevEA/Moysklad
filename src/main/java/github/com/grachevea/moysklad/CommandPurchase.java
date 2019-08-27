/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.time.DateTimeException;

/**
 *
 * @author GrachevEA
 */
public class CommandPurchase implements Command {

    private Receiver receiver;
    final static Logger logger = Logger.getLogger(CommandPurchase.class);

    CommandPurchase(Receiver rec) {
        receiver = rec;
    }

    @Override
    public void execute(List<String> strList) {
        if (strList.size() != 5) {
            logger.error("ERROR! Wrong command(PURCHASE) size, != 5\n");
            receiver.resultMessage("ERROR");
        } else {
            boolean check = receiver.commandsetNP.contains(strList.get(1));
            if (check != true) {
                logger.error("ERROR! Need to add this product before purchase\n");
                receiver.resultMessage("ERROR");
            } else {
                try {
                    List<String> stringList = new ArrayList<String>(3);
                    String product_name = strList.get(1);
                    String strDate = strList.get(4);
                    String delimeter = "\\.";
                    for (String retval : strDate.split(delimeter)) {
                        stringList.add(retval);
                    }
                    if (stringList.size() != 3) {
                        logger.error("ERROR! Wrong date.size, != 3, command (PURCHASE)\n");
                        receiver.resultMessage("ERROR");
                    } else {
                        int amount = Integer.parseInt(strList.get(2));
                        int price = Integer.parseInt(strList.get(3));
                        if (price < 0 || amount < 0) {
                            logger.error("ERROR! price or amount < 0, command(PURCHASE)\n");
                            receiver.resultMessage("ERROR");
                        } else {
                            int day = Integer.parseInt(stringList.get(0));
                            int month = Integer.parseInt(stringList.get(1));
                            int year = Integer.parseInt(stringList.get(2));
                            receiver.purchase(product_name, amount, price, LocalDate.of(year, month, day));
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
