package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(long id);

    List<Transfer> getTransfersByAccountId(int accountId);

    Transfer createTransfer(Transfer transfer);

}
