package com.jonghyun.koneps.core.config;

import com.jonghyun.koneps.core.security.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 86400)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("com.jonghyun.koneps.core.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private MemberService memberService;

    @Autowired
    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/login", "/logout").permitAll()
                .antMatchers("/api/user/**", "/api/role/**").permitAll()
                .antMatchers("/docs/api-guide.html").permitAll()
                .antMatchers("/api/code").permitAll()
                .antMatchers("/api/interface/**/**").permitAll()
                .antMatchers("/api/g2b/**/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and().headers().frameOptions().sameOrigin()
        ;
    }
}
