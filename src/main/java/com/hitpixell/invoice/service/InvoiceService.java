package com.hitpixell.invoice.service;

import com.hitpixell.invoice.exception.DuplicateInvoiceException;
import com.hitpixell.invoice.exception.InvalidParameterException;
import com.hitpixell.invoice.exception.NoClientFoundException;
import com.hitpixell.invoice.exception.NoTransactionFoundException;
import com.hitpixell.invoice.model.*;
import com.hitpixell.invoice.repository.ClientBillingRepository;
import com.hitpixell.invoice.repository.ClientRepository;
import com.hitpixell.invoice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientBillingRepository clientBillingRepository;

    public Invoice generateInvoice(String clientName) {
        logger.info("Invoice generation started for {} ", clientName);
        if (clientName == null)
            throw new InvalidParameterException("ClientName parameter required");
        Invoice invoice = new Invoice();
        List<Transaction> transactions;
        LocalDateTime fromDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0, 0));
        LocalDateTime billingDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), 0));
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientName(clientName));
        if (client.isPresent()) {
            /**
             * Handle billing should not run multiple times for same billing cycle
             */
            List<ClientBilling> clientBillings = clientBillingRepository.findByClientNameOrderByBillingDateDesc(clientName);
            if (BillingInterval.MONTHLY.toString().equalsIgnoreCase(client.get().getBillingInterval())) {
                fromDate = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.of(0, 0, 0, 0));
            }
            if (!clientBillings.isEmpty() && !clientBillings.get(0).getBillingDate().isBefore(fromDate)) {
                logger.error("Invoice already generated for {} ", clientName);
                throw new DuplicateInvoiceException("Invoice already generated for {}" + clientName);
            }
            /**
             * get all transaction for given client and transaction date in billing cycle
             */
            transactions = transactionRepository.findByClientNameAndTransactionDateBetweenOrderByTransactionDateAsc(client.get().getClientName(),
                    fromDate, billingDate);
            if (transactions.isEmpty()) {
                logger.error("No transactions found for {} ", clientName);
                throw new NoTransactionFoundException("No transactions found for {}" + clientName);
            } else {
                /**
                 * prepare invoice for client
                 */
                prepareInvoice(clientName, invoice, transactions, fromDate, client.get());
                /**
                 * save billing cycle data in db
                 */
                clientBillingRepository.save(new ClientBilling(client.get().getClientName(), client.get().getBillingInterval(), billingDate));
            }

        } else {
            logger.error("No client found for {} ", clientName);
            throw new NoClientFoundException("No client found for {}" + clientName);
        }
        logger.info("Invoice generation Success for {} ", clientName);
        return invoice;

    }

    private void prepareInvoice(String clientName, Invoice invoice, List<Transaction> transactions, LocalDateTime fromDate, Client client) {
        List<InvoiceTransaction> invoiceTransactions = transactions.stream().map(transaction -> {
                    InvoiceTransaction invoiceTransaction = new InvoiceTransaction();
                    invoiceTransaction.setOrderId(transaction.getOrderId());
                    invoiceTransaction.setOrderName(transaction.getOrderName());
                    invoiceTransaction.setTransactionDate(transaction.getTransactionDate());
                    invoiceTransaction.setAmount(transaction.getAmount());
                    invoiceTransaction.setCurrency(transaction.getCurrency());
                    invoiceTransaction.setStatus(TransactionStatus.valueOf(transaction.getStatus().toUpperCase()));
                    invoiceTransaction.setCardType(transaction.getCardType());
                    switch (transaction.getStatus().toUpperCase()) {
                        case "APPROVED":
                            invoiceTransaction.setFee(client.fees);
                            break;
                        case "REFUNDED": {
                            /**
                             * handle fees charged for refunded transaction for already charged or same cycle transaction
                             */
                            if (transactionRepository.findByOrderId(transaction.getOrderId()).stream().anyMatch(pastTransaction -> pastTransaction.getStatus().equalsIgnoreCase("APPROVED")
                                    && pastTransaction.getTransactionDate().isBefore(fromDate))) {
                                /**
                                 * stream intermediate and terminal operations used to calculate transaction fee already charged or Not
                                 */
                                invoiceTransaction.setFee(BigDecimal.ZERO);
                                invoiceTransaction.setRefundFee(client.fees);
                            } else {
                                invoiceTransaction.setFee(client.fees.negate());
                            }
                            break;
                        }
                        case "DECLINED":
                            invoiceTransaction.setFee(BigDecimal.ZERO);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + transaction.getStatus());
                    }
                    return invoiceTransaction;
                }
        ).collect(Collectors.toList());
        invoice.setInvoiceId("INV-" + ThreadLocalRandom.current().nextInt());
        invoice.setClientName(clientName);
        invoice.setBillingInterval(client.getBillingInterval());
        invoice.setTransactions(invoiceTransactions);
        /**
         * map reduce used to get totalFees
         */
        Optional<BigDecimal> totalFees = invoiceTransactions.stream().map(InvoiceTransaction::getFee).reduce(BigDecimal::add);
        /**
         * map reduce used to get totalRefundFees
         */
        Optional<BigDecimal> totalRefundFees = invoiceTransactions.stream().map(InvoiceTransaction::getRefundFee).reduce(BigDecimal::add);
        invoice.setFeeChargesAmount(totalFees.orElse(BigDecimal.ZERO));
        invoice.setRefundAmount(totalRefundFees.orElse(BigDecimal.ZERO));
        invoice.setTotalBillingAmount(invoice.getFeeChargesAmount().add(invoice.getRefundAmount()));
    }
}
