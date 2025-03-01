package com.param.bs_backend;

import com.param.bs_backend.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:8080")
@Import(JwtUtil.class)
public class BsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BsBackendApplication.class, args);
    }

}
