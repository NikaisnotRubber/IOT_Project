package com.param.bs_backend.controller;

import com.param.bs_backend.annotation.TokenRequired;
import com.param.bs_backend.param.*;
import com.param.bs_backend.param.*;
import com.param.bs_backend.pojo.Device;
import com.param.bs_backend.pojo.PageResult;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin // 允許跨域，相當於是給我們服務器一個白名單，讓他不會攔截我們的靜態資源
@Slf4j
@RestController
@RequestMapping("device")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    /**
     * 獲取某個設備信息
     *
     * @param deviceId 設備ID
     * @return 返回設備信息
     */
    @GetMapping("/{device_id}")
     @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<Device> getDeviceInfo(
            @PathVariable("device_id") String deviceId
    ) {
        return deviceService.getDeviceInfo(deviceId);
    }

    /**
     * 獲取用戶設備列表
     *
     * @param userId 用戶的id
     * @return Result 包含設備列表的結果
     */
    @GetMapping("/user/{user_id}")
     @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<DeviceListWithCount> getUserDevices(
            @PathVariable("user_id") Integer userId) {
        return deviceService.getUserDevices(userId);
    }

    /**
     * 新增設備
     *
     * @param request 修改設備配置參數類
     * @return 返回新增設備處理結果
     */
    @PostMapping("/add")
     @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> addDevice(@RequestBody DeviceAddRequest request) {
        System.out.println(request);
        return deviceService.addDevice(request);
    }

    /**
     * 修改設備配置
     *
     * @param request  修改設備配置參數類
     * @param deviceId 設備id
     * @return 返回修改設備配置處理結果
     */
    @PutMapping("/update/{device_id}")
     @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> updateDevice(
            @PathVariable("device_id") String deviceId,
            @RequestBody DeviceUpdateRequest request) {
        System.out.println(request);
        return deviceService.updateDevice(deviceId, request);
    }

    /**
     * 獲取最近七天新增設備數量
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天新增的設備列表
     */
    @GetMapping("/new-devices-count")
    public Result<List<DeviceCountResponse>> getNewDevicesCount(
            @RequestParam("user_id") Integer userId,
            @RequestParam("today") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date today) {
        return deviceService.getNewDevicesCount(userId, today);
    }

    /**
     * 查詢設備列表（帶分頁）
     *
     * @param userId       用戶ID
     * @param deviceId     設備ID（可為空）
     * @param deviceName   設備名稱（可為空）
     * @param deviceType   設備類型（可為空）
     * @param page         頁碼
     * @param pageSize     每頁顯示數量
     * @return 設備列表（帶分頁）
     */
    @GetMapping("/search-with-pagination")
    public Result<PageResult<List<DeviceListResponse>>> searchDevicesWithPagination(
            @RequestParam("user_id") Integer userId,
            @RequestParam(value = "device_id", required = false) String deviceId,
            @RequestParam(value = "device_name", required = false) String deviceName,
            @RequestParam(value = "device_type", required = false) Integer deviceType,
            @RequestParam(value = "is_active", required = false) Boolean isActive,
            @RequestParam("current") int page,
            @RequestParam("size") int pageSize) {

        DeviceSearchRequest searchRequest = new DeviceSearchRequest();
        searchRequest.setUserId(userId);
        searchRequest.setDeviceId(deviceId);
        searchRequest.setDeviceName(deviceName);
        searchRequest.setDeviceType(deviceType);
        searchRequest.setIsActive(isActive);

        try {
            PageResult<List<DeviceListResponse>> pageResult = deviceService.searchDevicesWithPagination(
                    searchRequest, page, pageSize);
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("Error retrieving paginated device list", e);
            return Result.error("Error retrieving paginated device list: " + e.getMessage());
        }
    }

    /**
     * 刪除設備
     *
     * @param deviceId 設備id
     * @return 返回刪除結果
     */
    @DeleteMapping("/delete/{device_id}")
    public Result<String> deleteDevice(@PathVariable("device_id") String deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

}
