package com.fenseaizi.wmsdemo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
public interface UserService extends IService<User> {

    IPage PageList(IPage<User> page, Wrapper wrapper);

    List<ChartPie> usersex();

    List<ChartPie> usertype();
}
