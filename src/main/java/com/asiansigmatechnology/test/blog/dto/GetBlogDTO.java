package com.asiansigmatechnology.test.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class GetBlogDTO {

    @AllArgsConstructor
    @Data
    public static class Response{
        private BlogDTO data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlogDTO {
        private UUID id;
        private String title;
        private String body;
        private String author;
    }
}
