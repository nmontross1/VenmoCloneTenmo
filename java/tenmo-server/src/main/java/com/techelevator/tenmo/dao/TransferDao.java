package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferSummary;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(long id);

    List<TransferSummary> getTransactionsByAccountId(int accountId);

    Transfer createTransfer(Transfer transfer);

}
