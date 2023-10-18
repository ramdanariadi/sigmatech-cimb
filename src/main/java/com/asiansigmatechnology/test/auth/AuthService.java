package com.asiansigmatechnology.test.auth;

import com.asiansigmatechnology.test.config.security.TokenGenerationAlgorithm;
import com.asiansigmatechnology.test.user.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.asiansigmatechnology.test.exception.ApiRequestException;
import com.asiansigmatechnology.test.user.User;
import com.asiansigmatechnology.test.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    UserService userService;

    @Autowired
    public AuthService(UserService userService){
        this.userService = userService;
    }

    public Map<String, Object> token(String authorization){

        if(null == authorization || !authorization.startsWith("Bearer ")) throw new ApiRequestException("TOKEN_IS_MISSING", HttpStatus.UNAUTHORIZED);

        try {
            String refresh_token = authorization.substring("Bearer ".length());
            Algorithm algorithm = TokenGenerationAlgorithm.algorithm;
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
            String username = decodedJWT.getSubject();
            User user = userService.getUserByUsername(username);
            String access_token = JWT.create()
                    .withSubject(username)
                    .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
                    .sign(algorithm);
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("access_token",access_token);
            tokens.put("refresh_token",refresh_token);
            return tokens;
        }catch (Exception e){
            throw new ApiRequestException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
