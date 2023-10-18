package com.asiansigmatechnology.test.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class MyAuthorizationFilter extends OncePerRequestFilter {

    Logger LOGGER = LoggerFactory.getLogger(MyAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(httpServletRequest.getServletPath().equals("/api/v1/user/token")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else{
            String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = TokenGenerationAlgorithm.algorithm;
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(httpServletRequest){
                        @Override
                        public String getHeader(String name) {
                            LOGGER.info("this-name {}",name);
                            if("X-CUSTOM-ID".equalsIgnoreCase(name)){
                                JsonObject customId = new JsonObject(decodedJWT.getClaim("x-custom-id").asMap());
                                return customId.encode();
                            }
                            return super.getHeader(name);
                        }
                    };
                    filterChain.doFilter(httpServletRequestWrapper,httpServletResponse);
                }catch (Exception e){
                    log.error("error : {}",e.getMessage());
                    httpServletResponse.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error exception",e.getMessage());
                    httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(httpServletResponse.getOutputStream(),error);
                }
            }else{
                filterChain.doFilter(httpServletRequest,httpServletResponse);
            }
        }
    }
}
