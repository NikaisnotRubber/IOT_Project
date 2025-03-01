package com.param.bs_backend.service;


import com.param.bs_backend.param.MessageCountResponse;
import com.param.bs_backend.param.MessageResponse;
import com.param.bs_backend.param.TrackResponse;
import com.param.bs_backend.pojo.Result;

import java.util.Date;
import java.util.List;

public interface MessageService {

    /**
     * 查詢當前用戶所有設備的總消息數
     *
     * @param userId 用戶ID
     * @return 所有設備的總消息數量
     */
    Result<Integer> getTotalMessageCountByUserId(int userId);

    /**
     * 查詢設備的歷史軌跡信息
     *
     * @param deviceId 目標設備的id
     * @return 包含設備歷史軌跡信息的數組
     */
    Result<List<TrackResponse>> getDeviceHistoryTrack(String deviceId);

    /**
     * 查詢某一設備的歷史消息
     *
     * @param deviceId 目標設備的id
     * @return 設備歷史消息列表
     */
    Result<List<MessageResponse>> getDeviceHistory(String deviceId);

    /**
     * 查詢用戶所屬設備最近七天的消息總數
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天用戶接收的消息數量
     */
    Result<List<MessageCountResponse>> getReceivedMessageCount(Integer userId, Date today);
}
