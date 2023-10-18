package com.asiansigmatechnology.test.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

public class GetRoleDTO {

    @Data
    @AllArgsConstructor
    public static class Response{
        private UUID id;
        private String role;
    }
}
