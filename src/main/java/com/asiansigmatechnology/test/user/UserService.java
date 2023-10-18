package com.asiansigmatechnology.test.user;

import com.asiansigmatechnology.test.user.dto.RegisterDTO;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface UserService {

    public RegisterDTO.Response saveUser(HttpServletRequest servletRequest, RegisterDTO.Request request);
    public User getUserByUsername(String username);
    public Iterable<User> findAll();
    public void updateUser(User user);
    public void revokeRole(UUID userId, UUID roleId);
    public void grantRole(UUID userId, UUID roleId);
}
