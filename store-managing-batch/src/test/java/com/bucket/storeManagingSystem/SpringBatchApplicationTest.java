package com.bucket.storeManagingSystem;

import com.bucket.storeManagingSystem.config.BatchConfig;
import com.bucket.storeManagingSystem.job.JsonToDbBatchTasklet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {
		SpringBatchApplication.class,
		JsonToDbBatchTasklet.class,
		BatchConfig.class
})
class SpringBatchApplicationTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job jsonToDbJob;

	@Test
	void batchRun() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis()) // 중복 방지용
				.toJobParameters();

		JobExecution execution = jobLauncher.run(jsonToDbJob, jobParameters);

		assertEquals(BatchStatus.COMPLETED, execution.getStatus());
		System.out.println("배치 실행 결과: " + execution.getStatus());
	}
}
