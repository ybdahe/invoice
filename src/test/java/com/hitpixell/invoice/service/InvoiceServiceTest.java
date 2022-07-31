package com.hitpixell.invoice.service;

import com.hitpixell.invoice.exception.NoTransactionFoundException;
import com.hitpixell.invoice.model.Client;
import com.hitpixell.invoice.model.ClientBilling;
import com.hitpixell.invoice.model.Invoice;
import com.hitpixell.invoice.model.Transaction;
import com.hitpixell.invoice.repository.ClientBillingRepository;
import com.hitpixell.invoice.repository.ClientRepository;
import com.hitpixell.invoice.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientBillingRepository clientBillingRepository;

    @InjectMocks
    InvoiceService invoiceService;

    @Test
    void generateDuplicateInvoiceThrowsException() {
        Client client = new Client();
        client.setClientName("Pizza House 3");
        client.setBillingInterval("monthly");

        Invoice invoiceData = new Invoice();
        invoiceData.setInvoiceId("INV0001");
        invoiceData.setClientName(client.getClientName());

        List<ClientBilling> clientBillings = new ArrayList<>();
        ClientBilling clientBilling = new ClientBilling(client.getClientName(),client.getBillingInterval(), LocalDateTime.now());
        clientBillings.add(clientBilling);

        when(clientRepository.findByClientName(client.getClientName())).thenReturn(client);
        when(clientBillingRepository.findByClientNameOrderByBillingDateDesc(client.getClientName())).thenReturn(clientBillings);
        try {
            invoiceService.generateInvoice(client.getClientName());
        }catch (Exception e){
            assertThatExceptionOfType(NoTransactionFoundException.class);
        }
    }

    @Test
    void generateInvoiceSuccess() {
        Client client = new Client();
        client.setClientName("Pizza House 3");
        client.setBillingInterval("monthly");
        client.setFees(BigDecimal.ONE);

        LocalDateTime fromDate = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.of(0, 0, 0, 0));
        LocalDateTime billingDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), 0));

        Invoice invoiceData = new Invoice();
        invoiceData.setInvoiceId("INV0001");
        invoiceData.setClientName(client.getClientName());

        List<ClientBilling> clientBillings = new ArrayList<>();
        ClientBilling clientBilling = new ClientBilling(client.getClientName(),client.getBillingInterval(), billingDate);

        Transaction transaction = new Transaction();
        transaction.setOrderName("Veg Pizza");
        transaction.setStatus("approved");
        transaction.setClientName(client.getClientName());

        List<Transaction> allTransactions = Collections.singletonList(transaction);

        when(clientRepository.findByClientName(client.getClientName())).thenReturn(client);
        when(clientBillingRepository.findByClientNameOrderByBillingDateDesc(client.getClientName())).thenReturn(clientBillings);
        when(transactionRepository.findByClientNameAndTransactionDateBetweenOrderByTransactionDateAsc(client.getClientName(),fromDate,billingDate)).thenReturn(allTransactions);
        when(clientBillingRepository.save(clientBilling)).thenReturn(clientBilling);

        Invoice invoice = invoiceService.generateInvoice(client.getClientName());
        assertThat(invoice).isNotNull();
        assertThat(invoice.getClientName()).isEqualTo(invoiceData.getClientName());
    }

}