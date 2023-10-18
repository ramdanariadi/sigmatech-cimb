package com.asiansigmatechnology.test.auth;

import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping(path = "/token")
    public ResponseEntity<Object> token(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        Map<String, Object> token = authService.token(authorization);
        JsonObject result = new JsonObject();
        result.put("data", token);
        return ResponseEntity.ok(result.getMap());
    }
}
