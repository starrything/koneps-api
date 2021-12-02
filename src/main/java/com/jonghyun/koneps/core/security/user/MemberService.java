package com.jonghyun.koneps.core.security.user;

import com.jonghyun.koneps.core.security.role.UserRole;
import com.jonghyun.koneps.core.security.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(!user.isPresent()) {
            throw new UsernameNotFoundException(username + "is not found.");
        }

        Member member = new Member();
        member.setUsername(user.get().getUsername());
        member.setPassword(user.get().getPassword());
        member.setAuthorities(getAuthorities(username));
        member.setEnabled(true);
        member.setAccountNonExpired(true);
        member.setAccountNonLocked(true);
        member.setCredentialsNonExpired(true);

        return member;
    }

    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<UserRole> authList = userRoleRepository.findRolesByUser_username(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getRole()));
        }

        return authorities;
    }
}
