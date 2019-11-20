package com.csv.processor;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.csv.processor.job.JobStarter;

/**
 * Application Class starts the application.
 *
 * @author
 * @since
 * @version
 *
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories
public class CsvProcessorApplication {

	/**
	 * Starts the application
	 * 
	 * @param String[] args
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = SpringApplication.run(CsvProcessorApplication.class, args);
		JobStarter batchProcessStarter = (JobStarter) appContext.getBean("jobStarter");
		batchProcessStarter.startJob();
	}

}
