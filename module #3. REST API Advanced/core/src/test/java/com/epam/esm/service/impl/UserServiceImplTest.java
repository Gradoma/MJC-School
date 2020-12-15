package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @Test
    void getById() {
        long id = 5;

        User user = new User();
        user.setId(id);
        user.setName("name");
        Mockito.doReturn(user).when(userDao).findById(ArgumentMatchers.eq(id));
        userService.getById(5);

        Mockito.verify(userDao, Mockito.times(1)).findById(ArgumentMatchers.eq(id));
    }

    @Test
    void getAll() {
        List<User> userList = new ArrayList<>();
        User u1 = new User();
        u1.setId(1);
        u1.setName("name1");
        userList.add(u1);
        User u2 = new User();
        u2.setId(2);
        u2.setName("name2");
        userList.add(u2);

        Mockito.doReturn(userList).when(userDao).findAll(ArgumentMatchers.any());

        ArgumentCaptor<QueryCriteria> captor = ArgumentCaptor.forClass(QueryCriteria.class);

        int page = 3;

        userService.getAll(page);

        //dao was called 1 time
        Mockito.verify(userDao, Mockito.times(1)).findAll(captor.capture());
        // query was set correctly
        QueryCriteria criteria = captor.getValue();
        assertNotNull(criteria.getSortingCriteria());
        assertNotNull(criteria.getSortingOrder());
        assertEquals(20, criteria.getFirstResult());
        assertEquals(10, criteria.getResultLimit());

    }
}