package com.csv.processor.job;

import javax.sql.DataSource;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.csv.processor.entity.Order;
import com.csv.processor.listener.DatabaseItemWriterListener;
import com.csv.processor.listener.JobCompletionNotificationListener;
import com.csv.processor.process.Reader;
import com.csv.processor.process.Writer;
import com.csv.processor.skip.ApplicationSkipPolicy;

import lombok.extern.slf4j.Slf4j;

/**
 * BatchProcessStarter Class launches the jobs and logs their execution results.
 *
 * @author
 * @since
 * @version
 *
 */
@Configuration
@Slf4j
public class JobStarter {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	Job job;

	@Autowired
	JobLauncher jobLauncher;

	/**
	 * Starts the jobs and logs their execution error & results.
	 *
	 */
	public void startJob() {
		try {
			String builderName = "csvFileToDatabaseJob";
			JobParameters params = new JobParametersBuilder().addString(builderName, String.valueOf(System.currentTimeMillis())).toJobParameters();
			JobExecution jobExecution = jobLauncher.run(job, params);
			log.debug("Batch job ends with status as " + jobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			log.error("Error while execution ->" + e);
		}
	}

	@Bean
	Job csvFileToDatabaseJob() {
		return jobBuilderFactory.get("csvFileToDatabaseJob").incrementer(new RunIdIncrementer()).listener(jobExecutionListener()).flow(step()).end().build();
	}

	public Step step() {
		return stepBuilderFactory.get("csvFileToDatabaseStep").<Order, Order>chunk(1)
				//Read the records from file
				.reader(csvReader())
				// .processor(sales2OrderProcessor())
				// Write record to database, In case of Exception skip record
				.writer(dbWriter()).faultTolerant()
				//Skip exception policy
				.skipPolicy(new ApplicationSkipPolicy())
				//Log exception in listener
				.listener(itemWriteListener()).build();
	}

	@Bean
	public ItemReader<Order> csvReader() {
		return new Reader();
	}

	@Bean
	public Writer dbWriter() {
		return new Writer();
	}

	@Bean
	public ItemWriteListener<Order> itemWriteListener() {
		return new DatabaseItemWriterListener();
	}

	@Bean
	public JobExecutionListener jobExecutionListener() {
		return new JobCompletionNotificationListener();
	}

}