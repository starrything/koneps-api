package com.jonghyun.koneps.domain.system;

import com.jonghyun.koneps.global.security.role.Role;
import com.jonghyun.koneps.global.security.role.RoleDto;
import com.jonghyun.koneps.global.security.role.RoleRepository;
import com.jonghyun.koneps.global.security.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public String createRole(RoleDto roleDto) {
        Role newRole = new Role();
        newRole.newRole(roleDto.getRole(),
                roleDto.getCreatedBy(),
                LocalDateTime.now());

        roleRepository.save(newRole);

        return "created";
    }

    @Override
    public List<RoleDto> getRoleList() {
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleDto(role.getRole(), role.getCreatedBy(), role.getCreationDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> searchRoleList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();

        roleRepository.findAll(Sort.by(Sort.Direction.ASC, "role"))
                .stream()
                .forEach(role -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("action", "");
                    map.put("role", role.getRole());

                    result.add(map);
                });

        return result;
    }

    @Override
    @Transactional
    public boolean deleteRole(String role) {
        roleRepository.deleteById(role);
        return true;
    }

    @Override
    public List<Map<String, Object>> getUserListByRole(String role) {
        List<Map<String, Object>> result = new ArrayList<>();

        userRoleRepository.findByRole(role)
                .stream()
                .forEach(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("role", c.getRole());
                    map.put("username", c.getUsername());
                    map.put("firstName", c.getUser().getFirstName());
                    map.put("lastName", c.getUser().getLastName());

                    result.add(map);
                });

        return result;
    }
}
