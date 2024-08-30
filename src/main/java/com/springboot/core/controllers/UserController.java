package com.springboot.core.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.core.models.CommonResponse;
import com.springboot.core.models.User;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/v1")
public class UserController {
    @RequestMapping(path = "users", method = RequestMethod.GET)
    public ResponseEntity<CommonResponse<List<User>>> getUsers() {
        String message = "Successfully!";
        HttpStatus statusCode = HttpStatus.OK;
        return ResponseEntity.status(statusCode).body(CommonResponse.<List<User>>builder().status(statusCode.value())
                .message(message).success(true).data(null).build());
    }

}
