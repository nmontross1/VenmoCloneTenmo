package com.techelevator;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferTest {
    @Test
    public void get_transfer_id_returns_correct_id(){
        Transfer transfer = new Transfer();
        transfer.setTransferId(3000);
        Assert.assertEquals(3000, transfer.getTransferId());
    }

    @Test
    public void get_transfer_type_id_returns_correct_Id(){
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        Assert.assertEquals(2, transfer.getTransferTypeId());
    }

    @Test
    public void get_transfer_status_id_returns_correct_Id(){
        Transfer transfer = new Transfer();
        transfer.setTransferStatusId(2);
        Assert.assertEquals(2, transfer.getTransferStatusId());
    }


    @Test
    public void get_account_from_returns_correct_Account_from(){
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(1000);
        Assert.assertEquals(1000, transfer.getAccountFrom());

    }

    @Test
    public void get_account_to_returns_correct_account_to(){
        Transfer transfer = new Transfer();
        transfer.setAccountTo(1000);
        Assert.assertEquals(1000, transfer.getAccountTo());
    }

    @Test
    public void get_amount_returns_correct_amount(){
        Transfer transfer = new Transfer();
        transfer.setAmount(new BigDecimal("1000.00"));
        Assert.assertTrue(new BigDecimal("1000.00").compareTo(transfer.getAmount()) == 0);
    }

    @Test
    public void get_from_username_returns_correct_username(){
        Transfer transfer = new Transfer();
        transfer.setFromUserName("testuser");
        Assert.assertEquals("testuser", transfer.getFromUserName());
    }

    @Test
    public void get_to_username_returns_correct_username(){
        Transfer transfer = new Transfer();
        transfer.setToUserName("testuser");
        Assert.assertEquals("testuser", transfer.getToUserName());
    }
}
