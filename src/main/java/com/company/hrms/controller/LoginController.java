package com.company.hrms.controller;


import com.company.hrms.dto.LoginRequestDto;
import com.company.hrms.dto.LoginResponseDto;
import com.company.hrms.dto.OtpRequestDto;
import com.company.hrms.dto.ResendOtpRequestDto;
import com.company.hrms.service.AuthService;
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
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private ModelService modelService;
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDto> login(
//            @RequestBody LoginRequestDto loginRequestDto) {
//
//        try {
//
//            var authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequestDto.getEmail(),
//                            loginRequestDto.getPassword()
//                    )
//            );
//
//            Employee employee = (Employee) authentication.getPrincipal();
//
//            String token = jwtService.generateToken(employee);
//
//            EmployeeDto employeeDto = modelService.convertToDto(employee);
//
//            LoginResponseDto response = LoginResponseDto.builder()
//                    .token(token)
//                    .employee(employeeDto)
//                    .build();
//
//            return ResponseEntity.ok(response);
//
//        } catch (BadCredentialsException ex) {
//            throw new RuntimeException("Incorrect email or password");
//        }
//    }
    @Autowired
    private AuthService authService; // NEW SERVICE

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto loginRequestDto) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            // If password correct → generate and send OTP
            authService.generateAndSendOtp(loginRequestDto.getEmail());

            return ResponseEntity.ok("OTP sent to email");

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Incorrect email or password");
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDto> verifyOtp(
            @RequestBody OtpRequestDto request) {

        return ResponseEntity.ok(authService.verifyOtp(request));
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(
            @RequestBody ResendOtpRequestDto request) {

        authService.resendOtp(request.getEmail());

        return ResponseEntity.ok(
                "If OTP was expired, a new OTP has been sent to your email."
        );
    }
}
