package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GiftCertificateDto {
    private String id;
    private String name;
    private String[] tags;  //todo (list Tag)
    private String description;
    private String price;
    private String createDate;
    private String lastUpdateDate;
    private String duration;
}
