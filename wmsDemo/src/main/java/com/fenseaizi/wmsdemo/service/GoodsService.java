package com.fenseaizi.wmsdemo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenseaizi.wmsdemo.entity.Type;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
public interface GoodsService extends IService<Goods> {


    IPage PageList(IPage<Goods> page, Wrapper wrapper);
}
