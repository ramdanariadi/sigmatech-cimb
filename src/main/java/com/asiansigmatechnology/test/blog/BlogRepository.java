package com.asiansigmatechnology.test.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogRepository extends CrudRepository<Blog, UUID> {

    @Query("SELECT b FROM Blog b WHERE b.id = :id AND b.deletedAt IS NULL")
    public Optional<Blog> findBlogById(@Param("id") UUID id);

    @Query("SELECT b FROM Blog b WHERE b.user.id = :userId AND b.deletedAt IS NULL")
    public Optional<Blog> findBlogByUserId(@Param("userId") UUID userID);

    @Query("SELECT b FROM Blog b WHERE b.id = :id AND b.user.id = :userId AND b.deletedAt IS NULL")
    public Optional<Blog> findBlogByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

    @Query( value = "SELECT b FROM Blog b WHERE b.deletedAt IS NULL",
    countQuery = "SELECT COUNT(*) From Blog b WHERE b.deletedAt IS NULL")
    public Page<Blog> findAllBlog(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE FROM Blog b set b.deletedAt = NOW() WHERE b.id = :id AND b.user.id = :userId")
    public void deleteById(@Param("id") UUID id, @Param("userId") UUID userId);
}
