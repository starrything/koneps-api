package com.jonghyun.koneps.global.security.role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserRoleKey implements Serializable {
    String username;
    String role;

    @Builder
    public UserRoleKey(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
