package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserDtoMapper;
import com.epam.esm.service.pagination.PaginationUtil;
import com.epam.esm.service.sorting.UserSortingCriteria;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserDtoMapper userDtoMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, UserDtoMapper userDtoMapper,
                           BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userDtoMapper = userDtoMapper;
    }

    @Transactional
    @Override
    public long create(UserDto userDto) {
        User user = userDtoMapper.toEntity(userDto);
        user.setRole(new Role(RoleName.USER.name()));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userDao.add(user);
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
