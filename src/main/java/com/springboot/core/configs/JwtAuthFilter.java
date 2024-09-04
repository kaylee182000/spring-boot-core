package com.springboot.core.configs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.exceptions.ResourceNotFoundException;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.services.JwtService;
import com.springboot.core.services.PermissionService;
import com.springboot.core.services.RoleService;

import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

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
                Role role = this.roleService.findRoleByEmail(email).orElseThrow(() -> new ResourceNotFoundException());
                List<String> listMenuIds = this.roleService.findMenuByRole(role.getId());

                // Create authentication token and set in context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                if (!hasPermission(request, role.getCode(), listMenuIds)) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    // Create a JSON object with your desired error message and status
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("data", null);
                    errorResponse.put("success", false);
                    errorResponse.put("message", "You don't have permission to access this resource.");
                    errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);

                    PrintWriter out = response.getWriter();
                    out.print(errorResponse.toString());
                    out.flush();
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
            throws JsonMappingException, JsonProcessingException, ResourceNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> transformedListMenus = new ArrayList<>();

        String method = request.getMethod().toUpperCase();
        switch (method) {
            case "GET":
                method = "VIEW";
                break;
            case "PATCH":
                method = "UPDATE";
                break;
            case "POST":
                method = "CREATE";
                break;
            case "DELETE":
                method = "DELETE";
                break;
            default:
                method = "APPROVE";
                break;
        }
        for (String listMenu : listMenuIds) {
            transformedListMenus.add(listMenu + "." + roleCode + "." + method);
        }
        String specificApiName = extractApiFromRequest(request);
        if (specificApiName != null) {
            for (String transformedMenu : transformedListMenus) {
                Permission permission = this.permissionService.getPermissionByName(transformedMenu);
                if (permission != null) {
                    List<String> apis = objectMapper.readValue(permission.getApis(), new TypeReference<List<String>>() {
                    });
                    if (apis.contains(specificApiName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
