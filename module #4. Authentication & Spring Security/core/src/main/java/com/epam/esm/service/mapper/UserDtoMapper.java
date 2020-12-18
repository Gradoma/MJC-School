package com.epam.esm.service.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoMapper {

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public List<UserDto> toDto(List<User> userList){
        List<UserDto> resultList = new ArrayList<>();
        userList.forEach(user -> resultList.add(toDto(user)));
        return resultList;
    }

    public User toEntity(UserDto userDto){
        User user = new User();
        user.setCreateDate(LocalDateTime.now());
        user.setName(userDto.getName());
        return user;
    }
}
