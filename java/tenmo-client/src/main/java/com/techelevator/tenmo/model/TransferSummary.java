package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferSummary {

    int transfer_id;
    String direction;
    String username;
    BigDecimal amount;

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
