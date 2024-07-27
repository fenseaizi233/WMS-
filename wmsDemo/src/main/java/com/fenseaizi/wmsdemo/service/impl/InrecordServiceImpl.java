package com.fenseaizi.wmsdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.Inrecord;
import com.fenseaizi.wmsdemo.mapper.InrecordMapper;
import com.fenseaizi.wmsdemo.service.InrecordService;
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
public class InrecordServiceImpl extends ServiceImpl<InrecordMapper, Inrecord> implements InrecordService {
@Resource private InrecordMapper inrecordMapper;
    @Override
    public IPage PageList(Page<Inrecord> page, Wrapper wrapper) {
        return inrecordMapper.PageList(page,wrapper);
    }

    @Override
    public List<ChartPie> weekCount(String goods, String user) {
        return inrecordMapper.weekCount(goods,user);
    }

    @Override
    public List<ChartPie> monthCount(String goods, String user) {
        return inrecordMapper.monthCount(goods,user);
    }

    @Override
    public List<ChartPie> yearCount(String goods, String user) {
        return inrecordMapper.yearCount(goods,user);
    }
}
