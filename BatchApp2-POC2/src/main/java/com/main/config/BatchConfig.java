package com.main.config;

import javax.sql.DataSource;

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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.main.listener.JobMonitoringListener;
import com.main.model.Employee;
 
 

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
	private ItemProcessor employeeInfoProcessor;
	
	@Autowired
	private DataSource ds;
	
	
	@Bean(name = "ffiReader")
	 public FlatFileItemReader<Employee> createFFIReader()
	 {
		 //create reader object
		 FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
		 //set csv file as resource
		 reader.setResource(new ClassPathResource("Employee_Info.csv"));
		 //create line mapper object(to get each line from csv file)
		 DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		 //create tokenizer to get token from lines
		 DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		 tokenizer.setDelimiter(",");
		 tokenizer.setNames("empno","ename","eaddr","salary");
		 //create fileset mapper to map the model class properties
		 BeanWrapperFieldSetMapper<Employee> fieldMapper = new BeanWrapperFieldSetMapper<>();
		 fieldMapper.setTargetType(Employee.class);
		 lineMapper.setFieldSetMapper(fieldMapper);
		 lineMapper.setLineTokenizer(tokenizer);
		 
		 reader.setLineMapper(lineMapper);
		 
		 return reader;
		 
	 }
	@Bean(name = "jbiw")
	public JdbcBatchItemWriter<Employee> createJBIWriter()
	{
		JdbcBatchItemWriter<Employee>  writer = new JdbcBatchItemWriter<>();
		//set datasource
		writer.setDataSource(ds);
		//set INSERT SQL Query with named params
		writer.setSql("INSERT INTO Batch_Employeeinfo VALUES(:empno,:ename,:eaddr,:salary,:grossSalary,:netSalary)");
		//create BeanPropertyItemSqlParameterSourceProvider  object
		BeanPropertyItemSqlParameterSourceProvider<Employee> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<>();
		writer.setItemSqlParameterSourceProvider(sourceProvider);
		
		return writer;
	}
	@Bean(name = "step1")
	public Step createStep1()
	{
		return stepBuilderFactory.get("step1")
				.<Employee,Employee>chunk(1000)
				.reader(createFFIReader())
				.processor(employeeInfoProcessor)
				.writer(createJBIWriter())
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
