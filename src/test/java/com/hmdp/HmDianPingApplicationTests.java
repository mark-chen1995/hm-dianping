package com.hmdp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// @SpringBootTest
class HmDianPingApplicationTests {
    // @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void t1(){
        Pattern pattern = Pattern.compile("\\ba(bc)");
        Matcher matcher = pattern.matcher("abcde abcddabcff abeef");
        /*while (matcher.find()){
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(matcher.group(1));
            System.out.println("---------");
        }
        System.out.println("*****");*/
        System.out.println(matcher.matches());
    }

}
