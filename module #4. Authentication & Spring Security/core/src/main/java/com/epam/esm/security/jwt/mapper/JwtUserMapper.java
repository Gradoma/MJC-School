package com.epam.esm.security.jwt.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserMapper {
    public static JwtUser toJwtUser(User user){
        return new JwtUser(user.getId(), user.getName(), user.getPassword(), mapRole(user.getRole()));
    }

    private static GrantedAuthority mapRole(Role role){
        return new SimpleGrantedAuthority(role.getRoleName());
    }
}
