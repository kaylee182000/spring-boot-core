package com.springboot.core.configs;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.exceptions.ResourceNotFoundException;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.models.User;
import com.springboot.core.services.JwtService;
import com.springboot.core.services.PermissionService;
import com.springboot.core.services.RoleService;
import com.springboot.core.services.UserService;

import io.jsonwebtoken.lang.Arrays;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private final RoleService roleService;

    private final PermissionService permissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        // extract username from JWT token
        email = jwtService.extractEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwt, null)) {
                Role role = roleService.findRoleCodeName(email).orElseThrow(() -> new ResourceNotFoundException());
                List<String> listMenuIds = roleService.findMenuByRole(role.getId());

                // Create authentication token and set in context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                if (!hasPermission(request, role.getCode(), listMenuIds)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "You don't have permission to access this resource.");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractApiFromRequest(HttpServletRequest request) {
        String method = request.getMethod().toLowerCase();
        String path = request.getRequestURI().replaceAll("/v1/", ""); // Assuming "/v1/" prefix

        // Extract the resource path (e.g., "users", "users/2")
        Pattern pattern = Pattern.compile("^(\\w+)(/\\d+)?$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String resource = matcher.group(1);
            String id = matcher.group(2) != null ? "-" + "id" : "";
            return method + "-" + resource + id;
        } else {
            return null;
        }
    }

    private boolean hasPermission(HttpServletRequest request, String roleCode, List<String> listMenuIds)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> transformedListMenus = new ArrayList<>();

        String method = request.getMethod().toUpperCase();
        if (method.equals("GET")) {
            method = "VIEW";
        }
        for (String listMenu : listMenuIds) {
            transformedListMenus.add(listMenu + "." + roleCode + "." + method);
        }
        // String permissionName = "3.SYSTEM_ADMIN.VIEW";
        String specificApiName = extractApiFromRequest(request);
        if (specificApiName != null) {
            for (String transformedMenu : transformedListMenus) {
                Permission permission = permissionService.getPermissionByName(transformedMenu);
                List<String> apis = objectMapper.readValue(permission.getApis(), new TypeReference<List<String>>() {
                });
                if (apis.contains(specificApiName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
