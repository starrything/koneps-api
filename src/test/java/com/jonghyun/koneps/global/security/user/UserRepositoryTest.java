package com.jonghyun.koneps.global.security.user;

import com.jonghyun.koneps.domain.system.UserService;
import com.jonghyun.koneps.global.security.role.UserRole;
import com.jonghyun.koneps.global.security.role.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class UserRepositoryTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @Test
    public void getUser() {
        List<UserRole> roles = userRoleRepository.findAll();

        assertNotNull(roles);
    }
}
