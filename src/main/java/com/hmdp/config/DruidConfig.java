package com.hmdp.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.hmdp.filter.DruidAdvertisementFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author markchen
 * @version 1.0
 * @date 2022/12/9 16:01
 */
@Configuration
public class DruidConfig {
    // 配置druid监控页面
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        return servletRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean<DruidAdvertisementFilter> druidAdFilterRegistration(){
        FilterRegistrationBean<DruidAdvertisementFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        DruidAdvertisementFilter filter = new DruidAdvertisementFilter();
        filterFilterRegistrationBean.setFilter(filter);
        filterFilterRegistrationBean.addUrlPatterns("/druid/js/common.js");
        return filterFilterRegistrationBean;
    }
}
