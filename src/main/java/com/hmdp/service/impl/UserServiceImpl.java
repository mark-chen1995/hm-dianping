package com.hmdp.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String sendCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        /*redisTemplate.opsForValue().set(RedisConstants.CODE_PREFIX + phone, code, RedisConstants.CODE_PREFIX_TTL,
                TimeUnit.MINUTES);*/
        return code;
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) throws JsonProcessingException {
        // 判断验证码是否正确
        Object codeFromSession = session.getAttribute("code:" + loginForm.getPhone());
        // String codeFromSession = redisTemplate.opsForValue().get(RedisConstants.CODE_PREFIX+loginForm.getPhone());
        String code = loginForm.getCode();
        if (StringUtils.isEmpty(code)
                || StringUtils.isEmpty(codeFromSession)
                || !code.equals(codeFromSession)) {
            return Result.fail("验证码错误");
        }
        // 判读用户是否存在
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, loginForm.getPhone()));
        if (Objects.isNull(user)) {
            // 不存在则注册
            user = new User();
            user.setPhone(loginForm.getPhone());
            user.setNickName("user_" + UUID.randomUUID().toString(true).substring(0, 8));
            save(user);
        }
        session.setAttribute("user", user);
        String userStr = objectMapper.writeValueAsString(user);
        /*redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY+user.getPhone(),
                userStr,RedisConstants.LOGIN_USER_TTL,TimeUnit.SECONDS);*/
        Map map = new HashMap();
        map.put("token", userStr);
        session.removeAttribute("code:" + loginForm.getPhone());
        return Result.ok(map);
    }
}
