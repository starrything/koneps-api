package com.jonghyun.koneps.global.security.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
/*@SequenceGenerator(
        name="LOGIN_HISTORY_GEN", //시퀀스 제너레이터 이름
        sequenceName="login_history_seq", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)//Oracle*/
@Table(name = "a_login_history")
public class LoginHistory {
    @Id
    @Column(name = "log_id")
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)// case: Long type*/
    /*@GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "LOGIN_HISTORY_GEN")//Oracle*/
    String logId;

    @Column(name = "user_id")
    String userId;

    @Column(name = "access_addr")
    String accessAddr;

    @Column(name = "connect_method")
    String connectMethod;

    @Column(name = "request_error")
    String requestError;

    @Column(name = "requested_api")
    String requestedApi;

    @CreatedDate
    @Column(name = "creation_dt")
    LocalDateTime creationDate;

    @Builder
    public LoginHistory(String logId, String userId, String accessAddr, String connectMethod, String requestError, String requestedApi, LocalDateTime creationDate) {
        this.logId = logId;
        this.userId = userId;
        this.accessAddr = accessAddr;
        this.connectMethod = connectMethod;
        this.requestError = requestError;
        this.requestedApi = requestedApi;
        this.creationDate = creationDate;
    }
}
