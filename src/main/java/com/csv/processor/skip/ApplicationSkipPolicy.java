package com.csv.processor.skip;

import java.io.FileNotFoundException;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import lombok.extern.slf4j.Slf4j;

/**
 * This Class determining whether or not some record processing should be
 * skipped
 * 
 * @author sbbras
 *
 */
@Slf4j
public class ApplicationSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable throwable, int skipCount) throws SkipLimitExceededException {

		if (throwable instanceof FileNotFoundException) {
			return false;
		} else if (throwable instanceof NumberFormatException) {
			return true; 
		} else if (throwable instanceof NullPointerException) {
			return true;
		} else if (throwable instanceof DuplicateKeyException) {
			return true;
		} else if (throwable instanceof DataIntegrityViolationException) {
			return true;
		} else if (throwable instanceof FlatFileParseException) { // && skipCount <= 5) { 
			FlatFileParseException ffpe = (FlatFileParseException) throwable;
			StringBuilder errorMessage = new StringBuilder(); 
			errorMessage.append("Line number " + ffpe.getLineNumber() + " of the file.==> "); 
			errorMessage.append(ffpe.getLocalizedMessage());  
			errorMessage.append(ffpe.getInput()); 
			log.error("{}", errorMessage.toString());
			return true;
		} else if (throwable instanceof FileNotFoundException) {
			return false;
		} else {
			return false;
		}
	}
}
