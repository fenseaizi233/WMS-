package com.fenseaizi.wmsdemo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.Outrecord;
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
public interface OutrecordService extends IService<Outrecord> {

    IPage PageList(Page<Outrecord> page, Wrapper wrapper);

    List<ChartPie> weekCount(String goods, String user);

    List<ChartPie> monthCount(String goods, String user);

    List<ChartPie> yearCount(String goods, String user);
}
