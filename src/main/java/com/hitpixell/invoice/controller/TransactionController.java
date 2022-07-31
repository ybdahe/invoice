package com.hitpixell.invoice.controller;

import com.hitpixell.invoice.model.Transaction;
import com.hitpixell.invoice.model.TransactionDTO;
import com.hitpixell.invoice.repository.TransactionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Manage Transaction", tags = {"transactions"})
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    @ApiOperation(value = "API to get all transactions ", response = Transaction.class)
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam(required = false) String clientName, @RequestParam(required = false) String orderId) {
        try {
            List<Transaction> transactions = new ArrayList<>();

            if (clientName != null) {
                transactions.addAll(transactionRepository.findByClientName(clientName));
            } else if (orderId != null) {
                transactions.addAll(transactionRepository.findByOrderId(orderId));
            } else {
                transactions.addAll(transactionRepository.findAll());
            }

            if (transactions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/{id}")
    @ApiOperation(value = "API to get all transaction by id", response = Transaction.class)
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/transactions")
    @ApiOperation(value = "API to add transaction ", response = Transaction.class)
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionDTO transaction) {
        try {
            Transaction transactionObj = new Transaction();
            getTransactionObj(transaction, transactionObj);
            transactionObj.setCreatedAt(LocalDateTime.now());
            Transaction transactionData = transactionRepository
                    .save(transactionObj);
            return new ResponseEntity<>(transactionData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void getTransactionObj(TransactionDTO transaction, Transaction transactionObj) {
        transactionObj.setOrderId(transaction.getOrderId());
        transactionObj.setTransactionDate(transaction.getTransactionDate());
        transactionObj.setOrderName(transaction.getOrderName());
        transactionObj.setAmount(transaction.getAmount());
        transactionObj.setCurrency(transaction.getCurrency());
        transactionObj.setCardType(transaction.getCardType());
        transactionObj.setStatus(transaction.getStatus());
        transactionObj.setClientName(transaction.getClientName());
    }

    @PutMapping("/transactions/{id}")
    @ApiOperation(value = "API to update transaction ", response = Transaction.class)
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") long id, @RequestBody TransactionDTO transaction) {
        Optional<Transaction> transactionData = transactionRepository.findById(id);

        if (transactionData.isPresent()) {
            Transaction transactionObj = transactionData.get();
            getTransactionObj(transaction, transactionObj);
            transactionObj.setModifiedAt(LocalDateTime.now());
            return new ResponseEntity<>(transactionRepository.save(transactionObj), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/transactions/{id}")
    @ApiOperation(value = "API to delete transaction ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable("id") long id) {
        try {
            transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transactions/{clientName}")
    @ApiOperation(value = "API to delete transactions by clientName ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable("clientName") String clientName) {
        try {
            transactionRepository.deleteByClientName(clientName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transactions")
    @ApiOperation(value = "API to delete all transactions ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        try {
            transactionRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
