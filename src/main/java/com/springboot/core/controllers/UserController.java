package com.springboot.core.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.core.dtos.UserDto;
import com.springboot.core.exceptions.ResourceNotFoundException;
import com.springboot.core.mappers.UserMapperImp;
import com.springboot.core.models.CommonResponse;
import com.springboot.core.models.User;
import com.springboot.core.services.RoleService;
import com.springboot.core.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        private final RoleService roleService;

        private final UserMapperImp userMapperImp;

        @GetMapping("/users")
        public ResponseEntity<CommonResponse<List<UserDto>>> getUsers(
                        @RequestParam(required = false, defaultValue = "10") int limit,
                        @RequestParam(defaultValue = "1") int page) {
                String message = "Successfully!";
                HttpStatus statusCode = HttpStatus.OK;
                int offset = page * limit;
                List<User> users = userService.getAllUsers(limit, offset);
                List<UserDto> listUserDtos = new ArrayList<UserDto>();
                for (User user : users) {
                        UserDto userDto = userMapperImp.userToUserDto(user);
                        listUserDtos.add(userDto);
                }
                return ResponseEntity.status(statusCode)
                                .body(CommonResponse.<List<UserDto>>builder().status(statusCode.value())
                                                .message(message).success(true).data(listUserDtos).build());
        }

        @GetMapping("/users/{user_id}")
        public ResponseEntity<CommonResponse<UserDto>> getUserById(
                        @PathVariable String user_id) {
                String message = "Successfully!";
                HttpStatus statusCode = HttpStatus.OK;
                User user = userService.getUserById(Long.parseLong(user_id))
                                .orElseThrow(() -> new ResourceNotFoundException());
                UserDto userDto = userMapperImp.userToUserDto(user);
                return ResponseEntity.status(statusCode)
                                .body(CommonResponse.<UserDto>builder().status(statusCode.value())
                                                .message(message).success(true).data(userDto).build());
        }

        @PostMapping("/users")
        public ResponseEntity<CommonResponse<User>> createUser(
                        @RequestBody User reqBody) {
                String message = "Successfully!";
                HttpStatus statusCode = HttpStatus.OK;
                userService.saveUser(reqBody);
                return ResponseEntity.status(statusCode).body(CommonResponse.<User>builder().status(statusCode.value())
                                .message(message).success(true).data(reqBody).build());
        }

        @PatchMapping("/user/{user_id}")
        public ResponseEntity<CommonResponse<User>> updateUserById(
                        @PathVariable String user_id, @RequestBody User reqBody) {
                String message = "Successfully!";
                HttpStatus statusCode = HttpStatus.OK;
                userService.saveUser(reqBody);
                return ResponseEntity.status(statusCode).body(CommonResponse.<User>builder().status(statusCode.value())
                                .message(message).success(true).data(reqBody).build());
        }

        @DeleteMapping("/user/{user_id}")
        public ResponseEntity<CommonResponse<?>> deleteUserById(
                        @PathVariable String user_id) {
                String message = "Successfully!";
                HttpStatus statusCode = HttpStatus.OK;
                return ResponseEntity.status(statusCode).body(CommonResponse.builder().status(statusCode.value())
                                .message(message).success(true)
                                .data(userService.deleteUserById(Long.parseLong(user_id))).build());
        }

}
