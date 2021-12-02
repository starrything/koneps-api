package com.jonghyun.koneps.core.security.role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RolePermissionKey implements Serializable {
    private String role;
    private String permission;

    @Builder
    public RolePermissionKey(String role, String permission) {
        this.role = role;
        this.permission = permission;
    }
}
