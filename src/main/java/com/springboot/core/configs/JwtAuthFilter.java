package com.springboot.core.configs;

import java.io.IOException;

import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.models.Permission;
import com.springboot.core.models.User;
import com.springboot.core.services.JwtService;
import com.springboot.core.services.PermissionService;
import com.springboot.core.services.UserService;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

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
        logger.info("permissions----------------------");

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("kien----------------------");

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                logger.info("dat----------------------");

                User user = userService.findByUsername(email);
                Long roleId = user.getRole().getId();

                List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
                logger.info(permissions);
                logger.info("sang----------------------");

                // ObjectMapper objectMapper = new ObjectMapper();

                // Set<String> allowedApis = permissions.stream()
                // .map(Permission::getApis)
                // .flatMap(apiList -> {
                // try {
                // // Parse the JSON array string into a List of Strings
                // String[] apisArray = objectMapper.readValue(apiList, String[].class);
                // return Arrays.stream(apisArray);
                // } catch (Exception e) {
                // // Handle the exception as needed (e.g., log it)
                // return Stream.empty();
                // }
                // })
                // .collect(Collectors.toSet());

                // Check if the requested API is allowed
                String requestApi = extractApiFromRequest(request); // Extract the API from request
                // Create authentication token and set in context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // if (allowedApis.contains(requestApi)) {
                // } else {
                // response.sendError(HttpServletResponse.SC_FORBIDDEN,
                // "You don't have permission to access this resource.");
                // return;
                // }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractApiFromRequest(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return path.replaceAll("^/+", ""); // Remove leading slashes
    }
}
