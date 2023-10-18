package com.asiansigmatechnology.test.blog;

import com.asiansigmatechnology.test.blog.dto.AddBlogDTO;
import com.asiansigmatechnology.test.blog.dto.GetBlogDTO;
import com.asiansigmatechnology.test.blog.dto.InquireBlogDTO;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/blog")
@AllArgsConstructor
public class BlogController {

    private BlogService blogService;

    @PostMapping
    public ResponseEntity<AddBlogDTO.Response> addBlog(HttpServletRequest request, @RequestBody AddBlogDTO.Request blog){
        JsonObject userCustomId = new JsonObject(request.getHeader("x-custom-id"));
        AddBlogDTO.Response response = blogService.saveBlog(UUID.fromString(userCustomId.getString("userId")), blog);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<InquireBlogDTO.Response> getBlog(InquireBlogDTO.Request request){
        InquireBlogDTO.Response response = blogService.getAllBlog(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBlogDTO.Response> getBlogById(@PathVariable("id") UUID id){
        GetBlogDTO.Response blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBlog(HttpServletRequest request, @PathVariable("id") UUID id, @RequestBody AddBlogDTO.Request blog){
        JsonObject userCustomId = new JsonObject(request.getHeader("x-custom-id"));
        blogService.updateBlog(UUID.fromString(userCustomId.getString("userId")), id, blog);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(HttpServletRequest request, @PathVariable("id") UUID id){
        JsonObject userCustomId = new JsonObject(request.getHeader("x-custom-id"));
        blogService.deleteBlog(UUID.fromString(userCustomId.getString("userId")), id);
        return ResponseEntity.ok().build();
    }

}
