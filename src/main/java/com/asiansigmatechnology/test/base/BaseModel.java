package com.asiansigmatechnology.test.base;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public class BaseModel {
    @Id
    @Getter(AccessLevel.PUBLIC)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    protected UUID id;

    @CreationTimestamp
    @Getter(AccessLevel.PUBLIC)
    @Column(name = "created_at")
    protected Date createdAt;

    @UpdateTimestamp
    @Getter(AccessLevel.PUBLIC)
    @Column(name = "updated_at")
    protected Date updatedAt;

    @Getter(AccessLevel.PUBLIC)
    @Column(name = "deleted_at")
    protected Date deletedAt;

}
