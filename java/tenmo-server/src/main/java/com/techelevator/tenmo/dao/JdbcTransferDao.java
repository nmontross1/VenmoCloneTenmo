package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferSummary;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    public JdbcTransferDao(DataSource ds , UserDao userDao) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.userDao = userDao;
    }




    @Override
    public Transfer getTransfer(long id) {
        String query = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount From transfers Where transfer_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, id);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        return null;
    }

    @Override
    public List<TransferSummary> getTransactionsByAccountId(int accountId) {
        List<TransferSummary> transferSummaries = new ArrayList<>();
        String query = "SELECT transfer_id, transfer_type_id, account_from, transfer_status_id, account_to, amount FROM transfers JOIN accounts ON transfers.account_from = accounts.account_id JOIN users ON accounts.user_id = users.user_id WHERE account_to = ? OR account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query,accountId,accountId);
        while(results.next()){
            transferSummaries.add(mapRowToTransaction(results, accountId));
        }

        return transferSummaries;
    }



    @Override
    public Transfer createTransfer(Transfer transfer) {
        String query = "insert into transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) values(?,?,?,?,?) RETURNING transfer_id;";

        Long id = jdbcTemplate.queryForObject(query,Long.class, transfer.getTransfer_type_id(),transfer.getTransfer_status_id(),transfer.getAccount_from(),transfer.getAccount_to(),transfer.getAmount());
        return getTransfer(id);
    }

    private TransferSummary mapRowToTransaction(SqlRowSet rs, int accountId){
        TransferSummary transferSummary = new TransferSummary();
        transferSummary.setTransfer_id(rs.getInt("transfer_id"));
        int initiator = rs.getInt("account_from");
        if(initiator == accountId){
            User user  = userDao.findUserByAccountId(rs.getInt("account_to"));
            transferSummary.setDirection("To");
            transferSummary.setUsername(user.getUsername());
            transferSummary.setAmount(new BigDecimal(rs.getString("amount")).setScale(2, RoundingMode.HALF_UP));

        }
        else{
            User user  = userDao.findUserByAccountId(rs.getInt("account_from"));
            transferSummary.setDirection("From");
            transferSummary.setUsername(user.getUsername());
            transferSummary.setAmount(new BigDecimal(rs.getString("amount")).setScale(2, RoundingMode.HALF_UP));
        }
        return transferSummary;

    }

    private Transfer mapRowToUser(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));
        transfer.setAmount(new BigDecimal(rs.getString("amount")).setScale(2, RoundingMode.HALF_UP));
        return transfer;
    }


}
