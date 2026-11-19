package com.hungdev.controller;

import com.hungdev.domain.USER_ROLE;
import com.hungdev.modal.VerificationCode;
import com.hungdev.repository.UserRepository;
import com.hungdev.request.LoginRequest;
import com.hungdev.response.ApiResponse;
import com.hungdev.response.AuthResponse;
import com.hungdev.response.SignupRequest;
import com.hungdev.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {
        String jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);

    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode req) throws Exception {
        authService.sentLoginOtp(req.getEmail());
        ApiResponse response = new ApiResponse();
        response.setMessage("otp sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> LoginHandler(
            @RequestBody LoginRequest req) throws Exception {
        AuthResponse authResponse = authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }
    
}

