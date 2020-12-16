package com.epam.esm.dto;

import com.epam.esm.dto.method.DescriptionPatch;
import com.epam.esm.dto.method.DurationPatch;
import com.epam.esm.dto.method.NamePatch;
import com.epam.esm.dto.method.New;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDto{
    private Long id;
    @NotNull(groups = {New.class, NamePatch.class})
    @Size(groups = {New.class, NamePatch.class}, min = 1, max = 40)
    private String name;
    @NotNull(groups = {New.class})
    @Valid
    private List<TagDto> tags;
    @NotNull(groups = {New.class, DescriptionPatch.class})
    @Size(groups = {New.class, DescriptionPatch.class}, min = 1, max = 255)
    private String description;
    @NotNull(groups = {New.class})
    @Digits(groups = {New.class}, integer = 5, fraction = 2)
    private Double price;
    private String createDate;
    private String lastUpdateDate;
    @NotNull(groups = {New.class, DurationPatch.class})
    @Min(groups = {New.class, DurationPatch.class}, value = 1)
    private Long duration;
}
