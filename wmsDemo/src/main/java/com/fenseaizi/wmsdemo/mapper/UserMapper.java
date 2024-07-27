package com.fenseaizi.wmsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage PageList(IPage<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<ChartPie> usersex();

    List<ChartPie> usertype();
}
