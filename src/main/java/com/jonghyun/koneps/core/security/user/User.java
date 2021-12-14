package com.jonghyun.koneps.core.security.user;

import com.jonghyun.koneps.core.security.role.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "A_USER")
public class User {
    @Id
    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "tel")
    String tel;

    @Column(name = "email")
    String email;

    @Column(name = "address1")
    String address1;

    @Column(name = "address2")
    String address2;

    @Column(name = "is_active")
    int isActive;

    @Column(name = "attribute1")
    String attribute1;

    @Column(name = "attribute2")
    String attribute2;

    @Column(name = "attribute3")
    String attribute3;

    @Column(name = "attribute4")
    String attribute4;

    @Column(name = "attribute5")
    String attribute5;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @OneToMany
    @JoinColumn(name = "username")
    List<UserRole> roles;

    @Builder
    public User updateUserRoles(List<UserRole> roles) {
        this.roles = roles;

        return this;
    }

    @Builder
    public User updateAttribute1(String attribute1) {
        this.attribute1 = attribute1;

        return this;
    }

    public User updateUserDetail(String username, String firstName, String lastName, String tel, String email, int isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.isActive = isActive;

        return this;
    }

    public User updatePersonalUserInfo(String firstName, String lastName, String tel, String email, String address1, String address2) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;

        return this;
    }

    public User editUser(String firstName, String lastName, String tel, String email, String address1, String address2, int isActive, String modifiedBy, LocalDateTime modifiedDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.isActive = isActive;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }

    public User resetPassword(String password) {
        this.password = password;

        return this;
    }

    @Builder
    public User newUser(String username, String password, String firstName, String lastName, String tel, String email, List<UserRole> roles, String address1, String address2, int isActive, String createdBy, LocalDateTime creationDate) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.roles = roles;
        this.address1 = address1;
        this.address2 = address2;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }
}
