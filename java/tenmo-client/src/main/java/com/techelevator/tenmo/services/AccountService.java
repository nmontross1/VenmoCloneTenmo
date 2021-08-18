package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AccountService {

    private RestTemplate restTemplate;
    private String baseUrl;

    public AccountService(String BASEURL) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public Balance getUserBalance(String token){
        Balance balance = restTemplate.exchange(baseUrl+"balance", HttpMethod.GET,makeAuthEntity(token), Balance.class).getBody();
        return balance;

    }

    public User[] getAllAvailableUsers(String token){
        User[] allUsers = restTemplate.exchange(baseUrl+"users",HttpMethod.GET,makeAuthEntity(token), User[].class).getBody();
        return allUsers;

    }

    private HttpEntity makeAuthEntity(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(httpHeaders);

        return entity;
    }
}
