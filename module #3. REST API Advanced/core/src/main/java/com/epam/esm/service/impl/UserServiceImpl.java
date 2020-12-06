package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserDtoMapper;
import com.epam.esm.service.sorting.PaginationUtil;
import com.epam.esm.service.sorting.TagSortingCriteria;
import com.epam.esm.service.sorting.UserSortingCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserDtoMapper userDtoMapper;

    public UserServiceImpl(UserDao userDao, UserDtoMapper userDtoMapper){
        this.userDao = userDao;
        this.userDtoMapper = userDtoMapper;
    }
    @Override
    public UserDto getById(long id) {
        return userDtoMapper.toDto(userDao.findById(id));
    }

    @Override
    public List<UserDto> getAll(Integer page) {
        int startValue = PaginationUtil.startValue(page);
        QueryCriteria criteria = new QueryCriteria();
        criteria.setSortingOrder(QueryCriteria.Order.ASC);
        criteria.setSortingCriteria(UserSortingCriteria.ID.getFieldName());
        criteria.setFirstResult(startValue);
        criteria.setResultLimit(PaginationUtil.ON_PAGE);
        List<User> tagList = userDao.findAll(criteria);
        return userDtoMapper.toDto(tagList);
    }
}
