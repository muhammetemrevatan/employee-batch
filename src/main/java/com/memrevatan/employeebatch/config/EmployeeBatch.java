package com.memrevatan.employeebatch.config;


import com.memrevatan.employeebatch.entity.Employee;
import com.memrevatan.employeebatch.entity.EmployeeDetail;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class EmployeeBatch {

    private DataSource dataSource;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private StepCompletionNotificationListener listener;

    @Bean
    public JdbcPagingItemReader<Employee> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("guid", Order.ASCENDING);

        SqlPagingQueryProviderFactoryBean pagingQueryProvider = new SqlPagingQueryProviderFactoryBean();
        pagingQueryProvider.setDataSource(dataSource);
        pagingQueryProvider.setSelectClause("SELECT guid, name, country, gender, salary");
        pagingQueryProvider.setFromClause("FROM BATCH.EMPLOYEE");
        pagingQueryProvider.setSortKeys(sortKeys);
        pagingQueryProvider.setDatabaseType("ORACLE");

        JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReaderBuilder<Employee>()
                .name("employeeReader")
                .dataSource(dataSource)
                .queryProvider(Objects.requireNonNull(pagingQueryProvider.getObject()))
                .rowMapper((rs, rowNum) -> {
                    Employee employee = new Employee();
                    employee.setGuid(rs.getString("guid"));
                    employee.setName(rs.getString("name"));
                    employee.setCountry(rs.getString("country"));
                    employee.setGender(rs.getString("gender"));
                    employee.setSalary(rs.getBigDecimal("salary"));
                    return employee;
                })
                .pageSize(100)
                .build();

        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public EmployeeProcessor employeeProcessor() {
        return new EmployeeProcessor();
    }

//    @Bean
//    public JdbcBatchItemWriter<EmployeeDetail> writer() {
//        return new JdbcBatchItemWriterBuilder<Employee>()
//                .dataSource(dataSource)
//                .sql("UPDATE BATCH.EMPLOYEE SET name = :name WHERE guid = :guid")
//                .beanMapped()
//                .build();
//    }

    @Bean
    public JdbcBatchItemWriter<EmployeeDetail> writer() {
        JdbcBatchItemWriter<EmployeeDetail> writer = new JdbcBatchItemWriter<>();

        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO employee_detail (GUID, NAME, CURRENCY, BRANCH) VALUES (:guid, :name, :currency, :branch)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    @Lazy
    public Step employeeStep() throws Exception {
        return stepBuilderFactory.get("employeeStep")
                .<Employee, EmployeeDetail>chunk(10)
                .reader(reader())
                .processor(employeeProcessor())
                .writer(writer())
                .faultTolerant()
                .skipLimit(Integer.MAX_VALUE)
                .skip(DuplicateKeyException.class)
                .listener(this.listener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @Lazy
    public Job employeeJob(JobCompletionNotificationListener listener) throws Exception {
        return jobBuilderFactory.get("employeeJob")
                .listener(listener)
                .flow(employeeStep())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}
