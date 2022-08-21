package com.example.gao;

import com.example.gao.producer.ProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author ：gaozhiqi
 * @date ：2022/8/21 11:29
 */
@SpringBootApplication
public class RocketmqProducerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(RocketmqProducerApplication.class,args);
        ProducerService producerService = (ProducerService)configurableApplicationContext.getBean("producerService");
        // producerService.sendMessage(); //发送普通消息
        //producerService.sendSyncMessage(); //发送同步消息
        //producerService.sendAsyncMessage();//发送异步消息
        //producerService.sendOneWayMessage();//单向消息
        //producerService.sendOrderMessage();//发送同步顺序消息
        //producerService.sendDelayMessage(); //发送延迟消息
        //producerService.sendTransactionMessage();//发送事务消息
        //producerService.sendMessageWithTag();//发送带有tag的消息
        producerService.sendMessageWithSQL();//发送根据sql表达式过滤的消息
    }
}
