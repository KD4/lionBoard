package com.github.kd4.lionboard;

import org.quartz.SchedulerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by daum on 16. 2. 22..
 */
public class LionBatchMain {


    public static void main(String[] args) throws SchedulerException {

        new ClassPathXmlApplicationContext("spring-config.xml");

    }

}
