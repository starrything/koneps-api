package com.jonghyun.koneps.domain.system.user;

import com.jonghyun.koneps.global.security.AuthToken;
import com.jonghyun.koneps.global.security.user.UserDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface UserService {
    @Modifying
    ResponseEntity<UserDto> createUser(UserDto userDto);

    List<UserDto> getUserList();

    ResponseEntity<AuthToken> login(UserDto userDto, HttpSession session, HttpServletRequest request);

    void logout(HttpSession session);

    ResponseEntity<String> resetPassword(UserDto userDto);

    @Modifying
    ResponseEntity<UserDto> updateUser(UserDto userDto);

    List<Map<String, Object>> searchUserList(String keyword);

    @Modifying
    boolean deleteUser(String username);

    ResponseEntity<UserDto> getUserDetail(String username);

    @Modifying
    ResponseEntity<UserDto> editUser(UserDto userDto);

    ResponseEntity<UserDto> registerUser(UserDto userDto);
}
