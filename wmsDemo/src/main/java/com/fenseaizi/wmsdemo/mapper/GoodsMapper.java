package com.fenseaizi.wmsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenseaizi.wmsdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    IPage PageList(IPage<Goods> page, @Param(Constants.WRAPPER)Wrapper wrapper);
}
