package com.fenseaizi.wmsdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fenseaizi.wmsdemo.entity.Type;
import com.fenseaizi.wmsdemo.mapper.TypeMapper;
import com.fenseaizi.wmsdemo.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
@Resource private TypeMapper typeMapper;
    @Override
    public IPage PageList(IPage<Type> page, Wrapper wrapper) {
        return typeMapper.PageList(page,wrapper);
    }
}
