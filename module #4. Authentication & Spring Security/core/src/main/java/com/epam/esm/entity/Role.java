package com.epam.esm.entity;

import com.epam.esm.dao.column.RoleTableConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = RoleTableConst.TABLE_ROLE)
public class Role {
    public Role(String roleName){
        this.roleName = roleName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = RoleTableConst.ROLE, length = 45)
    private String roleName;
}
