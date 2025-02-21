<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'

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

const stateoptions = [
    {
        value: 0,
        label: '正常'
    },
    {
        value: 1,
        label: '警告'
    },
    {
        value: 2,
        label: '离线'
    }
];


// 搜索条件数据模型
const name = ref('');// 名字,模糊匹配
const categoryId = ref('');// 分类id,精确匹配
const state = ref('');// 状态,精确匹配


//分页条数据模型
const pageNum = ref(1);// 当前页
const pageSize = ref(10);// 每页条数
const total = ref(0);// 总条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    listDevice()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    listDevice()
}

//用于获取分类并显示
import { listCategoryService } from '@/api/device';
import { listDeviceService, addDeviceService,deleteDeviceService, updateDeviceService } from '@/api/device';
// 分类列表函数
const listCategory = async ()=>{
    let res = await listCategoryService();
    categories.value = res.data;
}
// 设备列表函数
const listDevice = async ()=>{
    let res = await listDeviceService({
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: name.value,
        categoryId: categoryId.value,
        state: state.value
    });
    // 数据模型赋值
    devices.value = res.data.items;
    total.value = res.data.total;

    // 处理分类,将id映射至名称
    for (let i = 0; i < devices.value.length; i++) {
        let device = devices.value[i];
        device.stateName = (device.state== 0) ? '正常' : (device.state== 1 ? '警告' : '离线');
        for (let j = 0; j < categories.value.length; j++) {
            if (device.categoryId == categories.value[j].id) {
                device.categoryName = categories.value[j].name;
            }
        }
    }

}
listCategory();
listDevice();

// 添加分类弹窗显示控制
const dialogVisible = ref(false);

//设备数据模型
const deviceDataModel = ref({
    name: '',
    categoryId: '',
    state: '',
    description: ''
});

const rules = {
    name: [
        { required: true, message: '请输入设备名称', trigger: 'blur' },
        { min: 1, max: 30, message: '长度在 1 到 32 个字符', trigger: 'blur' }
    ],
    categoryId: [
        { required: true, message: '请选择设备分类', trigger: 'blur' }
    ],
    state: [
        { required: true, message: '请选择设备状态', trigger: 'blur' }
    ]
}


import { ElMessage } from 'element-plus';

// 添加设备
const addDevice = async ()=>{
    // 调用接口添加设备
    let res = await addDeviceService(deviceDataModel.value);
    ElMessage.success(res.message ? res.message : '添加成功');
    dialogVisible.value = false;//关闭弹窗
    listDevice();//重新获取设备列表
}

// 编辑设备
const updateDevice = async ()=>{
    // 调用接口编辑设备
    let res = await updateDeviceService(deviceDataModel.value);
    ElMessage.success(res.message ? res.message : '编辑成功');
    dialogVisible.value = false;//关闭弹窗
    listDevice();//重新获取设备列表
}

//清空表单数据
const clearData = ()=>{
    deviceDataModel.value = {
        name: '',
        categoryId: '',
        state: 0,
        description: ''
    }
}

// 删除设备,通过信息窗体进行确认
import { ElMessageBox } from 'element-plus';
const deleteDevice = (row)=>{
    ElMessageBox.confirm('此操作将永久删除该设备,确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        // 调用接口删除设备
        let res = await deleteDeviceService(row.id);
        ElMessage.success(res.message ? res.message : '删除成功');
        listDevice();//重新获取设备列表
    }).catch(() => {
        ElMessage.info('删除操作已取消');
    });
}

const title = ref('');

// 显示编辑操作弹窗
const showOperationDialog = (row)=>{
    dialogVisible.value = true;
    title.value = '编辑设备';
    //拷贝数据
    deviceDataModel.value.name = row.name;
    deviceDataModel.value.categoryId = row.categoryId;
    deviceDataModel.value.description = row.description;
    deviceDataModel.value.state = row.state;
    //扩展id属性,用于传递给后端进行编辑
    deviceDataModel.value.id = row.id;
}
</script>



<template>
    <el-card>
        <template #header>
            <div>
                <strong><span>设备管理</span></strong>
                <div class="extra">
                    <el-button type="primary" size="large" @click="dialogVisible = true; title = '添加设备'; clearData()">添加设备</el-button>
                </div>
                <br><br>
            </div>
        </template>
        <el-form inline>
            <el-form-item label="设备名称">
                <el-input v-model="name" placeholder="请输入设备名称"></el-input>
            </el-form-item>
            <el-form-item label="设备分类">
                <el-select v-model="categoryId" placeholder="请选择设备分类">
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备状态">
                <el-select v-model="state" placeholder="请选择设备状态">
                    <el-option v-for="item in stateoptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="listDevice">查询</el-button>
                <el-button @click="name = '';categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        
        <el-table :data="devices" style="width: 100%;">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column prop="name" label="设备名称"></el-table-column>
            <el-table-column prop="categoryName" label="设备分类"></el-table-column>
            <el-table-column prop="description" label="设备描述"></el-table-column>
            <el-table-column prop="state" label="设备状态">
                <template #default="{row}">
                    <el-tag v-if="row.state == 0" type="success">正常</el-tag>
                    <el-tag v-else-if="row.state == 1" type="warning">警告</el-tag>
                    <el-tag v-else="row.state == 2" type="danger">离线</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="{row}">
                    <el-button :icon="Edit" circle plain type="primary" @click="showOperationDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteDevice(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="当前没有设备,请先创建设备" />
            </template>
        </el-table>

        <!--分页条-->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5, 8, 10]"
            layout="total, sizes, prev, pager, next, jumper" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <el-dialog :title="title" v-model="dialogVisible" width="30%" center align-center>
            <el-form :model="deviceDataModel" :rules="rules" label-width="80px" size="large" center>
                <el-form-item label="设备名称" prop="name">
                    <el-col :span="12">
                        <el-input v-model="deviceDataModel.name" minlength="1" maxlength = "30"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="设备分类" prop="categoryId">
                    <el-select v-model="deviceDataModel.categoryId" placeholder="请选择设备分类" style="width: 50%;">
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备状态" prop="state" required>
                    <el-select v-model="deviceDataModel.state" placeholder="请选择设备状态" style="width: 50%;">
                        <el-option v-for="item in stateoptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备描述" >
                    <el-col :span="24">
                        <el-input v-model="deviceDataModel.description" maxlength = "100"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item >
                    <el-button type="primary" auto-insert-space @click="dialogVisible = false; clearData()"  class="dialog-btn">取消</el-button>
                    <el-button type="primary" auto-insert-space @click="title == '添加设备' ? addDevice() : updateDevice()" class="btn" >确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </el-card>
</template>

<style lang="scss" scoped>
/* Your component styles here */
.extra {
    float: right;
}

</style>
