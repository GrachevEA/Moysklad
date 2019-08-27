/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.com.grachevea.moysklad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author GrachevEA
 */
public class TestOperationClass {

    Operation operation_1, operation_2, operation_3, operation_4, operation_5;

    @Before
    public void initialize() {
        operation_1 = new Operation("PURCHASE",
                "iphone",
                LocalDate.of(2019, 8, 24));
        operation_2 = new Operation("PURCHASE",
                "iphone",
                LocalDate.of(2019, 8, 24));
        operation_3 = new Operation("PURCHASE",
                "iphone",
                LocalDate.of(2020, 8, 24));
        operation_4 = new Operation("PURCHASE",
                "iphone",
                LocalDate.of(2019, 8, 24));
        operation_5 = operation_1;
    }

    @Test
    public void TestOperationEquals() {
        //Same object
        Assert.assertFalse(operation_1 == operation_2);
        Assert.assertTrue(operation_1 == operation_5);
        //reflexive
        Assert.assertTrue(operation_1.equals(operation_1));
        //symmetric
        Assert.assertFalse(operation_1.equals(operation_3));
        Assert.assertFalse(operation_3.equals(operation_2));
        //transitive
        Assert.assertTrue(operation_1.equals(operation_2));
        Assert.assertTrue(operation_2.equals(operation_4));
        Assert.assertTrue(operation_1.equals(operation_4));
        //consistent
        int i = 10;
        while (i != 0) {
            Assert.assertTrue(operation_1.equals(operation_2));
            i--;
        }
    }

    @Test
    public void TestOperationHash() {
        if (operation_1.equals(operation_2)) {
            Assert.assertEquals(operation_1.hashCode(), operation_2.hashCode());
        }
    }

    @Test
    public void TestOperationCompare()
    {
        Assert.assertEquals(1,(operation_3.compareTo(operation_1)));
    }
}
