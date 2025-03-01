package com.param.bs_backend.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAddRequest {
    // 使用 @JsonProperty 註解來指定請求體中的參數與實體類字段之間的映射關係
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("device_type")
    private int deviceType;

    @JsonProperty("device_description")
    private String deviceDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("registration_time")
    private Date registrationTime;

    @JsonProperty("is_active")
    private Boolean isActive;
}

