package com.fenseaizi.wmsdemo.controller;


import com.fenseaizi.wmsdemo.common.Result;
import com.fenseaizi.wmsdemo.entity.Menu;
import com.fenseaizi.wmsdemo.service.MenuService;
import com.fenseaizi.wmsdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/menu")
public class MenuController {
    @Autowired private MenuService menuService;
    @Autowired
    private UserService userService;
    //根据角色查询菜单权限
    @GetMapping("/role{role}")
    public Result rolemenu(@PathVariable Integer role){
        List list = menuService.lambdaQuery().like(Menu::getMenuright, role).list();
        return Result.suc(list);
    }
    //
    @GetMapping("/list")
    public List<Menu> list(){
        return menuService.list();
    }
    //根据id获取菜单权限
    @GetMapping("/{id}")
    public Result usermenu(@PathVariable Integer id){
        List list = menuService.lambdaQuery().like(Menu::getMenuright,userService.getById(id).getRole() ).list();
        return Result.suc(list);
    }
}
