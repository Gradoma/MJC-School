package com.epam.esm.entity;

import com.epam.esm.dao.column.RoleTableConst;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = RoleTableConst.TABLE_ROLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = RoleTableConst.ROLE, length = 45)
    private String roleName;
}
