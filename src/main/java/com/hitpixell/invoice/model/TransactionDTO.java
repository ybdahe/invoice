package com.hitpixell.invoice.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NonNull
public class TransactionDTO {

    private long id;

    private String orderId;

    private LocalDateTime transactionDate;

    private String orderName;

    private BigDecimal amount;

    private String currency;

    private String cardType;

    private String status;

    private String clientName;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

}
