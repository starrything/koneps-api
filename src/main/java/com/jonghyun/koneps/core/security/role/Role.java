package com.jonghyun.koneps.core.security.role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "a_role")
public class Role {
    @Id
    @Column(name = "role")
    String role;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_dt")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_dt")
    LocalDateTime modifiedDate;

    @OneToMany
    @JoinColumn(name = "role")
    List<RolePermission> permissionList;

    @Builder
    public Role(String role) {
        this.role = role;
    }

    public Role newRole(String role, String createdBy, LocalDateTime creationDate) {
        this.role = role;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }
}
