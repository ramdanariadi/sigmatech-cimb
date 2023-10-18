package com.asiansigmatechnology.test.role;

import com.asiansigmatechnology.test.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role extends BaseModel {
    private String name;
}
