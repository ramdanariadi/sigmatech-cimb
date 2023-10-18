package com.asiansigmatechnology.test.user;

import com.asiansigmatechnology.test.user.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO.Response> register(HttpServletRequest servletRequest, @RequestBody RegisterDTO.Request request){
        RegisterDTO.Response response = userService.saveUser(servletRequest, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody User user){
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/role/{userId}/{roleId}")
    public ResponseEntity<Void> grantRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){
        userService.grantRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/{userId}/{roleId}")
    public ResponseEntity<Void> revokeRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){
        userService.revokeRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

}
