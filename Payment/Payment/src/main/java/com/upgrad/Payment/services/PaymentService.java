package com.upgrad.Payment.services;

import com.upgrad.Payment.dao.PaymentDAO;
import com.upgrad.Payment.dto.PaymentDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;

public interface PaymentService {

    public TransactionDetailsEntity acceptTransactionDetails(TransactionDetailsEntity transactionDetailsEntity);

    public TransactionDetailsEntity getTransactionDetails(int id);
}
