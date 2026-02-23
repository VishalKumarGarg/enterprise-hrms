package com.company.hrms.controller;

import com.company.hrms.dto.EmployeeDto;
import com.company.hrms.dto.LoginRequestDto;
import com.company.hrms.dto.LoginResponseDto;
import com.company.hrms.entity.Employee;
import com.company.hrms.security.JwtService;
import com.company.hrms.service.ModelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private AuthenticationManager  authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ModelService modelService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        try {

            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            Employee employee = (Employee) authentication.getPrincipal();

            String token = jwtService.generateToken(employee);

            EmployeeDto employeeDto = modelService.convertToDto(employee);

            LoginResponseDto response = LoginResponseDto.builder()
                    .token(token)
                    .employee(employeeDto)
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Incorrect email or password");
        }
    }


}
