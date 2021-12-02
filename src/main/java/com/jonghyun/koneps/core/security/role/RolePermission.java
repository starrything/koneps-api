package com.jonghyun.koneps.core.security.role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@IdClass(RolePermissionKey.class)
@Table(name = "a_role_permission")
public class RolePermission {
    @Id
    @Column(name = "role", insertable = false, updatable = false)
    String roleName;

    @Id
    @Column(name = "permission")
    String permission;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "role", insertable = false, updatable = false)
    Role role;

    @Builder
    public RolePermission(String roleName, String permission, Role role) {
        this.roleName = roleName;
        this.permission = permission;
        this.role = role;
    }
}
