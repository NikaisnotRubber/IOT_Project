package com.param.bs_backend.mapper;

import com.param.bs_backend.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // 函數中的Param註解可以不用（只有在SpringBoot1.x版本或者單獨使用Mybatis時才需要）
    // 如果查詢的表字段與實體類的字段一致（駝峰對下划線可以視為一致），通常情況下不需要額外的 @Results 註解。
    // MyBatis 會自動將查詢結果映射到實體類的字段，前提是實體類的字段名與表字段名一致。（下面某幾個接口的Result註解其實可以刪去）

    // 註冊用戶並返回自動生成的用戶ID，返回的結果是新增的行數
    @Insert("INSERT INTO user (username, password, email, phone) VALUES (#{user.username}, #{user.password}, #{user.email}, #{user.phone})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    //使用@Options註解配置自動生成主鍵，以便獲取自動生成的用戶ID
    int registerUser(@Param("user") User user);

    // 通過用戶名查找用戶，同時也返回密碼以供校驗
    @Select("SELECT id, username, password, email, phone FROM user WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone")
    })
    User findByUsername(@Param("username") String username);

    // 通過email查找用戶
    @Select("SELECT id, username, password, email, phone FROM user WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone")
    })
    User findByEmail(@Param("email") String email);

    // 更新用戶密碼
    @Update("UPDATE user SET password = #{newPassword} WHERE username = #{username} AND password = #{oldPassword}")
    int updatePassword(@Param("username") String username, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    // 更新用戶信息（包括用戶名、郵箱和手機號）
    @Update({
            "<script>",
            "UPDATE user",
            "<set>",
            "<if test='newUsername != null'>",
            "username = #{newUsername},",
            "</if>",
            "<if test='newEmail != null'>",
            "email = #{newEmail},",
            "</if>",
            "<if test='newPhone != null'>",
            "phone = #{newPhone}",
            "</if>",
            "</set>",
            "WHERE id = #{userId}",
            "</script>"
    })
    int updateUserInfo(@Param("userId") Integer userId, @Param("newUsername") String newUsername, @Param("newEmail") String newEmail, @Param("newPhone") String newPhone);

    // 根據userId來查找用戶
    @Select("SELECT id, username, password, email, phone FROM user WHERE id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone")
    })
    User findById(@Param("userId") Integer userId);
}
