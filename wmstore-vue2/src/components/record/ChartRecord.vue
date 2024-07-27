<template>
  <div>
    <div style="margin-bottom: 5px; text-align: center">
      <el-select v-model="type" filterable placeholder="请选择货物类别" style="margin-left: 5px" @change="getGood">
        <el-option v-for="item in typeData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="storage" filterable placeholder="请选择仓库" style="margin-left: 5px" @change="getGood">
        <el-option v-for="item in storageData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goods" filterable placeholder="请选择货物" style="margin-left: 5px" @change="loadPost()">
        <el-option v-for="item in selectGoodData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-button icon="el-icon-refresh" type="success" style="margin-left: 5px" @click="resetParam()"
        circle></el-button>
      <el-radio-group v-model="time" style="margin-left: 50px" @change="loadPost()">
        <el-radio-button v-for="item in timeData" :key="item.id" :label="item.value"
          :value="item.value"></el-radio-button>
      </el-radio-group>
      <el-radio-group v-model="record" style="margin-left: 50px" @change="loadPost()">
        <el-radio-button v-for="item in recordData" :key="item.id" :label="item.value"
          :value="item.value"></el-radio-button>
      </el-radio-group>
    </div>

    <div class="body">
      <div class="charts">
        <el-carousel trigger="click" height="520px" :autoplay="false" arrow="always" ref="remarkCarusel">
          <el-carousel-item key="1">
            <el-card>
              <div id="container1" class="chart-div">
                <div class="text">条形统计图</div>
              </div>
            </el-card>
          </el-carousel-item>
          <el-carousel-item key="2">
            <el-card>
              <div id="container2" class="chart-div">
                <div class="text">折线统计图</div>
              </div>
            </el-card>
          </el-carousel-item>
          <el-carousel-item key="3">
            <el-card>
              <div id="container3" class="chart-div">
                <div class="text">面积统计图</div>
              </div>
            </el-card>
          </el-carousel-item>
          <el-carousel-item key="4">
            <el-card>
              <div id="container4" class="chart-div">
                <div class="text">扇形统计图</div>
              </div>
            </el-card>
          </el-carousel-item>
          <el-carousel-item key="5">
            <el-card>
              <div id="container5" class="chart-div">
                <div class="text">环形统计图</div>
              </div>
            </el-card>
          </el-carousel-item>
        </el-carousel>
      </div>
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
  name: "ChartRecord",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("CurUser")),
      goods: 0,
      type: "",
      storage: "",
      time: "今年",
      record: "出库数量",
      typeData: [],
      storageData: [],
      selectGoodData: [
        {
          id: 0,
          name: "全部",
          storage: "",
          type: "",
          count: "",
          remark: "",
          image: "1701089713285",
          createTime: "2023-11-27T20:55:13",
          status: 1,
        },
      ],
      timeData: [
        {
          id: 1,
          value: "今年",
        },
        {
          id: 2,
          value: "本月",
        },
        {
          id: 3,
          value: "近七天",
        },
      ],
      recordData: [
        {
          id: 1,
          value: "入库数量",
        },
        {
          id: 0,
          value: "出库数量",
        },
      ],
    };
  },
  methods: {
    resetParam() {
      this.type = "";
      this.storage = "";
      this.clear();
    },
    clear() {
      this.selectGoodData = [
        {
          id: 0,
          name: "全部",
          storage: "",
          type: "",
          count: "",
          remark: "",
          image: "1701089713285",
          createTime: "2023-11-27T20:55:13",
          status: 1,
        },
      ];
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
            this.clear();
            this.selectGoodData.push(...res.data);
          } else {
            alert("获取数据失败");
          }
        });
    },
    loadPost() {
      if (this.user.role == 2) var users = this.user.id;
      else users = "";
      this.$axios
        .post(this.$httpUrl + "/highchart/chart", {
          param: {
            goods: this.goods + "",
            record: this.record,
            time: this.time,
            user: users + "",
          },
        })
        .then((res) => res.data)
        .then((res) => {
          if (res.code == 200) {
            this.drawchart(res.data.pie, this.goods, this.record, this.time);
            this.chart(
              res.data.line.category,
              res.data.line.data,
              this.goods,
              this.record,
              this.time
            );
            console.log(res.data.line);
          } else {
            alert("获取数据失败");
          }
        });
    },
    chart(categories, series, goods, record, time) {
      console.log(series);
      console.log(categories);
      Highcharts.chart("container1", {
        chart: {
          type: "column",
        },
        title: {
          text: "条形统计图",
        },
        subtitle: {
          text: this.formatGoods(goods) + time + record + "条形统计图",
        },
        xAxis: {
          categories: categories,
        },
        yAxis: {
          title: {
            text: "数量",
          },
        },
        plotOptions: {
          line: {
            dataLabels: {
              // 开启数据标签
              enabled: true,
            },
            // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            enableMouseTracking: false,
          },
        },
        series: series,
      });
      Highcharts.chart("container2", {
        chart: {
          type: "line",
        },
        title: {
          text: "折线统计图",
        },
        subtitle: {
          text: this.formatGoods(goods) + time + record + "点值折线图",
        },
        xAxis: {
          categories: categories,
        },
        yAxis: {
          title: {
            text: "数量",
          },
        },
        plotOptions: {
          line: {
            dataLabels: {
              // 开启数据标签
              enabled: true,
            },
            // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            enableMouseTracking: false,
          },
        },
        series: series,
      });
      Highcharts.chart("container3", {
        chart: {
          type: "area",
        },
        title: {
          text: "面积统计图",
        },
        subtitle: {
          text: this.formatGoods(goods) + time + record + "面积图",
        },
        xAxis: {
          categories: categories,
        },
        yAxis: {
          title: {
            text: "数量",
          },
        },
        plotOptions: {
          line: {
            dataLabels: {
              // 开启数据标签
              enabled: true,
            },
            // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            enableMouseTracking: false,
          },
        },
        series: series,
      });
    },
    drawchart(data, goods, record, time) {
      console.log(data);
      Highcharts.chart("container4", {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: "pie",
        },
        title: {
          text: this.formatGoods(goods) + time + record + "扇形统计图",
        },
        tooltip: {
          pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>",
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: "pointer",
            dataLabels: {
              enabled: true,
              format: "<b>{point.name}</b>: {point.percentage:.1f} %",
              style: {
                color:
                  (Highcharts.theme && Highcharts.theme.contrastTextColor) ||
                  "black",
              },
            },
          },
        },
        series: [
          {
            name: "数量",
            colorByPoint: true,
            data: data,
          },
        ],
      });
      Highcharts.chart(
        "container5",
        {
          chart: {
            spacing: [40, 0, 40, 0],
          },
          title: {
            floating: true,
            text: this.formatGoods(goods) + time + record + "环形统计图",
          },
          tooltip: {
            pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>",
          },
          plotOptions: {
            pie: {
              allowPointSelect: true,
              cursor: "pointer",
              dataLabels: {
                enabled: true,
                format: "<b>{point.name}</b>: {point.percentage:.1f} %",
                style: {
                  color:
                    (Highcharts.theme && Highcharts.theme.contrastTextColor) ||
                    "black",
                },
              },
              point: {},
            },
          },
          series: [
            {
              type: "pie",
              innerSize: "60%",
              name: "数量",
              data: data,
            },
          ],
        },
        function (c) {
          var centerY = c.series[0].center[1],
            titleHeight = parseInt(c.title.styles.fontSize);
          c.setTitle({
            y: centerY + titleHeight / 2,
          });
        }
      );
    },
    formatGoods(goods) {
      let temp = this.selectGoodData.find((item) => {
        return item.id == goods;
      });
      return temp && temp.name;
    },
  },
  mounted() {
    this.loadStorage();
    this.loadType();
    this.loadPost()
  },
};
</script>

<style>
.chart-div {
  height: 450px;
}
</style>