import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'
import { createPersistedState } from 'pinia-persistedstate-plugin'
// import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import ECharts from 'vue-echarts'
import 'echarts';

const app = createApp(App);
const pinia = createPinia();
const persist = createPersistedState();
pinia.use(persist);

app.use(pinia);
app.use(router);
app.use(ElementPlus, {
    locale: zhCn,
  });
// for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
//     app.component(key, component)
//   }
app.component('ECharts',ECharts)
app.mount('#app');
