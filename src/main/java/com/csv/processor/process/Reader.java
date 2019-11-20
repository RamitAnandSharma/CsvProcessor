package com.csv.processor.process;

import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.csv.processor.entity.Order;
import com.csv.processor.mapper.OrderFieldSetMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@StepScope
public class Reader extends FlatFileItemReader<Order> {

	private Validator factory = Validation.buildDefaultValidatorFactory().getValidator();

	@Value("${input.file.path}")
	private String inputResourcePath;

	public Reader() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// load file in csvResource variable
		setResource(new FileSystemResource(this.inputResourcePath));
		this.setLinesToSkip(1);
		this.setLineMapper(lineMapper());
		this.setSkippedLinesCallback(new LineCallbackHandler() {
			@Override
			public void handleLine(String line) {
				log.debug("Skipping header Row: " + line);
			}
		});
		super.afterPropertiesSet();
	}

	public LineMapper<Order> lineMapper() {
		DefaultLineMapper<Order> lineMapper = new DefaultLineMapper<Order>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] { "Row ID", "Order ID", "Order Date", "Ship Date", "Ship Mode", "Customer ID", "Customer Name", "Product ID", "Category", "Quantity", "Discount", "Profit" });
		lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3, 4, 5, 6, 13, 14, 18, 19, 20 });
		OrderFieldSetMapper fieldSetMapper = new OrderFieldSetMapper();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Override
	protected Order doRead() throws Exception {
		Order order = super.doRead();
		if (Objects.isNull(order))
			return null;
		Set<ConstraintViolation<Order>> violations = this.factory.validate(order);
		if (!violations.isEmpty()) {
			String errorMsg = violations.stream().findFirst()
					.map(errorRecord -> String.format("Validation error for property '%s' with value '%s' and validation message '%s'", errorRecord.getPropertyPath(), errorRecord.getInvalidValue(), errorRecord.getMessage())).get().toString();
			throw new FlatFileParseException(errorMsg, Objects.toString(order), super.getCurrentItemCount());
		} else {
			return order;
		}
	}

}
