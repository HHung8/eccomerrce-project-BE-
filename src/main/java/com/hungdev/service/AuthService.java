package com.hungdev.service;

import com.hungdev.request.LoginRequest;
import com.hungdev.response.AuthResponse;
import com.hungdev.response.SignupRequest;

public interface AuthService {
    void sentLoginOtp(String email) throws Exception;

    String createUser(SignupRequest request) throws Exception;

    AuthResponse signing(LoginRequest req);
}
