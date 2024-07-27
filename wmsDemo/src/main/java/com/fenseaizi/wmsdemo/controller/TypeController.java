package com.fenseaizi.wmsdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.fenseaizi.wmsdemo.entity.Inrecord;
import com.fenseaizi.wmsdemo.entity.Outrecord;
import com.fenseaizi.wmsdemo.entity.Type;
import com.fenseaizi.wmsdemo.service.GoodsService;
import com.fenseaizi.wmsdemo.service.InrecordService;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import com.fenseaizi.wmsdemo.service.TypeService;
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
@RequestMapping("/type")
public class TypeController {
    @Autowired
    private TypeService typeService;
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
        List<Goods> list = goodsService.lambdaQuery().eq(Goods::getType, id).list();
        for (Goods goods:list)
        {
            inrecordService.lambdaQuery().eq(Inrecord::getGoods, goods.getId()).list().forEach(inrecordService::removeById);
            outrecordService.lambdaQuery().eq(Outrecord::getGoods, goods.getId()).list().forEach(outrecordService::removeById);
        }
        goodsService.lambdaQuery().eq(Goods::getType, id).list().forEach(goodsService::removeById);
        return typeService.removeById(id) ? Result.suc() : Result.fail();
    }
    //逻辑删除仓库
    //控制仓库状态
    //获取仓库信息
    //批量删除仓库
    @PostMapping("/deleteLogical/{id}")
    public Result deleteLogical(@PathVariable("id") Long id) {
        // 获取用户数据
        Type type = typeService.getById(id);
        //    修改用户状态
        type.setStatus(0);
        // 更新数据
        return typeService.updateById(type) ? Result.suc() : Result.fail();
    }

    @PostMapping("/control/{id}")
    public Result control(@PathVariable("id") Long id) {
        Type type = typeService.getById(id);
        if (type.getStatus() == 1) {
            type.setStatus(0);
        } else {
            type.setStatus(1);
        }
        return typeService.updateById(type) ? Result.suc() : Result.fail();
    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        Type type = typeService.getById(id);
        return Result.suc(type);
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
        typeService.removeByIds(Arrays.asList(ids));
        return Result.suc("批量删除成功");
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String status = (String) param.get("status");
        Page<Type> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Type> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Type::getName, name);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {
            lambdaQueryWrapper.eq(Type::getStatus, status);
        }

        IPage result = typeService.PageList(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }




    @GetMapping("/count")
    public Integer count() {
        return typeService.list().size();
    }

    @GetMapping("/list")
    public List<Type> list() {
        return typeService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody Type type) {
        List<Type> list = typeService.lambdaQuery().eq(Type::getName, type.getName()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return typeService.save(type) ? Result.suc(type) : Result.fail("新增失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Type type) {
        List<Type> list = typeService.lambdaQuery().eq(Type::getName, type.getName()).ne(Type::getId, type.getId()).list();
        if (list.size() > 0) {
            return Result.fail("仓库名称重复，换一个吧！");
        }
        return typeService.updateById(type) ? Result.suc(type) : Result.fail("新增失败");
    }
}
