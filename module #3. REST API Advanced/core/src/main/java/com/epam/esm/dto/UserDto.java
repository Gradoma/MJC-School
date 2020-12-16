package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserDto {
    @NotNull
    @Digits(integer = 20, fraction = 0)
    @Positive
    private Long id;
    @NotNull
    @Size(max = 45)
    private String name;
}
