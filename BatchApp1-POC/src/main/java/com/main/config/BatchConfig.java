package com.main.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.main.listener.JobMonitoringListener;
import com.main.processor.BookItemProcessor;
 

@Configuration
@EnableBatchProcessing
public class BatchConfig 
{
    @Autowired
	private JobBuilderFactory jobBuilderFactory;
    @Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	@Autowired
	private JobMonitoringListener listener;
	@Autowired
	private ItemWriter bookItemWriter;
	@Autowired
	private ItemReader bookItemReader;
	@Autowired
	private ItemProcessor bookItemProcessor;
	
	@Bean(name = "step1")
	public Step createStep1()
	{
		return stepBuilderFactory.get("step1")
				.<String,String>chunk(15)
				.reader(bookItemReader)
				.processor(bookItemProcessor)
				.writer(bookItemWriter)
				.build();
	}
	@Bean(name = "job1")
	public Job createJob1()
	{
		return jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(createStep1())
				.build();
	}
}
