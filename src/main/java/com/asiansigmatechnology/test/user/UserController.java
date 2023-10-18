package com.asiansigmatechnology.test.user;

import com.asiansigmatechnology.test.user.dto.RegisterDTO;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO.Response> register(HttpServletRequest servletRequest, @RequestBody RegisterDTO.Request request){
        RegisterDTO.Response response = userServiceImpl.saveUser(servletRequest, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUser(){
        JsonObject result = new JsonObject();
        result.put("data", userServiceImpl.findAll());
        return ResponseEntity.ok(result.getMap());
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody User user){
        userServiceImpl.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/role/{userId}/{roleId}")
    public ResponseEntity<Void> grantRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){
        userServiceImpl.grantRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/{userId}/{roleId}")
    public ResponseEntity<Void> revokeRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){
        userServiceImpl.revokeRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

}
