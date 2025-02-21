<script setup>
import DeviceStatePieVue from '@/components/chart/DeviceStatePie.vue'
import MessageStatePieVue from '@/components/chart/MessageStatePie.vue'
import { listCategoryService } from '@/api/device';
import { listDeviceService } from '@/api/device';
import {msgAllService, msgNormService, msgWarnService } from '@/api/msg';
import {countWarnDeviceService, countNormDeviceService, countOfflineDeviceService} from '@/api/device';

import { ref ,computed, watch, provide, toRef ,readonly} from 'vue'
const categories = ref([
    {
        "id": 1,
        "name": "默认分类",
        "description": "未分类项目",
        "count": 1,
        "createTime": "2024-01-03 22:21:24",
        "updateTime": "2024-01-03 22:21:24"
    }
]);
const devices = ref([
    {
        "id": 1,
        "name": "默认分类",
        "state": 1,
        "categoryId": 1,
        "description": "未分类项目",
        "createTime": "2024-01-03 22:21:24",
        "updateTime": "2024-01-03 22:21:24"
    }
]);
// 分类列表函数
const listCategory = async ()=>{
    let res = await listCategoryService();
    categories.value = res.data;
}
const listDevice = async ()=>{
    let res = await listDeviceService({
        pageNum: 1,
        pageSize: 10,
        name:null,
        categoryId:null,
        state:null
    });
    deviceTotal.value = res.data.total;
}
listCategory();
listDevice();
const deviceTotal = ref(0);

const getMsgAll = async ()=>{
    let res = await msgAllService();
    msgAllNum.value = res.data;
}
const getMsgWarn = async ()=>{
    let res = await msgWarnService();
    msgWarnNum.value = res.data;
}
const getMsgNorm = async ()=>{
    let res = await msgNormService();
    msgNormNum.value = res.data;
}

const categoryNum = computed(() => {
    return categories.value.length
})
const deviceNum = computed(() => {
    return deviceTotal
})
getMsgAll();
getMsgWarn();
getMsgNorm();
const msgAllNum = ref('');
const msgWarnNum = ref('');
const msgNormNum = ref('');

provide('warnMsgData', msgWarnNum);
provide('normMsgData', msgNormNum);

const stateDataModel = ref({
    warn: 0,
    norm: 0,
    offline: 0
});
const warn = ref(0);
const norm = ref(0);
const offline = ref(0);

provide('warnData', warn);
provide('normData', norm);
provide('offlineData', offline);



// 统计
const countWarnDevice = async (id)=>{
    let res = await countWarnDeviceService(id);
    warn.value = res.data;
}
const countNormDevice = async (id)=>{
    let res = await countNormDeviceService(id);
    norm.value = res.data;

}
const countOfflineDevice = async (id)=>{
    let res = await countOfflineDeviceService(id);
    offline.value = res.data;
}

countNormDevice(0);
countOfflineDevice(0);
countWarnDevice(0);

// sleep(1000);


// const isStatePieVisible = ref(false);
// 监听选择的分类变化,展示图表
// watch(selectedCategory, async (newVal, oldVal) => {
//     //分类变化,调用接口,获取设备状态数据
//     countNormDevice(newVal);
//     countOfflineDevice(newVal);
//     countWarnDevice(newVal);
// })


</script>


<template>
    <el-descriptions title="信息统计" size="large" style="margin-bottom: 10px;">
        <el-descriptions-item label="创建分类数">
            <el-tag>{{categoryNum}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设备总量">
            <el-tag>{{deviceNum}}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="接收信息总数">
            <el-tag>{{msgAllNum}}</el-tag>
        </el-descriptions-item>
    </el-descriptions>


    <el-row :gutter="24">
        <el-col :span="12">
            <el-card :body-style="{height: '500px', width: '95%'  }">
                <template #header>
                    <el-form :inline="true"  size="large" autocapitalize="">
                        <el-form-item >
                            <strong><span style="font-size: large;">设备状态统计</span></strong>
                        </el-form-item>
                    </el-form>
                </template>
                <DeviceStatePieVue  />
            </el-card>
        </el-col>
        <el-col :span="12">
            <el-card :body-style="{height: '500px', width: '95%'  }">
                <template #header>
                    <el-form :inline="true"  size="large" autocapitalize="">
                        <el-form-item >
                            <strong><span style="font-size: large;">接收信息统计</span></strong>
                        </el-form-item>
                    </el-form>
                </template>
                <MessageStatePieVue  />
            </el-card>
        </el-col>
    </el-row>
</template>


<style lang="scss" scoped>
/* Your component styles here */
// .el-card__body {
//     border-radius: 8px;
//     overflow: hidden;
// }
</style>
