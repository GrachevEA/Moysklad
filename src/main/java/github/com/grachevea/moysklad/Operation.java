/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;
import java.time.LocalDate;
/**
 *
 * @author GrachevEA
 */
public class Operation implements Comparable<Operation> {

    String purOrDem;
    String product;
    LocalDate date;

    Operation(String passed_purOrDem, String passed_product, LocalDate passed_date) {
        purOrDem = passed_purOrDem;
        product = passed_product;
        date = passed_date;
    }
    Operation(Operation x) {
        this.purOrDem = x.purOrDem;
        this.product = x.product;
        this.date = x.date;
    }
    @Override
    public int hashCode()
    {
        final int X = 37;
        int hash = 17;
        hash = X*hash + product.hashCode();
        hash = X*hash + purOrDem.hashCode();
        hash = X*hash + date.hashCode();
        return hash;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if ((obj.getClass() != this.getClass()) || obj == null)
            return false;
        Operation passed_operation = (Operation) obj;
        if((purOrDem!=null && purOrDem.equals(passed_operation.purOrDem))
                &&
                (product!=null && product.equals(passed_operation.product))
                &&
                (date!=null && date.equals(passed_operation.date)))    
        return true;
        else 
        return false;      
    }
    
    @Override
    public String toString() {
        return "\nOperation: " + purOrDem + ", product - " + product + ", " + date;
    }

    @Override
    public int compareTo(Operation x) {
        if (this.date.compareTo(x.date) == 0) {
            return 0;
        } else if (this.date.compareTo(x.date) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
