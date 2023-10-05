package com.memrevatan.employeebatch.config;

import com.memrevatan.employeebatch.entity.Employee;
import com.memrevatan.employeebatch.entity.EmployeeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StepCompletionNotificationListener implements SkipListener<Employee, EmployeeDetail> {

    @Override
    public void onSkipInRead(Throwable t) {
        // TODO document why this method is empty
    }

    @Override
    public void onSkipInWrite(EmployeeDetail item, Throwable t) {
        log.error("Skipped during write: " + item.getName());
    }

    @Override
    public void onSkipInProcess(Employee item, Throwable t) {
        // TODO document why this method is empty
    }
}
