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
    private static final long USER_ID = 2;
    private static final long ADMIN_ID = 3;

    public Role(RoleName roleName){
        this.roleName = roleName.name();
        switch (roleName){
            case USER:
                this.id = USER_ID;
                break;
            case ADMIN:
                this.id = ADMIN_ID;
                break;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = RoleTableConst.ROLE, length = 45)
    private String roleName;
}
