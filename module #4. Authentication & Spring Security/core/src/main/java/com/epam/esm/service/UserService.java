package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserService {
    long create(UserDto userDto);
    UserDto getById(long id);
    List<UserDto> getAll(Integer page);
}
