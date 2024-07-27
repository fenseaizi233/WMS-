package com.fenseaizi.wmsdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.Outrecord;
import com.fenseaizi.wmsdemo.mapper.OutrecordMapper;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OutrecordServiceImpl extends ServiceImpl<OutrecordMapper, Outrecord> implements OutrecordService {
@Resource private OutrecordMapper outrecordMapper;
    @Override
    public IPage PageList(Page<Outrecord> page, Wrapper wrapper) {
        return outrecordMapper.PageList(page,wrapper);
    }

    @Override
    public List<ChartPie> weekCount(String goods, String user) {
        return outrecordMapper.weekCount(goods,user);
    }

    @Override
    public List<ChartPie> monthCount(String goods, String user) {
        return outrecordMapper.monthCount(goods,user);
    }

    @Override
    public List<ChartPie> yearCount(String goods, String user) {
        return outrecordMapper.yearCount(goods,user);
    }
}
