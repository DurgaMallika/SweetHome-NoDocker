package com.upgrad.Payment.services;

import com.upgrad.Payment.dao.PaymentDAO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentDAO paymentDAO;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public TransactionDetailsEntity acceptTransactionDetails(TransactionDetailsEntity transactionDetailsEntity) {
        return paymentDAO.save(transactionDetailsEntity);
    }

    @Override
    public TransactionDetailsEntity getTransactionDetails(int id) {
        return paymentDAO.findById(id).get();
    }

}
