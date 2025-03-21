package com.param.bs_backend.mapper;

import com.param.bs_backend.param.MessageCountResponse;
import com.param.bs_backend.param.MessageResponse;
import com.param.bs_backend.param.TrackResponse;
import com.param.bs_backend.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * 查詢當前用戶所有設備的總消息數
     *
     * @param userId 用戶ID
     * @return 所有設備的總消息數量
     */
    @Select("SELECT COUNT(*) FROM message WHERE device_id IN (SELECT device_id FROM device WHERE user_id = #{userId})")
    int getTotalMessageCountByUserId(@Param("userId") int userId);

    /**
     * 查詢設備的歷史軌跡信息
     *
     * @param deviceId 目標設備的id
     * @return 包含設備歷史軌跡信息的數組
     */
    @Select("SELECT timestamp, latitude, longitude FROM message " +
            "WHERE device_id = #{deviceId} " +
            "ORDER BY timestamp DESC ")
    List<TrackResponse> getDeviceHistoryTrack(@Param("deviceId") String deviceId);

    /**
     * 查詢某一設備的歷史消息
     *
     * @param deviceId 目標設備的id
     * @return 設備歷史消息列表
     */
    @Select("SELECT message_id, timestamp, message_type, message_content, latitude, longitude, value " +
            "FROM message " +
            "WHERE device_id = #{deviceId} " +
            "ORDER BY timestamp DESC")
    // 按照時間戳降序排序
    List<MessageResponse> getDeviceHistory(@Param("deviceId") String deviceId);

    /**
     * 查詢用戶所屬設備最近七天的消息總數
     *
     * @param userId 用戶id
     * @param today  當天的日期
     * @return 最近七天用戶接收的消息數量
     */
    @Select("SELECT DATE(m.timestamp) AS date, " +
            "COUNT(*) AS count, " +
            "SUM(CASE WHEN m.message_type = 0 THEN 1 ELSE 0 END) AS normalCount, " +
            "SUM(CASE WHEN m.message_type = 1 THEN 1 ELSE 0 END) AS abnormalCount " +
            "FROM message m " +
            "JOIN device d ON m.device_id = d.device_id " +
            "WHERE d.user_id = #{userId} AND DATE(m.timestamp) BETWEEN DATE_SUB(#{today}, INTERVAL 6 DAY) AND #{today} " +
            "GROUP BY DATE(m.timestamp)" +
            "ORDER BY date DESC")
    List<MessageCountResponse> getReceivedMessageCount(@Param("userId") Integer userId, @Param("today") Date today);

    /**
     * 新增消息
     *
     * @param message 封裝了新增消息數據的對象
     */
    @Insert("INSERT INTO message (device_id, timestamp, message_type, message_content, latitude, longitude, value) " +
            "VALUES (#{message.deviceId}, #{message.timestamp}, #{message.messageType}, " +
            "#{message.messageContent}, #{message.latitude}, #{message.longitude}, #{message.value})")
    void insertMessage(@Param("message") Message message);
}
