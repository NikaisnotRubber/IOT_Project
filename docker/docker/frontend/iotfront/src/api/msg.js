//导入request.js请求工具
import request from '@/utils/request.js';


// 提供获取msg总数的函数
export const msgAllService = ()=>{
    return request.get('/msg/all');
}

// 提供获取msg警告的函数
export const msgWarnService = ()=>{
    return request.get('/msg/warn');
}
// 提供获取msg正常的函数
export const msgNormService = ()=>{
    return request.get('/msg/norm');
}

//获取点的最新信息
export const getNewPointsMsgService = ()=>{
    return request.get('/msg/points');
}
