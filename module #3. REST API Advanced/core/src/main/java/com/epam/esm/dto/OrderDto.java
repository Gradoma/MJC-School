package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long id;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private Long userId;
    @NotNull
    @Digits(integer = 20, fraction = 0)
    private Long certificateId;
    private Double cost;
    private String purchase_date;
}
