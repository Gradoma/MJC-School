package com.epam.esm.service.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoMapper {
    private static final Logger log = LogManager.getLogger();

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
}
