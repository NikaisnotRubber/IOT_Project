package com.param.bs_backend.service;

import com.param.bs_backend.param.*;
import com.param.bs_backend.param.*;
import com.param.bs_backend.pojo.Device;
import com.param.bs_backend.pojo.PageResult;
import com.param.bs_backend.pojo.Result;

import java.util.Date;
import java.util.List;

public interface DeviceService {

    /**
     * 獲取某個設備信息
     *
     * @param deviceId 設備ID
     * @return 返回設備信息
     */
    Result<Device> getDeviceInfo(String deviceId);

    /**
     * 獲取用戶設備列表
     *
     * @param userId 用戶ID
     * @return 返回用戶設備信息列表
     */
    Result<DeviceListWithCount> getUserDevices(Integer userId);

    /**
     * 新增設備
     *
     * @param request 新增設備參數類
     * @return 返回新增設備處理結果
     */
    Result<String> addDevice(DeviceAddRequest request);

    /**
     * 修改設備配置
     *
     * @param request  修改設備配置參數類
     * @param deviceId 設備id
     * @return 返回修改設備配置處理結果
     */
    Result<String> updateDevice(String deviceId, DeviceUpdateRequest request);

    /**
     * 獲取最近七天新增設備數量
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天新增的設備列表
     */
    Result<List<DeviceCountResponse>> getNewDevicesCount(Integer userId, Date today);

    /**
     * 根據查詢條件返回分頁數據
     *
     * @param deviceSearchRequest 查詢條件
     * @param page                 當前的頁號
     * @param pageSize             頁的大小
     * @return 分頁結果(帶當前頁號和所有符合條件的結果數量+當前頁號+頁的大小)
     */
    PageResult<List<DeviceListResponse>> searchDevicesWithPagination(
            DeviceSearchRequest deviceSearchRequest, int page, int pageSize);

    /**
     * 刪除設備
     *
     * @param deviceId 設備id
     * @return 返回刪除結果
     */
    Result<String> deleteDevice(String deviceId);
}
