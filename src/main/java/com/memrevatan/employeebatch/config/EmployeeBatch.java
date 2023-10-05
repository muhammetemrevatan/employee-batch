package com.memrevatan.employeebatch.config;


import com.memrevatan.employeebatch.config.listener.JobCompletionNotificationListener;
import com.memrevatan.employeebatch.config.listener.StepCompletionNotificationListener;
import com.memrevatan.employeebatch.config.processor.EmployeeProcessor;
import com.memrevatan.employeebatch.data.entity.Employee;
import com.memrevatan.employeebatch.data.entity.EmployeeDetail;
import com.memrevatan.employeebatch.util.BatchUtil;
import com.memrevatan.employeebatch.util.QueryUtil;
import com.memrevatan.employeebatch.util.VariableUtil;
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
        sortKeys.put(VariableUtil.GUID, Order.ASCENDING);

        SqlPagingQueryProviderFactoryBean pagingQueryProvider = new SqlPagingQueryProviderFactoryBean();
        pagingQueryProvider.setDataSource(dataSource);
        pagingQueryProvider.setSelectClause(QueryUtil.SELECT_CLAUSE);
        pagingQueryProvider.setFromClause(QueryUtil.FROM);
        pagingQueryProvider.setSortKeys(sortKeys);
        pagingQueryProvider.setDatabaseType(QueryUtil.DB_TYPE);

        JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReaderBuilder<Employee>()
                .name(BatchUtil.EMPLOYEE_READER)
                .dataSource(dataSource)
                .queryProvider(Objects.requireNonNull(pagingQueryProvider.getObject()))
                .rowMapper((rs, rowNum) -> {
                    Employee employee = new Employee();
                    employee.setGuid(rs.getString(VariableUtil.GUID));
                    employee.setName(rs.getString(VariableUtil.NAME));
                    employee.setCountry(rs.getString(VariableUtil.COUNTRY));
                    employee.setGender(rs.getString(VariableUtil.GENDER));
                    employee.setSalary(rs.getBigDecimal(VariableUtil.SALARY));
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
        writer.setSql(QueryUtil.INSERT_INTO_EMPLOYEE_DETAIL);
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    @Lazy
    public Step employeeStep() throws Exception {
        return stepBuilderFactory.get(BatchUtil.EMPLOYEE_STEP)
                .<Employee, EmployeeDetail>chunk(BatchUtil.CHUNK)
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
        return jobBuilderFactory.get(BatchUtil.EMPLOYEE_JOB)
                .listener(listener)
                .flow(employeeStep())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(BatchUtil.CONCURRENCY_LIMIT);
        return asyncTaskExecutor;
    }

}
