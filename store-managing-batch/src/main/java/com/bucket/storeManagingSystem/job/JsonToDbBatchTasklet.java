package com.bucket.storeManagingSystem.job;

import com.bucket.storeManagingSystem.component.JsonReader;
import com.bucket.storeManagingSystem.repository.dto.AccessLogDto;
import com.bucket.storeManagingSystem.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JsonToDbBatchTasklet implements Tasklet {

    @Autowired
    ManageService manageService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("=====Batch PROCESS START=====");

        manageService.setAccessLogs();

        log.info("=====Batch PROCESS COMPLETE=====");
        return RepeatStatus.FINISHED;
    }

}
