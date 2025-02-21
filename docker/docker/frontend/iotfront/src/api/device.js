import request from '@/utils/request.js';

// 分类接口
// 设备分类列表
export const listCategoryService = ()=>{
    return request.get('/category');
}

// 新增设备分类
export const addCategoryService = (categoryData)=>{
    return request.post('/category', categoryData);
}

// 获取某分类详情
export const getCategoryService = (id)=>{
    return request.get('/category/detail?id='+id);
}
// 更新分类信息
export const updateCategoryService = (categoryData)=>{
    return request.put('/category',categoryData);
}
// 删除分类
export const deleteCategoryService = (id)=>{
    return request.delete('/category?id='+id);
}

// 设备管理接口
// 获取设备列表,传递分页参数
export const listDeviceService = (params)=>{
    return request.get('/device',{params:params});
}

// 新增设备
export const addDeviceService = (deviceData)=>{
    return request.post('/device',deviceData);
}

// 获取某设备详情
export const getDeviceService = (id)=>{
    return request.get('/device/detail?id='+id);
}

// 更新设备信息
export const updateDeviceService = (deviceData)=>{
    return request.put('/device',deviceData);
}

// 删除设备
export const deleteDeviceService = (id)=>{
    return request.delete('/device?id='+id);
}


//统计警告设备数
export const countWarnDeviceService = (id)=>{
    return request.get('/device/warn?id='+id);
}
//统计正常设备数
export const countNormDeviceService = (id)=>{
    return request.get('/device/norm?id='+id);
}

//统计掉线设备数
export const countOfflineDeviceService = (id)=>{
    return request.get('/device/offline?id='+id);
}






