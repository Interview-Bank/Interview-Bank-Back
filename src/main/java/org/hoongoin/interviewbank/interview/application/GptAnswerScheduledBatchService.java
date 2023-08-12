package org.hoongoin.interviewbank.interview.application;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GptAnswerScheduledBatchService {

	private final JobLauncher jobLauncher;
	private final Job updateGptAnswerJob;

	@Scheduled(cron = "0 0 2 * * ?")
	public void runJob() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {
		jobLauncher.run(updateGptAnswerJob, new JobParameters());
	}
}
