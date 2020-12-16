package com.epam.esm.entity;

import com.epam.esm.dao.column.OrdersTableConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = OrdersTableConst.TABLE_ORDERS)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = OrdersTableConst.USER_ID)
    private long userId;
    @Column(name = OrdersTableConst.CERTIFICATE_ID)
    private long certificateId;
    @Column(name = OrdersTableConst.COST)
    private Double cost;
    @Column(name = OrdersTableConst.PURCHASE_TIME)
    private ZonedDateTime purchaseDate;
}
