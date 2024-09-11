package com.springboot.core.configs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.springboot.core.services.RedisService;
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
    private final RedisService<String, String, Object> redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractJwtFromRequest(request);
        String email = null;

        if (jwt != null) {
            email = jwtService.extractEmail(jwt);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwt, null)) {
                Role role = roleService.findRoleByEmail(email).orElseThrow(ResourceNotFoundException::new);
                List<String> listMenuIds = roleService.findMenuByRole(role.getId());

                // Get data from Redis
                Object redisData = redisService.get(email);
                logger.info("No data found in Redis for email: ---------------------- " + redisData);

                // if (redisData instanceof List) {
                // List<?> dataList = (List<?>) redisData;
                // for (Object item : dataList) {
                // if (item instanceof Map) {
                // Map<?, ?> map = (Map<?, ?>) item;
                // logger.info(map.toString());
                // }
                // }
                // } else {
                // logger.info("No data found in Redis for email: " + email);
                // }

                // Create authentication token and set in context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                if (!hasPermission(request, role.getCode(), listMenuIds)) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    // Create a JSON object with your desired error message and status
                    JSONObject json = new JSONObject();
                    json.put("status", HttpServletResponse.SC_FORBIDDEN);
                    json.put("message", "You do not have permission to access this resource.");

                    PrintWriter out = response.getWriter();
                    out.print(json.toString());
                    out.flush();
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
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
                Permission permission = permissionService.getPermissionByName(transformedMenu);
                if (permission != null) {
                    // List<String> apis = objectMapper.readValue(permission.getApis(), new
                    // TypeReference<List<String>>() {
                    // });
                    if (permission.getApis().contains(specificApiName)) {
                        return true;
                    }
                }
            }
        }

        return false;
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
}