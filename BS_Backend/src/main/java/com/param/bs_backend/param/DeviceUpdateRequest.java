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
public class DeviceUpdateRequest {
    @JsonProperty("device_name")
    private String deviceName;

    /*
    後端最好用封裝類代替基本類來定義接受請求體參數的變量，因為基本類可能無法保存Null值，
    會將Null存為默認值，從而對動態sql的判斷條件產生影響，比如這裡用int存的話，如果前端傳入null或者undefined
    它就會變成默認值0，影響後面動態sql非null的判斷
     */
    @JsonProperty("device_type")
    private Integer deviceType;

    @JsonProperty("device_description")
    private String deviceDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("last_update")
    private Date lastUpdate;

    @JsonProperty("is_active")
    private Boolean isActive;
}

