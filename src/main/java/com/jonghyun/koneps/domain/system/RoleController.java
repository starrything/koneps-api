package com.jonghyun.koneps.domain.system;

import com.jonghyun.koneps.global.security.role.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public String createRole(@RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @GetMapping("/list")
    public List<RoleDto> getRoleList() {
        return roleService.getRoleList();
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchRoleList(@RequestParam String keyword) {
        return roleService.searchRoleList(keyword);
    }

    @DeleteMapping
    public boolean deleteRole(@RequestParam String role) {
        return roleService.deleteRole(role);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getUserListByRole(@RequestParam String role) {
        return roleService.getUserListByRole(role);
    }
}
