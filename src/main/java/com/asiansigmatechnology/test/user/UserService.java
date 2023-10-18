package com.asiansigmatechnology.test.user;

import com.asiansigmatechnology.test.config.security.TokenGenerationAlgorithm;
import com.asiansigmatechnology.test.exception.ApiRequestException;
import com.asiansigmatechnology.test.role.Role;
import com.asiansigmatechnology.test.role.RoleRepository;
import com.asiansigmatechnology.test.user.dto.RegisterDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterDTO.Response saveUser(HttpServletRequest servletRequest, RegisterDTO.Request request){
        if(null != userRepository.findUserByUsername(request.getUsername())){
                throw new ApiRequestException("USER_TAKEN");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        Algorithm algorithm = TokenGenerationAlgorithm.algorithm;
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
                .withIssuer(servletRequest.getRequestURL().toString())
                .withClaim("roles", user.getRoles() == null ? new ArrayList<>() : user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .withClaim("x-custom-id", Map.of("userId", user.getId().toString()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10080 * 60 * 1000))
                .withIssuer(servletRequest.getRequestURL().toString())
                .sign(algorithm);

        RegisterDTO.Response response = new RegisterDTO.Response();
        response.setAccessToken(access_token);
        response.setRefreshToken(refresh_token);
        response.setUserId(user.getId());
        return response;
    }

    public void updateUser(User user){
        if(null == user.getId()){
            throw new ApiRequestException("USER_NOT_FOUND");
        }
        Optional<User> userTmp = userRepository.findById(user.getId());
        if(userTmp.isEmpty()){
            throw new ApiRequestException("USER_NOT_FOUND");
        }
        User existUser = userRepository.findUserByUsername(user.getUsername());
        if(null != existUser && !Objects.equals(existUser.getId(), user.getId())){
            throw new ApiRequestException("USERNAME_TAKEN");
        }
        User userContext = userTmp.get();
        userContext.setName(user.getName());
        userContext.setUsername(user.getUsername());
        userRepository.save(userContext);
    }

    public void grantRole(UUID userId, UUID roleId){
        Map<String, Object> entities = this.validateUserAndRole(userId, roleId);
        User user = (User) entities.get("user");

        if(!user.isAdmin()){
            throw new ApiRequestException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        Role role = (Role) entities.get("role");
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void revokeRole(UUID userId, UUID roleId){
        Map<String, Object> entities = this.validateUserAndRole(userId, roleId);
        User user = (User) entities.get("user");

        if(!user.isAdmin()){
            throw new ApiRequestException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        Role role = (Role) entities.get("role");
        user.setRoles(user.getRoles().stream().filter(roleItem -> !roleItem.getName().equals(role.getName())).collect(Collectors.toList()));
        userRepository.save(user);
    }

    private Map<String, Object> validateUserAndRole(UUID userId, UUID roleId){
        Optional<User> userContext = userRepository.findById(userId);
        if(userContext.isEmpty()){
            throw new ApiRequestException("USER_NOT_FOUND");
        }
        Optional<Role> roleContext = roleRepository.findById(roleId);
        if(roleContext.isEmpty()){
            throw new ApiRequestException("ROLE_NOT_FOUND");
        }
        User user = userContext.get();
        Role role = roleContext.get();
        Map<String, Object> entities = new HashMap<>();
        entities.put("user", user);
        entities.put("role", role);
        return entities;
    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(null == user){
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
    }

}
