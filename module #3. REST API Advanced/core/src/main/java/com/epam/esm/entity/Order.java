package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Order {
    private long id;
    private long userId;
    private long certificateId;
    private Double cost;
    private ZonedDateTime purchaseDate;
}
