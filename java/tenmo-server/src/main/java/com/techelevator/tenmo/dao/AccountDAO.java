package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {


    List<Account> findAll();

    Balance getBalance(String user) throws UserNotFoundException;

    Account getAccountByUserId(int userId) throws AccountNotFoundException;

    Balance updateBalance(long userId, BigDecimal amount) throws InsufficientBalanceException, UserNotFoundException;


}
