<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input
        placeholder="请输入仓库名字"
        prefix-icon="el-icon-search"
        v-model="name"
        style="width: 200px; margin-left: 5px"
        @keyup.enter.native="loadPost()"
      >
      </el-input>

      <el-select
        v-model="status"
        placeholder="请选择仓库状态"
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
      tooltip-effect="dark"
      border
      style="width: 100%"
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>

      <el-table-column prop="name" label="仓库名称" width="200">
      </el-table-column>

      <el-table-column prop="remark" label="仓库介绍" width="400">
      </el-table-column>
      <el-table-column prop="status" label="仓库状态" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status == 1">正常</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="200"
        :formatter="formatDate"
      >
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="300" prop="operate">
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
            inactive-text="禁用"
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
      title="提示"
      :visible.sync="centerDialogVisible"
      width="30%"
      center
    >
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="仓库名字" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库介绍" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>
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
  name: "RecordManager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      pageSize: 5,
      pageNum: 1,
      total: 0,
      tableData: [],
      name: "",
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

      form: {
        id: "",
        name: "",
        remark: "",
        status: 1,
      },
      rules: {
        name: [{ required: true, message: "请输入仓库名称", trigger: "blur" }],
      },

      delBtlStatu: true,
      multipleSelection: [],
      centerDialogVisible: false,
    };
  },
  methods: {
    resetParam() {
      if (this.user.role == 0) this.status = "";
      else this.status = 1;
      this.name = "";
    },
    resetForm() {
      this.$refs.form.resetFields();
      this.form.id = "";
      this.form.name = "";

      this.form.status = 1;
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
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        //赋值到表单
        this.$axios
          .get(this.$httpUrl + "/storage/info/" + row.id)
          .then((res) => {
            this.form = res.data.data;
          });
      });
    },
    async save() {
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
    },
    doSave() {
      this.$axios
        .post(this.$httpUrl + "/storage/save", this.form)
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
        .post(this.$httpUrl + "/storage/update", this.form)
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
    loadPost() {
      this.$axios
        .post(this.$httpUrl + "/storage/listPage", {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            name: this.name,
            status: this.status + "",
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
        .post(this.$httpUrl + "/storage/control/" + id)
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
      this.$confirm("将删除仓库一切有关数据， 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post(this.$httpUrl + "/storage/deleteBatch", ids, {
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
</style>
