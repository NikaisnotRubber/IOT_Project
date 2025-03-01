package com.param.bs_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() { // 测试连接数据库
        Long aLong = jdbcTemplate.queryForObject("select count(*) from iotdb", Long.class);

        System.out.println("========================");
        System.out.println(aLong);
    }

}