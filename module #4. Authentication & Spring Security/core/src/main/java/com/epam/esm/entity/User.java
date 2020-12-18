package com.epam.esm.entity;

import com.epam.esm.dao.column.UserTableConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = UserTableConst.PASSWORD)
    private String password;

    @Column(name = UserTableConst.CREATE_DATE)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name=UserTableConst.ROLE_ID)
    private Role role;
}
