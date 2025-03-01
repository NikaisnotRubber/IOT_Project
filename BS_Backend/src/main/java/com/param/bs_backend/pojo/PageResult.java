package com.param.bs_backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页获取设备结果类
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private T data;
    private int totalCount; // 總共返回的數量
    private int currentPage; // 當前頁
    private int pageSize; // 頁的大小
}

