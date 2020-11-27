package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDto{
    private Long id;
    @NotNull
    @Size(min = 1, max = 40)
    private String name;
    @NotNull
    @Valid
    private List<TagDto> tags;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Double price;
    private String createDate;      //todo custom annotation validation
    private String lastUpdateDate;
    @NotNull
    @Min(1)
    private Long duration;
}
