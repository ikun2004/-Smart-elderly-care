package cn.yangeit.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring Boot 中使用 Swagger UI 构建 RESTful API")//文档信息
                        .contact(new Contact().email("798391056@qq.com"))//联系信息
                        .description("中州养老AI+IOT平台提供的 RESTful API")//文档描述
                        .version("v1.0.0")//文档版本
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))//许可信息
                        .externalDocs(new ExternalDocumentation()//文档外链
                        .description("学习笔记")
                        .url("http://www.yangeit.cn:21010/expand/schoolsx/zzoldpeople20/"));

    }
}
