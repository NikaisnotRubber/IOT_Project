package com.param.bs_backend.service;

import com.param.bs_backend.mapper.DeviceMapper;
import com.param.bs_backend.param.DeviceAddRequest;
import com.param.bs_backend.param.LoginResponse;
import com.param.bs_backend.pojo.Result;
import com.param.bs_backend.pojo.User;
import com.param.bs_backend.mapper.UserMapper;
import com.param.bs_backend.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 處理用戶登錄的service方法
     *
     * @param username 登錄輸入的用戶名
     * @param password 登錄輸入的密碼
     * @return 登錄結果的response類
     */
    public Result<LoginResponse> login(String username, String password) {
        try {
            // 檢查用戶是否存在
            User user = userMapper.findByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            } else {
                // 驗證密碼
                if (!user.getPassword().equals(password)) {
                    return Result.error("密码错误");
                } else {
                    return createLoginResponse(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }



    /**
     * 處理用戶註冊的service方法
     *
     * @param user 用註冊時輸入的用戶信息構造的 User 類
     * @return 註冊結果的response類（附代token，便於前端實現註冊完畢直接跳轉）
     */
    public Result<LoginResponse> register(User user) {
        try {
            // 判斷email是否已經存在
            User existingEmail = userMapper.findByEmail(user.getEmail());
            if(existingEmail!=null){
                return Result.error("注册邮箱已存在！");
            }
            // 判斷用戶名是否已存在
            User existingUser = userMapper.findByUsername(user.getUsername());
            if (existingUser != null) {
                return Result.error("用户名已存在！");
            }

            // (TODO:判斷其他業務邏輯，例如密碼複雜度等)

            // 註冊用戶並返回用戶信息及JWT令牌（調用registerUser方法時，會自動把自增id賦值給user對象）
            int insertRowCount = userMapper.registerUser(user);
            if (insertRowCount == 0) {
                return Result.error("新增用戶失敗！");
            }

            // 在用户注册成功后，自动帮用户注册device0001-device0005的设备（为了接受mqtt的消息）
            addDevicesForUser(user.getId());

            return createLoginResponse(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("註冊失敗：" + e.getMessage());
        }
    }

    /**
     * 創建 LoginResponse 類的方法（在內部實現了創建token的邏輯）
     *
     * @param user 用於創建 LoginResponse 類的 User 類
     * @return 用user信息和token封裝成的 LoginResponse 類
     */
    private Result<LoginResponse> createLoginResponse(User user) {
        String token = JwtUtil.sign(user.getId());

        // 構造 LoginResponse 對象
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setToken(token);

        // 返回成功結果，包含 LoginResponse 對象
        return Result.success(loginResponse);
    }

    /**
     * 處理用戶修改密碼的service方法
     *
     * @param username    用戶名
     * @param oldPassword 舊密碼
     * @param newPassword 新密碼
     * @return 修改密碼結果的response類
     */
    // @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> updatePassword(String username, String oldPassword, String newPassword) {
        try {
            // 通过用户名查找用户，同时也返回密码以供校验
            User user = userMapper.findByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }

            System.out.println("原密碼: "+user.getPassword()+" 傳入的舊密碼: "+oldPassword);
            // 驗證舊密碼是否正確
            if (!user.getPassword().equals(oldPassword)) {
                return Result.error("舊密碼不正確");
            }

            // 更新密碼
            int updateRowCount = userMapper.updatePassword(username, oldPassword, newPassword);

            if (updateRowCount > 0) {
                return Result.success("密碼更新成功!");
            } else {
                return Result.error("密碼更新失敗!");
            }
        } catch (Exception e) {
            log.error("修改密碼失敗：" + e.getMessage());
            return Result.error("修改密碼失敗：" + e.getMessage());
        }
    }

    /**
     * 處理用戶編輯個人信息的service方法
     *
     * @param userId      用戶ID
     * @param newUsername 新用戶名
     * @param newEmail    新郵箱
     * @param newPhone    新手機號
     * @return 編輯個人信息結果的response類
     */
    // @TokenRequired // 添加 TokenRequired 註解，表示需要 token 鑒權
    public Result<String> editUserInfo(Integer userId, String newUsername, String newEmail, String newPhone) {
        try {
            // 判斷email是否已經存在
            User existingEmail = userMapper.findByEmail(newEmail);
            if(existingEmail!=null){
                return Result.error("新郵箱已存在！");
            }
            // 判斷用戶名是否已存在
            User existUser = userMapper.findByUsername(newUsername);
            if (existUser != null) {
                return Result.error("新用戶名已存在！");
            }

            // 檢查新用戶名是否和現有用戶名一致
            User existingUser = userMapper.findById(userId);

            if (existingUser != null) {
                if (Objects.equals(existingUser.getUsername(), newUsername)) {
                    return Result.error("新用戶名與當前用戶名相同!");
                } else if (Objects.equals(existingUser.getEmail(), newEmail)) {
                    return Result.error("新郵箱與當前郵箱相同!");
                } else if (Objects.equals(existingUser.getPhone(), newPhone)) {
                    return Result.error("新手機號與當前手機號相同!");
                }
            }
            assert existingUser != null;
            System.out.println(existingUser);

            // 更新用户信息
            int updateRowCount = userMapper.updateUserInfo(userId, newUsername, newEmail, newPhone);

            if (updateRowCount > 0) {
                return Result.success("個人信息編輯成功");
            } else {
                return Result.error("個人信息編輯失敗或沒有修改任何信息");
            }
        } catch (Exception e) {
            log.error("編輯個人信息失敗：" + e.getMessage());
            return Result.error("編輯個人信息失敗：" + e.getMessage());
        }
    }

    public User findUserById(Integer userId) {
        try {
            // Call the method in your UserMapper to find user by ID
            return userMapper.findById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            // handle exceptions or return null based on requirements
            return null;
        }
    }

    /**
     * 根據user_id重新獲取用戶信息
     *
     * @param userId 用戶id
     * @return 登錄結果的response類（存儲用戶信息和token）
     */
    public Result<LoginResponse> getUserInfo(Integer userId) {
        try {
            // 检查用户是否存在
            User user = userMapper.findById(userId);
            if (user == null) {
                return Result.error("用戶不存在");
            } else {
                    return createLoginResponse(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("獲取用戶信息失敗：" + e.getMessage());
        }
    }

    /**
     * 在用戶註冊成功後新增設備的方法
     *
     * @param userId 新註冊用戶的ID
     */
    private void addDevicesForUser(Integer userId) {
        try {
            // 新增五个设备
            for (int i = 1; i <= 5; i++) {
                DeviceAddRequest device = new DeviceAddRequest();
                device.setUserId(userId);
                device.setDeviceName("device000" + i);
                device.setDeviceDescription("系統自動新增的設備");
                device.setDeviceType(new Random().nextInt(6) + 1);
                device.setIsActive(true);

                // 設置 registrationTime 和 lastUpdate（這個會在mapper中自動設成和registrationTime一致） 為當前時間
                Date currentTime = new Date();
                device.setRegistrationTime(currentTime);

                // 調用 deviceMapper 插入設備
                deviceMapper.insertDevice(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增設備失敗：" + e.getMessage());
        }
    }
}
