package com.param.bs_backend.controller;

import com.param.bs_backend.annotation.TokenRequired;
import com.param.bs_backend.param.MessageCountResponse;
import com.param.bs_backend.param.MessageResponse;
import com.param.bs_backend.param.TrackResponse;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin // 允許跨域
@Slf4j
@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 查詢當前用戶所有設備的總消息數
     *
     * @param userId 用戶ID
     * @return 返回所有設備的總消息數量
     */
    @GetMapping("/total-count")
     @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<Integer> getTotalMessageCount(@RequestParam("user_id") int userId) {
        return messageService.getTotalMessageCountByUserId(userId);
    }

    /**
     * 查詢設備的歷史軌跡信息
     *
     * @param deviceId 目標設備的id
     * @return 響應結果
     */
    @GetMapping("/history-track/{device_id}")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<List<TrackResponse>> getDeviceHistoryTrack(@PathVariable("device_id") String deviceId) {
        return messageService.getDeviceHistoryTrack(deviceId);
    }

    /**
     * 查詢某一設備的歷史消息
     *
     * @param deviceId 目標設備的id
     * @return 設備歷史消息列表
     */
    @GetMapping("/device-history/{device_id}")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<List<MessageResponse>> getDeviceHistory(
            @PathVariable("device_id") String deviceId) {
        return messageService.getDeviceHistory(deviceId);
    }

    /**
     * 查詢最近七天接收的消息數
     *
     * @param userId 用戶id
     * @return 響應結果
     */
    @GetMapping("/received-count")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<List<MessageCountResponse>> getReceivedMessageCount(
            @RequestParam("user_id") Integer userId,
            @RequestParam("today") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date today) {
        return messageService.getReceivedMessageCount(userId, today);
    }

}
