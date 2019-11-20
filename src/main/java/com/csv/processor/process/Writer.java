package com.csv.processor.process;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.csv.processor.entity.Order;
import com.csv.processor.entity.OrderRepo;


public class Writer implements ItemWriter<Order> {

	@Autowired
	private OrderRepo repo;

	@Override
	public void write(List<? extends Order> items) throws Exception {
		repo.saveAll(items);
	}

}
