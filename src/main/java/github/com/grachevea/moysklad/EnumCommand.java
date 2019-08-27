/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

/**
 *
 * @author GrachevEA
 */
public enum EnumCommand {
    PURCHASE("PURCHASE"),
    DEMAND("DEMAND"),
    SALESREPORT("SALESREPORT"),
    NEWPRODUCT("NEWPRODUCT"),
    INFO("INFO");
    
    private String commandName;
    
    EnumCommand(String theCommandName){
        commandName = theCommandName;
    }
}
