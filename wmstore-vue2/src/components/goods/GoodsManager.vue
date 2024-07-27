<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input placeholder="请输入货物名字" prefix-icon="el-icon-search" v-model="name" style="width: 200px; margin-left: 5px"
        @keyup.enter.native="loadPost()">
      </el-input>

      <el-select v-model="storage" placeholder="请选择货物仓库" style="margin-left: 5px">
        <el-option v-for="item in storageData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="type" placeholder="请选择货物类型" style="margin-left: 5px">
        <el-option v-for="item in typeData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="status" placeholder="请选择货物状态" style="margin-left: 5px" v-if="user.role == 0">
        <el-option v-for="item in statusData" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-button icon="el-icon-search" type="primary" style="margin-left: 5px" @click="loadPost()" circle></el-button>
      <el-button icon="el-icon-refresh" type="success" style="margin-left: 5px" @click="resetParam()"
        circle></el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
      <el-button type="danger" slot="reference" :disabled="delBtlStatu" @click="delHandle(null)"
        style="margin-left: 5px" v-if="user.role == 0">批量删除</el-button>
    </div>
    <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" border style="width: 100%" stripe
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="name" label="货物名称" width="150">
      </el-table-column>
      <el-table-column prop="image" label="货物展示" width="150">
        <template slot-scope="scope">
          <img :src="'http://localhost:8844/upload/image/' + scope.row.image" min-width="70" height="70" />
        </template>
      </el-table-column>

      <el-table-column prop="storage" label="货物仓库" width="100" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="type" label="货物类别" width="100" :formatter="formatType">
      </el-table-column>
      <el-table-column prop="count" label="货物库存" width="100">
      </el-table-column>
      <el-table-column prop="remark" label="货物说明" width="300">
      </el-table-column>
      <el-table-column prop="status" label="货物状态" width="80">

        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status == 1">正常</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200" :formatter="formatDate">
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="370px" prop="operate">

        <template slot-scope="scope">
          <el-button type="success" @click="mod(scope.row)" size="small" v-if="user.role != 2">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-switch :value="scope.row.status == 1" active-color="#13ce66" inactive-color="#ff4949" active-text="正常"
            inactive-text="注销" v-if="user.role != 2" @change="modStatus(scope.row.id)">
          </el-switch>
          <el-divider direction="vertical"></el-divider>
          <el-button type="danger" @click="delHandle(scope.row.id)" size="small" v-if="user.role == 0">
            <i class="el-icon-delete"></i></el-button>
          <el-button type="success" @click="inrecord(scope.row)" size="small" style="margin-left: 5px"
            v-if="user.role == 2 && scope.row.status == 1">
            申请入库</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="primary" @click="outrecord(scope.row)" size="small" style="margin-left: 5px"
            v-if="user.role == 2 && scope.row.status == 1">
            申请出库</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
      :page-sizes="[5, 20, 50, 100]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
    <el-dialog title="货物增改表单" :visible.sync="centerDialogVisible" width="50%" center>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" style="width: 800px; margin: 10px auto"
        enctype="multipart/form-data">
        <el-row>
          <el-col :span="12">
            <el-form-item label="货物名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="货物仓库" prop="storage">
              <el-select v-model="form.storage" placeholder="请选择货物仓库">
                <el-option v-for="item in storageData" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="货物类别" prop="type">
              <el-select v-model="form.type" placeholder="请选择货物类别">
                <el-option v-for="item in typeData" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="货物库存" prop="count">
              <el-input-number v-model="form.count" :min="1" :max="9999999" label="请输入货物库存"></el-input-number>
            </el-form-item>
            <el-form-item label="货物说明" prop="remark">
              <el-col :span="20">
                <el-input type="textarea" v-model="form.remark"></el-input>
              </el-col>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="货物图片" prop="avatar">
              <div class="image-show" v-if="imgUrl">
                <img :src="imgUrl" class="avatar" v-if="!ismod" />
                <img :src="'http://localhost:8844/upload/image/' + form.image" class="avatar" v-if="ismod" />
                <span class="actions">
                  <span class="item">
                    <i class="el-icon-delete" @click="del()"> </i>
                  </span>
                </span>
              </div>
              <el-upload v-else action="#" class="uploader-avatar" list-type="picture" :auto-upload="false"
                :show-file-list="false" :on-change="imgPreview">
                <i class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="出入库申请" :visible.sync="recordDialogVisible" width="50%" center>
      <div style="text-align: center">
        <img :src="'http://localhost:8844/upload/image/' + goodsData.image" min-width="150" height="150" />
      </div>
      <el-descriptions class="margin-top" title="货物信息" :column="2" size="medium" border>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-user"></i>
            货物名称
          </template>
          {{ goodsData.name }}
        </el-descriptions-item>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            库存数量
          </template>
          {{ goodsData.count }}
        </el-descriptions-item>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-location-outline"></i>
            储货仓库
          </template>
          {{ formatStorage(goodsData) }}
        </el-descriptions-item>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-tickets"></i>
            货物类型
          </template>
          <el-tag size="small">{{ formatType(goodsData) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-office-building"></i>
            货物信息 </template>{{ goodsData.remark }}
        </el-descriptions-item>
        <el-descriptions-item>

          <template slot="label">
            <i class="el-icon-office-building"></i>
            货物创建时间 </template>{{ formatDates(goodsData.createTime) }}
        </el-descriptions-item>
      </el-descriptions>
      <el-form :model="recordform" ref="recordform" label-width="100px" style="width: 800px; margin: 50px auto">
        <el-form-item label="申请用户">
          <el-col :span="20">
            <el-select v-model="recordform.user" filterable placeholder="请选择用户" style="margin-left: 5px"
              :disabled="user.role == 2">
              <el-option v-for="item in userData" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item :label="recordform.action == 1 ? '入库数量' : '出库数量'" prop="count">
          <el-col :span="20">
            <el-input-number v-model="recordform.count" :min="1" :max="999999999"></el-input-number>
          </el-col>
        </el-form-item>
        <el-form-item :label="recordform.action == 1 ? '入库说明' : '出库说明'">
          <el-col :span="20">
            <el-input type="textarea" v-model="recordform.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="recordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="record">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "GoodsManager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      pageSize: 5,
      pageNum: 1,
      total: 0,
      tableData: [],
      name: "",
      storage: "",
      type: "",
      status: 1,
      statusData: [
        {
          value: 1,
          label: "正常",
        },
        {
          value: 0,
          label: "禁用",
        },
      ],
      goodsData: {},
      storageData: [],
      typeData: [],
      userData: [],
      form: {
        id: "",
        name: "",
        storage: "",
        type: "",
        status: 1,
        remark: "",
        count: 1,
        image: null,
      },
      rules: {
        name: [{ required: true, message: "请输入货物名称", trigger: "blur" }],
        storage: [{ required: true, message: "请选择货物仓库", trigger: "blur" }],
        type: [{ required: true, message: "请选择货物分类", trigger: "blur" }],
        count: [{ required: true, message: "请输入货物库存", trigger: "blur" }],
        image: [{ required: true, message: "请上传货物图片", trigger: "blur" }],
      },
      imgUrl: "",
      dialogUrl: "",
      file: null,
      delBtlStatu: true,
      multipleSelection: [],
      ismod: false,
      centerDialogVisible: false,
      recordDialogVisible: false,
      dialogVisible: false,
      recordform: {
        goods: "",
        count: "",
        user: "",
        remark: "",
        action: "",
        // 0 是出库，1是入库
      },
      recordrules: {
        count: [
          { required: true, message: "请输入数量", trigger: "blur" },
          {
            type: "number",
            min: 1,
            max: 10000,
            message: "数量应在1-9999之间",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    resetParam() {
      if (this.user.role == 0) this.status = "";
      else this.status = 1;
      this.name = "";
      this.storage = "";
      this.type = "";
    },

    resetForm() {
      this.$refs.form.resetFields();
      this.form.id = "";
      this.form.name = "";
      this.form.storage = "";
      this.form.type = "";
      this.form.remark = "";
      this.form.status = 1;
      this.form.count = 1;
      this.form.image = null;
      this.imgUrl = "";
      this.dialogUrl = "";
      this.ismod = false;
      this.dialogVisible = false;
      this.file = null;
    },
    loadUser() {
      this.$axios
        .post(this.$httpUrl + "/user/listPage", {
          pageSize: 99999,
          pageNum: 1,
          param: {
            role: 2 + "",
          },
        })
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.userData = res.data;
          } else {
            this.$message({
              message: "数据查询失败",
              type: "warning",
            });
          }
        });
    },
    loadStorage() {
      this.$axios
        .get(this.$httpUrl + "/storage/list")
        .then((res) => res.data)
        .then((res) => {
          this.storageData = res;
        });
    },
    loadType() {
      this.$axios
        .get(this.$httpUrl + "/type/list")
        .then((res) => res.data)
        .then((res) => {
          this.typeData = res;
        });
    },
    formatStorage(row) {
      console.log(row);
      let temp = this.storageData.find((item) => {
        return item.id == row.storage;
      });
      return temp && temp.name;
    },
    formatType(row) {
      let temp = this.typeData.find((item) => {
        return item.id == row.type;
      });
      return temp && temp.name;
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
    formatDates(data) {
      console.log(data);
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
    record() {
      if (this.recordform.action == 0) {
        this.$axios
          .post(this.$httpUrl + "/outrecord/save", this.recordform)
          .then((res) => res.data)
          .then((res) => {
            console.log(res);
            if (res.code == 200) {
              this.$message({
                message: "添加成功！",
                type: "success",
              });
              this.recordDialogVisible = false;

              this.loadPost();
              //关闭弹窗
            } else {
              this.$message({
                message: res.msg,
                type: "error",
              });
            }
          });
      } else {
        this.$axios
          .post(this.$httpUrl + "/inrecord/save", this.recordform)
          .then((res) => res.data)
          .then((res) => {
            console.log(res);
            if (res.code == 200) {
              this.$message({
                message: "添加成功！",
                type: "success",
              });
              this.recordDialogVisible = false;

              this.loadPost();
              //关闭弹窗
            } else {
              this.$message({
                message: res.msg,
                type: "error",
              });
            }
          });
      }
    },
    outrecord(row) {
      this.recordDialogVisible = true;
      this.$nextTick(() => {
        this.recordform.action = 0;
        this.recordform.goods = row.id;
        this.recordform.user = this.user.id;
        this.$axios.get(this.$httpUrl + "/goods/info/" + row.id).then((res) => {
          this.goodsData = res.data.data;
        });
      });
    },
    inrecord(row) {
      this.recordDialogVisible = true;
      this.$nextTick(() => {
        this.recordform.action = 1;
        this.recordform.goods = row.id;
        this.recordform.user = this.user.id;
        this.$axios.get(this.$httpUrl + "/goods/info/" + row.id).then((res) => {
          this.goodsData = res.data.data;
        });
      });
    },
    add() {
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        this.resetForm();
      });
    },
    mod(row) {
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        //赋值到表单
        this.$axios.get(this.$httpUrl + "/goods/info/" + row.id).then((res) => {
          this.form = res.data.data;

          if (this.form.image != "") {
            this.imgUrl = true;
            this.ismod = true;
          }
        });
      });
    },
    async save() {
      if (this.file != null) {
        this.submitUpload();
      }
      setTimeout(() => {
        this.$refs.form.validate((valid) => {
          if (valid) {
            if (this.form.id) {
              this.doMod();
            } else this.doSave();
          } else {
            this.$message({
              message: "请填写正确数据",
              type: "error",
            });
            return false;
          }
        });
      }, 500);
    },
    doSave() {
      this.$axios
        .post(this.$httpUrl + "/goods/save", this.form)
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
        .post(this.$httpUrl + "/goods/update", this.form)
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
    submitUpload() {
      if (this.file == null) {
        this.$message({
          message: "请先选择货物头像",
          type: "error",
        });
        return false;
      } else {
        const config = {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        };
        if (this.beforeUpload(this.file.name)) {
          let fd = new FormData();
          fd.append("file", this.file.raw);
          fd.append("fileName", this.file.name);
          this.$axios
            .post(this.$httpUrl + "/upload/image", fd, config)
            .then((res) => {
              console.log(res);
              if (res.status === 200) {
                this.form.image = res.data.data;
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
        } else {
          return false;
        }
      }
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
        .post(this.$httpUrl + "/goods/listPage", {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            name: this.name,
            status: this.status + "",
            type: this.type + "",
            storage: this.storage + "",
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
        .post(this.$httpUrl + "/goods/control/" + id)
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
      this.$confirm("将删除货物一切有关数据， 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post(this.$httpUrl + "/goods/deleteBatch", ids, {
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
    this.loadStorage();
    this.loadType();
    this.loadUser();
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