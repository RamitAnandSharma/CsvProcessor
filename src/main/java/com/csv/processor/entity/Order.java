package com.csv.processor.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "STORE_ORDER")
@Data 
@Table
@EqualsAndHashCode
public class Order {

	@Id
	@Column(name = "ROW_ID") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "ORDER_ID", length = 20) 
	@NotNull
	@Size(min = 1, max = 20)
	private String orderId;

	@Column(nullable = false, name = "ORDER_DATE") 
	@NotNull
	private LocalDate orderDate;

	@Column(nullable = false, name = "SHIP_DATE") 
	@NotNull
	private LocalDate shipDate;

	@Column(nullable = true, name = "SHIP_MODE", length = 20)
	@Size(max = 20)
	private String shipMode;

	@Column(nullable = false, name = "QUANTITY") 
	@NotNull
	private Integer quantity;

	@Column(name = "DISCOUNT") // DISCOUNT DECIMAL(3,2), 
	@NotNull
	private BigDecimal discount;

	@Column(nullable = false, name = "PROFIT") // PROFIT DECIMAL(6,2) NOT NULL, 
	@NotNull
	private BigDecimal profit;

	@Column(nullable = false, unique = true, name = "PRODUCT_ID", length = 20) 
	@NotNull
	@Size(min = 1, max = 20)
	private String productId;

	@Column(nullable = false, name = "CUSTOMER_NAME", length = 255) 
	@NotNull
	@Size(min = 1, max = 255)
	private String customerName;

	@Column(nullable = false, name = "CATEGORY", length = 255) 
	@NotNull
	@Size(min = 1, max = 255)
	private String category;

	@Column(nullable = false, unique = true, name = "CUSTOMER_ID", length = 20) 
	@NotNull
	@Size(min = 1, max = 20)
	private String customerId;
}
