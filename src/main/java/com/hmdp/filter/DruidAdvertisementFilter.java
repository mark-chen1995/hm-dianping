package com.hmdp.filter;

import com.alibaba.druid.util.Utils;
import com.hmdp.config.DruidConfig;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author markchen
 * @version 1.0
 * @date 2023/1/10 16:46
 */
public class DruidAdvertisementFilter extends OncePerRequestFilter {
    private String COMMON_SOURCE_PATH = "support/http/resources/js/common.js";
    // /support/http/resources/js/common.js
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request,response);
        String commonJs = Utils.readFromResource(COMMON_SOURCE_PATH);
        String replaceCommonJs = commonJs.replaceAll("<div class=\"container\">[\\s\\S]*?</div>", "");
        response.resetBuffer();
        response.getWriter().write(replaceCommonJs);
    }
}
