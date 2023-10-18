package com.asiansigmatechnology.test.blog;

import com.asiansigmatechnology.test.blog.dto.AddBlogDTO;
import com.asiansigmatechnology.test.blog.dto.InquireBlogDTO;
import com.asiansigmatechnology.test.user.User;
import com.asiansigmatechnology.test.user.UserRepository;
import com.google.common.base.Strings;
import com.asiansigmatechnology.test.exception.ApiRequestException;
import com.asiansigmatechnology.test.blog.dto.GetBlogDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public AddBlogDTO.Response saveBlog(UUID userId, AddBlogDTO.Request addBlogDTO){
        if(Strings.isNullOrEmpty(addBlogDTO.getTitle())
        || Strings.isNullOrEmpty(addBlogDTO.getBody())){
            throw new ApiRequestException(ApiRequestException.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new ApiRequestException(ApiRequestException.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Blog blog = new Blog();
        User user = userOptional.get();
        blog.setUser(user);
        blog.setTitle(addBlogDTO.getTitle());
        blog.setBody(addBlogDTO.getBody());
        blog.setAuthor(user.getName());
        blogRepository.save(blog);
        return new AddBlogDTO.Response(blog.getId());
    }

    @Override
    public GetBlogDTO.Response getUserBlog(UUID userId){
        Optional<Blog> blogByUserId = blogRepository.findBlogByUserId(userId);
        if(blogByUserId.isEmpty()){
            throw new ApiRequestException("BLOG_NOT_EXIST");
        }

        Blog blog = blogByUserId.get();
        GetBlogDTO.BlogDTO blogDTO = new GetBlogDTO.BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setBody(blog.getBody());
        blogDTO.setAuthor(blog.getAuthor());
        return new GetBlogDTO.Response(blogDTO);
    }

    @Override
    public GetBlogDTO.Response getBlogById(UUID id) {
        Optional<Blog> blogById = blogRepository.findBlogById(id);
        if(blogById.isEmpty()){
            throw new ApiRequestException("BLOG_NOT_EXIST");
        }
        Blog blog = blogById.get();
        GetBlogDTO.BlogDTO blogDTO = new GetBlogDTO.BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setBody(blog.getBody());
        blogDTO.setAuthor(blog.getAuthor());
        return new GetBlogDTO.Response(blogDTO);
    }

    @Override
    public InquireBlogDTO.Response getAllBlog(InquireBlogDTO.Request request){
        Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
        Page<Blog> allBlog = blogRepository.findAllBlog(pageable);
        InquireBlogDTO.Response response = new InquireBlogDTO.Response();
        response.setPageIndex(request.getPageIndex());
        response.setPageSize(request.getPageSize());
        response.setTotalData(allBlog.getTotalElements());
        response.setData(allBlog.getContent().stream().map(blog -> new GetBlogDTO.BlogDTO(blog.getId(), blog.getTitle(), blog.getBody(), blog.getAuthor())).toArray(GetBlogDTO.BlogDTO[]::new));
        return response;
    }

    @Override
    public void updateBlog(UUID userId, UUID blogId, AddBlogDTO.Request addBlogDTO){
        if(Strings.isNullOrEmpty(addBlogDTO.getTitle())
                || Strings.isNullOrEmpty(addBlogDTO.getBody())){
            throw new ApiRequestException(ApiRequestException.BAD_REQUEST);
        }

        Optional<Blog> blogOptional = blogRepository.findBlogByIdAndUserId(blogId, userId);
        if(blogOptional.isEmpty()){
            throw new ApiRequestException(ApiRequestException.BAD_REQUEST);
        }

        Blog blog = blogOptional.get();
        blog.setTitle(addBlogDTO.getTitle());
        blog.setBody(addBlogDTO.getBody());
        blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(UUID userId, UUID blogId){
        Optional<Blog> blogOptional = blogRepository.findBlogByIdAndUserId(blogId, userId);
        if(blogOptional.isEmpty()){
            throw new ApiRequestException(ApiRequestException.BAD_REQUEST);
        }
        blogRepository.deleteById(blogId, userId);
    }
}
