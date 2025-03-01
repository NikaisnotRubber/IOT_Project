package com.param.bs_backend.service.impl;

import com.param.bs_backend.mapper.MessageMapper;
import com.param.bs_backend.param.MessageCountResponse;
import com.param.bs_backend.param.TrackResponse;
import com.param.bs_backend.param.MessageResponse;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 查詢當前用戶所有設備的總消息數
     *
     * @param userId 用戶id
     * @return 所有設備的總消息數
     */
    @Override
    public Result<Integer> getTotalMessageCountByUserId(int userId) {
        try {
            int counts = messageMapper.getTotalMessageCountByUserId(userId);
            return Result.success(counts);
        } catch (Exception e) {
            log.error("查詢當前用戶所有設備的總消息數失敗", e);
            return Result.error("查詢當前用戶所有設備的總消息數失敗：" + e.getMessage());
        }
    }

    /**
     * 查詢設備的歷史軌跡信息
     *
     * @param deviceId 目標設備的id
     * @return 包含設備歷史軌跡信息的數組
     */
    @Override
    public Result<List<TrackResponse>> getDeviceHistoryTrack(String deviceId) {
        try {
            // 調用mapper查詢設備的歷史軌跡信息
            List<TrackResponse> tracks = messageMapper.getDeviceHistoryTrack(deviceId);
            return Result.success(tracks);
        } catch (Exception e) {
            log.error("查詢歷史軌跡信息失敗", e);
            return Result.error("查詢歷史軌跡信息失敗：" + e.getMessage());
        }

    }

    /**
     * 查詢某一設備的歷史消息
     *
     * @param deviceId 目標設備的id
     * @return 設備歷史消息列表
     */
    @Override
    public Result<List<MessageResponse>> getDeviceHistory(String deviceId) {
        try {
            List<MessageResponse> messages = messageMapper.getDeviceHistory(deviceId);
            return Result.success(messages);
        } catch (Exception e) {
            log.error("查詢歷史消息失敗", e);
            return Result.error("查詢歷史消息失敗：" + e.getMessage());
        }
    }

    /**
     * 查詢用戶所屬設備最近七天的消息總數
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天用戶接收的消息數量
     */
    public Result<List<MessageCountResponse>> getReceivedMessageCount(Integer userId, Date today) {
        try {
            List<MessageCountResponse> messageCountLists = messageMapper.getReceivedMessageCount(userId, today);
            return Result.success(messageCountLists);
        } catch (Exception e) {
            log.error("查詢用戶所屬設備最近七天的消息總數失敗", e);
            return Result.error("查詢用戶所屬設備最近七天的消息總數失敗：" + e.getMessage());
        }
    }

}
