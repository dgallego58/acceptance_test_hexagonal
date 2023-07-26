package co.com.bancolombia.batch.config;

import co.com.bancolombia.model.Customer;
import co.com.bancolombia.sftp.config.SftpConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class JobConfig {

    private static final Logger log = LoggerFactory.getLogger(JobConfig.class);
    public static RowMapper<Customer> asCustomer = (rs, rowNum) -> Customer.builder()
            .id(rs.getLong("id"))
            .dni(rs.getString("dni"))
            .name(rs.getString("name"))
            .type(rs.getString("type"))
            .build();
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job dailyJob(Step dataBaseToFileStep,
                        Step fileToSftp) {
        return new JobBuilder("dailyJob", jobRepository)
                .flow(dataBaseToFileStep)
                .next(fileToSftp)
                .end()
                .incrementer(new RunIdIncrementer())
                .build();

    }

    @Bean
    public Step dataBaseToFileStep(ItemReader<Customer> itemReader,
                                   FlatFileItemWriter<Customer> itemWriter) {
        return new StepBuilder("dataBaseToFileStep", jobRepository)
                .<Customer, Customer>chunk(100, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step fileToSftp(PlatformTransactionManager transactionManager,
                           JobRepository jobRepository,
                           SftpConfiguration.TransferGateway transferGateway) {

        return new StepBuilder("fileToSftp", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    var filename = (String) chunkContext.getStepContext().getJobParameters().get("file");
                    log.info("Filename {}", filename);
                    var file = new FileSystemResource("tmp" + File.separator + filename).getFile();
                    transferGateway.sendToSftp(file);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Customer> itemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("dbReader")
                .dataSource(dataSource)
                .sql("""
                        select * from acceptance_test.customers
                        """)
                .rowMapper(asCustomer)
                .verifyCursorPosition(false)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> itemWriter(@Value("#{jobParameters['file']}") String filename,
                                                   ResourceLoader resourceLoader) {

        FlatFileItemWriter<Customer> fileItemWriter = new FlatFileItemWriter<>();
        fileItemWriter.setLineAggregator(item -> String.join(",",
                String.valueOf(item.getId()),
                item.getDni(),
                item.getType(),
                item.getName()));
        fileItemWriter.setHeaderCallback(writer -> writer.write("id,dni,type,name"));
        fileItemWriter.setResource(new FileSystemResource("tmp" + File.separator + filename));


        return fileItemWriter;
    }

    @Bean
    public ExecutionContextPromotionListener executionContextPromotionListener() {
        var ctx = new ExecutionContextPromotionListener();
        ctx.setKeys(new String[]{"file"});
        return ctx;
    }

}
