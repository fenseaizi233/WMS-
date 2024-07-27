package com.fenseaizi.wmsdemo.controller;

import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.ChartPie;
import com.fenseaizi.wmsdemo.service.InrecordService;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import com.fenseaizi.wmsdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/highchart")
public class HighchartsController {
    @Autowired
    private UserService userService;
    @Autowired
    private InrecordService inrecordService;
    @Autowired
    private OutrecordService outrecordService;

    @GetMapping("/pie1")
    public Result usersex() {
        List<ChartPie> usersex = userService.usersex();
        List<Map<String, Object>> hash = new ArrayList<>();
        for (int i = 0; i < usersex.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("name", usersex.get(i).getName());
            maps.put("y", usersex.get(i).getValue());
            hash.add(maps);
        }
        return Result.suc(hash);
    }

    @GetMapping("/pie2")
    public Result usertype() {
        List<ChartPie> usertype = userService.usertype();
        List<Map<String, Object>> hash = new ArrayList<>();
        for (int i = 0; i < usertype.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("name", usertype.get(i).getName());
            maps.put("y", usertype.get(i).getValue());
            hash.add(maps);
        }
        return Result.suc(hash);
    }

    @PostMapping("/chart")
    public Result chart(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String goods = (String) param.get("goods");
        String time = (String) param.get("time");
        String record = (String) param.get("record");
        String user = (String) param.get("user");
        List<ChartPie> chart = new ArrayList<>();
        System.out.println(record);
        System.out.println(time);
        System.out.println(goods);
        if (record.equals("入库数量")) {
            if (time.equals("近七天")) {
                chart = inrecordService.weekCount(goods, user);
            }
            if (time.equals("本月")) {
                chart = inrecordService.monthCount(goods, user);
            }
            if (time.equals("今年")) {
                chart = inrecordService.yearCount(goods, user);
            }
        } else {
            if (time.equals("近七天")) {
                chart = outrecordService.weekCount(goods, user);
            }
            if (time.equals("本月")) {
                chart = outrecordService.monthCount(goods, user);
            }
            if (time.equals("今年")) {
                chart = outrecordService.yearCount(goods, user);
            }
        }
        System.out.println(chart);
        List<String> name = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        for (ChartPie chartPie : chart) {
            name.add(chartPie.getName());
            value.add(chartPie.getValue());
        }
        List<Map<String, Object>> pie = new ArrayList<>();
        for (int i = 0; i < chart.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("name", chart.get(i).getName());
            maps.put("y", chart.get(i).getValue());
            pie.add(maps);
        }
        Map<String, Object> line = new HashMap<>();
        List<Map<String, Object>> map = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", time + record);
        maps.put("data", value);
        map.add(maps);
        line.put("category", name);
        line.put("data", map);
        Map<String, Object> res = new HashMap<>();
        res.put("line", line);
        res.put("pie", pie);
        System.out.println(res);
        return Result.suc(res);
    }
}
