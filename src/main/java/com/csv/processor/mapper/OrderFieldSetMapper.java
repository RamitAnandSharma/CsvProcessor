package com.csv.processor.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.csv.processor.entity.Order;


public class OrderFieldSetMapper implements FieldSetMapper<Order> {
	private String DATE_FORMAT = "dd.MM.yy";
	private DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);

	@Override
	public Order mapFieldSet(FieldSet fieldSet) throws BindException {
		Order order = new Order();
		order.setId(Long.parseLong(fieldSet.readString("Row ID")));
		order.setOrderId(emptyReplaceWithNull(fieldSet.readString("Order ID")));
		order.setShipMode(emptyReplaceWithNull(fieldSet.readString("Ship Mode")));
		order.setQuantity(fieldSet.readInt("Quantity"));
		order.setDiscount(fieldSet.readBigDecimal("Discount"));
		order.setProfit(fieldSet.readBigDecimal("Profit"));
		order.setProductId(emptyReplaceWithNull(fieldSet.readString("Product ID")));
		order.setCustomerName(emptyReplaceWithNull(fieldSet.readString("Customer Name")));
		order.setCategory(emptyReplaceWithNull(fieldSet.readString("Category")));
		order.setCustomerId(emptyReplaceWithNull(fieldSet.readString("Customer ID")));
		LocalDate orderDate = LocalDate.parse(fieldSet.readString("Order Date"), format);
		LocalDate shipDate = LocalDate.parse(fieldSet.readString("Ship Date"), format);
		order.setOrderDate(orderDate);
		order.setShipDate(shipDate);
		return order;
	}

	private String emptyReplaceWithNull(String value) {
		if (value == null || "".equals(value.trim())) {
			return null;
		}
		return value.trim();
	}
}
