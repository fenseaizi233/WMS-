package com.fenseaizi.wmsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.entity.Outrecord;
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
public interface OutrecordMapper extends BaseMapper<Outrecord> {
    IPage PageList(IPage<Outrecord>page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<ChartPie> weekCount(String goods, String user);

    List<ChartPie> monthCount(String goods, String user);

    List<ChartPie> yearCount(String goods, String user);
}
