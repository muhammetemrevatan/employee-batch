package com.memrevatan.employeebatch.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryUtil {
    public static final String DB_TYPE = "ORACLE";

    // SQL query
    public static final String SELECT_CLAUSE = "SELECT guid, name, country, gender, salary";
    public static final String FROM = "FROM BATCH.EMPLOYEE";
    public static final String INSERT_INTO_EMPLOYEE_DETAIL = "INSERT INTO employee_detail (GUID, NAME, CURRENCY, BRANCH) VALUES (:guid, :name, :currency, :branch)";
}
