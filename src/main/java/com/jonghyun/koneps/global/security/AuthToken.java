package com.jonghyun.koneps.global.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@Getter
public class AuthToken {
    /*
     * Login Information from User Entity
     * */
    private String username;
    private String firstName;
    private String lastName;
    private String tel;
    private String email;
    private String address1;
    private String address2;
    private String loginId;
    private String loginCompCd;
    private String loginCompNm;
    private String loginDeptCd;
    private String loginDeptNm;
    private String loginJobDutCd;
    private String loginJobDutNm;
    private String loginJobGradeCd;
    private String loginJobGradeNm;
    private String token;
    private Collection authorities;
    private int isValid;

    @Builder
    public AuthToken(String username,
                     String firstName,
                     String lastName,
                     String tel,
                     String email,
                     String address1,
                     String address2,
                     String token,
                     Collection authorities,
                     int isValid) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.token = token;
        this.authorities = authorities;
        this.isValid = isValid;
    }
    /*@Builder
    public AuthToken(String userName,
                     String loginId,
                     String loginCompCd,
                     String loginCompNm,
                     String loginDeptCd,
                     String loginDeptNm,
                     String loginJobDutCd,
                     String loginJobDutNm,
                     String loginJobGradeCd,
                     String loginJobGradeNm,
                     String token,
                     Collection authorities) {
        this.userName = userName;
        this.loginId = loginId;
        this.loginCompCd = loginCompCd;
        this.loginCompNm = loginCompNm;
        this.loginDeptCd = loginDeptCd;
        this.loginDeptNm = loginDeptNm;
        this.loginJobDutCd = loginJobDutCd;
        this.loginJobDutNm = loginJobDutNm;
        this.loginJobGradeCd = loginJobGradeCd;
        this.loginJobGradeNm = loginJobGradeNm;
        this.token = token;
        this.authorities = authorities;
    }*/
}
