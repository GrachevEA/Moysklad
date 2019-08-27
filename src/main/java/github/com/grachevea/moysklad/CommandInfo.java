/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.util.List;

/**
 *
 * @author GrachevEA
 */
public class CommandInfo implements Command {

    private Receiver receiver;

    CommandInfo(Receiver rec) {
        receiver = rec;
    }

    @Override
    public void execute(List<String> strList) {
        if (strList.size() != 1) {
            receiver.resultMessage("Try again! It's simple:INFO");
        } else {
            receiver.resultMessage("Commands:\n"+
                    "\t1)command: NEWPRODUCT <productName>"+
                    "\t2)command: PURCHASE <productName> <amount> <price> <date>"+
                    "\t3)command: DEMAND <productName> <amount> <price> <date>"+
                    "\t4)command: SALESREPORT <productName> <date>"+
                    "\t5)command: INFO"+
                    "Rules: \n\t- NEWPRODUCT before PURCHASE/DEMAND\n\t- PURCHASE before DEMAND\n\t- DEMAND before SALESREPORT\n\t- Amount > 0 AND Price > 0\n\t- Date <date> in format DD.MM.YYYY");
            receiver.info();
        }
    }
}
