package com.fenseaizi.wmsdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.Goods;
import com.fenseaizi.wmsdemo.entity.Inrecord;
import com.fenseaizi.wmsdemo.service.GoodsService;
import com.fenseaizi.wmsdemo.service.InrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@RequestMapping("/inrecord")
public class InrecordController {
    @Autowired private InrecordService inrecordService;
    @Autowired private GoodsService goodsService;
    @GetMapping("/allcount")
    public Result allcount() {
        List<Inrecord> list = inrecordService.list();
        int count=0;
        for (Inrecord inrecord:list)
        {
            if (inrecord.getStatus()==1){
                count+=inrecord.getCount();
            }
        }
        return Result.suc(count);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Inrecord inrecord) {
        return inrecordService.save(inrecord) ? Result.suc() : Result.fail();
    }
    @Transactional
    @PostMapping("/pass")
    public Result pass(@RequestParam Integer id,Integer admin) {
        Inrecord inrecord = inrecordService.getById(id);
        Goods goods = goodsService.getById(inrecord.getGoods());
        goods.setCount(goods.getCount() + inrecord.getCount());
        goodsService.updateById(goods);
        inrecord.setStatus(1);
        inrecord.setAdmin(admin);

        inrecord.setUpdateTime(LocalDateTime.now());
        return inrecordService.updateById(inrecord) ? Result.suc() : Result.fail();
    }
    @PostMapping("/refuse")
    public Result refuse(@RequestParam Integer id,Integer admin) {
        Inrecord inrecord = inrecordService.getById(id);
        inrecord.setStatus(2);
        inrecord.setAdmin(admin);
        inrecord.setUpdateTime(LocalDateTime.now());
        return inrecordService.updateById(inrecord) ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String goods = (String) param.get("goods");
        String user = (String) param.get("user");
        String admin = (String) param.get("admin");
        String status = (String) param.get("status");

        String role = (String) param.get("role");
        String createTime = (String) param.get("createTime");
        Page<Inrecord> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Inrecord> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(goods) && !"null".equals(goods)) {
            lambdaQueryWrapper.like(Inrecord::getGoods, goods);
        }
        if (StringUtils.isNotBlank(createTime) && !"null".equals(createTime)) {
            lambdaQueryWrapper.like(Inrecord::getCreateTime, createTime);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {
            lambdaQueryWrapper.eq(Inrecord::getStatus, status);
        }
        if (StringUtils.isNotBlank(user) && !"null".equals(user)) {
            lambdaQueryWrapper.eq(Inrecord::getUser, user);
        }
        if(role.equals("1")&&StringUtils.isNotBlank(admin) && !"null".equals(admin)){
//如果是管理员，查询管理员审核的数据和未审核的数据
            lambdaQueryWrapper.eq(Inrecord::getAdmin, admin).or().isNull(Inrecord::getAdmin);

        }
        IPage result = inrecordService.PageList(page, lambdaQueryWrapper);
        for (Object inrecord : result.getRecords()) {
            Inrecord inrecord1 = (Inrecord) inrecord;
            Goods goods1 = goodsService.getById(inrecord1.getGoods());
            inrecord1.setGood(goods1);
        }
        return Result.suc(result.getTotal(), result.getRecords().toString());
    }
}
