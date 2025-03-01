package com.param.bs_backend.param;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class DeviceSqlProvider {

    /**
     * 動態生成查詢設備列表的 SQL 語句
     *
     * @param deviceSearchRequest 查詢條件
     * @param offset 偏移量（(page - 1) * pageSize）
     * @param pageSize 頁的大小
     * @return SQL 語句
     */
    public String searchDevicesWithPaginationSql(
            @Param("searchRequest") DeviceSearchRequest deviceSearchRequest,
            @Param("offset") int offset, @Param("pageSize") int pageSize) {
        return new SQL() {{
            SELECT("device_id, device_name, device_type, device_description, registration_time, is_active");
            FROM("device");
            WHERE("user_id = #{searchRequest.userId}");
            if (deviceSearchRequest.getDeviceId() != null && !deviceSearchRequest.getDeviceId().equals("")) {
                AND().WHERE("device_id = #{searchRequest.deviceId}");
            }
            if (deviceSearchRequest.getDeviceName() != null && !deviceSearchRequest.getDeviceName().equals("")) {
                AND().WHERE("device_name LIKE CONCAT('%', #{searchRequest.deviceName}, '%')");
            }
            if (deviceSearchRequest.getDeviceType() != null) {
                AND().WHERE("device_type = #{searchRequest.deviceType}");
            }
            if(deviceSearchRequest.getIsActive()!= null){
                AND().WHERE("is_active = #{searchRequest.isActive}");
            }

            ORDER_BY("registration_time DESC");
            LIMIT("#{pageSize} OFFSET #{offset}");
        }}.toString();
    }

    /**
     * 計算分頁獲得數據數量
     *
     * @param deviceSearchRequest 查詢請求條件
     * @return Total count
     */
    public String countDevicesSql(@Param("searchRequest") DeviceSearchRequest deviceSearchRequest) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("device");
            WHERE("user_id = #{searchRequest.userId}");

            if (deviceSearchRequest.getDeviceId() != null && !deviceSearchRequest.getDeviceId().equals("")) {
                AND().WHERE("device_id = #{searchRequest.deviceId}");
            }
            if (deviceSearchRequest.getDeviceName() != null && !deviceSearchRequest.getDeviceName().equals("")) {
                AND().WHERE("device_name LIKE CONCAT('%', #{searchRequest.deviceName}, '%')");
            }
            if (deviceSearchRequest.getDeviceType() != null) {
                AND().WHERE("device_type = #{searchRequest.deviceType}");
            }
            if(deviceSearchRequest.getIsActive()!= null){
                AND().WHERE("is_active = #{searchRequest.isActive}");
            }
        }}.toString();
    }
}
