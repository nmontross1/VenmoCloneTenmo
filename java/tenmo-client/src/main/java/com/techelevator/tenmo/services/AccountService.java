package com.techelevator.tenmo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.exception.ExceptionMapper;
import com.techelevator.tenmo.model.*;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AccountService {

    private static String AUTH_TOKEN = "";
    private RestTemplate restTemplate;
    private String baseUrl;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();

    }

    public Balance getUserBalance(){
        Balance balance = null;
        try {
            balance = restTemplate.exchange(baseUrl + "balance", HttpMethod.GET, makeAuthEntity(), Balance.class).getBody();
            return balance;
        }  catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return balance;
    }

    public User[] getAllAvailableUsers(){
        User[] allUsers = restTemplate.exchange(baseUrl+"users",HttpMethod.GET,makeAuthEntity(), User[].class).getBody();
        return allUsers;

    }

    public User getUserDetails(int userId){
        User user = null;
        try {
            user = restTemplate.exchange(baseUrl+"users/"+userId,HttpMethod.GET,makeAuthEntity(), User.class).getBody();
        } catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return user;
    }

    public Account getAccountById(int accountId){
        Account account = null;
        try {
            account = restTemplate.exchange(baseUrl+"accounts/"+accountId,HttpMethod.GET,makeAuthEntity(), Account.class).getBody();
        }  catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return account;
    }


    public Account getAccountInfo(int userid){
        Account account = null;
        try {
            account = restTemplate.exchange(baseUrl+"account?userid="+userid,HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
        }  catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return account;
    }

    public Transfer sendTransfer(Transfer transfer){
        Transfer transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl+"transfers",HttpMethod.POST, makeTransferEntity(transfer), Transfer.class).getBody();
        } catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return transfers;
    }
    public Transfer updateTransfer(Transfer transfer){
        Transfer transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl+"transfers",HttpMethod.PUT, makeTransferEntity(transfer), Transfer.class).getBody();
        }  catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return transfers;
    }
    public Transfer[] getTransferSummary(int accountId){
        Transfer[] transferSummaries = restTemplate.exchange(baseUrl+"account/"+accountId+"/transfers",HttpMethod.GET,makeAuthEntity(), Transfer[].class).getBody();
        return transferSummaries;
    }
    public Transfer[] getPendingTransfers(int accountId){
        Transfer[] transferSummaries = restTemplate.exchange(baseUrl+"account/"+accountId+"/transfers?isPending=true",HttpMethod.GET,makeAuthEntity(), Transfer[].class).getBody();
        return transferSummaries;
    }
    public Transfer getTransferInfo(int transferId){
        Transfer transfer = null;
        try{
            transfer = restTemplate.exchange(baseUrl+"transfers/"+transferId,HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException ex){
            printExceptionMessage(ex);
        } catch (ResourceAccessException ex){
            System.out.println(ex.getMessage());
        }
        return transfer;
    }

    public static void setAuthToken(String token){
        AUTH_TOKEN = token;
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


    private void printExceptionMessage(RestClientResponseException ex){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ExceptionMapper[] exceptionMappers = objectMapper.readValue(ex.getMessage().split(" : ")[1],ExceptionMapper[].class);
            System.out.println(exceptionMappers[0].getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(ex.getMessage());

        }
    }

}
