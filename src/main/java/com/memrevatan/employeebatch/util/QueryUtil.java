package com.memrevatan.employeebatch.util;

import lombok.experimental.UtilityClass;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class QueryUtil {
    public static final String DB_TYPE = "ORACLE";

    // SQL query
    public static final String SELECT_CLAUSE = "SELECT guid, name, country, gender, salary";
    public static final String FROM = "FROM BATCH.EMPLOYEE";
    public static final String INSERT_INTO_EMPLOYEE_DETAIL = "INSERT INTO employee_detail (GUID, NAME, CURRENCY, BRANCH) VALUES (:guid, :name, :currency, :branch)";

    public static SqlPagingQueryProviderFactoryBean queryProviderFactoryBean(DataSource dataSource, String selectClause, String fromClause) {
        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put(VariableUtil.GUID, Order.ASCENDING);

        SqlPagingQueryProviderFactoryBean pagingQueryProvider = new SqlPagingQueryProviderFactoryBean();
        pagingQueryProvider.setDataSource(dataSource);
        pagingQueryProvider.setSelectClause(selectClause);
        pagingQueryProvider.setFromClause(fromClause);
        pagingQueryProvider.setSortKeys(sortKeys);
        pagingQueryProvider.setDatabaseType(QueryUtil.DB_TYPE);
        return pagingQueryProvider;
    }
}
