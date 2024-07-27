<template>
  <div style="text-align: center; background-color: #f1f1f3">
    <el-row :gutter="12" class="card-row">
      <el-col :span="12">
        <el-card shadow="hover" class="box-card centered-card red">
          <div>用户名： {{ user.name }}</div>
        </el-card>
      </el-col>
      <el-col :span="12" v-if="user.telephone">
        <el-card shadow="hover" class="box-card centered-card orange">
          手机号
          <el-tag type="success">{{ user.telephone }}</el-tag>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="card-row">
      <el-col :span="8">
        <el-card shadow="hover" class="box-card centered-card blue">
          <el-tag type="primary">角色</el-tag>
          <el-tag
            disable-transitions
            :type="
              user.role == 0
                ? 'success'
                : user.role == 1
                ? 'primary'
                : user.role == 2
                ? 'info'
                : 'danger'
            "
          >
            {{
              user.role == 0
                ? "超级管理员"
                : user.role == 1
                ? "管理员"
                : user.role == 2
                ? "用户"
                : "商家"
            }}
          </el-tag>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="box-card centered-card grey">
          <el-tag type="primary">性别</el-tag>
          <el-tag
            :type="
              user.sex == 1 ? 'success' : user.sex == 0 ? 'primary' : 'danger'
            "
            >{{
              user.sex == 1 ? "男生" : user.sex == 0 ? "女生" : "其他"
            }}</el-tag
          >
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="box-card centered-card green">
          <el-tag type="primary">年龄</el-tag>
          <el-tag
            :type="
              user.age < 25 ? 'success' : user.age < 40 ? 'primary' : 'danger'
            "
            >{{ user.age }}岁</el-tag
          >
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="card-row">
      <el-col :span="24">
        <el-card shadow="hover" class="box-card centered-card gold">
          <div>
            <el-button
              class="el-icon-s-check"
              type="primary"
              @click="userDialog = true"
            >
              查看个人信息</el-button
            >
          </div>
        </el-card></el-col
      >
    </el-row>
    <el-dialog :visible.sync="userDialog" width="80%" center>
      <div style="text-align: center">
        <img
          :src="'http://localhost:8844/upload/avatar/' + user.avatar"
          min-width="150"
          height="150"
        />
      </div>
      <el-card shadow="hover" style="margin-top: 50px">
        <div>
          <el-descriptions
            class="margin-top"
            title="个人信息列表"
            :column="3"
            size="medium"
            border
          >
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                用户名
              </template>
              {{ user.name }}
            </el-descriptions-item>
            <el-descriptions-item v-if="user.telephone">
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                联系方式
              </template>
              {{ user.telephone }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                角色
              </template>
              <el-tag size="small" v-if="user.role === 0" type="success"
                >超级管理员</el-tag
              >
              <el-tag size="small" v-else-if="user.role === 1" type="success"
                >管理员</el-tag
              >
              <el-tag size="small" v-else-if="user.role === 2" type="info"
                >用户</el-tag
              >
              <el-tag size="small" v-else-if="user.role === 3" type="primary"
                >商家</el-tag
              >
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                性别
              </template>
              {{ user.sex == 1 ? "男生" : user.sex == 0 ? "女生" : "其他" }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                注册时间
              </template>
              {{ formatDate(user.registerTime) }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                账号状态
              </template>

              <el-tag size="small" v-if="user.status === 0" type="danger"
                >注销</el-tag
              >
              <el-tag size="small" v-else-if="user.status === 1" type="success"
                >正常</el-tag
              >
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                用户年龄
              </template>
              {{ user.age }}岁
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </el-dialog>
  </div>
</template>
  
  <script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "SystemSetting",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      userDialog: false,
    };
  },
  methods: {
    formatDate(data) {
      if (data == null) {
        return null;
      }
      let dt = new Date(data);
      return (
        dt.getFullYear() +
        "年" +
        (dt.getMonth() + 1) +
        "月" +
        dt.getDate() +
        "日" +
        dt.getHours() +
        "时" +
        dt.getMinutes() +
        "分" +
        dt.getSeconds() +
        "秒"
      );
    },
    init() {
      this.$axios
        .get(this.$httpUrl + "/user/info/" + this.user.id)
        .then((res) => {
          this.user = res.data.data;
        });
    },
  },
  mounted() {
    setInterval(() => {
      this.init();
    }, 1000);
  },
};
</script>
  
 
<style scoped>
.box-card {
  height: auto;
  min-height: 150px;
  border-radius: 25px;
  margin: 20px;
}
.card-content {
  display: flex;
  align-items: center;
}

.image-container {
  height: 50px;
  width: 50px;
  margin-right: 10px;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tag-container {
  margin-left: auto;
}
.card-row {
  display: flex;
  justify-content: center;
}
.base-card {
  height: 50px;
}
.centered-card {
  display: flex;
  justify-content: center;
  align-items: center;
}
.red {
  background-color: rgb(231, 9, 9);
}

.green {
  background-color: rgb(152, 246, 11);
}

.purple {
  background-color: rgb(135, 53, 212);
}

.blue {
  background-color: rgb(10, 138, 207);
}

.orange {
  background-color: chocolate;
}

.grey {
  background-color: grey;
}

.pink {
  background-color: blanchedalmond;
}

.gold {
  background-color: goldenrod;
}
.purper {
  background-color: rgb(129, 26, 225);
}
</style>