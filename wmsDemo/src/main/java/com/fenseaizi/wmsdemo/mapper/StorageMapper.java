package com.fenseaizi.wmsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fenseaizi.wmsdemo.entity.Storage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface StorageMapper extends BaseMapper<Storage> {

    IPage PageList(IPage<Storage> page, @Param(Constants.WRAPPER)Wrapper wrapper);

}
