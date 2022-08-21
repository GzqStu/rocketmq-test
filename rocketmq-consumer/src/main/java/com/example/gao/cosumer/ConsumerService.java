package com.example.gao.cosumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * consumeMode= ConsumeMode.ORDERLY 消息消费顺序
 * 消息模式：负载均衡模式和广播模式(messageModel= MessageModel.BROADCASTING)
 * 根据tag过滤消息：selectorExpression = "TAG01||TAG02",selectorType = SelectorType.TAG
 * 根据sql表达式过滤消息：selectorExpression = "type='user' or a<7",selectorType = SelectorType.SQL92
 * @author ：gaozhiqi
 * @date ：2022/8/21 11:48
 */
@Component
@RocketMQMessageListener(topic="${rocketmq.consumer.topic}",consumerGroup = "${rocketmq.consumer.group}",selectorExpression = "type='user' or a<7",selectorType = SelectorType.SQL92)
public class ConsumerService implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("收到了消息:"+message);
    }
}
