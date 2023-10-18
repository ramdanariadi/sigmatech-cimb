package com.asiansigmatechnology.test.blog;

import com.asiansigmatechnology.test.blog.dto.AddBlogDTO;
import com.asiansigmatechnology.test.blog.dto.GetBlogDTO;
import com.asiansigmatechnology.test.exception.ApiRequestException;
import com.asiansigmatechnology.test.role.RoleRepository;
import com.asiansigmatechnology.test.user.User;
import com.asiansigmatechnology.test.user.UserRepository;
import com.asiansigmatechnology.test.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    BlogService blogService;
    @Mock
    BlogRepository blogRepository;

    UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        blogService = new BlogServiceImpl(blogRepository, userRepository);
        userServiceImpl = new UserServiceImpl(userRepository, roleRepository, encoder);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findById() {
        // given
        UUID uuid = UUID.fromString("0b589615-f910-11eb-936c-41a335bdee2c");
        Blog blog = new Blog();
        blog.setId(uuid);
        blog.setTitle("blog title");
        blog.setBody("blog body");

        // when
        Mockito.when(blogRepository.findBlogById(uuid)).thenReturn(Optional.of(blog));
        GetBlogDTO.Response blog1 = blogService.getBlogById(uuid);

        // then
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(blogRepository, times(1)).findBlogById(argumentCaptor.capture());
        Assertions.assertEquals(0, argumentCaptor.getValue().compareTo(blog1.getData().getId()));
    }

    @Test
    void addCategory() {
        // given
        AddBlogDTO.Request request = new AddBlogDTO.Request("Blog title", "body");
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Admin Blog");

        // when
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        blogService.saveBlog(userId, request);

        // then
        ArgumentCaptor<Blog> argumentCaptor = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Assertions.assertEquals(0, argumentCaptor.getValue().getUser().getId().compareTo(userId));
    }

    @Test
    void addCategoryShouldThrownError(){
        // given
        AddBlogDTO.Request request = new AddBlogDTO.Request("", "body");
        UUID userId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(() -> blogService.saveBlog(userId, request))
                .isInstanceOf(ApiRequestException.class)
                .hasMessage(ApiRequestException.BAD_REQUEST);
    }

    @Test
    void updateCategory(){
        // given
        UUID userId = UUID.randomUUID();
        UUID blogId = UUID.randomUUID();
        AddBlogDTO.Request request = new AddBlogDTO.Request("I love java", "java is the best programming language for backend");

        //when
        Blog blog = new Blog();
        blog.setId(blogId);
        Mockito.when(blogRepository.findBlogByIdAndUserId(blogId, userId)).thenReturn(Optional.of(blog));
        blogService.updateBlog(userId, blogId, request);

        //then
        ArgumentCaptor<Blog> acABR = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository, Mockito.times(1)).save(acABR.capture());

        Blog value = acABR.getValue();
        Assertions.assertEquals(value.getId(), blogId);
    }

    @Test
    void updateCategoryShouldThrownError(){
        // given
        UUID userId = UUID.randomUUID();
        UUID blogId = UUID.randomUUID();
        AddBlogDTO.Request request = new AddBlogDTO.Request("I love java", "");

        //when
        //then
        assertThatThrownBy(() -> blogService.updateBlog(userId, blogId, request))
                .isInstanceOf(ApiRequestException.class)
                .hasMessage(ApiRequestException.BAD_REQUEST);
    }

    @Test
    void destroyCategory(){
        // given
        UUID userId = UUID.randomUUID();
        UUID blogId = UUID.randomUUID();

        //when
        Mockito.when(blogRepository.findBlogByIdAndUserId(blogId, userId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> blogService.deleteBlog(userId, blogId))
                .isInstanceOf(ApiRequestException.class)
                .hasMessage(ApiRequestException.BAD_REQUEST);
    }
}