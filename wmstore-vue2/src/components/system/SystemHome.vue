<template>
  <div>
    <div
      style="
        text-align: center;
        background-color: #f1f1f3;
        height: 100%;
        padding: 0px;
        margin: 0px;
      "
    >
      <el-row class="card-row">
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card">
            <div>
              <i class="el-icon-s-custom"></i>
              用户数量:
              <el-tag type="success">
                {{ userCount }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card blue">
            <div>
              <i class="el-icon-s-custom"></i>
              管理员数量:
              <el-tag type="success">
                {{ adminCount }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card purple">
            <div>
              <i class="el-icon-s-custom"></i>
              仓库数量:
              <el-tag type="success">
                {{ storageCount }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="card-row">
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card gold">
            <div>
              <i class="el-icon-s-promotion"></i>
              库存数量:
              <el-tag disable-transitions>{{ goodsCount }}</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card red">
            <div>
              <i class="el-icon-s-custom"></i>
              出库数量:
              <el-tag type="success">
                {{ outCount }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="box-card centered-card green">
            <div>
              <i class="el-icon-s-custom"></i>
              入库数量:
              <el-tag type="success">
                {{ inCount }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row class="card-row">
        <el-col :span="12">
          <el-card shadow="hover" class="box-card centered-card blue">
            <div id="container1" style="height: 320px; width: 500px"></div>
            <div class="text">用户男女比例</div>
          </el-card></el-col
        >
        <el-col :span="12">
          <el-card shadow="hover" class="box-card centered-card pink">
            <div id="container2" style="height: 320px; width: 500px"></div>
            <div class="text">用户角色比例</div>
          </el-card></el-col
        >
      </el-row>
    </div>
  </div>
</template>
  
  <script>
  import Highcharts from "highcharts/highstock";
import HighchartsMore from "highcharts/highcharts-more";
import HighchartsDrilldown from "highcharts/modules/drilldown";
import Highcharts3D from "highcharts/highcharts-3d";
import Highmaps from "highcharts/modules/map";
import cylinder from "highcharts/modules/cylinder";
HighchartsMore(Highcharts);
HighchartsDrilldown(Highcharts);
Highcharts3D(Highcharts);
Highmaps(Highcharts);
cylinder(Highcharts);
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "SystemHome",
  props: {
    dataHeight: {
      type: String,
      default: "320px",
    },
  },
  data() {
    return {
      //用户信息
      user: JSON.parse(localStorage.getItem("LocalUser")),
      userCount: 0,
      adminCount: 0,
      storageCount: 0,
      goodsCount: 0,
      outCount: 0,
      inCount: 0,
    };
  },
  methods: {
    getUserCount() {
      this.$axios
        .get(this.$httpUrl + "/user/allcount")
        .then((res) => res.data)
        .then((res) => {
          this.userCount = res.data.userCount;
          this.adminCount = res.data.adminCount;
        });
    },
    getStorageCount() {
      this.$axios
        .get(this.$httpUrl + "/storage/count")
        .then((res) => res.data)
        .then((res) => {
          this.storageCount = res;
        });
    },
    getGoodsCount() {
      this.$axios
        .get(this.$httpUrl + "/goods/allcount")
        .then((res) => res.data)
        .then((res) => {
          this.goodsCount = res.data;
        });
    },
    getOutCount() {
      this.$axios
        .get(this.$httpUrl + "/outrecord/allcount")
        .then((res) => res.data)
        .then((res) => {
          this.outCount = res.data;
        });
    },
    getInCount() {
      this.$axios
        .get(this.$httpUrl + "/inrecord/allcount")
        .then((res) => res.data)
        .then((res) => {
          this.inCount = res.data;
        });
    },

    getUserSex() {
      this.$axios
        .get(this.$httpUrl + "/highchart/pie1")
        .then((res) => res.data)
        .then((res) => {
          this.drawChartPie1(res.data);
        });
    },
    getUserType() {
      this.$axios
        .get(this.$httpUrl + "/highchart/pie2")
        .then((res) => res.data)
        .then((res) => {
          console.log(res);
          this.drawChartPie2(res.data);
        });
    },
    drawChartPie1(data) {
      Highcharts.chart('container1', {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: 'pie'
        },
        title: {
          text: '用户性别分布饼图'
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
              style: {
                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
              }
            }
          }
        },
        series: [{
          name: '分布数量',
          colorByPoint: true,
          data: data
        }]
      });
    },
    drawChartPie2(data) {
      Highcharts.chart('container2', {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: 'pie'
        },
        title: {
          text: '用户角色分布饼图'
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
              style: {
                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
              }
            }
          }
        },
        series: [{
          name: '分布数量',
          colorByPoint: true,
          data: data
        }]
      });
    }
  },
  created() {
    this.getUserCount();
    this.getGoodsCount();
    this.getOutCount();
    this.getInCount();
    this.getStorageCount();
    this.getUserSex();
    this.getUserType();
  },
};
</script>
  

<style scoped>
.box-card {
  height: auto;
  text-align: center;
  border-radius: 25px;
  margin: 5px;
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
