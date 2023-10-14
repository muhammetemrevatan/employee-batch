package com.memrevatan.employeebatch.batch.processor;

import com.memrevatan.employeebatch.data.entity.Employee;
import com.memrevatan.employeebatch.data.entity.EmployeeDetail;
import com.memrevatan.employeebatch.enums.Currency;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class EmployeeProcessor implements ItemProcessor<Employee, EmployeeDetail> {

    @Override
    public EmployeeDetail process(Employee employee) throws Exception {
        String branch = employee.getName().substring(0, 4);
        String name = employee.getName().substring(4, employee.getName().indexOf("    ")).trim();

        EmployeeDetail detail = new EmployeeDetail();
        detail.setGuid(UUID.randomUUID().toString());
        detail.setName(name);
        switch (employee.getCountry()) {
            case "USA" -> detail.setCurrency(Currency.USD.name());
            case "UK" -> detail.setCurrency(Currency.GBP.name());
            case "Canada" -> detail.setCurrency(Currency.CAD.name());
            case "Australia" -> detail.setCurrency(Currency.AUD.name());
            default -> detail.setCurrency(Currency.EUR.name());
        }
        detail.setBranch(branch);

        return detail;
    }

}
