package com.springboot.core.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.springboot.core.controllers.dtos.AuthenticationRequest;
import com.springboot.core.controllers.dtos.AuthenticationResponse;
import com.springboot.core.exceptions.ResourceNotFoundException;
import com.springboot.core.models.CommonResponse;
import com.springboot.core.models.FcmToken;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.models.User;
import com.springboot.core.repositories.FcmTokenRepository;
import com.springboot.core.repositories.PermissionRepository;
import com.springboot.core.repositories.RoleRepository;
import com.springboot.core.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final FcmTokenRepository fcmTokenRepository;
        private final RedisService redisService;
        private final RoleRepository roleRepository;

        public CommonResponse<Map<String, Object>> login(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, jwtToken);
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("accessToken", jwtToken);
                response.put("refreshToken", refreshToken);
                response.put("user", user);

                // store user to redis
                // Get the role and role permissions
                Role role = user.getRole();
                Set<Permission> rolePermissions = roleRepository.findPermissionByRoleId(role.getId());
                // Create a map to store the role and role permissions data
                Map<String, Object> roleData = new HashMap<>();
                roleData.put("id", role.getId());
                roleData.put("name", role.getName());
                roleData.put("code", role.getCode());

                // Create a list to store the role permissions data
                List<Map<String, Object>> permissionData = new ArrayList<>();
                for (Permission permission : rolePermissions) {
                        Map<String, Object> permissionMap = new HashMap<>();
                        permissionMap.put("id", permission.getId());
                        permissionMap.put("deletedDate", permission.getDeletedDate());
                        permissionMap.put("name", permission.getName());
                        permissionMap.put("apis", permission.getApis());
                        permissionData.add(permissionMap);
                }

                // Add the role permissions data to the role data map
                roleData.put("rolePermissions", permissionData);

                // Store the role data in Redis
                redisService.set(user.getEmail(), roleData);

                return CommonResponse.<Map<String, Object>>builder().status(HttpStatus.OK.value())
                                .message("LOGIN_SUCCESS").success(true)
                                .data(response)
                                .build();
        }

        private void saveUserToken(User user, String jwtToken) {
                var token = FcmToken.builder()
                                .user(user)
                                .fcmToken(jwtToken)
                                .createdDate(LocalDateTime.now())
                                .createdBy(user.getId().intValue()).updatedDate(null)
                                .updatedBy(null)
                                .deviceName("test")
                                .deviceHash("test").updatedBy(null)
                                .build();
                fcmTokenRepository.save(token);
        }

}
