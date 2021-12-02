package com.jonghyun.koneps.api.system;

import com.jonghyun.koneps.core.security.AuthToken;
import com.jonghyun.koneps.core.security.user.UserDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface UserService {
    @Modifying
    @Transactional
    ResponseEntity<UserDto> createUser(UserDto userDto);

    List<UserDto> getUserList();

    ResponseEntity<AuthToken> login(UserDto userDto, HttpSession session, HttpServletRequest request);

    void logout(HttpSession session);

    ResponseEntity<String> resetPassword(UserDto userDto);

    @Modifying
    @Transactional
    ResponseEntity<UserDto> updateUser(UserDto userDto);

    List<Map<String, Object>> searchUserList(String keyword);

    @Modifying
    @Transactional
    boolean deleteUser(String username);

    ResponseEntity<UserDto> getUserDetail(String username);

    @Modifying
    @Transactional
    ResponseEntity<UserDto> editUser(UserDto userDto);
}
