package com.pig.easy.bpm.generator.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
public class MyBatisPlusConfiguration {

    private static final String SYSTEM_TENANT_ID = "tenant_id";

    private static final String USER_TENANT_PREFIX = "best:bpm:tenantId:";

    @Autowired
    MybatisPlusProperties mybatisPlusProperties;

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @PostConstruct
    public void setMybatisPlusProperties() {
        mybatisPlusProperties.setMapperLocations(new String[]{"classpath*:/mapper/*Mapper.xml", "classpath*:/mapper/**/*Mapper.xml"});
        GlobalConfig globalConfig = new GlobalConfig();

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setCapitalMode(false);
        dbConfig.setIdType(IdType.AUTO);
        dbConfig.setTableUnderline(true);
        globalConfig.setDbConfig(dbConfig);
        mybatisPlusProperties.setGlobalConfig(globalConfig);
    }

}
