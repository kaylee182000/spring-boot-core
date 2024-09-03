package com.springboot.core.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.core.models.CommonResponse;
import com.springboot.core.models.User;
import com.springboot.core.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public ResponseEntity<CommonResponse<List<User>>> getUsers(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page) {
        String message = "Successfully!";
        HttpStatus statusCode = HttpStatus.OK;
        int offset = (page - 1) * limit;
        List<User> users = userService.getAllUsers(limit, offset);
        return ResponseEntity.status(statusCode).body(CommonResponse.<List<User>>builder().status(statusCode.value())
                .message(message).success(true).data(users).build());
    }

}
