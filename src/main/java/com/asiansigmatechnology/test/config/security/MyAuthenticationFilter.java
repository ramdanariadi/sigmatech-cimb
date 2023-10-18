package com.asiansigmatechnology.test.config.security;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.asiansigmatechnology.test.user.UserServiceImpl;
import io.vertx.core.json.JsonObject;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private UserServiceImpl userServiceImpl;

    public MyAuthenticationFilter(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;
        if(request.getHeader(CONTENT_TYPE).equalsIgnoreCase(APPLICATION_JSON_VALUE)){
            try {
                byte[] bytes = StreamUtils.copyToByteArray(request.getInputStream());
                JsonObject requestBody = new JsonObject(new String(bytes, StandardCharsets.UTF_8));
                username = requestBody.getString("username");
                password = requestBody.getString("password");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else{
            username = request.getParameter("username");
            password = request.getParameter("password");
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = TokenGenerationAlgorithm.algorithm;
        com.asiansigmatechnology.test.user.User rawUser = userServiceImpl.getUserByUsername(user.getUsername());

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("x-custom-id", Map.of("userId", rawUser.getId().toString()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10080 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, Object> result = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("access_token",access_token);
        data.put("refresh_token",refresh_token);
        result.put("data", data);
        
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }
}