<template>
  <div class="register-container">
    <div class="register-box">
      <el-form
        :model="form"
        ref="form"
        :rules="rules"
        label-width="100px"
        style="margin-top: 50px"
      >
        <el-form-item label="用户账号" prop="name">
          <el-input
            style="width: 300px"
            type="text"
            v-model="form.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户性别" prop="sex">
          <el-radio v-model="form.sex" label="1">男</el-radio>
          <el-radio v-model="form.sex" label="0">女</el-radio>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input
            style="width: 300px"
            v-model="form.password"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="rpassword">
          <el-input
            style="width: 300px"
            v-model="form.rpassword"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 20px">
          <el-button
            type="primary"
            class="login-button"
            @click="Register"
            style="width: 300px"
            >注册</el-button
          >
        </el-form-item>
        <el-form-item style="margin-top: 20px">
          <el-button
            type="success"
            class="login-button"
            @click="Login"
            style="width: 300px"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
    
    <script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Register",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.form.password !== value) {
          callback(new Error("确认密码与密码不一致"));
        }
        callback();
      }
    };
    return {
      form: {
        name: "",
        sex: "1",
        password: "",
        rpassword: "",
      },
      rules: {
        name: [{ required: true, message: "请输入账号", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        rpassword: [
          { required: true, validator: validatePass, trigger: "blur" },
          { min: 6, max: 16, message: "6-16个字符", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    Login() {
      this.$router.replace("/");
    },
    Register() {
      // 校验表单
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 发送注册请求
          this.$axios
            .post(this.$httpUrl + "/user/register", this.form)
            .then((res) => res.data)
            .then((res) => {
              console.log(res);
              if (res.code == 200) {
                // 200表示注册成功
                this.$router.replace("/");
                // 回到登录页面
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
                return false;
              }
            });
        } else {
          this.$message({
            message: "校验失败",
            type: "error",
          });
          return false;
        }
      });
    },
  },
};
</script>
    
    <style>
.register-container {
  align-items: center;
  justify-content: center;
  height: 100vh;
  display: flex;
  background-size: 100% 100%;
  background-image: url(http://localhost:8844/upload/file/bj3);
}

.register-box {
  width: 400px;
  border-radius: 6px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  backdrop-filter: blur(10px);
  text-align: center;
  backdrop-filter: blur(10px);
}
.register-button {
  width: 80%;
  margin: 0 auto;
  display: block;
}
</style>
    