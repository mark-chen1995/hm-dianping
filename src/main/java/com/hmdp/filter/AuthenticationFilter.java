package com.hmdp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author markchen
 * @version 1.0
 * @date 2022/12/9 17:34
 */

public class AuthenticationFilter implements Filter {

    private List<String> whiteList;
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public AuthenticationFilter(List<String> whiteList, StringRedisTemplate stringRedisTemplate,ObjectMapper objectMapper) {
        this.whiteList = whiteList;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public AuthenticationFilter(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (Objects.nonNull(whiteList)&&!whiteList.isEmpty()){
            for (String pattern : whiteList) {
                String path = httpServletRequest.getRequestURI().toString();
                if (pathMatcher.match(pattern, path)) {
                    chain.doFilter(request,response);
                    return;
                }
            }
        }
        /*HttpSession session = httpServletRequest.getSession();
        Object user = session.getAttribute("user");*/

        String userStr = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY);


        if (StringUtils.isEmpty(userStr)){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.flushBuffer();
            return;
        }
        User user = objectMapper.readValue(userStr,User.class);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        UserHolder.saveUser(userDTO);
        chain.doFilter(request,response);
    }
}
