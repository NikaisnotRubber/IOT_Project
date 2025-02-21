<script setup>
import { ref } from 'vue';

//密码数据模型
const resetPasswordData = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
});

//校验密码是否一致
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    } else if (value !== resetPasswordData.value.new_pwd) {
        callback(new Error('请确保两次输入的密码一样'))
    } else {
        callback()
    }
}

//校验表单规则
const pwrules = {
    old_pwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 32, message: '长度在 5 到 32 个字符', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 32, message: '长度在 5 到 32 个字符', trigger: 'blur' }
    ],
    re_pwd: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}

import { userResetPassword } from '@/api/user.js';
import { ElMessage } from 'element-plus';
import { useTokenStore } from '@/stores/token.js';
const tokenStore = useTokenStore();
const resetPassword = async ()=>{
    let res = await userResetPassword(resetPasswordData.value);
    ElMessage.success(res.message ? res.message : '修改成功');

    tokenStore.removeToken();
}



</script>

<template>
    <el-card class="user-container">
        <template #header>
            <strong><span>修改密码</span></strong>
        </template>
        <el-row>
            <el-col :span="9">
                <!--注意prop 和设定的rules内命名需要和 input v-model的绑定属性名 一致 -->
                <el-form  label-width="100px" size="large" :model="resetPasswordData" :rules="pwrules" >
                    <el-form-item  label="旧密码" prop="old_pwd">
                        <el-input type="password" v-model="resetPasswordData.old_pwd" ></el-input>
                    </el-form-item>
                    <el-form-item  label="新密码" prop="new_pwd">
                        <el-input type="password" v-model="resetPasswordData.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item  label="确认密码" prop="re_pwd">
                        <el-input type="password" v-model="resetPasswordData.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item >
                        <el-button type="primary" auto-insert-space @click="resetPassword" class="btn" >提交</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>


<style lang="scss" scoped>
/* Your component styles here */
.btn{
    margin: 0 auto;
}
</style>
