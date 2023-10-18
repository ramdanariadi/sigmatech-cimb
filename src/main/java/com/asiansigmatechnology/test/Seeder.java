package com.asiansigmatechnology.test;

import com.asiansigmatechnology.test.role.Role;
import com.asiansigmatechnology.test.role.RoleRepository;
import com.asiansigmatechnology.test.user.User;
import com.asiansigmatechnology.test.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) {
        addRoleIfNotExist();
        addUserIfNotExist();
    }

    public void addUserIfNotExist(){
        if (null == userRepository.findUserByUsername("admin")) {
            Optional<Role> roleOptional = roleRepository.findRoleByName("ADMIN");
            if (roleOptional.isPresent()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("rahasia"));
                user.setName("Admin Blog");
                user.setRoles(List.of(roleOptional.get()));
                userRepository.save(user);
            }
        }
    }

    public void addRoleIfNotExist(){
        if (roleRepository.findRoleByName("ADMIN").isEmpty()) {
            Role role = new Role();
            role.setName("ADMIN");
            roleRepository.save(role);
        }
    }

}
