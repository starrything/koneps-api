package com.jonghyun.koneps.global.security.role;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RoleDto implements Serializable {
    String role;
    String createdBy;
    LocalDateTime creationDate;

    public RoleDto(String role, String createdBy, LocalDateTime creationDate) {
        this.role = role;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
    }
}
