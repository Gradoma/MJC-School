package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDto {
    private String id;
    @NotNull
    @Size(min = 1, max = 40)
    private String name;
    @NotNull
    private List<TagDto> tags;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    @NotNull
    @Pattern(regexp = "^((\\p{Digit}){1,5}([.]\\d{1,2})?)$")
    private String price;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private String createDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private String lastUpdateDate;
    @NotNull
    @Pattern(regexp = "\\d+")
    private String duration;
}
