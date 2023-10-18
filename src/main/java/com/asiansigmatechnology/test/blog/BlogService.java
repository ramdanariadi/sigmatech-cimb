package com.asiansigmatechnology.test.blog;

import com.asiansigmatechnology.test.blog.dto.AddBlogDTO;
import com.asiansigmatechnology.test.blog.dto.GetBlogDTO;
import com.asiansigmatechnology.test.blog.dto.InquireBlogDTO;

import java.util.UUID;

public interface BlogService {
    public AddBlogDTO.Response saveBlog(UUID userId, AddBlogDTO.Request addBlogDTO);
    public GetBlogDTO.Response getUserBlog(UUID userId);
    public GetBlogDTO.Response getBlogById(UUID id);
    public InquireBlogDTO.Response getAllBlog(InquireBlogDTO.Request request);
    public void updateBlog(UUID userId, UUID blogId, AddBlogDTO.Request addBlogDTO);
    public void deleteBlog(UUID userId, UUID blogId);
}
