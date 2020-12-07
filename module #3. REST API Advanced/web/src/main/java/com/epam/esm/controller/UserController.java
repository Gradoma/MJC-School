package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = UserController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    public static final String URL = "/users";
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id){
        UserDto userDto = userService.getById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(value = "page", required = false,
            defaultValue = "1") Integer page){
        List<UserDto> userDtoList = userService.getAll(page);
        return ResponseEntity.ok().body(userDtoList);
    }
}
