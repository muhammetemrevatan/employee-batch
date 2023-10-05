package com.memrevatan.employeebatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    public void startBatch() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new Date().toString())
                .addLong("id", 123L)
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}