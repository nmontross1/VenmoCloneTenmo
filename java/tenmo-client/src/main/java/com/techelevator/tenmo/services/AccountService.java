package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    public static String AUTH_TOKEN = "";
    private RestTemplate restTemplate;
    private String baseUrl;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public Balance getUserBalance(){
        Balance balance = restTemplate.exchange(baseUrl+"balance", HttpMethod.GET,makeAuthEntity(), Balance.class).getBody();
        return balance;

    }

    public User[] getAllAvailableUsers(){
        User[] allUsers = restTemplate.exchange(baseUrl+"users",HttpMethod.GET,makeAuthEntity(), User[].class).getBody();
        return allUsers;

    }

    public ResponseEntity<Transfer> sendAmount(Transfer transfer){
       return restTemplate.exchange(baseUrl+"sendmoney",HttpMethod.POST, makeTransferEntity(transfer), Transfer.class);
    }

    private HttpEntity makeAuthEntity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity(httpHeaders);

        return entity;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity(transfer, httpHeaders);
        return  entity;
    }



}
