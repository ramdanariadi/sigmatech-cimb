package com.asiansigmatechnology.test.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {
    public List<Role> findAll();

    @Query("SELECT r FROM Role r WHERE LOWER(r.name) = LOWER(:name) AND r.deletedAt IS NULL")
    public Optional<Role> findRoleByName(String name);
}
