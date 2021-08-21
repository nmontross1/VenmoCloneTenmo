package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.constants.TransferStatus;
import com.techelevator.tenmo.constants.TransferType;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    @Autowired
    AccountDAO accountDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TransferDao transferDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal) throws UserNotFoundException{

        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path="/account")
    public Account getAccount(@RequestParam int userid) throws AccountNotFoundException {
        return accountDao.getAccountByUserId(userid);
    }

    @RequestMapping(path="/accounts/{accountId}")
    public Account getAccountById(@PathVariable int accountId){
        Account returnValue  = null;
        for (Account account : accountDao.findAll()){
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
    public User getUser( @PathVariable int userId){
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
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer) throws InsufficientBalanceException, UserNotFoundException, TransferNotFoundException {
        Transfer returnValue = null;
        User fromUser = userDao.findUserByAccountId(transfer.getAccountFrom());
        User toUser = userDao.findUserByAccountId(transfer.getAccountTo());
        //CHECK IF USERS IN ACCOUNT IDS ARE INVALID
        if(fromUser == null || toUser == null){
            throw new UserNotFoundException();
        }
        //IF TRANSFER TYPE IS SEND
        if(transfer.getTransferTypeId() == TransferType.SEND) {
            Balance currentUserBalance = accountDao.getBalance(fromUser.getUsername());
            //CHECK IF USER HAS ENOUGH MONEY ACCOUNT TO SEND
            if (checkSufficientBalance(currentUserBalance,transfer.getAmount())) {
                //UPDATE BALANCES AND CREATE NEW TRANSFER RECORD IN TRANSFER TABLE
                accountDao.updateBalance(fromUser.getId(), currentUserBalance.getBalance().subtract(transfer.getAmount()));
                accountDao.updateBalance(toUser.getId(), accountDao.getBalance(toUser.getUsername()).getBalance().add(transfer.getAmount()));
                returnValue = transferDao.createTransfer(transfer);

            } else {
                throw new InsufficientBalanceException();
            }
        }
        //ELSE IF TRANSFER TYPE IS REQUEST - ONLY PENDING TRANSFERS ARE ACCEPTED IN POST
        else if(transfer.getTransferTypeId() == TransferType.REQUEST) {
            //IS REQUEST TRANSFER PENDING
            if(transfer.getTransferStatusId() == TransferStatus.PENDING) {
                returnValue = transferDao.createTransfer(transfer);
            }
        }
        return returnValue;
    }


    @RequestMapping(path = "/transfers", method = RequestMethod.PUT)
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer) throws InsufficientBalanceException, UserNotFoundException, TransferNotFoundException {
        Transfer returnValue = null;
        User fromUser = userDao.findUserByAccountId(transfer.getAccountFrom());
        User toUser = userDao.findUserByAccountId(transfer.getAccountTo());
        //CHECK IF USERS IN ACCOUNT IDS ARE INVALID
        if(fromUser == null || toUser == null){
            throw new UserNotFoundException();
        }
        //IF TRANSFER TYPE IS REQUEST - ONLY APPROVED AND REJECTED ARE ACCEPTED
        if(transfer.getTransferTypeId() == TransferType.REQUEST) {
            //IS REQUEST TRANSFER APPROVED
             if(transfer.getTransferStatusId()==TransferStatus.APPROVED){
                Balance currentUserBalance = accountDao.getBalance(toUser.getUsername());
                    //CHECK IF USER HAS ENOUGH MONEY ACCOUNT TO SEND
                if (checkSufficientBalance(currentUserBalance,transfer.getAmount())) {
                    accountDao.updateBalance(toUser.getId(), currentUserBalance.getBalance().subtract(transfer.getAmount()));
                    accountDao.updateBalance(fromUser.getId(), accountDao.getBalance(fromUser.getUsername()).getBalance().add(transfer.getAmount()));
                    returnValue = transferDao.updateTransfer(transfer);

                } else {
                    throw new InsufficientBalanceException();
                }
            }
            //IS REQUEST TRANSFER APPROVED
            else if (transfer.getTransferStatusId()==TransferStatus.REJECTED){ // it was rejected
                returnValue = transferDao.updateTransfer(transfer);
            }
        }
        return returnValue;
    }

    @RequestMapping(path = "/account/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> transferList(@PathVariable int accountId, @RequestParam(defaultValue = "false") boolean isPending){
        if(isPending){
            List<Transfer> filterList = new ArrayList<>();
            for(Transfer transfer : transferDao.getTransfers(accountId)){
                if(transfer.getTransferStatusId() == 1 && transfer.getAccountFrom() != accountId){
                    filterList.add(transfer);
                }
            }
            return filterList;
        }

        return transferDao.getTransfers(accountId);

    }

    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int transferId , Principal principal) throws TransferNotFoundException, AccountNotFoundException {
        Transfer transfer = transferDao.getTransfer(transferId);
        //Check to see if the transfer details returning belongs to the user requesting
        int accountId = accountDao.getAccountByUserId(userDao.findIdByUsername(principal.getName())).getAccountId();
        if(transfer.getAccountFrom() == accountId || transfer.getAccountTo() == accountId){
            return transfer;
        }else
            throw new TransferNotFoundException();
    }


    private boolean checkSufficientBalance(Balance balance, BigDecimal amountToTransfer ){
        if(balance.getBalance().compareTo(amountToTransfer) >= 0){
            return true;
        }
        return false;
    }

}
