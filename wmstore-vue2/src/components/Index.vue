<template>
  <el-container>
    <el-aside
      :width="asideWith"
      style="min-height: 90vh; background-color: blueviolet; color: #333"
    >
      <Aside :isCollaspe="isCollapse"></Aside>
    </el-aside>
    <el-container style="min-height: 100vh">
      <el-header
        style="
          text-align: right;
          font-size: 12px;
          width: 100%;
          border-bottom: #abc;
          line-height: 60px;
          background-color: aqua;
          color: bisque;
        "
      >
        <Header @doCollaspe="doCollaspe" :icon="icon"></Header>
      </el-header>
      <el-main style="height: 100%; padding: 5px">
        <router-view></router-view
      ></el-main>
      <el-footer style="text-align: center; font-size: 12px">
        Copyright @2024 fenseaizi.All rights reserved.
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "./Aside.vue";
import Header from "./Header.vue";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Index",
  components: { Aside, Header },
  data() {
    return {
      isCollapse: false,
      asideWith: "220px",
      icon: "el-icon-s-fold",
    };
  },
  methods: {
    doCollaspe() {
      this.isCollapse = !this.isCollapse;
      if (!this.isCollapse) {
        // 展开
        (this.asideWith = "220px"), (this.icon = "el-icon-s-fold");
      } else {
        (this.asideWith = "64px"), (this.icon = "el-icon-s-unfold");
      }
    },
  },
};
</script>

<style></style>
