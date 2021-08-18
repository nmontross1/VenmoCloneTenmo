package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }




    @Override
    public Transfer getTransfer(long id) {
        return null;
    }

    @Override
    public List<Transfer> getTranfersByUserId() {
        return null;
    }


    //#TODO FINSIH IMPLEMENTATION DID NOT TEST METHOD YET.
    @Override
    public Transfer createTransfer(Transfer transfer) {
        String query = "insert into transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "values(?,?,?,?,?) RETURNING transfer_id";

        long id = jdbcTemplate.update(query,Long.class, transfer.getTransfer_type_id(),transfer.getTransfer_status_id(),transfer.getAccount_from(),transfer.getAccount_to(),transfer.getAmount());
        return getTransfer(id);
    }
}
