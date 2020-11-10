package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class TagDto {
    private String id;
    @NotNull(message = "{message.nameNotNull}")
    @Size(min = 1, max = 20, message = "{message.nameSize}")
    private String name;
}
