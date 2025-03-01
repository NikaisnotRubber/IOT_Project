package com.param.bs_backend.param;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 無參構造函數
@AllArgsConstructor // 有參構造函數
public class PasswordRequest {

    private String username;

    @JsonProperty("old_password")
    @JsonAlias("oldPassword") // 兼容其他名稱
    private String oldPassword;

    @JsonProperty("new_password")
    @JsonAlias("newPassword") // 兼容其他名稱
    private String newPassword;

}

