package com.hitpixell.invoice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NonNull
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "orderId")
	@Pattern(regexp = "^\\d{6}", message = "invalid orderId")
	private String orderId;

	@Column(name = "transactionDate")
	private LocalDateTime transactionDate;

	@Column(name = "orderName")
	private String orderName;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "currency")
	private String currency;

	@Column(name = "cardType")
	private String cardType;

	@Column(name = "status")
	private String status;

	@Column(name = "clientName")
	private String clientName;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "modifiedAt")
	private LocalDateTime modifiedAt;

}
