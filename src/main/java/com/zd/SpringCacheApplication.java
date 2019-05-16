package com.zd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zd.dao.mapper")
@EnableCaching
public class SpringCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheApplication.class, args);
        System.out.println("启动成功！");
    }

}
