<template>
  <div class="analysis-info">
    <el-row>
      <el-col :span="24">
        <el-card shadow="hover" class="card">
          <div class="header-title">
            <h4 class="text-muted">近七天借书情况</h4>
            <div class="dropdown"><MoreFilled /></div>
          </div>
          <div class="chart-bg padding-top-1 padding-bottom-1">
            <el-row>
              <el-col :md="24">
                <p class="text-muted">近七天借书总量</p>
                <h2 class="font-nomal text-center">
                  <small class="text-primary dot bg-primary"></small>
                  <span>{{ totalAmount }}本</span>
                </h2>
              </el-col>

            </el-row>
          </div>
          <div class="e-chart" style="height: 364px">
            <div ref="refLineChart" style="width: inherit; height: inherit"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :xl="14">
        <el-card shadow="hover" class="card">
          <div class="header-title">
            <h4 class="text-muted">图书分类统计</h4>
            <div class="dropdown"><MoreFilled /></div>
          </div>
          <div style="height: 300px" class="e-chart">
            <div ref="refProduct" style="width: inherit; height: inherit"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue'
import { Top, MoreFilled, ShoppingCartFull, Refresh, Search, GoodsFilled, Coin, DataLine } from '@element-plus/icons-vue'

import XLSX from 'xlsx'
import { useInitChart } from './useInitEcharts'
import { useInitLineChart } from './useInitLineCharts'

interface stateTypes {
  tableData: {
    date: string
    name: string
    province: string
    city: string
    address: string
    zip: number
  }[]
  form: {
    fileName: string
    fileType: XLSX.BookType
  }
}


const refProduct = ref<HTMLElement | undefined>()
const refLineChart = ref<HTMLElement | undefined>()
const totalAmount = ref(0);

// 更新 totalAmount 的方法
const setTotalAmount = (total: number) => {
  totalAmount.value = total;
};
const state = reactive<stateTypes>({
  form: {
    fileName: 'exportExcel',
    fileType: 'xlsx'
  }
})

const tableRef = ref()
const formatJson = (filterVal: any[], jsonData: any[]) => jsonData.map((v) => filterVal.map((j) => v[j]))
const filterArrayProp = (arrayObj: any, prop: string | number) => {
  const tempArray = []
  for (let i = 0; i < arrayObj.length; i++) {
    tempArray.push(arrayObj[i][prop])
  }
  return tempArray
}

onMounted(() => {
  useInitChart(refProduct.value)
  useInitLineChart(refLineChart.value,setTotalAmount)
})
</script>
<style lang="stylus" scoped>
.analysis-info{
  color black
  padding 0px 20px
  background-color #fafbfe

  .page-title-box{
    box-sizing border-box
    display flex
    flex-direction row
    justify-content space-between
    align-items center
    color #6c757d
    .page-title{
      font-size  18px
      margin  0
      line-height  85px
      overflow  hidden
      white-space: nowrap
      text-overflow  ellipsis
      color  inherit
      font-weight 700
    }
    .page-title-right{
      display flex
      flex-direction row
      justify-content space-around
      align-items center
    }
  }
  :deep(.el-card){
    margin-bottom 30px
    margin-right 20px
  }
  :deep(.el-card.bg-primary){
    background-color #46a0fc
  }
  :deep(.text-white.el-icon-more){
    color white
  }
  .card {
    .padding-top-1{
      padding-top  10px
    }
    .padding-bottom-1{
      padding-bottom  10px
    }
    .margin-bottom-pro{
      margin-bottom 15px
    }
    .margin-right-1{
      margin-right 10px
    }
    .margin-top-2{
      margin-top 20px
    }
    .margin-bottom-1{
      margin-bottom 10px
    }
    .margin-bottom-2{
      margin-bottom 20px
    }
    .font-nomal{
      font-weight 400
    }
    .icon{
      width 14px
      height 14px
      display inline-block
      border-radius 50%
    }
    .text-muted{
      color #98a6ad
    }
    .text-left{
      text-align left
    }
    .text-nowrap{
      white-space nowrap
    }
    .text-success{
      color #0acf97
    }
    .text-danger{
      color #fa5c7c
    }
    .text-primary{
      color #409eff
    }
    .text-light{
      color #e3eaef
    }
    .text-white{
      color white
    }
    .text-center{
      display flex
      flex-direction row
      justify-content center
      align-items center
    }
    .text-badge{
      margin-top 20px
      margin-bottom  10px
    }
    .font-13{
      font-size 13px
    }
    .badge{
      display: inline-block;
      padding: 0.25em 0.4em;
      font-size: 75%;
      font-weight: 700;
      line-height: 1;
      text-align: center;
      white-space: nowrap;
      vertical-align: baseline;
      border-radius: 0.25rem;
      -webkit-transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
      transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
      transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;
      transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    }
    .badge-danger{
      color #fff
      background-color #fa5c7c
    }
    .btn-link{
      font-weight 400
      color #409eff
      text-decoration none
      cursor pointer
    }
    .bg-primary{
      background-color #409eff
    }
    .bg-green{
      background-color green
    }
    .icon-square{
      width 12px
      height 12px
      display inline-block
    }
    .red{
      background-color #ec6769
    }
    .green{
      background-color  #93cb79
    }
    .yellow {
      background-color #f9c761
    }
    .deep-blue{
      background-color #5572c3
    }
    .dot{
      width 12px
      height 12px
      display inline-block
      border-radius 50%
    }
    .chart-widget-list p{
      border-bottom 1px solid #f1f3fa
      margin-bottom 0.5rem
      padding-bottom 0.5rem
      display flex
      flex-direction row
      justify-content space-between
      align-items center
    }
    .header-title{
      display flex
      flex-direction row
      justify-content space-between
      align-items center
      margin-bottom 5px
      .dropdown{
        cursor pointer
      }
    }
    .chart-bg{
      background-color #f9f9fd
    }
    .card-header{
      display flex
      flex-direction row
      justify-content space-between
      align-items flex-start

      .widget-icon{
        color #248fed
        font-size 20px
        background-color #7fbdf33b
        height 40px
        width 40px
        text-align: center
        line-height 40px
        border-radius 3px
        display flex
        flex-direction column
        justify-content center
        align-items center
        .icon{
          width 20px
          height 20px
        }
      }
    }
    .card-middle{
      display flex
      flex-direction row
      justify-content flex-start
      align-items center
      color #6c757d
    }
    .card-footer{
      display flex
      flex-direction row
      justify-content flex-start
      align-items center
    }

    .e-chart{
      margin auto
      position relative
      width 100%
    }
  }

}
</style>
