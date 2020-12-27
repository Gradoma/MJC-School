package com.epam.esm.config;

import com.epam.esm.entity.RoleName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String[] GUESTS_URL = {"/auth/login"};
    private final String[] USER_URL = {"/orders/**"};
    private final String[] ADMIN_URL = {"/tags/**"};
    private final JwtTokenProvider tokenProvider;


    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/certificates/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(GUESTS_URL).permitAll()

                .antMatchers(HttpMethod.POST, USER_URL).hasRole(RoleName.USER.name())
                .antMatchers(HttpMethod.GET, USER_URL).hasRole(RoleName.USER.name())

                .antMatchers(HttpMethod.GET, "/users/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(ADMIN_URL).hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/certificates/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/certificates/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.PATCH, "/certificates/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/certificates/**").hasRole(RoleName.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(tokenProvider));
    }
}
