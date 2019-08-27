/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author GrachevEA
 */
public class CommandAddNewProduct implements Command {

    private Receiver receiver;
    final static Logger logger = Logger.getLogger(CommandAddNewProduct.class);

    CommandAddNewProduct(Receiver rec) {
        receiver = rec;
        this.toString();
    }

    @Override
    public void execute(List<String> strList) {
        if (strList.size() != 2) {
            logger.error("ERROR! Wrong command(NEWPRODUCT) size, != 2\n");
            System.out.println("ERROR");
        } else {
            boolean check = receiver.commandsetNP.contains(strList.get(1));
            if (check == true) {
                logger.error("ERROR! Product already exists\n");
                System.out.println("ERROR");
            } else {
                String product_name = strList.get(1);
                receiver.add(product_name);
            }
        }

    }
}
