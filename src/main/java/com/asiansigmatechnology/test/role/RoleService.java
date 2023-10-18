package com.asiansigmatechnology.test.role;

import com.asiansigmatechnology.test.role.dto.AddRoleDTO;
import com.asiansigmatechnology.test.role.dto.GetRoleDTO;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    public AddRoleDTO.Response saveRole(AddRoleDTO.Request request);
    public GetRoleDTO.Response findById(UUID id);
    public List<GetRoleDTO.Response> findAll();
}
