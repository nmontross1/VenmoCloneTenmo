package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Balance;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;


public class BalanceTest {


    @Test
    public void getBalance_return_correct_Balance(){
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("100"));
        Assert.assertEquals(new BigDecimal("100"), balance.getBalance());
    }
}
