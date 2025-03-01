package com.param.bs_backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor // 無參構造函數
@AllArgsConstructor // 有參構造函數
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    
}

