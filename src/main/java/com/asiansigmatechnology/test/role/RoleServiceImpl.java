package com.asiansigmatechnology.test.role;

import com.asiansigmatechnology.test.role.dto.AddRoleDTO;
import com.asiansigmatechnology.test.role.dto.GetRoleDTO;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.asiansigmatechnology.test.exception.ApiRequestException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public AddRoleDTO.Response saveRole(AddRoleDTO.Request request){
        if(Strings.isNullOrEmpty(request.getName())){
            throw new ApiRequestException("BAD_REQUEST");
        }

        if (roleRepository.findRoleByName(request.getName()).isPresent()) {
            throw new ApiRequestException("ROLE_ALREADY_EXIST");
        }

        Role role = new Role();
        role.setName(request.getName());
        roleRepository.save(role);
        return new AddRoleDTO.Response(role.getId());
    }

    @Override
    public GetRoleDTO.Response findById(UUID id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isEmpty()){
            throw new ApiRequestException("ROLE_NOT_FOUND");
        }
        Role role = roleOptional.get();
        return new GetRoleDTO.Response(role.getId(), role.getName());
    }

    @Override
    public List<GetRoleDTO.Response> findAll() {
        return roleRepository.findAll().stream().map(r -> new GetRoleDTO.Response(r.getId(),r.getName())).collect(Collectors.toList());
    }
}
