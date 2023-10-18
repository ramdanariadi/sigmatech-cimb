package com.asiansigmatechnology.test.role;

import com.asiansigmatechnology.test.role.dto.AddRoleDTO;
import com.asiansigmatechnology.test.role.dto.GetRoleDTO;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
@AllArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    @PostMapping
    public ResponseEntity<Object> saveRole(@RequestBody AddRoleDTO.Request request){
        AddRoleDTO.Response response = roleServiceImpl.saveRole(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllRole(){
        JsonObject result = new JsonObject();
        result.put("data", roleServiceImpl.findAll());
        return ResponseEntity.ok(result.getMap());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetRoleDTO.Response> getRoleById(@PathVariable("id") UUID id){
        GetRoleDTO.Response byId = roleServiceImpl.findById(id);
        return ResponseEntity.ok(byId);
    }
}
