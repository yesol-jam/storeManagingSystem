package com.bucket.storeManagingSystem.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class BatchConfig {
    @Bean
    public Job jsonToDbJob(JobRepository jobRepository, Step jsonToDbStep) {
        return new JobBuilder("jsonToDbJob", jobRepository)
                .start(jsonToDbStep)
                .build();
    }
    @Bean
    public Step jsonToDbStep(JobRepository jobRepository, Tasklet jsonToDbBatchTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("jsonToDbStep", jobRepository)
                .tasklet(jsonToDbBatchTasklet, platformTransactionManager).build();
    }

}
