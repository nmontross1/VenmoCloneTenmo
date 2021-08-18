package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "sendMoney", method = RequestMethod.POST)
    public  Balance sentMondy(@RequestBody String username){
      //  return userDao.findIdByUsername(username);
        return null;
    }


}
