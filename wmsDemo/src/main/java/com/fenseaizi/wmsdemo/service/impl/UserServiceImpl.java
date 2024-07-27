package com.fenseaizi.wmsdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.User;
import com.fenseaizi.wmsdemo.mapper.UserMapper;
import com.fenseaizi.wmsdemo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
@Resource
private UserMapper userMapper;
    @Override
    public IPage PageList(IPage<User> page, Wrapper wrapper) {
        return userMapper.PageList(page,wrapper);
    }

    @Override
    public List<ChartPie> usersex() {
        return userMapper.usersex();
    }

    @Override
    public List<ChartPie> usertype() {
        return userMapper.usertype();
    }
}
