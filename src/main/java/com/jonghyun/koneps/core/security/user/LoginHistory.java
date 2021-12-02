package com.jonghyun.koneps.core.security.user;

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

    @Column(name = "connect_id")
    String connectId;

    @Column(name = "connect_ip")
    String connectIp;

    @Column(name = "connect_method")
    String connectMethod;

    @Column(name = "connect_error")
    String connectError;

    @Column(name = "connect_url")
    String connectUrl;

    @CreatedDate
    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Builder
    public LoginHistory(String logId, String connectId, String connectIp, String connectMethod, String connectError, String connectUrl, LocalDateTime creationDate) {
        this.logId = logId;
        this.connectId = connectId;
        this.connectIp = connectIp;
        this.connectMethod = connectMethod;
        this.connectError = connectError;
        this.connectUrl = connectUrl;
        this.creationDate = creationDate;
    }
}
