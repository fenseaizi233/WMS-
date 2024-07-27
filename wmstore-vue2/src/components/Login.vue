<template>
  <div class="login-container">
    <div class="login-box">
      <div class="avatar-container">
        <div class="avatar-border">
          <div>
            <el-avatar
              :size="100"
              :src="
                form.avatar
                  ? 'http://localhost:8844/upload/avatar/' + form.avatar
                  : 'http://localhost:8844/upload/avatar/' + 'defaultman'
              "
            >
            </el-avatar>
          </div>
        </div>
      </div>
      <el-form
        :model="form"
        ref="form"
        :rules="rules"
        label-width="100px"
        style="margin-top: 50px"
      >
        <el-form-item label="用户名" prop="name">
          <el-input
            v-model="form.name"
            aria-placeholder="用户名/邮箱"
            @keyup.enter.native="selectUser"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 20px">
          <div style="display: flex; justify-content: center">
            <el-input
              v-model="form.verCode"
              prefix-icon="el-icon-user"
              style="width: 45%; margin-top: 5px; margin-right: 15px"
              placeholder="请输入验证码"
            ></el-input>
            <img
              :src="captchaUrl"
              @click="clickImg()"
              width="140px"
              height="46px"
            />
          </div>
        </el-form-item>
        <el-form-item style="margin-top: 20px">
          <el-button
            type="primary"
            class="login-button"
            @click="Login"
            style="width: 300px"
            >登录</el-button
          >
        </el-form-item>

        <el-form-item style="margin-top: 20px">
          <el-button
            type="success"
            class="login-button"
            @click="Register"
            style="width: 300px"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  data() {
    return {
      key: "",
      captchaUrl: "",
      form: {
        name: "",
        password: "",
        avatar: null,
      },
      rules: {
        name: [
          {
            required: true,
            message: "请输入账号",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.key = Math.random();
    this.captchaUrl = "http://localhost:8844/captcha?key=" + this.key;
  },
  methods: {
    selectUser() {
      this.$axios
        .get(this.$httpUrl + "/user/getAvatar/" + this.form.name)
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.form.avatar = res.data;
          }
        });
    },
    Login() {
      if (this.form.avatar == null) {
        this.selectUser();
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios
            .post(this.$httpUrl + "/user/login/?key=" + this.key, this.form)
            .then((res) => res.data)
            .then((res) => {
              console.log(res);
              if (res.code == 200) {
                this.$store.commit("setUser", JSON.stringify(res.data.user));
                this.$store.commit("setMenu", JSON.stringify(res.data.menu));
                this.$message({
                  message: "恭喜你，登录成功！欢迎回家",
                  type: "success",
                  onClose: () => {
                    this.$router.push("/");
                  },
                });
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
                this.key = Math.random();
                this.captchaUrl =
                  "http://localhost:8844/captcha?key=" + this.key;
                this.form.verCode = "";
              }
            });
        } else {
          this.$message.error("校验失败");
        }
      });
    },
    Register() {
      this.$router.push("/Register");
    },
    clickImg() {
      this.key = Math.random();
      this.captchaUrl = "http://localhost:8844/captcha?key=" + this.key;
    },
  },
};
</script>

<style>
.login-container {
  align-items: center;
  justify-content: center;
  height: 100vh;
  display: flex;
  background-size: 100% 100%;
  background-image: url(http://localhost:8844/upload/file/bj);
}

.login-box {
  width: 400px;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  position: relative;
  backdrop-filter: blur(10px);
  text-align: center;
}

.avatar-container {
  position: absolute;
  top: -50px;
  left: 50%;
  transform: translateX(-50%);
}

.avatar-border {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px solid #ccc;
  position: relative;
  z-index: 1;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 47px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
}

.login-button {
  width: 80%;
  margin: 0 auto;
  display: block;
}
</style>
