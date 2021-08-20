package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    @Autowired
    AccountDAO accountDAOao;
    @Autowired
    UserDao userDao;

    @Autowired
    TransferDao transferDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal) throws UserNotFoundException{

        return accountDAOao.getBalance(principal.getName());
    }

    @RequestMapping(path="/account")
    public Account getAccount(@RequestParam int userid) throws UserNotFoundException {
        return accountDAOao.getAccountByUserId(userid);
    }

    @RequestMapping(path="/accounts/{accountId}")
    public Account getAccountById(@PathVariable int accountId){
        Account returnValue  = null;
        for (Account account : accountDAOao.findAll()){
            if(account.getAccountId() == accountId){
                returnValue = account;
                break;
            }
        }

        return returnValue;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUser( Principal principal){
        List<User> filteredList = new ArrayList<>();
        String name = principal.getName().toLowerCase();
        for (User user : userDao.findAll()){
            if(!user.getUsername().toLowerCase().equals(name)){
                filteredList.add(user);
            }
        }


        return filteredList;
    }

    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public User getAllUser( @PathVariable int userId){
        User returnValue  = null;
        for (User user : userDao.findAll()){
            if(user.getId() == userId){
                returnValue = user;
                break;
            }
        }

        return returnValue;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer sendMoney(@RequestBody Transfer transfer) throws InsufficientBalanceException, UserNotFoundException, TransferNotFoundException {

        User fromUser = userDao.findUserByAccountId(transfer.getAccountFrom());
        User toUser = userDao.findUserByAccountId(transfer.getAccountTo());
        Transfer returnValue = null;
            Balance balance = accountDAOao.getBalance(fromUser.getUsername());
            if(balance.getBalance().compareTo(transfer.getAmount()) >= 0){
                BigDecimal diff = balance.getBalance().subtract(transfer.getAmount());
                accountDAOao.updateBalance(fromUser.getId(), diff);
                BigDecimal sum = accountDAOao.getBalance(toUser.getUsername()).getBalance().add(transfer.getAmount());
                accountDAOao.updateBalance(toUser.getId(), sum);
                returnValue =  transferDao.createTransfer(transfer);

            }


        return returnValue;
    }

    @RequestMapping(path = "/account/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> transferList(@PathVariable int accountId){
        return transferDao.getTransfers(accountId);

    }

    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int transferId , Principal principal) throws TransferNotFoundException, UserNotFoundException {
        Transfer transfer = transferDao.getTransfer(transferId);
        //Check to see if the transfer details returning belongs to the user requesting
        int accountId = accountDAOao.getAccountByUserId(userDao.findIdByUsername(principal.getName())).getAccountId();
        if(transfer.getAccountFrom() == accountId || transfer.getAccountTo() == accountId){
            return transfer;
        }else
            throw new TransferNotFoundException();
    }



}
