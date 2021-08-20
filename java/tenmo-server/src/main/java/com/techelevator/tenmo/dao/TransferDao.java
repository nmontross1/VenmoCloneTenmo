package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(long id) throws TransferNotFoundException;

    List<Transfer> getTransfers(int accountId);

    Transfer createTransfer(Transfer transfer) throws TransferNotFoundException;

}
