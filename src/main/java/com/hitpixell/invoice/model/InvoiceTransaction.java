package com.hitpixell.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceTransaction {

    private String orderId;

    private String orderName;

    private BigDecimal amount = BigDecimal.ZERO;

    private String currency;

    private LocalDateTime transactionDate;

    private BigDecimal fee = BigDecimal.ZERO;

    private BigDecimal refundFee = BigDecimal.ZERO;

    private String cardType;

    private TransactionStatus status;

}
