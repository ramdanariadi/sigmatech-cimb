package com.asiansigmatechnology.test.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

public class AddBlogDTO {

    @Data
    @AllArgsConstructor
    public static class Request{
        private String title;
        private String body;
    }

    @AllArgsConstructor
    @Data
    public static class Response{
        private UUID id;
    }
}
