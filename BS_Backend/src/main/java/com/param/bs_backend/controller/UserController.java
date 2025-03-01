package com.param.bs_backend.controller;

import com.param.bs_backend.param.LoginRequest;
import com.param.bs_backend.param.LoginResponse;
import com.param.bs_backend.param.PasswordRequest;
import com.param.bs_backend.param.UserInfoRequest;
import com.param.bs_backend.pojo.*;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.pojo.User;
import com.param.bs_backend.service.UserService;
import com.param.bs_backend.annotation.TokenRequired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin // 允許跨域
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用戶登錄
     *
     * @param loginRequest 傳入的用戶實例信息（用一個封裝類接受）
     * @return 返回service層處理後的Result實例
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * 用戶註冊
     *
     * @param user 註冊用戶信息
     * @return 返回註冊結果
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 用戶修改密碼(TODO:還未實現token鑒權攔截)
     *
     * @param passwordRequest 包含用戶名、舊密碼、新密碼的請求體
     * @return 返回修改密碼結果
     */
    @PostMapping("/updatePassword")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> updatePassword(@RequestBody PasswordRequest passwordRequest) {
        return userService.updatePassword(passwordRequest.getUsername(), passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
    }

    /**
     * 編輯個人信息
     *
     * @param userInfoRequest 包含用戶ID、新用戶名、新郵箱和新手機號的請求體
     * @return 返回編輯個人信息結果
     */
    @PostMapping("/editUserInfo")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> editUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        System.out.println(userInfoRequest);
        return userService.editUserInfo(userInfoRequest.getUserId(), userInfoRequest.getNewUsername(), userInfoRequest.getNewEmail(), userInfoRequest.getNewPhone());
    }

    /**
     * 根據user_id重新獲取用戶信息
     *
     * @param userId 用戶id
     * @return 登錄結果的response類（存儲用戶信息和token）
     */
    @GetMapping("/getUserInfo")
    @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<LoginResponse> login(@RequestParam("user_id") Integer userId) {
        return userService.getUserInfo(userId);
    }
}
