package com.fenseaizi.wmsdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.*;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.fenseaizi.wmsdemo.service.GoodsService;
import com.fenseaizi.wmsdemo.service.InrecordService;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fenseaizi
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired private GoodsService goodsService;

    @Autowired
    private InrecordService inrecordService;
    @Autowired
    private OutrecordService outrecordService;
    @GetMapping("/allcount")
    public Result allcount() {
        List<Goods> list = goodsService.list();
        int count = 0;
        for (Goods goods : list) {
            if (goods.getStatus() == 1) {
                count += goods.getCount();
            }
        }
        return Result.suc(count);
    }
    @GetMapping("/list")
    public List<Goods> list() {
        return goodsService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        List<Goods> list = goodsService.lambdaQuery().eq(Goods::getName, goods.getName()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return goodsService.save(goods) ? Result.suc(goods) : Result.fail("新增失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        List<Goods> list = goodsService.lambdaQuery().eq(Goods::getName, goods.getName()).ne(Goods::getId, goods.getId()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return goodsService.updateById(goods) ? Result.suc(goods) : Result.fail("新增失败");
    }
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        //货物放在仓库里，删除仓库，就需要删除对应的货物
        //出入库数据与货物关联 也需关联删除
        List<Goods> list = goodsService.lambdaQuery().eq(Goods::getType, id).list();
        for (Goods goods:list)
        {
            inrecordService.lambdaQuery().eq(Inrecord::getGoods, goods.getId()).list().forEach(inrecordService::removeById);
            outrecordService.lambdaQuery().eq(Outrecord::getGoods, goods.getId()).list().forEach(outrecordService::removeById);
        }
        goodsService.lambdaQuery().eq(Goods::getType, id).list().forEach(goodsService::removeById);
        return goodsService.removeById(id) ? Result.suc() : Result.fail();
    }
    @PostMapping("/deleteLogical/{id}")
    public Result deleteLogical(@PathVariable("id") Long id) {
        // 获取用户数据
        Goods goods = goodsService.getById(id);
        //    修改用户状态
        goods.setStatus(0);
        // 更新数据
        return goodsService.updateById(goods) ? Result.suc() : Result.fail();
    }

    @PostMapping("/control/{id}")
    public Result control(@PathVariable("id") Long id) {
        Goods goods = goodsService.getById(id);
        if (goods.getStatus() == 1) {
            goods.setStatus(0);
        } else {
            goods.setStatus(1);
        }
        return goodsService.updateById(goods) ? Result.suc() : Result.fail();
    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        Goods goods = goodsService.getById(id);
        return Result.suc(goods);
    }
    @Transactional
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody Long[] ids) {
        System.out.println(ids[0]);
        for (int i = 0; i < ids.length; i++) {
            List<Goods> list = goodsService.lambdaQuery().eq(Goods::getType, ids[i]).list();
            for (Goods goods:list)
            {
                inrecordService.lambdaQuery().eq(Inrecord::getGoods, goods.getId()).list().forEach(inrecordService::removeById);
                outrecordService.lambdaQuery().eq(Outrecord::getGoods, goods.getId()).list().forEach(outrecordService::removeById);

            }
            goodsService.lambdaQuery().eq(Goods::getType, ids[i]).list().forEach(goodsService::removeById);
        }
        goodsService.removeByIds(Arrays.asList(ids));
        return Result.suc("批量删除成功");
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String status = (String) param.get("status");
        String storage = (String) param.get("storage");
        String type = (String) param.get("type");
        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {
            lambdaQueryWrapper.eq(Goods::getStatus, status);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            lambdaQueryWrapper.eq(Goods::getStorage, storage);
        }
        if (StringUtils.isNotBlank(type) && !"null".equals(type)) {
            lambdaQueryWrapper.eq(Goods::getType, type);
        }

        IPage result = goodsService.PageList(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

}
