package com.springboot.core.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.dtos.AuthenticationRequest;
import com.springboot.core.dtos.PermissionDto;
import com.springboot.core.dtos.RoleDto;
import com.springboot.core.dtos.UserDto;
import com.springboot.core.mappers.PermissionMapper;
import com.springboot.core.mappers.RoleMapper;
import com.springboot.core.mappers.UserMapper;
import com.springboot.core.mappers.UserMapperImp;
import com.springboot.core.models.CommonResponse;
import com.springboot.core.models.FcmToken;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.models.User;
import com.springboot.core.repositories.FcmTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserService userService;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final FcmTokenRepository fcmTokenRepository;
        private final RedisService redisService;
        private final UserMapperImp mapperImp;

        public CommonResponse<Map<String, Object>> login(AuthenticationRequest request) {
                ObjectMapper objectMapper = new ObjectMapper();

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                User user = userService.findByEmail(request.getEmail());
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, jwtToken);

                // Role role = roleService.findRoleWithPermissions(user.getRole().getId());
                // RoleDto roleDto = RoleMapper.INSTANCE.roleToRoleDto(role);

                // List<Permission> permissions =
                // roleService.findPermissionByRoleId(role.getId());
                // List<PermissionDto> listPermissionDto = new ArrayList<>();
                // for (Permission permission : permissions) {
                // PermissionDto permissionDto =
                // PermissionMapper.INSTANCE.permissionToPermissionDto(permission);

                // listPermissionDto.add(permissionDto);

                // }
                // roleDto.setRolePermissions(listPermissionDto);

                // UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
                // userDto.setRole(roleDto);
                UserDto userDto = mapperImp.userToUserDto(user);
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("accessToken", jwtToken);
                response.put("refreshToken", refreshToken);
                response.put("user", userDto);
                // Store the role data in Redis
                try {
                        String userJson = objectMapper.writeValueAsString(userDto);
                        System.out.println(userJson);

                        redisService.set(user.getEmail(), userJson);
                } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the exception appropriately
                }

                return CommonResponse.<Map<String, Object>>builder().status(HttpStatus.OK.value())
                                .message("LOGIN_SUCCESS").success(true)
                                .data(response)
                                .build();
        }

        private void saveUserToken(User user, String jwtToken) {
                var token = FcmToken.builder()
                                .user(user)
                                .fcmToken(jwtToken)
                                .createdDate(LocalDate.now())
                                .createdBy(user.getId().intValue()).updatedDate(null)
                                .updatedBy(null)
                                .deviceName("test")
                                .deviceHash("test").updatedBy(null)
                                .build();
                fcmTokenRepository.save(token);
        }

}
