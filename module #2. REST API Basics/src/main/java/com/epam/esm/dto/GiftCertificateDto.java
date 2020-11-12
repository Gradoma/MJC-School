package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDto implements Comparable<GiftCertificateDto>{
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
    private String createDate;
    private String lastUpdateDate;
    @NotNull
    @Pattern(regexp = "\\d+")
    private String duration;

    @Override
    public int compareTo(GiftCertificateDto o) {
        return name.compareTo(o.getName());
    }
}
