package com.param.bs_backend.service.impl;

import com.param.bs_backend.mapper.DeviceMapper;
import com.param.bs_backend.param.*;
import com.param.bs_backend.param.*;
import com.param.bs_backend.pojo.Device;
import com.param.bs_backend.pojo.PageResult;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceMapper deviceMapper;

    /**
     * 獲取某個設備信息
     *
     * @param deviceId 目標設備的id
     * @return Result 包含設備信息的結果
     */
    @Override
    public Result<Device> getDeviceInfo(String deviceId) {
        try {
            // 調用 Mapper 層查詢設備信息
            Device device = deviceMapper.getDeviceInfo(deviceId);

            if (device != null) {
                return Result.success(device);
            } else {
                return Result.error("設備不存在");
            }
        } catch (Exception e) {
            log.error("獲取設備信息失敗", e);
            return Result.error("獲取設備信息失敗：" + e.getMessage());
        }
    }

    /**
     * 查詢用戶設備列表
     *
     * @param userId 用戶的id
     * @return Result 包含設備列表的結果
     */
    @Override
    public Result<DeviceListWithCount> getUserDevices(Integer userId) {
        try {
            // 調用 Mapper 層查詢用戶設備列表
            List<DeviceListResponse> devices = deviceMapper.getUserDevices(userId);

            // 計算設備類型數量
            Map<Integer, Long> countMap;
            countMap = devices.stream()
                    .collect(Collectors.groupingBy(DeviceListResponse::getDeviceType, Collectors.counting()));

            // 驗證設備類型是否在指定範圍內
            for (Integer type : countMap.keySet()) {
                if (type < 1 || type > 6) {
                    return Result.error("無效的設備類型：" + type);
                }
            }

            // 創建 countList
            List<Integer> countList = Arrays.asList(
                    Math.toIntExact(countMap.getOrDefault(1, 0L)),
                    Math.toIntExact(countMap.getOrDefault(2, 0L)),
                    Math.toIntExact(countMap.getOrDefault(3, 0L)),
                    Math.toIntExact(countMap.getOrDefault(4, 0L)),
                    Math.toIntExact(countMap.getOrDefault(5, 0L)),
                    Math.toIntExact(countMap.getOrDefault(6, 0L))
            );

            // 封裝到新的類中
            DeviceListWithCount result = new DeviceListWithCount(devices, countList);

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("獲取設備列表失敗：" + e.getMessage());
        }
    }

    /**
     * 新增設備
     *
     * @param request 新增設備參數類
     * @return 返回新增設備處理結果
     */
    @Override
    public Result<String> addDevice(DeviceAddRequest request) {
        try {
            int insertRowCount = deviceMapper.insertDevice(request);
            if (insertRowCount != 0) {
                return Result.success("設備添加成功");
            } else {
                return Result.error("新增設備失敗：");
            }

        } catch (Exception e) {
            log.error("新增設備失敗", e);
            return Result.error("新增設備失敗：" + e.getMessage());
        }
    }


    /**
     * 修改設備配置
     *
     * @param request  修改設備配置參數類
     * @param deviceId 設備id
     * @return 返回修改設備配置處理結果
     */
    @Override
    public Result<String> updateDevice(String deviceId, DeviceUpdateRequest request) {
        try {
            // 更新設備信息時，只更新參數不為null的字段
            int updateRowCount = deviceMapper.updateDevice(deviceId, request);
            if (updateRowCount != 0) {
                return Result.success("設備信息更新成功");
            } else {
                return Result.error("設備信息更新失敗，設備不存在或更新行數為0");
            }
        } catch (Exception e) {
            log.error("更新設備信息失敗", e);
            return Result.error("更新設備信息失敗：" + e.getMessage());
        }
    }

    /**
     * 獲取最近七天新增設備數量
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天新增的設備列表
     */
    @Override
    public Result<List<DeviceCountResponse>> getNewDevicesCount(Integer userId, Date today) {
        try {
            List<DeviceCountResponse> counts = deviceMapper.getNewDevicesCount(userId, today);
            return Result.success(counts);
        } catch (Exception e) {
            log.error("獲取最近七天新增設備數量失敗", e);
            return Result.error("獲取最近七天新增設備數量失敗：" + e.getMessage());
        }
    }

    /**
     * 根據查詢條件返回分頁數據
     *
     * @param deviceSearchRequest 查詢條件
     * @param page                 當前的頁號
     * @param pageSize             頁的大小
     * @return 分頁結果
     */
    @Override
    public PageResult<List<DeviceListResponse>> searchDevicesWithPagination(
            DeviceSearchRequest deviceSearchRequest, int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            List<DeviceListResponse> lists = deviceMapper.searchDevicesWithPagination(
                    deviceSearchRequest, offset, pageSize);

            int totalCount = deviceMapper.countDevices(deviceSearchRequest);

            return new PageResult<>(lists, totalCount, page, pageSize);
        } catch (Exception e) {
            log.error("Querying paginated device list failed", e);
            throw new RuntimeException("Querying paginated device list failed: " + e.getMessage());
        }
    }

    /**
     * 刪除設備
     *
     * @param deviceId 設備id
     * @return 返回刪除結果
     */
    @Override
    public Result<String> deleteDevice(String deviceId) {
        try {
            // 調用 Mapper 層刪除設備
            int deleteRowCount = deviceMapper.deleteDevice(deviceId);
            if (deleteRowCount > 0) {
                return Result.success("設備刪除成功");
            } else {
                return Result.error("設備刪除失敗，設備不存在或刪除行數為0");
            }
        } catch (Exception e) {
            log.error("刪除設備失敗", e);
            return Result.error("刪除設備失敗：" + e.getMessage());
        }
    }
}

