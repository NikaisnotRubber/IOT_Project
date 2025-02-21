//定制请求的实例
import axios from 'axios';
import { ElMessage } from 'element-plus';

import router from '@/router';
import { useTokenStore } from '@/stores/token';

//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = '/api';
const instance = axios.create({baseURL})

instance.interceptors.request.use(
    config=>{
        //获取pinia
        const tokenStore = useTokenStore();
        //判断是否有token
        if(tokenStore.token){
            config.headers.Authorization = tokenStore.token;
        }
        return config;
    },
    err=>{
        return Promise.reject(err);
    }
)


//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        if(result.data.code===0){
            return result.data;
        }
        ElMessage.error(result.data.message?result.data.message:'服务异常');
        return Promise.reject(result.data);
    },
    err=>{
        if(err.response && err.response.status===401){
            ElMessage.error('请登录后再访问资源');
            //跳转到登录页面
            router.push('/login');
        }else {
            ElMessage.error('服务异常');
        }
        
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;