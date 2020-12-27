package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserService {
    long create(UserDto userDto);
    UserDto getById(long id);
    User getByName(String name);
    List<UserDto> getAll(Integer page);
}
