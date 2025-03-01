package com.param.bs_backend.mapper;


import com.param.bs_backend.param.*;
import com.param.bs_backend.param.*;
import org.apache.ibatis.annotations.*;
import com.param.bs_backend.pojo.Device;

import java.util.List;
import java.util.Date;

@Mapper
public interface DeviceMapper {

    /**
     * 獲取某個設備信息
     *
     * @param deviceId 設備ID
     * @return 返回設備信息
     */
    @Select("SELECT * FROM device WHERE device_id = #{deviceId}")
    Device getDeviceInfo(@Param("deviceId") String deviceId);

    /**
     * 獲取用戶設備信息列表
     *
     * @param userId 用戶ID
     * @return 返回用戶設備信息列表
     */
    @Select("SELECT device_id, device_name, device_type, device_description, registration_time, is_active FROM device WHERE user_id = #{userId}")
    List<DeviceListResponse> getUserDevices(@Param("userId") Integer userId);

    /**
     * 新增設備
     * 使用了 UUID() 生成 唯一的device_id，並將 registration_time 同時賦值給 last_update。
     *
     * @param request 新增設備參數類
     * @return 插入的記錄數
     */
    @Insert("INSERT INTO device (device_id, user_id, device_name, device_type, device_description, is_active, registration_time, last_update) " +
            "VALUES (UUID(), #{userId}, #{deviceName}, #{deviceType}, #{deviceDescription}, #{isActive}, #{registrationTime}, #{registrationTime})")
    int insertDevice(DeviceAddRequest request);

    /**
     * 修改設備配置
     *
     * @param request  修改設備配置參數類
     * @param deviceId 設備id
     * @return 修改的記錄數
     */
    @Update({
            "<script>",
            "UPDATE device",
            "<set>",
            "<if test='request.deviceName != null'>device_name = #{request.deviceName}, </if>",
            "<if test='request.deviceType != null'>device_type = #{request.deviceType}, </if>",
            "<if test='request.deviceDescription != null'>device_description = #{request.deviceDescription}, </if>",
            "<if test='request.lastUpdate != null'>last_update = #{request.lastUpdate}, </if>",
            "<if test='request.isActive != null'>is_active = #{request.isActive} </if>",
            "</set>",
            "WHERE device_id = #{deviceId}",
            "</script>"
    })
    int updateDevice(@Param("deviceId") String deviceId, @Param("request") DeviceUpdateRequest request);


    /**
     * 獲取最近七天新增設備數量
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天新增的設備列表
     */
    @Select("SELECT DATE(registration_time) AS date, COUNT(*) AS count " +
            "FROM device " +
            "WHERE user_id = #{userId} AND DATE(registration_time) BETWEEN DATE_SUB(#{today}, INTERVAL 6 DAY) AND #{today} " +
            "GROUP BY DATE(registration_time)" +
            "ORDER BY date ASC")
    // 按照日期升序排序
    List<DeviceCountResponse> getNewDevicesCount(@Param("userId") Integer userId, @Param("today") Date today);

    /**
     * 根據查詢條件返回分頁數據
     *
     * @param deviceSearchRequest 查詢條件
     * @param offset               偏移量（(page - 1) * pageSize）
     * @param pageSize             頁的大小
     * @return 分頁結果
     */
    @SelectProvider(type = DeviceSqlProvider.class, method = "searchDevicesWithPaginationSql")
    List<DeviceListResponse> searchDevicesWithPagination(
            @Param("searchRequest") DeviceSearchRequest deviceSearchRequest,
            @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 計算分頁獲得數據數量
     *
     * @param deviceSearchRequest 查詢請求條件
     * @return Total count
     */
    @SelectProvider(type = DeviceSqlProvider.class, method = "countDevicesSql")
    int countDevices(@Param("searchRequest") DeviceSearchRequest deviceSearchRequest);

    /**
     * 刪除設備
     *
     * @param deviceId 設備ID
     * @return 刪除的記錄數
     */
    @Delete("DELETE FROM device WHERE device_id = #{deviceId}")
    int deleteDevice(@Param("deviceId") String deviceId);

    /**
     * 根據設備名稱查詢匹配的所有設備ID
     *
     * @param deviceName 設備名稱
     * @return 匹配的所有設備ID列表
     */
    @Select("SELECT device_id FROM device WHERE device_name = #{deviceName}")
    List<String> findDeviceIdsByDeviceName(@Param("deviceName") String deviceName);

}
