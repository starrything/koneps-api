package com.jonghyun.koneps.global.security.role;

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
    @Column(name = "permission_cd")
    String permissionCd;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_dt")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_dt")
    LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "role", insertable = false, updatable = false)
    Role role;

    @Builder
    public RolePermission(String roleName, String permissionCd, Role role) {
        this.roleName = roleName;
        this.permissionCd = permissionCd;
        this.role = role;
    }
}
