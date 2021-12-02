package com.jonghyun.koneps.core.security.role;

import com.jonghyun.koneps.core.security.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "A_USER_ROLE")
@IdClass(UserRoleKey.class)
public class UserRole {
    @Id
    @Column(name = "username", insertable = false, updatable = false)
    String username;

    @Id
    @Column(name = "role")
    String role;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    User user;

    @Builder
    public UserRole(String role, User user) {
        this.role = role;
        this.user = user;
    }

    public UserRole newUserRole(String username, String role, String createdBy, LocalDateTime creationDate) {
        this.username = username;
        this.role = role;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }

    public UserRole updateRole(String role) {
        this.role = role;

        return this;
    }
}
