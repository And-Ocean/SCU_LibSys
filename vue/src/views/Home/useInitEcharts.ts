import * as echarts from 'echarts/core'
import { TitleComponent, TitleComponentOption, GridComponent, GridComponentOption, DataZoomComponent, DataZoomComponentOption } from 'echarts/components'
import { BarChart, BarSeriesOption } from 'echarts/charts'
import { CanvasRenderer } from 'echarts/renderers'
import request from "@/utils/request";

echarts.use([TitleComponent, GridComponent, DataZoomComponent, BarChart, CanvasRenderer])

type EChartsOption = echarts.ComposeOption<TitleComponentOption | GridComponentOption | DataZoomComponentOption | BarSeriesOption>

let option: EChartsOption


// 修改为 async 函数
async function getBookCategory() {
    try {
        const res = await request({
            url: "http://localhost:8080/api/BookCategory/getBookCategory",
            method: 'POST',
            json: true,
        });

        if (res.status === 0) {
            console.log("getBookCategory success")
            return res.data; // 假设返回的数据是 res.data
        }
        return null;
    } catch (error) {
        console.error("Error fetching data:", error);
        return null;
    }
}

async function getCategoryNum(record:any) {
    try {
        const res = await request({
            url: "http://localhost:8080/api/BookCategory/getCategoryNum",
            method: 'POST',
            json: true,
            data: record,
        });

        if (res.status === 0) {
            console.log("getCategoryNum success")
            return res.data.length; // 假设返回的数据是 res.data
        }
        return null;
    } catch (error) {
        console.error("Error fetching data:", error);
        return null;
    }
}

// 修改 useInitChart 使其支持 async 操作
export const useInitChart = async (chartDom: HTMLElement | undefined) => {
    if (!chartDom) return; // 防止 chartDom 为 undefined

    const records = await getBookCategory(); // 等待数据加载

    console.log(records);
    const dataAxis = [];
    const data = [];
    for(let i=0;i<records.length;i++){
        let num = await getCategoryNum(records[i].category)
        dataAxis.push(records[i].category)
        data.push(num)
    }

    const myChart = echarts.init(chartDom as HTMLElement)
//使用上面的函数来统计书籍数量
  //prettier-ignore

  const yMax = 500
  const dataShadow = []

  for (let i = 0; i < data.length; i++) {
    dataShadow.push(yMax)
  }

  option = {
    title: {
      text: 'the number of book category',
      subtext: 'zoom to show every details of data'
    },
    xAxis: {
      data: dataAxis,
      axisLabel: {
        inside: true,
        color: '#fff'
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
      z: 10
    },
    yAxis: {
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#999'
      }
    },
    dataZoom: [
      {
        type: 'inside'
      }
    ],
    series: [
      {
        type: 'bar',
        showBackground: true,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#2378f7' },
              { offset: 0.7, color: '#2378f7' },
              { offset: 1, color: '#83bff6' }
            ])
          }
        },
        data
      }
    ]
  }

  // Enable data zoom when user click bar.
  const zoomSize = 6
  myChart.on('click', (params) => {
    myChart.dispatchAction({
      type: 'dataZoom',
      startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
      endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
    })
  })

  // eslint-disable-next-line no-unused-expressions
  option && myChart.setOption(option)
}

export default { useInitChart }
