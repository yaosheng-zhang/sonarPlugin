package com.zhangys.carplugin.Config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2) // 文档类型，使用 Swagger2
                .apiInfo(this.apiInfo()) // 设置 API 信息
                // 扫描 Controller 包路径，获得 API 接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhangys.carplugin.Controller"))
                .paths(PathSelectors.any())
                // 构建出 Docket 对象
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("汽车项目")
                .description("汽车项目接口文档")
                .version("1.0.0") // 版本号
                .contact(new Contact("ZhangYgod", "http://www.192.168.3.13:190000", "ZhangYaGod@gmail.com")) // 联系人
                .build();
    }


}