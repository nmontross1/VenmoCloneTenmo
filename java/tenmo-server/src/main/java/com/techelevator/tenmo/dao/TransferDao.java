package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(long id);

    List<Transaction> getTransactionsByAccountId(int accountId);

    Transfer createTransfer(Transfer transfer);

}
