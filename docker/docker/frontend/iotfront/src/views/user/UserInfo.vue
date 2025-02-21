<script setup>
import {ref} from 'vue';
import useUserInfoStore from '@/stores/userInfo.js';
const userInfoStore = useUserInfoStore();

//用户信息
const userInfo = ref({...userInfoStore.info});

//表单验证规则
const rules = {
    nickname: [
        {min: 0, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'},
    ],
    email: [
        {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']},
    ]
};

import { userInfoUpdateService } from '@/api/user.js';
import { ElMessage } from 'element-plus';
// 更新用户信息
const updateUserInfo = async ()=>{
    //调用接口
    let result = await userInfoUpdateService(userInfo.value);
    ElMessage.success(result.message? result.message : '提交成功');
    
    //修改pinia中的个人信息
    userInfoStore.setInfo(userInfo.value)
}


</script>

<template>
    <el-card class="user-container">
        <template #header>
            <strong><span>基本资料</span></strong>
        </template>
        <el-row>
            <el-col :span="9">
                <el-form :model="userInfo" :rules="rules"  label-width="100px" size="large">
                    <el-form-item label="用户名">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname" ></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item label="个人介绍">
                        <el-input v-model="userInfo.description"></el-input>
                    </el-form-item>
                    <el-form-item label="注册时间">
                        <el-input v-model="userInfo.createTime" disabled></el-input>
                    </el-form-item>
                    <el-form-item >
                        <el-button type="primary" auto-insert-space @click="updateUserInfo" class="btn" >提交</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>

</template>

<style lang="scss" scoped>
.btn{
    margin: 0 auto;
}
</style>
