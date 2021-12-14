package com.jonghyun.koneps.core.security.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {
    String username;
    String password;
    int isActive;
    String firstName;
    String lastName;
    String tel;
    String email;
    String address1;
    String address2;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    List<String> roles;
    String token;

    String logId;
    String connectId;
    String connectIp;
    //W:web, M:Mobile
    String connectMethod;
    //error msg
    String connectError;
    String connectUrl;
    LocalDateTime creationDate;

    /* Sign up page */
    String name;

    public UserDto(String username, String password, String firstName, String lastName, String tel, String email, String address1, String address2, List<String> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.roles = roles;
    }

    public UserDto() {

    }

    public UserDto editUser(String username, int isActive, String firstName, String lastName, String tel, String email, String address1, String address2) {
        this.username = username;
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;

        return this;
    }
}
