<template>
  <el-menu
    default-active="/Home"
    :collapse="isCollaspe"
    style="height: 100%"
    background-color="#9811d7"
    text-color="#fff"
    :unique-opened="true"
    menu-trigger="click"
    active-text-color="#ffd04b"
    router
  >
    <div v-for="(item, i) in menu" :key="i">
      <el-menu-item v-if="item.menulevel == 1" :index="'/' + item.menuclick">
        <i :class="item.menuicon"></i>
        <span slot="title"> &nbsp;&nbsp;&nbsp;{{ item.menuname }}</span>
      </el-menu-item>
      <el-submenu v-if="item.menulevel == 0" :index="'/' + item.menuclick">
        <template slot="title">
          <i :class="item.menuicon"></i>
          <span slot="title"> &nbsp;&nbsp; {{ item.menuname }}</span>
        </template>
        <div v-for="(menu, n) in menu" :key="n">
          <el-menu-item
            v-if="item.menucode == menu.menuparentcode"
            :index="'/' + menu.menuclick"
          >
            <i :class="menu.menuicon"></i>
            <span slot="title">{{ menu.menuname }}</span>
          </el-menu-item>
        </div>
      </el-submenu>
    </div>
  </el-menu>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Aside",
  data() {
    return {};
  },
  props: {
    isCollaspe: Boolean,
  },
  methods: {
    firstClick() {
      this.$router.push("/Home");
    },
  },
  computed: {
    menu: {
      get() {
        return JSON.parse(localStorage.getItem("Menu"));
      },
    },
  },
  beforeMount() {
    this.firstClick();
  },
};
</script>

<style></style>
