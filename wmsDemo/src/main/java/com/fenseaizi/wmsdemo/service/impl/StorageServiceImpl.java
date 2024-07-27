package com.fenseaizi.wmsdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.fenseaizi.wmsdemo.entity.Storage;
import com.fenseaizi.wmsdemo.mapper.StorageMapper;
import com.fenseaizi.wmsdemo.service.StorageService;
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
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    @Resource
    StorageMapper storageMapper;
    @Override
    public IPage PageList(IPage<Storage> page, Wrapper wrapper) {
        return storageMapper.PageList(page,wrapper);
    }

}
