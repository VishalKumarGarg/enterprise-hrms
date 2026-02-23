package com.company.hrms.service;

import com.company.hrms.dto.EmployeeDto;
import com.company.hrms.entity.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeDto convertToDto(Employee employee) {

        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);

        // 🔹 Manual nested mapping
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }

        if (employee.getManager() != null) {
            dto.setManagerId(employee.getManager().getId());
            dto.setManagerName(
                    employee.getManager().getFirstName() + " " +
                    employee.getManager().getLastName()
            );
        }

        return dto;
    }
}