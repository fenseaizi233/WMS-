<template>
  <div>
    <div style="margin-bottom: 5px; text-align: center">
      <el-select
        v-model="type"
        filterable
        placeholder="请选择货物类别"
        style="margin-left: 5px"
        @change="getGood"
      >
        <el-option
          v-for="item in typeData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="storage"
        filterable
        placeholder="请选择仓库"
        style="margin-left: 5px"
        @change="getGood"
      >
        <el-option
          v-for="item in storageData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="goods"
        filterable
        placeholder="请选择货物"
        style="margin-left: 5px"
        v-if="storage || type"
      >
        <el-option
          v-for="item in selectGoodData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="status"
        filterable
        placeholder="请选择状态"
        style="margin-left: 5px"
      >
        <el-option
          v-for="item in statusData"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
    </div>
    <div style="margin-bottom: 5px; text-align: center">
      <el-select
        v-model="user"
        filterable
        placeholder="请选择用户"
        style="margin-left: 5px"
        v-if="CurUser.role != 2"
      >
        <el-option
          v-for="item in userData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="admin"
        filterable
        placeholder="请选择管理员"
        style="margin-left: 5px"
        v-if="CurUser.role != 1"
      >
        <el-option
          v-for="item in adminData"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-date-picker
        v-model="createTime"
        type="date"
        placeholder="选择提交时间"
      >
      </el-date-picker>
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
      <el-button
        type="danger"
        slot="reference"
        :disabled="delBtlStatu"
        @click="delHandle(null)"
        style="margin-left: 5px"
        v-if="CurUser.role == 0"
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
      :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column
        prop="user"
        label="申请用户"
        width="100"
        :formatter="formatUser"
      >
      </el-table-column>
      <el-table-column prop="outrecord" label="货物名称" width44="100">
        <template slot-scope="scope">
          {{ scope.row.good.name }}
        </template>
      </el-table-column>
      <el-table-column prop="image" label="货物展示" width="150">
        <template slot-scope="scope">
          <img
            :src="'http://localhost:8844/upload/image/' + scope.row.good.image"
            min-width="70"
            height="70"
          />
        </template>
      </el-table-column>

      <el-table-column prop="outrecord" label="货物分类" width="100">
        <template slot-scope="scope">
          {{ formatType(scope.row.good.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="storage" label="货物仓库" width="100">
        <template slot-scope="scope">
          {{ formatStorage(scope.row.good.storage) }}
        </template>
      </el-table-column>
      <el-table-column prop="count" label="出库数量" width="100">
      </el-table-column>
      <el-table-column prop="status" label="审核状态" width="100">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0" type="warning"
            >待审核</el-tag
          >
          <el-tag size="small" v-else-if="scope.row.status === 1" type="success"
            >审核通过</el-tag
          >
          <el-tag size="small" v-else-if="scope.row.status === 2" type="danger"
            >审核拒绝</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="申请说明" width="200">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="提交时间"
        width="200"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column
        prop="admin"
        label="审核管理员"
        width="100"
        :formatter="formatAdmin"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="审核时间"
        width="200"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column
        prop="operate"
        label="操作"
        fixed="right"
        :width="'300px'"
        v-if="CurUser.role != 2"
      >
        <template slot-scope="scope">
          <el-button
            slot="reference"
            size="small"
            type="danger"
            @click="delHandle(scope.row.id)"
            style="margin-right: 20px"
            v-if="CurUser.role == 0"
          >
            <i class="el-icon-delete"></i>
          </el-button>
          <el-button
            size="small"
            type="primary"
            @click="pass(scope.row)"
            v-if="scope.row.status == 0"
            >审核通过</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="refuse(scope.row)"
            v-if="scope.row.status == 0"
            >审核拒绝</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[4, 12, 16, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "OutRecord",
  data() {
    return {
      CurUser: JSON.parse(localStorage.getItem("CurUser")),
      pageSize: 4,
      pageNum: 1,
      total: 0,
      tableData: [],
      goods: "",
      type: "",
      storage: "",
      user: "",
      admin: "",
      createTime: "",
      status: 1,
      userData: [],
      adminData: [],
      typeData: [],
      storageData: [],
      selectGoodData: [],
      statusData: [
        {
          value: 2,
          label: "被拒绝",
        },
        {
          value: 1,
          label: "已通过",
        },
        {
          value: 0,
          label: "待审核",
        },
      ],

      delBtlStatu: true,
      multipleSelection: [],
    };
  },
  methods: {
    resetParam() {
      if (this.CurUser.role == 2) this.user = this.CurUser.id;
      if (this.CurUser.role == 1) this.admin = this.CurUser.id;
      this.type = "";
      this.storage = "";
      this.createTime = "";
      this.goods = "";
      if (this.CurUser.role == 0) this.status = "";
      else this.status = 0;
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
    loadAdmin() {
      this.$axios
        .post(this.$httpUrl + "/user/adminPage", {
          pageSize: 99999,
          pageNum: 1,
          param: {
            role: "",
          },
        })
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.adminData = res.data;
          } else {
            this.$message({
              message: "数据查询失败",
              type: "warning",
            });
          }
        });
    },
    formatStorage(storage) {
      let temp = this.storageData.find((item) => {
        return item.id == storage;
      });
      return temp && temp.name;
    },
    formatType(type) {
      let temp = this.typeData.find((item) => {
        return item.id == type;
      });
      return temp && temp.name;
    },
    formatUser(row) {
      let temp = this.userData.find((item) => {
        return item.id == row.user;
      });
      return temp && temp.name;
    },
    formatAdmin(row) {
      let temp = this.adminData.find((item) => {
        return item.id == row.admin;
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
    loadType() {
      this.$axios
        .get(this.$httpUrl + "/type/list")
        .then((res) => res.data)
        .then((res) => {
          this.typeData = res;
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
    getGood() {
      this.$axios
        .post(this.$httpUrl + "/goods/listPage", {
          pageSize: 99999,
          pageNum: 1,
          param: {
            type: this.type + "",
            status: 1 + "",
            storage: this.storage + "",
          },
        })
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.selectGoodData = res.data;
          } else {
            alert("获取数据失败");
          }
        });
    },
    pass(row) {
      this.$confirm("同意用户的出库申请, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success",
      })
        .then(() => {
          this.$axios
            .post(
              this.$httpUrl +
                "/outrecord/pass?id=" +
                row.id +
                "&admin=" +
                this.CurUser.id
            )
            .then((res) => {
              console.log(res);
              if (res.data.code == 200) {
                this.$message({
                  message: "恭喜你，操作成功",
                  type: "success",
                });
                this.loadPost();
              } else {
                this.$message({
                  message: res.data.msg,
                  type: "error",
                });
              }
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
    refuse(row) {
      this.$confirm("同意用户的出库申请, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success",
      })
        .then(() => {
          this.$axios
            .post(
              this.$httpUrl +
                "/outrecord/refuse?id=" +
                row.id +
                "&admin=" +
                this.CurUser.id
            )
            .then((res) => {
              console.log(res);
              if (res.data.code == 200) {
                this.$message({
                  message: "恭喜你，操作成功",
                  type: "success",
                });
                this.loadPost();
              } else {
                this.$message({
                  message: res.data.msg,
                  type: "error",
                });
              }
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
      this.$confirm("将删除出库一切有关数据， 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post(this.$httpUrl + "/outrecord/deleteBatch", ids, {
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
    selectedDay() {
      if (this.createTime) {
        var date = new Date(this.createTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? "0" + m : m;
        var d = date.getDate();
        d = d < 10 ? "0" + d : d;
        this.createTime = y + "-" + m + "-" + d;
      } else {
        this.createTime = "";
      }
    },
    loadPost() {
      this.selectedDay();
      if (this.CurUser.role == 2) this.user = this.CurUser.id;
      if (this.CurUser.role == 1) this.admin = this.CurUser.id;
      console.log(this.CurUser);
      this.$axios
        .post(this.$httpUrl + "/inrecord/listPage", {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            createTime: this.createTime,
            goods: this.goods + "",
            status: this.status + "",
            user: this.user + "",
            admin: this.admin + "",
            role: this.CurUser.role + "",
          },
        })
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          if (res.code == 200) {
            this.tableData = res.data;
            this.total = res.total;
          } else {
            alert("获取数据失败");
          }
        });
    },
  },
  mounted() {
    this.loadStorage();
    this.loadAdmin();
    this.loadUser();
    this.loadType();
    this.loadPost();
  },
};
</script>

<style></style>
