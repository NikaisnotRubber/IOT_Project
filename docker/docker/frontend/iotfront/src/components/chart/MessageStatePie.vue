<template>
    <e-charts class="chart" :option="option" autoresize />
</template>
  
<script setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { ref, provide, inject, computed } from 'vue';

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

const warnMsgData =  inject('warnMsgData');
const normMsgData =  inject('normMsgData');

const option = computed(() => {
   return {
  title: {
    text: '信息接收',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: ['异常', '正常'],
  },
  series: [
    {
      name: '状态统计',
      type: 'pie',
      radius: '60%',
      center: ['50%', '60%'],
      data: [
        { value: warnMsgData.value, name: '异常' },
        { value: normMsgData.value, name: '正常' },
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    },
  ],
}
});
</script>

<style scoped>
</style>
