package com.atguigu.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: admin
 * @date: 2021/10/29 9:41
 * @description:
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.srb", "com.atguigu.common"})
@EnableFeignClients
public class ServiceSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}