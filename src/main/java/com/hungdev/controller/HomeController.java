package com.hungdev.controller;

import com.hungdev.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public ApiResponse HomeControllerHandler() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Welcome to fist learn java o day co phan hoi cua api");
        return apiResponse;
    }
}
