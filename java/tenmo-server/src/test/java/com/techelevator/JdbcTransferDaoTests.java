package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends TenmoDaoTest{

    private static JdbcTransferDao sut;
    @Before
    public void setUp(){
        sut = new JdbcTransferDao(dataSource, new JdbcUserDao(new JdbcTemplate(dataSource)));
    }

    @Test
    public void getTransfer_returns_correct_transfer_using_valid_transferId() throws TransferNotFoundException {
        Transfer expected = new Transfer();
        expected.setTransferId(3001);
        expected.setTransferTypeId(2);
        expected.setTransferStatusId(2);
        expected.setAccountFrom(2001);
        expected.setAccountTo(2003);
        expected.setFromUserName("test1");
        expected.setToUserName("test3");
        expected.setAmount(new BigDecimal(100));
        Transfer actual = sut.getTransfer(3001);
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getFromUserName(), actual.getFromUserName());
        Assert.assertEquals(expected.getToUserName(), actual.getToUserName());
        Assert.assertEquals(expected.getAmount().compareTo(actual.getAmount()),0);
    }

    @Test
    public void getTransfers_returns_correct_amount_of_transfers(){
        Assert.assertEquals(3, sut.getTransfers(2002).size());
    }
    @Test
    public void getTransfers_returns_correct_amount_of_transfers2(){
        Assert.assertEquals(0, sut.getTransfers(2004).size());
    }
    @Test
    public void createTransfer_returns_new_transfer() throws TransferNotFoundException {
        Transfer expected = new Transfer();
        expected.setTransferTypeId(2);
        expected.setTransferStatusId(2);
        expected.setAccountFrom(2001);
        expected.setAccountTo(2003);
        expected.setFromUserName("test1");
        expected.setToUserName("test3");
        expected.setAmount(new BigDecimal(100));
        Transfer actual = sut.createTransfer(expected);
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getFromUserName(), actual.getFromUserName());
        Assert.assertEquals(expected.getToUserName(), actual.getToUserName());
        Assert.assertEquals(expected.getAmount().compareTo(actual.getAmount()),0);
    }

    @Test
    public void createTransfer_throws_exception_when_accountIds_are_invalid() throws TransferNotFoundException {
        boolean isExceptionThrown = false;

        Transfer expected = new Transfer();
        expected.setTransferTypeId(2);
        expected.setTransferStatusId(2);
        expected.setAccountFrom(9999);
        expected.setAccountTo(2003);
        expected.setFromUserName("test1");
        expected.setToUserName("test3");
        expected.setAmount(new BigDecimal(100));

        try {
            Transfer actual = sut.createTransfer(expected);
        }catch (DataIntegrityViolationException exception){
            isExceptionThrown = true;

        }
        Assert.assertTrue(isExceptionThrown);

    }


    @Test
    public void getTransfer_throws_transferNotFound_exception_when_transferId_is_invalid(){
        boolean isExceptionThrown = false;

        try{
            sut.getTransfer(99999999);
        } catch (TransferNotFoundException e) {
            isExceptionThrown = true;
        }

        Assert.assertTrue(isExceptionThrown);
    }
}
