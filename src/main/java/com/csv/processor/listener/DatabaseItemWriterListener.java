package com.csv.processor.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

import com.csv.processor.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseItemWriterListener implements ItemWriteListener<Order> {

	@Override
	public void beforeWrite(List<? extends Order> items) {
		
	}

	@Override
	public void afterWrite(List<? extends Order> items) {
		log.debug("Order saved successfully with OrderID [{}]", items.get(0).getId());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Order> items) {
		log.error(exception.getMessage());  
	}

}
