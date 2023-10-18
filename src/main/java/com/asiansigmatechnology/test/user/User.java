package com.asiansigmatechnology.test.user;

import com.asiansigmatechnology.test.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.asiansigmatechnology.test.role.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User extends BaseModel {
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public boolean hasRole(String name){
        return this.roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(name));
    }

    public boolean isAdmin(){
        return this.roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));
    }

}
