package com.epam.esm.entity;

import com.epam.esm.dao.column.UserTableConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = UserTableConst.TABLE_USER)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = UserTableConst.NAME, length = 45)
    private String name;
}
