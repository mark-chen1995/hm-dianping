package com.hmdp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author markchen
 * @version 1.0
 * @date 2022/12/9 18:01
 */
// @Configuration
public class FilterConfig {
    @Value("${authenticationFilter.whiteList}")
    private List<String> whiteList;

    @Bean
    @Autowired
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(whiteList, redisTemplate, objectMapper);
        FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean =
                new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(authenticationFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
