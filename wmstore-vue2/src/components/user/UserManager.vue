<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input
        placeholder="请输入名字"
        prefix-icon="el-icon-search"
        v-model="name"
        style="width: 200px; margin-left: 5px"
        @keyup.enter.native="loadPost()"
      >
      </el-input>
      <el-input
        placeholder="请输入手机号"
        prefix-icon="el-icon-search"
        v-model="telephone"
        style="width: 200px; margin-left: 5px"
        @keyup.enter.native="loadPost()"
      >
      </el-input>
      <el-select
        v-model="sex"
        placeholder="请选择性别"
        style="margin-left: 5px"
      >
        <el-option
          v-for="item in sexData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="status"
        placeholder="请选择用户状态"
        style="margin-left: 5px"
        v-if="user.role == 0"
      >
        <el-option
          v-for="item in statusData"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button
        icon="el-icon-search"
        type="primary"
        style="margin-left: 5px"
        @click="loadPost()"
        circle
      ></el-button>
      <el-button
        icon="el-icon-refresh"
        type="success"
        style="margin-left: 5px"
        @click="resetParam()"
        circle
      ></el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add"
        >新增</el-button
      >
      <el-button
        type="danger"
        slot="reference"
        :disabled="delBtlStatu"
        @click="delHandle(null)"
        style="margin-left: 5px"
        v-if="user.role == 0"
        >批量删除</el-button
      >
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      border
      tooltip-effect="dark"
      style="width: 100%"
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="avatar" label="用户头像" width="100">
        <template slot-scope="scope">
          <el-avatar
            size="small"
            :src="'http://localhost:8844/upload/avatar/' + scope.row.avatar"
            v-if="scope.row.avatar"
          >
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="用户昵称" width="120">
      </el-table-column>
      <el-table-column
        prop="password"
        label="用户密码"
        v-if="user.role == 0"
        width="120"
      >
      </el-table-column>
      <el-table-column prop="sex" label="用户性别" width="80">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.sex == 1">男</el-tag>
          <el-tag type="danger" v-else>女</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="telephone" label="电话号码" width="150">
      </el-table-column>
      <el-table-column prop="age" label="用户年龄" width="100">
      </el-table-column>
      <el-table-column prop="status" label="用户状态" width="80">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status == 1">正常</el-tag>
          <el-tag type="danger" v-else>注销</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="registerTime"
        label="注册时间"
        width="200"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="修改时间"
        width="200"
        :formatter="formatDate"
      >
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="370px" prop="operate">
        <template slot-scope="scope">
          <el-button type="success" @click="mod(scope.row)" size="small"
            >编辑</el-button
          >
          <el-divider direction="vertical"></el-divider>
          <el-switch
            :value="scope.row.status == 1"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="正常"
            inactive-text="注销"
            @change="modStatus(scope.row.id)"
          >
          </el-switch>
          <el-divider direction="vertical"></el-divider>
          <el-button
            type="danger"
            @click="delHandle(scope.row.id)"
            size="small"
            v-if="user.role == 0"
          >
            <i class="el-icon-delete"></i
          ></el-button>
          <el-button
            type="success"
            @click="repass(scope.row.id, scope.row.name)"
            size="small"
            style="margin-left: 5px"
          >
            <i class="el-icon-refresh"></i
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[5, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-dialog
      title="用户增改表单"
      :visible.sync="centerDialogVisible"
      width="50%"
      center
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="form"
        label-width="100px"
        style="width: 800px; margin: 10px auto"
        enctype="multipart/form-data"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="用户密码" prop="password">
              <el-input v-model="form.password" type="password"></el-input>
            </el-form-item>
            <el-form-item label="用户手机号" prop="telephone">
              <el-input v-model="form.telephone"></el-input>
            </el-form-item>
            <el-form-item label="用户年龄" prop="age">
              <el-input v-model="form.age" :min="18" :max="50"></el-input>
            </el-form-item>
            <el-form-item label="用户性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择用户性别">
                <el-option
                  v-for="item in sexData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="用户头像" prop="avater">
              <el-upload
                action="#"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :auto-upload="false"
                :on-exceed="handleExceed"
                accept="image/*"
                :on-remove="handleRemove"
                :on-change="handleChanges"
                :before-upload="beforePictureUpload"
                :file-list="fileList"
                :limit="1"
              >
                <i class="el-icon-plus"></i>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="" />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "UserManager",
  data() {
    const validateAge = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入年龄"));
      } else {
        if (value < 18 || value > 50) {
          callback(new Error("年龄在18-50之间"));
        }
        callback();
      }
    };
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      pageSize: 5,
      pageNum: 1,
      total: 0,
      tableData: [],
      name: "",
      sex: "",
      telephone: "",
      status: 1,
      statusData: [
        {
          value: 1,
          label: "正常",
        },
        {
          value: 0,
          label: "注销",
        },
      ],
      sexData: [
        {
          id: 0,
          name: "女",
        },
        {
          id: 1,
          name: "男",
        },
      ],
      form: {
        id: "",
        name: "",
        telephone: "",
        password: "",
        status: 1,
        sex: 1,
        role: 2,
        age: 18,
        avatar: null,
      },
      rules: {
        name: [
          { required: true, message: "请输入用户姓名", trigger: "blur" },
          {
            min: 2,
            max: 16,
            message: "长度在 2 到 16 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入用户密码", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "长度在 6 到 16 个字符",
            trigger: "blur",
          },
        ],
        age: [
          { required: true, message: "请输入用户年龄", trigger: "blur" },
          {
            type: "number",
            validator: validateAge,
            trigger: "blur",
          },
        ],
        sex: [{ required: true, message: "请选择用户性别", trigger: "blur" }],
        avatar: [{ required: true, message: "请上传头像", trigger: "blur" }],
        telephone: [
          { required: true, message: "请输入用户手机号", trigger: "blur" },
          {
            min: 11,
            max: 11,
            message: "清输入11位手机号",
            trigger: "blur",
          },
          {
            pattern:
              /^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}$/,
            message: "手机号格式不正确",
          },
        ],
      },
      imgUrl: "",
      dialogUrl: "",
      file: null,
      delBtlStatu: true,
      multipleSelection: [],
      ismod: false,
      centerDialogVisible: false,

      dialogVisible: false,
    };
  },
  methods: {
    resetParam() {
      if (this.user.role == 0) this.status = "";
      else this.status = 1;
      this.name = "";
      this.telephone = "";
      this.sex = "";
    },
    resetForm() {
      this.form = {
        id: "",
        name: "",
        telephone: "",
        password: "",
        status: 1,
        sex: 1,
        role: 2,
        age: 18,
        avatar: null,
      };
      this.fileList = [];
      this.picList = [];
      this.dialogImageUrl = "";
      this.dialogVisible = false;
      this.handleRemove();
    },
    formatDate(row, column) {
      let data = row[column.property];
      if (data == null) {
        return null;
      }
      let dt = new Date(data);
      return (
        dt.getFullYear() +
        "-" +
        (dt.getMonth() + 1) +
        "-" +
        dt.getDate() +
        " " +
        dt.getHours() +
        ":" +
        dt.getMinutes() +
        ":" +
        dt.getSeconds()
      );
    },
    add() {
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        this.resetForm();
      });
    },
    mod(row) {
      this.$nextTick(() => {
        //赋值到表单
        this.$axios.get(this.$httpUrl + "/user/info/" + row.id).then((res) => {
          this.form = res.data.data;

          if (this.form.avatar != "") {
            this.fileList = [];
            this.fileList.push({
              url: "http://localhost:8844/upload/avatar/" + this.form.avatar,
            });
          }
          this.centerDialogVisible = true;
        });
      });
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            if (
              this.fileList[0].url !==
              "http://localhost:8844/upload/avatar/" + this.form.avatar
            ) {
              this.submitUpload(this.fileList[0]);
              this.doMod(); // 完成上传后执行 doMod 函数
            } else {
              this.doMod();
            }
          } else {
            if (this.fileList.length) {
              this.submitUpload(this.fileList[0]);
              this.doSave(); // 完成上传后执行 doSave 函数
            } else {
              this.doSave();
            }
          }
        } else {
          this.$message({
            message: "请填写正确数据",
            type: "error",
          });
          return false;
        }
      });
    },
    doSave() {
      this.$axios
        .post(this.$httpUrl + "/user/save", this.form)
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.$message({
              message: "操作成功！",
              type: "success",
            });
            this.centerDialogVisible = false;
            this.loadPost();
            this.resetForm();
          } else {
            this.$message({
              message: res.msg,
              type: "error",
            });
          }
        });
    },
    doMod() {
      this.$axios
        .post(this.$httpUrl + "/user/update", this.form)
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.$message({
              message: "操作成功！",
              type: "success",
            });
            this.centerDialogVisible = false;
            this.loadPost();
            this.resetForm();
          } else {
            this.$message({
              message: res.msg,
              type: "error",
            });
          }
        });
    },
    submitUpload(file) {
      const config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      };

      let fd = new FormData();
      fd.append("file", file.raw);
      fd.append("fileName", file.name);
      this.$axios
        .post(this.$httpUrl + "/upload/avatar", fd, config)
        .then((res) => {
          console.log(res);
          if (res.status === 200) {
            this.form.avatar = res.data.data;
            this.$message({
              message: "上传成功！",
              type: "success",
            });
          } else {
            this.$message({
              message: "未上传成功！",
              type: "error",
            });
          }
        });
    },
    handleSuccess(response) {
      console.log(response);
      this.$message({
        message: "上传成功",
        type: "success",
      });
      return true;
    },

    beforePictureUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPng = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPng) {
        this.$message.error("上传图片只能是 JPG或png 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return (isJPG || isPng) && isLt2M;
    },
    handleRemove(file, fileList) {
      console.log(fileList);
      this.fileList = [];
      for (const i in this.picList) {
        if (this.picList[i].key === file.uid) {
          this.picList.splice(i, 1);
        }
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    /**
     * 清空上传组件
     */
    emptyUpload() {
      const mainImg = this.$refs.upload;
      if (mainImg) {
        if (mainImg.length) {
          mainImg.forEach((item) => {
            item.clearFiles();
          });
        } else {
          this.$refs.upload.clearFiles();
        }
      }
    },
    handleChanges(files) {
      this.fileList.push(files);
      this.submitUpload(files);
    },
    handleExceed(files) {
      console.log(files);
      this.$message.error("只能上传一张头像!");
    },
    beforeUpload(fileName) {
      const isImg = this.checkImgType(fileName);
      if (!isImg) {
        this.$message.error("只能上传图片文件");
      }
      return isImg;
    },
    checkImgType(fileName) {
      //用文件名name后缀判断文件类型，可用size属性判断文件大小不能超过500k ， 前端直接判断的好处，免去服务器的压力。
      console.log(fileName);
      if (!/\.(jpg|jpeg|png)$/.test(fileName)) {
        return false;
      } else {
        return true;
      }
    },
    imgPreview: function (file) {
      //生成临时缩略图
      this.imgUrl = URL.createObjectURL(file.raw);
      this.file = file;
    },
    del: function () {
      this.imgUrl = "";
      this.form.avatar = "";
      this.ismod = false;
    },
    loadPost() {
      this.$axios
        .post(this.$httpUrl + "/user/listPage", {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            role: 2 + "",
            name: this.name,
            status: this.status + "",
            sex: this.sex + "",
            telephone: this.telephone,
          },
        })
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.tableData = res.data;
            this.total = res.total;
          } else {
            this.$message({
              message: "数据查询失败",
              type: "warning",
            });
          }
        });
    },
    modStatus(id) {
      this.$axios
        .post(this.$httpUrl + "/user/control/" + id)
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.loadPost();
            this.$message({
              message: "修改成功",
              type: "success",
            });
          } else {
            this.$message({
              message: "操作失败！",
              type: "error",
            });
          }
        });
    },
    repass(id, username) {
      this.$confirm("将重置用户【" + username + "】的密码, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post(this.$httpUrl + "/user/repass?id=" + id, {
              headers: {
                "Content-Type": "application/json;charset=UTF-8",
              },
            })
            .then((res) => {
              console.log(res);
              this.$message({
                showClose: true,
                message: "恭喜你，操作成功",
                type: "success",
              });
              this.loadPost();
            })
            .catch((err) => {
              console.error(err);
              this.$message({
                showClose: true,
                message: "操作失败，请重试",
                type: "error",
              });
            });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            message: "已取消操作",
            type: "info",
          });
        });
    },
    delHandle(id) {
      console.log(id);
      var ids = [];
      if (id) {
        ids.push(id);
      } else {
        this.multipleSelection.forEach((row) => {
          ids.push(row.id);
        });
      }
      console.log(ids);
      this.$confirm("将删除用户一切有关数据， 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post(this.$httpUrl + "/user/deleteBatch", ids, {
              headers: {
                "Content-Type": "application/json;charset=UTF-8",
              },
            })
            .then((res) => {
              console.log(res);

              this.$message({
                showClose: true,
                message: "恭喜你，操作成功",
                type: "success",
              });
              this.loadPost();
            })
            .catch((err) => {
              console.error(err);
              this.$message({
                showClose: true,
                message: "操作失败，请重试",
                type: "error",
              });
            });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            message: "已取消操作",
            type: "info",
          });
        });
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.delBtlStatu = val.length == 0;
    },

    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.loadPost();
    },
  },
  mounted() {
    this.loadPost();
  },
};
</script>

<style>
.el-pagination {
  text-align: center;
  margin-top: 22px;
}

.uploader-avatar {
  display: flex;
  /* 添加flex属性 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  box-sizing: border-box;
  width: 200px;
  height: 200px;
  cursor: pointer;
  line-height: 200px;
  vertical-align: top;
  margin-bottom: 10px;
  overflow: hidden;
}

.img-show {
  position: relative;
  border: 1px solid #c0ccda;
  border-radius: 6px;
  box-sizing: border-box;
  width: 200px;
  height: 200px;
  cursor: pointer;
  overflow: hidden;
}

.uploader-avatar:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.avatar {
  width: 200px;
  height: 200px;
  display: block;
}

.actions {
  position: absolute;
  width: 200px;
  height: 100%;
  line-height: 200px;
  left: 0;
  top: 0;
  cursor: default;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
}

.actions:hover {
  opacity: 1;
}

.actions:hover span {
  display: inline-block;
}

.actions span {
  display: none;
  margin: 0 16px;
  cursor: pointer;
}
</style>
