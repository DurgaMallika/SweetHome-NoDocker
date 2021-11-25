package com.upgrad.Payment.controller;

import com.upgrad.Payment.dto.PaymentDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import com.upgrad.Payment.services.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping(value="/transaction")
public class PaymentController {

    private PaymentService paymentService;

    private ModelMapper modelMapper;

    @Autowired
    public PaymentController(PaymentService paymentService, ModelMapper modelMapper) {
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> performTransaction(@RequestBody PaymentDTO paymentDTO){
        TransactionDetailsEntity transactionDetails = modelMapper.map(paymentDTO, TransactionDetailsEntity.class);
        TransactionDetailsEntity savedTransactionDetails = paymentService.acceptTransactionDetails(transactionDetails);

        return new ResponseEntity(savedTransactionDetails.getTransactionId(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{transactionId}",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDetailsEntity> getTransactionDetails(@PathVariable(name="transactionId") int transactionId){
        TransactionDetailsEntity retrievedTransactionDetails = paymentService.getTransactionDetails(transactionId);

        PaymentDTO transactionDetailsDTO = modelMapper.map(retrievedTransactionDetails, PaymentDTO.class);

        return new ResponseEntity(transactionDetailsDTO, HttpStatus.OK);

    }


}
