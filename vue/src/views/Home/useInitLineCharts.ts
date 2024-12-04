import * as echarts from 'echarts/core'
import { TitleComponent, ToolboxComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components'
import { LineChart } from 'echarts/charts'
import { CanvasRenderer } from 'echarts/renderers'
import request from "@/utils/request";

echarts.use([TitleComponent, ToolboxComponent, TooltipComponent, GridComponent, LegendComponent, LineChart, CanvasRenderer])

let option

const getLendNum = async () => {
    try {
        const res = await request({
            url: "http://localhost:8080/api/BookCategory/getLendNum",
            method: 'POST',
            json: true,
        });

        if (res.status === 0) {
            console.log("getLendNum success")
            return res.data; // 假设返回的数据是 res.data
        }
        return null;
    } catch (error) {
        console.error("Error fetching data:", error);
        return null;
    }
}

export const useInitLineChart = async (chartDom: HTMLElement | undefined, setTotalAmount: (total: number) => void) => {
  // var chartDom = document.getElementById('main');

  const myChart = echarts.init(chartDom as HTMLElement)


    let record=await getLendNum();
    console.log(record);
    console.log(record[0][0]);
    let total=0;
    for (let i=0;i<record[0].length;i++){
        total+=record[0][i];
    }
    console.log(total);
    setTotalAmount(total);
  option = {
    title: {
      //   text: 'Stacked Line'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['日借书量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '日借书量',
        type: 'line',
        stack: 'Total',
        smooth: true,
        data: record[0]
      },

    ]
  }

  // eslint-disable-next-line no-unused-expressions
  option && myChart.setOption(option)
}

export default { useInitLineChart }
