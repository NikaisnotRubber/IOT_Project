package com.param.bs_backend.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 無參構造函數
@AllArgsConstructor // 有參構造函數
public class UserInfoRequest {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("new_username")
    private String newUsername;

    @JsonProperty("new_email")
    private String newEmail;

    @JsonProperty("new_phone")
    private String newPhone;

}

