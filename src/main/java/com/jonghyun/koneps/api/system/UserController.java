package com.jonghyun.koneps.api.system;

import com.jonghyun.koneps.core.security.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registration(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }
    @PutMapping
    public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto) {
        return userService.editUser(userDto);
    }

    @GetMapping("/list")
    public List<UserDto> getUserList() {
        return userService.getUserList();
    }

    /**
     * Subject : User 찾기
     */
    @GetMapping("/search")
    public List<Map<String, Object>> searchUserList(@RequestParam String keyword) {
        return userService.searchUserList(keyword);
    }

    @GetMapping("/detail")
    public ResponseEntity<UserDto> getUserDetail(@RequestParam String username) {
        return userService.getUserDetail(username);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserDto userDto) {
        return userService.resetPassword(userDto);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping
    public boolean deleteUser(@RequestParam String username) {
        return userService.deleteUser(username);
    }

}
