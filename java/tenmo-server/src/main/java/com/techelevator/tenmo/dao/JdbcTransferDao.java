package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
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
    public Transfer getTransfer(long id) throws TransferNotFoundException {
        String query = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount From transfers Where transfer_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, id);
        if (rowSet.next()){
            return mapRowToTransfer(rowSet);
        } else {
            throw new TransferNotFoundException();
        }
    }

    @Override
    public List<Transfer> getTransfers(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String query = "SELECT transfer_id, transfer_type_id, account_from, transfer_status_id, account_to, amount FROM transfers JOIN accounts ON transfers.account_from = accounts.account_id JOIN users ON accounts.user_id = users.user_id WHERE account_to = ? OR account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query,accountId,accountId);
        while(results.next()){
            transfers.add(mapRowToTransfer(results));
        }

        return transfers;
    }



    @Override
    public Transfer createTransfer(Transfer transfer) throws TransferNotFoundException {
        String query = "insert into transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) values(?,?,?,?,?) RETURNING transfer_id;";

        Long id = jdbcTemplate.queryForObject(query,Long.class, transfer.getTransferTypeId(),transfer.getTransferStatusId(),transfer.getAccountFrom(),transfer.getAccountTo(),transfer.getAmount());
        return getTransfer(id);
    }

    @Override
    public Transfer updateTransfer(Transfer transfer) throws TransferNotFoundException {
        String query = "Update transfers set transfer_status_id = ? where transfer_id = ? RETURNING transfer_id;";
        Long id = jdbcTemplate.queryForObject(query,Long.class, transfer.getTransferStatusId(),transfer.getTransferId());
        return getTransfer(id);
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        int initiator = rs.getInt("account_from");
        User fromUser =  userDao.findUserByAccountId(initiator);
        int receiver = rs.getInt("account_to");
        User toUser = userDao.findUserByAccountId(receiver);
        transfer.setAccountFrom(initiator);
        transfer.setFromUserName(fromUser.getUsername());
        transfer.setAccountTo(receiver);
        transfer.setToUserName(toUser.getUsername());
        transfer.setAmount(new BigDecimal(rs.getString("amount")).setScale(2, RoundingMode.HALF_UP));
        return transfer;
    }


}
