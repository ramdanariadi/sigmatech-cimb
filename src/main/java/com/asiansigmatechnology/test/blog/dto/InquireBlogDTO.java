package com.asiansigmatechnology.test.blog.dto;

import lombok.Data;

public class InquireBlogDTO {

    @Data
    public static class Request{
        private Integer pageIndex = 0;
        private Integer pageSize = 10;
    }

    @Data
    public static class Response{
        private GetBlogDTO.BlogDTO[] data;
        private Long totalData;
        private Integer pageIndex;
        private Integer pageSize;
    }
}
