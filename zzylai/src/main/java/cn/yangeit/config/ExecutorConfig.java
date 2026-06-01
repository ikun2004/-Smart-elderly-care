package cn.yangeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        // 创建一个固定大小的线程池，线程数量可根据业务需求调整
        return Executors.newFixedThreadPool(10); 
    }
}