package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    private String id;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private String userId;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private String certificateId;
    private String cost;
    private String purchase_date;   //todo custom annotation validation
}
