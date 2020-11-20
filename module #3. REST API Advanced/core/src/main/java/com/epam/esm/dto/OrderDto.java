package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OrderDto {
    private String id;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private String user_id;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private String certificate_id;
    private String cost;
    private String purchase_date;   //todo custom annotation validation
}
