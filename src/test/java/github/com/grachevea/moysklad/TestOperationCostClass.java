/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author GrachevEA
 */
public class TestOperationCostClass {
    @Test
    public void TestOperationCost()
    {
        OperationСost x = new OperationСost(1,1000);
        OperationСost y = new OperationСost(1,1000);
        Assert.assertTrue(x.equals(y));
        Assert.assertTrue(x.hashCode()==y.hashCode());
    }
}
