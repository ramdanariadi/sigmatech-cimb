package com.asiansigmatechnology.test.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

public class AddRoleDTO {

    @Data
    public static class Request{
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Response{
        private UUID id;
    }
}
