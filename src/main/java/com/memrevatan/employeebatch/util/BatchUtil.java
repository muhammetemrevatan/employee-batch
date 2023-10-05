package com.memrevatan.employeebatch.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BatchUtil {
    public static final String EMPLOYEE_READER = "employeeReader";
    public static final String EMPLOYEE_STEP = "employeeStep";
    public static final String EMPLOYEE_JOB = "employeeJob";
    public static final Integer CONCURRENCY_LIMIT = 10;
    public static final Integer CHUNK = 10;
}
