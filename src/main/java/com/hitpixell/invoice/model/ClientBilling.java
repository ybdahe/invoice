package com.hitpixell.invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientBilling")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "billingInterval")
    @JsonProperty("billing-interval")
    private String billingInterval;

    @Column(name = "billingDate")
    private LocalDateTime billingDate;

    public ClientBilling(String clientName, String billingInterval, LocalDateTime billingDate) {
        this.clientName = clientName;
        this.billingInterval = billingInterval;
        this.billingDate = billingDate;
    }
}
