package com.csv.processor.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			try {
				System.out.println(jobExecution.getJobId());
				log.debug("Job finished sucesfully. JobId: {}", jobExecution.getJobId());
			} catch (Exception e) {
				log.error("Error occurred while writing {} file");
			}
		}
	}
}
