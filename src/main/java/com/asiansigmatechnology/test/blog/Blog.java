package com.asiansigmatechnology.test.blog;

import com.asiansigmatechnology.test.base.BaseModel;
import com.asiansigmatechnology.test.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "blog")
@NoArgsConstructor
@Data
public class Blog extends BaseModel {
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "body")
    private String body;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
