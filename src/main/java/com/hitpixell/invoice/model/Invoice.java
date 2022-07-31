package com.hitpixell.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invoice {

    private String invoiceId;

    private String clientName;

    private String billingInterval;

    private List<InvoiceTransaction> transactions;

    private BigDecimal totalBillingAmount;

    private BigDecimal feeChargesAmount;

    private BigDecimal refundAmount;

}
