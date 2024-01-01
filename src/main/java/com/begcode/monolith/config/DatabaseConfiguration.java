package com.begcode.monolith.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(
    basePackages = { "com.diboot.core", "com.begcode.monolith.**.repository", "com.begcode.monolith.**.repository" },
    annotationClass = Mapper.class
)
@ComponentScan(
    basePackages = { "com.diboot.core" },
    excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class }) }
)
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
