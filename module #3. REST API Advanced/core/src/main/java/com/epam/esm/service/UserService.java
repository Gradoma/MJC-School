package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(long id);
    List<UserDto> getAll(Integer page);
}
