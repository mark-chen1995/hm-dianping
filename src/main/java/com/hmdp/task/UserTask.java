package com.hmdp.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author markchen
 * @version 1.0
 * @date 2022/12/16 10:48
 */
// @Component
public class UserTask {
    @Scheduled(cron = "${schedule.task.cron-expression.e1}")
    public void t1(){
        System.out.println(Thread.currentThread().getName()+":"+"任务一执行中...");
    }
}
