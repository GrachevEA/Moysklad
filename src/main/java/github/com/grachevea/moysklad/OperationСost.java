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
public class OperationСost {

    int operationCost;
    int amount;
    int price;

    OperationСost(int amount, int price) {
        this.amount = amount;
        this.price = price;
        this.operationCost = amount * price;
    }

    @Override
    public String toString() {
        return "OperationCost: amount = " + amount + ", price =" + price + ", Cost: " + operationCost;
    }

    @Override
    public int hashCode() {
        final int X = 37;
        int hash = 17;
        hash = X*hash + amount;
        hash = X*hash + price;
        hash = X*hash + operationCost;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj.getClass() != this.getClass()) || obj == null) {
            return false;
        }
        OperationСost passed_operationCost = (OperationСost) obj;
        if (this.amount == passed_operationCost.amount && 
                this.price == passed_operationCost.price && 
                this.operationCost == passed_operationCost.operationCost) 
        {
            return true;
        } else {
            return false;
        }
    }
}
