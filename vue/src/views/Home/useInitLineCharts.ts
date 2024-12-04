import * as echarts from 'echarts/core'
import { TitleComponent, ToolboxComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components'
import { LineChart } from 'echarts/charts'
import { CanvasRenderer } from 'echarts/renderers'
import request from "@/utils/request";

echarts.use([TitleComponent, ToolboxComponent, TooltipComponent, GridComponent, LegendComponent, LineChart, CanvasRenderer])

let option
function getLastSevenDays(): string[] {
    const lastSevenDays: string[] = [];
    const today = new Date();

    // 获取今天的日期
    lastSevenDays.push(formatDate(today));

    // 获取近六天的日期（从今天开始，向前推六天）
    for (let i = 1; i < 7; i++) {
        const previousDay = new Date(today);
        previousDay.setDate(today.getDate() - i);  // 向前推一天
        lastSevenDays.push(formatDate(previousDay));
    }

    return lastSevenDays.reverse();  // 将数组倒序，确保从最早到最近
}

// 格式化日期为 yyyy-MM-dd 格式
function formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

const dates = getLastSevenDays();


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
    let reverseRecord=[]
    for (let i=0;i<record[0].length;i++){
        total+=record[0][i];
        reverseRecord[record[0].length-i-1]=record[0][i];
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
      data: dates
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
        data: reverseRecord
      },

    ]
  }

  // eslint-disable-next-line no-unused-expressions
  option && myChart.setOption(option)
}

export default { useInitLineChart }
