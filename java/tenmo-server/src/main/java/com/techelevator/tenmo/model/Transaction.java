package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transaction {

    int transfer_id;
    int transfer_status_id;
    int account_from_id;
    int account_to_id;
    BigDecimal amount;

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getAccount_from_id() {
        return account_from_id;
    }

    public void setAccount_from_id(int account_from_id) {
        this.account_from_id = account_from_id;
    }

    public int getAccount_to_id() {
        return account_to_id;
    }

    public void setAccount_to_id(int account_to_id) {
        this.account_to_id = account_to_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
