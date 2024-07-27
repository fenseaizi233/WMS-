<template>
  <div>
    <div style="display: flex; line-height: 60px">
      <div style="margin-top: 8px">
        <i
          :class="icon"
          style="font-size: 30px; cursor: pointer"
          @click="collaspe"
        ></i>
      </div>
      <div style="flex: 1; text-align: center; font-size: 24px">
        <span> 基于SpringBoot+Vue2前后端分离的货运列车出入库管理系统</span>
      </div>

      <div style="margin-top: 5px; margin-left: 50px">
        <el-avatar
          :size="50"
          :src="'http://localhost:8844/upload/avatar/' + user.avatar"
        >
        </el-avatar>
      </div>
      <el-dropdown style="margin-left: 15px; margin-top: 5px">
        <el-button type="primary">
          {{ user.name
          }}<i
            class="el-icon-arrow-down el-icon--right"
            style="margin-left: 15px"
          ></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="toIndex">系统首页</el-dropdown-item>
          <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
          <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          <el-dropdown-item @click.native="delUser">注销用户</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <div style="margin-top: 10px; margin-left: 30px">
        <i
          class="el-icon-setting"
          style="font-size: 40px; cursor: pointer"
          @click="drawer = true"
        ></i>
      </div>
    </div>
    <el-drawer :visible.sync="drawer" direction="rtl" size="25%">
      <el-card
        shadow="hover"
        class="box-card centered-card blue"
        style="display: flex; align-items: center"
        :show-close="false"
      >
        <div>
          <el-avatar
            size="large"
            :src="'http://localhost:8844/upload/avatar/' + user.avatar"
            fit="cover"
            style="width: 100px; height: 100px"
          ></el-avatar>
        </div>
        <el-button type="success" size="small" @click="modAvatar"
          >更换头像</el-button
        >
      </el-card>
      <el-col :span="24">
        <el-card shadow="hover" class="box-card centered-card pink">
          <div>
            <el-button
              class="el-icon-edit-outline"
              type="success"
              @click="dialogNameVisible = true"
              >修改昵称</el-button
            >
          </div>
        </el-card>
        <el-card shadow="hover" class="box-card centered-card green">
          <div>
            <el-button
              class="el-icon-lock"
              type="primary"
              @click="dialogPwdVisible = true"
            >
              修改密码</el-button
            >
          </div>
        </el-card>

        <el-card shadow="hover" class="box-card centered-card orange">
          <div>
            <el-button
              class="el-icon-switch-button"
              type="danger"
              @click="logout"
              >退出登录</el-button
            >
          </div>
        </el-card>
        <el-card shadow="hover" class="box-card centered-card red">
          <div>
            <el-button class="el-icon-delete" type="danger" @click="delUser"
              >注销账号</el-button
            >
          </div>
        </el-card>
      </el-col>
    </el-drawer>
    <el-dialog
      title="修改昵称"
      :visible.sync="dialogNameVisible"
      width="30%"
      center
    >
      <el-form
        :model="modNameForm"
        label-width="100px"
        :rules="namerules"
        ref="modNameForm"
      >
        <el-form-item label="账号昵称">
          <el-input
            style="width: 200px"
            type="text"
            :placeholder="user.name"
            size="small"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="新昵称" prop="name">
          <el-input
            style="width: 200px"
            type="text"
            v-model="modNameForm.name"
            autocomplete="off"
            size="small"
            @keyup.enter.native="modName"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="dialogNameVisible = false"
          >取消修改</el-button
        >
        <el-button type="success" @click="modName">开始修改</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="修改密码"
      :visible.sync="dialogPwdVisible"
      width="30%"
      center
    >
      <el-form
        :model="modPwdForm"
        label-width="100px"
        :rules="rules"
        ref="modPwdForm"
      >
        <el-form-item label="原密码" prop="password">
          <el-input
            style="width: 200px"
            type="text"
            v-model="modPwdForm.password"
            show-password
            autocomplete="off"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newpassword">
          <el-input
            style="width: 200px"
            type="password"
            v-model="modPwdForm.newpassword"
            show-password
            autocomplete="off"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="重复密码" prop="rpassword">
          <el-input
            style="width: 200px"
            type="password"
            v-model="modPwdForm.rpassword"
            show-password
            autocomplete="off"
            size="small"
            @keyup.enter.native="modPwd"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="dialogPwdVisible = false"
          >取消修改</el-button
        >
        <el-button type="success" @click="modPwd">开始修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Header",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.modPwdForm.newpassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      drawer: false,
      dialogNameVisible: false,
      dialogPwdVisible: false,
      modNameForm: {
        name: "",
      },
      namerules: {
        name: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 2, max: 10, message: "2-10个字符", trigger: "blur" },
        ],
      },
      modPwdForm: {
        password: "",
        newpassword: "",
        rpassword: "",
      },
      rules: {
        password: [
          { required: true, message: "请输入原密码", trigger: "blur" },
          { min: 6, max: 16, message: "6-16个字符", trigger: "blur" },
        ],
        newpassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, max: 16, message: "6-16个字符", trigger: "blur" },
        ],
        rpassword: [
          { required: true, validator: validatePass, trigger: "blur" },
          { min: 6, max: 16, message: "6-16个字符", trigger: "blur" },
        ],
      },
    };
  },
  props: {
    icon: String,
  },

  methods: {
    modAvatar() {
      const input = document.createElement("input");
      // 创建input元素
      input.type = "file";
      // 设计文件类型input
      input.accept = "image/*";
      // 接收图片
      input.onchange = async () => {
        const file = input.files[0];
        // 获取文件
        if (file) {
          // 使用 FormData 构造表单数据
          let fd = new FormData();
          fd.append("file", file); // 修改这行代码
          fd.append("fileName", file.name);
          // 发送 POST 请求将表单数据提交到服务器
          try {
            const config = {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            };
            // 上传文件
            await this.$axios
              .post(this.$httpUrl + "/upload/avatar", fd, config)
              .then((res) => {
                console.log(res);
                if (res.status === 200) {
                  this.modUserAvatar(res.data.data);
                  this.$message({
                    message: "修改成功！",
                    type: "success",
                  });
                } else {
                  this.$message({
                    message: "未上传成功！",
                    type: "error",
                  });
                }
              });
          } catch (err) {
            console.error(err);
            // 提示用户上传失败
            this.$message.error("修改失败，请重试");
          }
        } else {
          // 用户没有选择文件
          this.$message.error("未选择文件");
        }
      };
      input.click();
    },
    // 提交修改用户头像
    modUserAvatar(avatar) {
      this.$axios
        .get(
          this.$httpUrl +
            "/user/modAvatar?id=" +
            this.user.id +
            "&avatar=" +
            avatar
        )
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.getUserById(this.user.id);
          } else {
            this.$message({
              message: res.msg,
              type: "error",
            });
          }
        });
    },
    getUserById() {
      this.$axios
        .get(this.$httpUrl + "/user/info/" + this.user.id)
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res) {
            this.$store.commit("setUser", JSON.stringify(res.data));
            this.user = res.data;
            // 更新本地数据
          } else {
            this.$message({
              message: res.msg,
              type: "error",
            });
          }
        });
    },
    collaspe() {
      this.$emit("doCollaspe");
    },
    toUser() {
      this.$router.push("/Setting");
    },
    toIndex() {
      this.$router.push("/Home");
    },
    logout() {
      this.$confirm("您确定要退出登录吗?", "提示", {
        confirmButtonText: "确定", //确认按钮的文字显示
        type: "warning",
        center: true, //文字居中显示
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "退出登录成功",
          });
          this.$store.commit("resetState");
          this.$router.push("/Login");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消退出登录",
          });
        });
    },
    delUser() {
      this.$confirm("您确定要注销用户吗?", "一旦注销，您将再也无法进入系统", {
        confirmButtonText: "确定", //确认按钮的文字显示
        type: "warning",
        center: true, //文字居中显示
      })
        .then(() => {
          this.$axios
            .get(this.$httpUrl + "/user/delete?id=" + this.user.id)
            .then((res) => res.data)
            .then((res) => {
              console.log(res);
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "注销用户成功",
                });

                this.$router.push("/");
                sessionStorage.clear();
              } else {
                this.$message({
                  message: "注销失败，请稍后再试！",
                  type: "error",
                });
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消用户注销",
          });
        });
    },
    modName() {
      this.$refs.modNameForm.validate((valid) => {
        if (valid) {
          this.$axios
            .get(
              this.$httpUrl +
                "/user/modName?id=" +
                this.user.id +
                "&name=" +
                this.modNameForm.name
            )
            .then((res) => res.data)
            .then((res) => {
              console.log(res);
              if (res.code == 200) {
                this.$message({
                  message: "修改成功",
                  type: "success",
                });
                this.dialogNameVisible = false;
                this.getUserById(this.user.id);
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
              }
            });
        } else {
          this.$message({
            message: "新账号昵称不符合规范",
            type: "warning",
          });
          return false;
        }
      });
    },
    modPwd() {
      this.$refs.modPwdForm.validate((valid) => {
        if (valid) {
          this.$axios
            .get(
              this.$httpUrl +
                "/user/modPwd?id=" +
                this.user.id +
                "&password=" +
                this.modPwdForm.newpassword
            )
            .then((res) => res.data)
            .then((res) => {
              console.log(res);
              if (res.code == 200) {
                this.$axios
                  .get(this.$httpUrl + "/user/logout")
                  .then((res) => res.data)
                  .then((res) => {
                    if (res.code == 200) {
                      this.$store.commit("resetState");
                      this.$message({
                        message: "修改成功",
                        type: "success",
                      });
                      (this.dialogPwdVisible = false),
                        this.$message({
                          type: "success",
                          message: "退出登录成功",
                        });
                      this.$router.push("/login");
                    } else {
                      this.$message({
                        message: res.msg,
                        type: "error",
                      });
                    }
                  });
              } else {
                this.$message({
                  message: "修改失败",
                  type: "error",
                });
              }
            });
        } else {
          this.$message({
            message: "请检查密码",
            type: "warning",
          });
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
.box-card {
  height: auto;
  min-height: 50px;
  text-align: center;
  border-radius: 25px;
  margin: 10px;
}

.card-row {
  display: flex;
  justify-content: center;
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
</style>
