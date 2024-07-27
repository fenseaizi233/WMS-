import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store";
Vue.use(VueRouter);
// 定义路由
const routes = [
  {
    path: "/",
    name: "index",
    component: () => import("../components/Index.vue"),
    children: [
      {
        mode:'history',
        path: "/Home",
        name: "home",
        meta: {
          title: "首页",
        },
        component: () => import("../components/system/SystemHome.vue"),
      },
    ],
  },
  {
    path: "/Login",
    name: "login",
    component: () => import("../components/Login.vue"),
  },
  {
    path: "/Register",
    name: "register",
    component: () => import("../components/Register.vue"),
  },
];
const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach((to, from, next) => {
  let hasRoutes = store.state.hasRoutes;
  const user = localStorage.getItem("CurUser");
  const menu = localStorage.getItem("Menu");
  let routes = router.options.routes;
  if (routes[0].children.length == 1) {
    hasRoutes = false;
    store.commit("changeRouteStates", hasRoutes);
  }
  console.log(hasRoutes);
  console.log(user);
  if (!user && to.path != "/Login" && to.path != "/Register") {
    next({ path: "/Login" });
  } else if (user && !hasRoutes) {
    addRoutes(JSON.parse(menu));
    store.commit("changeRouteStates", true);
    next({ ...to, replace: true });
    // console.log("构建路由");
  } else {
    next();
  }
});
function addRoutes(menuList) {
  let routes = router.options.routes;
  routes.forEach((routeItem) => {
    if (routeItem.path == "/") {
      menuList.forEach((menu) => {
        let childRoute = {
          path: "/" + menu.menuclick,
          name: menu.menuname,
          meta: {
            title: menu.menuname,
          },
          component: () => import("../components/" + menu.menucomponent),
        };
        routeItem.children.push(childRoute);
      });
    }
  });
  router.addRoutes(routes);
}
// 修改 router.push 和 router.replace 方法，增加错误处理
const originalPush = router.push;
router.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};

const originalReplace = router.replace;
router.replace = function replace(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalReplace.call(this, location, onResolve, onReject);
  return originalReplace.call(this, location).catch((err) => err);
};

export default router;
