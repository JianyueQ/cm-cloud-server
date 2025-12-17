package com.cm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 31373
 */
@SpringBootApplication
@EnableTransactionManagement // 开启事务
@EnableScheduling // 开启定时任务
@EnableAspectJAutoProxy(proxyTargetClass = true) // 开启aop
public class CmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmServerApplication.class, args);
    }

}
