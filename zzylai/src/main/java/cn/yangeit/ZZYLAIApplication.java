package cn.yangeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//  启动类
@SpringBootApplication
@EnableCaching//开启缓存
public class ZZYLAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZZYLAIApplication.class, args);
        System.out.println("启动成功");
    }

}
