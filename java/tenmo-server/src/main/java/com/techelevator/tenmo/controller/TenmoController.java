package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal){

        return accountDAOao.getBalance(principal.getName());
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

    @RequestMapping(path = "/sendmoney", method = RequestMethod.POST)
    public Transfer sendMoney(@RequestBody Transfer transfer){
      //  return userDao.findIdByUsername(username);
        int fromUserId = userDao.findIdByUsername(transfer.getAccount_from());
        int toUserId = userDao.findIdByUsername(transfer.getAccount_to());
        if(toUserId != -1){
            Balance balance = accountDAOao.getBalance(transfer.getAccount_from());
            if(balance.getBalance().compareTo(transfer.getAmount()) >= 0){
                //Create update balance method
                BigDecimal diff = balance.getBalance().subtract(transfer.getAmount());
                accountDAOao.updateBalance(fromUserId, diff);
                BigDecimal sum = accountDAOao.getBalance(transfer.getAccount_to()).getBalance().add(transfer.getAmount());
                accountDAOao.updateBalance(toUserId, sum);
            }

        }
        return null;
    }


}
