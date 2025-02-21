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
import { listCategoryService, addCategoryService, updateCategoryService,deleteCategoryService } from '@/api/device';

// 调用接口获取分类列表 并绑定数据
const listCategory = async ()=>{
    let res = await listCategoryService();
    categories.value = res.data;
}
listCategory();

// 添加分类弹窗显示控制
const dialogVisible = ref(false);
// 弹窗种类控制
// const isEditCategory = ref(false);

// 分类信息的数据模型,用于编辑和添加
const categoryDataModel = ref({
    name: '',
    description: '',
});

// 表单校验规则
const rules = {
    name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur' }
    ]
}

// 接口调用进行操作,表单控制
import { ElMessage } from 'element-plus';
// 添加分类
const addCategory = async ()=>{
    // 调用接口添加分类
    let res = await addCategoryService(categoryDataModel.value);
    ElMessage.success(res.message ? res.message : '添加成功');
    dialogVisible.value = false;//关闭弹窗
    listCategory();//重新获取分类列表
}
// 编辑分类
const updateCategory = async ()=>{
    // 调用接口编辑分类
    let res = await updateCategoryService(categoryDataModel.value);
    ElMessage.success(res.message ? res.message : '编辑成功');
    dialogVisible.value = false;//关闭弹窗
    listCategory();//重新获取分类列表
}
//清空表单数据
const clearData = ()=>{
    categoryDataModel.value = {
        name: '',
        description: '',
    }
}

// 删除分类,通过信息窗体进行确认
import { ElMessageBox } from 'element-plus';
const deleteCategory = (row)=>{
    ElMessageBox.confirm('此操作将永久删除该分类,确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        // 调用接口删除分类
        let res = await deleteCategoryService(row.id);
        ElMessage.success(res.message ? res.message : '删除成功');
        listCategory();//重新获取分类列表
    }).catch(() => {
        ElMessage.info('删除操作已取消');
    });
}

const title = ref('');

// 显示编辑操作弹窗
const showOperationDialog = (row)=>{
    dialogVisible.value = true;
    title.value = '编辑分类';
    //拷贝数据
    categoryDataModel.value.name = row.name;
    categoryDataModel.value.description = row.description;
    //扩展id属性,用于传递给后端进行编辑
    categoryDataModel.value.id = row.id;
}

</script>

<template>
    <el-card class="device-container">
        <template #header>
            <div class="header">
                <strong><span>设备分类</span></strong>
                <div class="extra">
                    <el-button type="primary" size="large" @click="dialogVisible = true; title = '添加分类'; clearData()">添加分类</el-button>
                </div>
                <br><br>
            </div>
        </template>
        <el-table :data="categories" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="name"> </el-table-column>
            <el-table-column label="设备数" prop="count"> </el-table-column>
            <el-table-column label="描述" prop="description"> </el-table-column>
            <el-table-column label="操作"> 
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showOperationDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 操作弹窗 -->
        <el-dialog :title="title" v-model="dialogVisible" width="30%" center>
            <el-form :model="categoryDataModel" :rules="rules" label-width="80px" size="large" >
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="categoryDataModel.name" minlength="1" maxlength = "32"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="categoryDataModel.description" maxlength = "100"></el-input>
                </el-form-item>
                <el-form-item >
                    <span>
                    <el-button type="primary" auto-insert-space @click="dialogVisible = false; clearData()"  class="dialog-btn">取消</el-button>
                    <el-button type="primary" auto-insert-space @click="title == '添加分类' ? addCategory() : updateCategory()" class="dialog-btn" >确认</el-button>
                    </span>
                </el-form-item>
            </el-form>
        </el-dialog>
    </el-card>
</template>


<style lang="scss" scoped>
/* Your component styles here */
.extra{
    float: right;
}
</style>
