package com.epam.esm.security;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.mapper.JwtUserMapper;
import com.epam.esm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.getByName(userName);
        JwtUser jwtUser = JwtUserMapper.toJwtUser(user);
        log.info("jwtUser=" + jwtUser);
        return jwtUser;
    }
}
