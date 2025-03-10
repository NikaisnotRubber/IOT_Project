package com.param.bs_backend.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int messageId;
    private String deviceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;
    private int messageType;
    private String messageContent;
    private double latitude;
    private double longitude;
    // 設備數據（根據mqtt客戶端新增的字段）
    private int value;

}
