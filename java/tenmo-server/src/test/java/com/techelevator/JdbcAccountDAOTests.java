package com.techelevator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDAOTests extends TenmoDaoTest {

    private JdbcAccountDAO sut;
    private  final int TEST1 = 1001;
    @Before
    public void setUp(){
        sut = new JdbcAccountDAO(dataSource);
    }



    @Test
    public void findAll_returns_the_correct_amount_of_accounts(){
        List<Account> accounts = sut.findAll();
        int expected =  4;
        Assert.assertEquals(expected, accounts.size());
    }

    @Test
    public void findAccountByUserId_returns_account_when_userId_is_valid() throws AccountNotFoundException {
        Account expected = new Account();
        expected.setAccountId(2001);
        expected.setUserid(1001);
        expected.setBalance(new BigDecimal("1000"));


        Account actual = sut.getAccountByUserId(TEST1);
        Assert.assertEquals(expected.getAccountId(),actual.getAccountId() );
        Assert.assertEquals(expected.getUserid(),actual.getUserid() );
        Assert.assertTrue(expected.getBalance().compareTo(actual.getBalance()) == 0 );

    }

    @Test
    public void findAccountByUserId_returns_null_when_userid_is_invalid() throws AccountNotFoundException {
        boolean isExceptionThrown = false;
        try {
            Account actual = sut.getAccountByUserId(999999999);

        } catch (AccountNotFoundException e) {
            isExceptionThrown = true;
        }

        Assert.assertTrue(isExceptionThrown);
    }

    @Test
    public void updateBalance_updates_balance_correctly_when_userid_is_valid() throws UserNotFoundException {
        Balance expected = new Balance();
        expected.setBalance(new BigDecimal("600"));

        Balance actual = sut.updateBalance(TEST1,new BigDecimal("600"));
        Assert.assertEquals(actual.getBalance().compareTo(expected.getBalance()),0);
    }


    @Test()
    public void updateBalance_updates_return_null_userid_is_invalid() {
       Balance balance = null;
        try {
            sut.updateBalance(9999,new BigDecimal("600"));
        } catch (UserNotFoundException e) {

        }
        Assert.assertNull(balance);
    }

    @Test()
    public void getBalance_returns_correct_balance_for_user() throws UserNotFoundException {
        Balance expected = new Balance();
        expected.setBalance(new BigDecimal(1000));

        Balance actual = sut.getBalance("test1");
        Assert.assertTrue(actual.getBalance().compareTo(expected.getBalance()) == 0);
    }

    @Test
    public void getBalance_throws_exception_when_username_is_invalid(){
        boolean isExceptionThrown = false;
        try {
            sut.getBalance("autoauto");

        } catch (UserNotFoundException e) {
            isExceptionThrown = true;
        }

        Assert.assertTrue(isExceptionThrown);

    }
}
