package com.hitpixell.invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NonNull
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "status")
    private String status;

    @Column(name = "billingInterval")
    @JsonProperty("billing-interval")
    private String billingInterval;

    @Column(name = "email")
    public String email;

    @JsonProperty("fees-type")
    @Column(name = "feesType")
    public String feesType;

    @Column(name = "fees")
    public BigDecimal fees;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;

    public Client(String clientName, String status, String billingInterval, String email, String feesType, BigDecimal fees, LocalDateTime createdAt) {
        this.clientName = clientName;
        this.status = status;
        this.billingInterval = billingInterval;
        this.email = email;
        this.feesType = feesType;
        this.fees = fees;
        this.createdAt = createdAt;
    }
}
