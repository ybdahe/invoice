package com.hitpixell.invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NonNull
public class ClientDTO {

    private long id;

    private String clientName;

    private String status;

    @JsonProperty("billing-interval")
    private String billingInterval;

    private String email;

    @JsonProperty("fees-type")
    private String feesType;

    private BigDecimal fees;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public ClientDTO(String clientName, String status, String billingInterval, String email, String feesType, BigDecimal fees, LocalDateTime createdAt) {
        this.clientName = clientName;
        this.status = status;
        this.billingInterval = billingInterval;
        this.email = email;
        this.feesType = feesType;
        this.fees = fees;
        this.createdAt = createdAt;
    }
}
