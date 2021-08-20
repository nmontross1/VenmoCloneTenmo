package com.techelevator;

import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Test;


import java.math.BigDecimal;

public class AccountTest {

    @Test
    public void get_Account_Id_returns_correct_Account_Id(){
        Account account = new Account();
        account.setAccountId(1001);
        Assert.assertEquals(1001,account.getAccountId());
    }


    @Test
    public void get_Account_UserId_returns_correct_userId(){
        Account account = new Account();
        account.setUserid(2001);
        Assert.assertEquals(2001,account.getUserid());
    }


    @Test
    public void get_Account_Balance_returns_correct_Balance(){
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.50"));
        Assert.assertEquals(new BigDecimal("1000.50"),account.getBalance());
    }
}
