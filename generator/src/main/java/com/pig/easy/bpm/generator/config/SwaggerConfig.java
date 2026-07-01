package com.pig.easy.bpm.generator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local", "test", "prod"})
public class SwaggerConfig {

    @Value(value = "${swagger.controller:}")
    private String controller;
    @Value(value = "${swagger.title:}")
    private String title;
    @Value(value = "${swagger.description:}")
    private String description;
    @Value(value = "${swagger.version:}")
    private String version;
    @Value(value = "${swagger.license:}")
    private String license;
    @Value(value = "${swagger.licenseUrl:}")
    private String licenseUrl;
    @Value(value = "${swagger.author:}")
    private String author;
    @Value(value = "${swagger.authorBlogUrl:}")
    private String authorBlogUrl;
    @Value(value = "${swagger.email:}")
    private String email;

    @Bean
    public OpenAPI customOpenAPI() {
        checkData();
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name(license).url(licenseUrl))
                        .contact(new Contact().name(author).url(authorBlogUrl).email(email)));
    }

    private void checkData() {
        if (StringUtils.isEmpty(controller) || StringUtils.isEmpty(title)) {
            throw new RuntimeException("SwaggerConfig init fail, please config on nacos");
        }
        System.out.println("SwaggerConfig  ############################## = " + title);
    }
}
