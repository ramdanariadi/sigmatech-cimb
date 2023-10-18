package com.asiansigmatechnology.test.user.dto;

import lombok.Data;

import java.util.UUID;

public class RegisterDTO {

    @Data
    public static class Request{
        private String username;
        private String password;
        private String name;
    }

    @Data
    public static class Response{
        private String accessToken;
        private String refreshToken;
        private UUID userId;
    }
}
