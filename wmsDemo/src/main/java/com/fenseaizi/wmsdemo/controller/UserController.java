package com.fenseaizi.wmsdemo.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenseaizi.wmsdemo.common.CaptureConfig;
import com.fenseaizi.wmsdemo.common.QueryPageParam;
import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.Menu;
import com.fenseaizi.wmsdemo.entity.User;
import com.fenseaizi.wmsdemo.service.InrecordService;
import com.fenseaizi.wmsdemo.service.MenuService;
import com.fenseaizi.wmsdemo.service.OutrecordService;
import com.fenseaizi.wmsdemo.service.UserService;
import com.fenseaizi.wmsdemo.entity.Inrecord;
import com.fenseaizi.wmsdemo.entity.Outrecord;

import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OutrecordService outrecordService;
    @Autowired
    private InrecordService inrecordService;
    @Autowired private MenuService menuService;
    @GetMapping("/count")
    public Integer count() {
        return userService.list().size();
    }

    @GetMapping("/allcount")
    public Result allcount() {
        HashMap<Object, Object> res = new HashMap<>();
        Integer userCount = userService.lambdaQuery().eq(User::getRole, 2).eq(User::getStatus, 1).list().size();
        Integer adminCount = userService.lambdaQuery().ne(User::getRole, 2).eq(User::getStatus, 1).list().size();
        res.put("userCount", userCount);
        res.put("adminCount", adminCount);
        return Result.suc(res);
    }

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        List<User> list = userService.lambdaQuery().eq(User::getName, user.getName()).eq(User::getRole, 2).list();
        if (list.size() > 0) {
            return Result.fail("名称重复，换一个吧！");
        }
        list = userService.lambdaQuery().eq(User::getTelephone, user.getTelephone()).eq(User::getRole, 2).list();
        if (list.size() > 0) {
            return Result.fail("手机号重复了，换一个吧！");
        }
        return userService.save(user) ? Result.suc() : Result.fail();
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        List<User> list = userService.lambdaQuery().eq(User::getName, user.getName()).ne(User::getId, user.getId()).eq(User::getRole, 2).list();
        if (list.size() > 0) {
            return Result.fail("名称重复，换一个吧！");
        }
        list = userService.lambdaQuery().eq(User::getTelephone, user.getTelephone()).ne(User::getId, user.getId()).eq(User::getRole, 2).list();
        if (list.size() > 0) {
            return Result.fail("手机号重复了，换一个吧！");
        }
        user.setUpdateTime(LocalDateTime.now());
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        User user = userService.getById(id);
        if (user.getRole() == 2) {
//                判断是用户还是管理员
            inrecordService.lambdaQuery().eq(Inrecord::getUser, id).list().forEach(inrecordService::removeById);
//                循环删除入库表中与用户关联的数据
            outrecordService.lambdaQuery().eq(Outrecord::getUser, id).list().forEach(outrecordService::removeById);
//循环删除出库表中与用户关联的数据
        } else {
            inrecordService.lambdaQuery().eq(Inrecord::getAdmin, id).list().forEach(inrecordService::removeById);
            //                循环删除入库表中与管理员关联的数据
            outrecordService.lambdaQuery().eq(Outrecord::getAdmin, id).list().forEach(outrecordService::removeById);
//                循环删除出库表中与管理员关联的数据
        }
        return userService.removeById(id) ? Result.suc() : Result.fail();
    }

    @PostMapping("/deleteLogical/{id}")
    public Result deleteLogical(@PathVariable("id") Long id) {
        // 获取用户数据
        User user = userService.getById(id);
        //    修改用户状态
        user.setStatus(0);
        // 更新数据
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }

    @PostMapping("/control/{id}")
    public Result control(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user.getStatus() == 1) {
            user.setStatus(0);
        } else {
            user.setStatus(1);
        }
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return Result.suc(user);
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody Long[] ids) {

        for (int i = 0; i < ids.length; i++) {
//            通过用户ID数组循环
            User user = userService.getById(ids[i]);
//            获取用户
            if (user.getRole() == 2) {
//                判断是用户还是管理员
                inrecordService.lambdaQuery().eq(Inrecord::getUser, user.getId()).list().forEach(inrecordService::removeById);
//                循环删除入库表中与用户关联的数据
                outrecordService.lambdaQuery().eq(Outrecord::getUser, user.getId()).list().forEach(outrecordService::removeById);
//循环删除出库表中与用户关联的数据
            } else {
                inrecordService.lambdaQuery().eq(Inrecord::getAdmin, user.getId()).list().forEach(inrecordService::removeById);
                //                循环删除入库表中与管理员关联的数据
                outrecordService.lambdaQuery().eq(Outrecord::getAdmin, user.getId()).list().forEach(outrecordService::removeById);
//                循环删除出库表中与管理员关联的数据
            }
        }

        userService.removeByIds(Arrays.asList(ids));
//        最后,批量删除用户信息
        return Result.suc("批量删除成功,超级管理员无法删除");
    }

    @GetMapping("/getAvatar/{name}")
    public Result getAvatar(@PathVariable("name") String name) {
        // 获取用户数据
        List<User> list = userService.lambdaQuery().eq(User::getName, name).list();
        if (list.size() > 0) {
            return Result.suc(list.get(0).getAvatar());
        }
        list = userService.lambdaQuery().eq(User::getTelephone, name).list();
        if (list.size() > 0) {
            return Result.suc(list.get(0).getAvatar());
        }
        return Result.fail("用户不存在");
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String telephone = (String) param.get("telephone");
        String sex = (String) param.get("sex");
//        String role = (String) param.get("role");
        String status = (String) param.get("status");
//        配置分页插件
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
//            进行模糊查询拼接
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(telephone) && !"null".equals(telephone)) {
//            进行模糊查询拼接
            lambdaQueryWrapper.like(User::getTelephone, telephone);
        }
        if (StringUtils.isNotBlank(sex) && !"null".equals(sex)) {
//            进行等值查询拼接
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {

            lambdaQueryWrapper.eq(User::getStatus, status);
        }
//        if (StringUtils.isNotBlank(role) && !"null".equals(role)) {
//            lambdaQueryWrapper.eq(User::getRole, role);
//        }
        lambdaQueryWrapper.eq(User::getRole, 2);
        IPage result = userService.PageList(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    @PostMapping("/adminPage")
    public Result adminPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String telephone = (String) param.get("telephone");
        String sex = (String) param.get("sex");
        String role = (String) param.get("role");
        String status = (String) param.get("status");
//        配置分页插件
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
//            进行模糊查询拼接
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(telephone) && !"null".equals(telephone)) {
//            进行模糊查询拼接
            lambdaQueryWrapper.like(User::getTelephone, telephone);
        }
        if (StringUtils.isNotBlank(sex) && !"null".equals(sex)) {
//            进行等值查询拼接
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(status) && !"null".equals(status)) {

            lambdaQueryWrapper.eq(User::getStatus, status);
        }
        if (StringUtils.isNotBlank(role) && !"null".equals(role)) {
            lambdaQueryWrapper.eq(User::getRole, role);
        }
        lambdaQueryWrapper.ne(User::getRole, 2);
        IPage result = userService.PageList(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, @RequestParam String key, HttpServletRequest request) {
        if(!user.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))){
            CaptchaUtil.clear(request);
            return Result.fail("验证码不正确");
        }
        if (StpUtil.isLogin()) {
            User user1 = userService.lambdaQuery().eq(User::getName, user.getName()).eq(User::getPassword, user.getPassword()).list().get(0);
            StpUtil.login(user1.getId());
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRole()).list();
            HashMap res = new HashMap();
            System.out.println(menuList);
            res.put("user", user1);
            res.put("menu", menuList);
            CaptureConfig.CAPTURE_MAP.remove(key);
            return Result.suc(res);
        } else {
            List list = userService.lambdaQuery().eq(User::getName, user.getName()).eq(User::getPassword, user.getPassword()).list();
            if (list.size() > 0) {
                User user1 = (User) list.get(0);
                if (user1.getStatus() != 1) {
                    return Result.fail("该账号已注销");
                } else {
                    StpUtil.login(user1.getId());
                    List menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRole()).list();
                    HashMap res = new HashMap();
                    System.out.println(menuList);
                    res.put("user", user1);
                    res.put("menu", menuList);
                    return Result.suc(res);
                }
            }


        }
        return Result.fail("账号不存在或密码错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        List<User> list = userService.lambdaQuery().eq(User::getName, user.getName()).list();
        if (list.size() > 0) {
            return Result.fail("账号已被注册，换一个把");
        } else {
            if (user.getSex() == 1) user.setAvatar("defaultman");
            else user.setAvatar("defaultwoman");
            return userService.save(user) ? Result.suc(user) : Result.fail("注册失败");
        }
    }
    @Transactional
    @GetMapping("/modAvatar")
    public Result modAvatar(@RequestParam Integer id, String avatar) {
        User user = userService.getById(id);
        user.setAvatar(avatar);
        userService.updateById(user);
        User user1 = userService.getById(id);
        return Result.suc(user1);
    }
    @Transactional
    @GetMapping("/modName")
    public Result modName(@RequestParam Integer id, String name) {
        User user = userService.getById(id);
        user.setName(name);
        List list =
                userService.lambdaQuery().eq(User::getName, name).ne(User::getId, id).list();
        if (list.size() > 0) return Result.fail("账号已被注册，请换一个名字");
        else {
            userService.updateById(user);
            User user1 = userService.getById(id);

            return Result.suc(user1);
        }
    }
    @GetMapping("/modPwd")
    public Result modPwd(@RequestParam Integer id, String password) {
        User user = userService.getById(id);
        user.setPassword(password);
        userService.updateById(user);
        User user1 = userService.getById(id);
        return Result.suc(user1);
    }
    @PostMapping("/repass")
    public Result repass(@RequestParam Integer id) {
        User user = userService.getById(id);
        user.setPassword("123456");
        return userService.updateById(user) ? Result.suc() : Result.fail();
    }
    @GetMapping("/logout")
    public SaResult logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
        return SaResult.ok();
    }
}
