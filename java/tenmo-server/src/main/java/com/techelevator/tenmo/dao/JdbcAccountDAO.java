package com.techelevator.tenmo.dao;

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
}
