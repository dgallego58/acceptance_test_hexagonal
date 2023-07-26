package co.com.bancolombia.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class SchedulerJob {


    private final Job dailyJob;
    private final JobLauncher launcher;

    public SchedulerJob(Job dailyJob, JobLauncher jobLauncher) {
        this.dailyJob = dailyJob;
        this.launcher = jobLauncher;
    }

    @Scheduled(fixedDelay = 10_000L)
    public void executor() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        var param = new JobParameter<>(LocalDate.now() + ".csv", String.class);
        var param2 = new JobParameter<>(Instant.now().toString(), String.class);
        var jobParams = new JobParametersBuilder()
                .addJobParameter("file", param)
                .addJobParameter("startTime", param2)
                .toJobParameters();

        launcher.run(dailyJob, jobParams);
    }
}
