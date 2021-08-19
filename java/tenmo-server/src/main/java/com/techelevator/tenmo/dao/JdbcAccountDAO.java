package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class JdbcAccountDAO implements AccountDAO {
    private JdbcTemplate jdbcTemplate;


    public JdbcAccountDAO(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }


    @Override
    public Balance getBalance(String user) {
        Balance balance = null;
        String query = "select balance from accounts join users on accounts.user_id = users.user_id where username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query, user);

        if(results.next()){
             balance = new Balance();
             balance.setBalance(new BigDecimal(results.getString("balance")).setScale(2, RoundingMode.HALF_UP));
        }

        return balance;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String query = "SELECT account_id , user_id, balance from accounts where user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query,userId);

        if(results.next()){
            account = new Account();
            account.setAccountId(results.getInt("account_id"));
            account.setUserid(results.getInt("user_id"));
            account.setBalance(new BigDecimal(results.getString("balance")).setScale(2, RoundingMode.HALF_UP));
        }
        return account;
    }


    @Override
    public Balance updateBalance(long user, BigDecimal amount) {
        Balance balance = null;
        String query = "update accounts set balance = ? where user_id = ?";
        jdbcTemplate.update(query, amount, user);
        String query2 =  "select username from users where user_id = ?";
 //       jdbcTemplate.update(query2, user, amount);
        SqlRowSet results = jdbcTemplate.queryForRowSet(query2, user);

        if(results.next()){
            String username = results.getString("username");
            balance = getBalance(username);
        }
        return balance;
    }


}
