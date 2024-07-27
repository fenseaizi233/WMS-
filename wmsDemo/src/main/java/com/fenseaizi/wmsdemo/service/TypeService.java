package com.fenseaizi.wmsdemo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fenseaizi.wmsdemo.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
public interface TypeService extends IService<Type> {
IPage  PageList(IPage<Type>page, Wrapper wrapper);
}
