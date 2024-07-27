package com.fenseaizi.wmsdemo.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.fenseaizi.wmsdemo.entity.Storage;
import com.fenseaizi.wmsdemo.entity.Inrecord;
import com.fenseaizi.wmsdemo.entity.Outrecord;
import com.fenseaizi.wmsdemo.entity.User;
import com.fenseaizi.wmsdemo.mapper.StorageMapper;
import com.fenseaizi.wmsdemo.service.GoodsService;
import com.fenseaizi.wmsdemo.service.InrecordService;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import com.fenseaizi.wmsdemo.service.StorageService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private InrecordService inrecordService;
    @Autowired
    private OutrecordService outrecordService;
    //删除仓库
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        //货物放在仓库里，删除仓库，就需要删除对应的货物
        //出入库数据与货物关联 也需关联删除
        List<Goods> list = goodsService.lambdaQuery().eq(Goods::getStorage, id).list();
        for (Goods goods:list)
        {
            inrecordService.lambdaQuery().eq(Inrecord::getGoods, goods.getId()).list().forEach(inrecordService::removeById);
            outrecordService.lambdaQuery().eq(Outrecord::getGoods, goods.getId()).list().forEach(outrecordService::removeById);
        }
        goodsService.lambdaQuery().eq(Goods::getStorage, id).list().forEach(goodsService::removeById);
        return storageService.removeById(id) ? Result.suc() : Result.fail();
    }
    //逻辑删除仓库
    //控制仓库状态
    //获取仓库信息
    //批量删除仓库
    @PostMapping("/deleteLogical/{id}")
    public Result deleteLogical(@PathVariable("id") Long id) {
        // 获取用户数据
        Storage storage = storageService.getById(id);
        //    修改用户状态
        storage.setStatus(0);
        // 更新数据
        return storageService.updateById(storage) ? Result.suc() : Result.fail();
    }

    @PostMapping("/control/{id}")
    public Result control(@PathVariable("id") Long id) {
        Storage storage = storageService.getById(id);
        if (storage.getStatus() == 1) {
            storage.setStatus(0);
        } else {
            storage.setStatus(1);
        }
        return storageService.updateById(storage) ? Result.suc() : Result.fail();
    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        Storage storage = storageService.getById(id);
        return Result.suc(storage);
    }
    @Transactional
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody Long[] ids) {
        System.out.println(ids[0]);
        for (int i = 0; i < ids.length; i++) {
            List<Goods> list = goodsService.lambdaQuery().eq(Goods::getStorage, ids[i]).list();
            for (Goods goods:list)
            {
                inrecordService.lambdaQuery().eq(Inrecord::getGoods, goods.getId()).list().forEach(inrecordService::removeById);
                outrecordService.lambdaQuery().eq(Outrecord::getGoods, goods.getId()).list().forEach(outrecordService::removeById);

            }
            goodsService.lambdaQuery().eq(Goods::getStorage, ids[i]).list().forEach(goodsService::removeById);
        }
        storageService.removeByIds(Arrays.asList(ids));
        return Result.suc("批量删除成功");
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String status = (String) param.get("status");
        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Storage::getName, name);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {
            lambdaQueryWrapper.eq(Storage::getStatus, status);
        }

        IPage result = storageService.PageList(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }




    @GetMapping("/count")
    public Integer count() {
        return storageService.list().size();
    }

    @GetMapping("/list")
    public List<Storage> list() {
        return storageService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody Storage storage) {
        List<Storage> list = storageService.lambdaQuery().eq(Storage::getName, storage.getName()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return storageService.save(storage) ? Result.suc(storage) : Result.fail("新增失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Storage storage) {
        List<Storage> list = storageService.lambdaQuery().eq(Storage::getName, storage.getName()).ne(Storage::getId, storage.getId()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return storageService.updateById(storage) ? Result.suc(storage) : Result.fail("新增失败");
    }
}
