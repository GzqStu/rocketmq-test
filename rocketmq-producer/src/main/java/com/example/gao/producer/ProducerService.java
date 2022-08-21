package com.example.gao.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：gaozhiqi
 * @date ：2022/8/21 11:34
 */
@Component("producerService")
public class ProducerService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.producer.topic}")
    private String topic;

    /**
     * 发送简单消息
     */
    public void sendMessage(){
        for(int i=0;i<10;i++){
            rocketMQTemplate.convertAndSend(topic,"rocket-test,你好哦"+i);
        }
    }

    /**
     * 发送同步消息
     */
    public void sendSyncMessage(){
        for(int i=0;i<10;i++){
            SendResult result = rocketMQTemplate.syncSend(topic,"rocket-test，同步消息,你好哦"+i);
            System.out.println(result);
        }
    }

    /**
     * 发送异步消息
     */
    public void sendAsyncMessage(){
        for(int i=0;i<10;i++){
            rocketMQTemplate.asyncSend(topic, "rocket-test，异步消息,你好哦" + i, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("异步消息发送成功");
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("异步消息发送失败");
                }
            });
        }
    }

    /**
     * 发送单向消息
     */
    public void sendOneWayMessage(){
        for(int i=0;i<10;i++){
            rocketMQTemplate.sendOneWay(topic,"rocket-test，单向消息,你好哦"+i);
        }
    }

    /**
     * 发动同步顺序消息
     */
    public void sendOrderMessage(){
        rocketMQTemplate.syncSendOrderly(topic,"创建订单1111111111","订单1111111111");
        rocketMQTemplate.syncSendOrderly(topic,"支付成功1111111111","订单1111111111");
        rocketMQTemplate.syncSendOrderly(topic,"完成购物1111111111","订单1111111111");
        rocketMQTemplate.syncSendOrderly(topic,"创建订单2222222222","订单2222222222");
        rocketMQTemplate.syncSendOrderly(topic,"支付成功2222222222","订单2222222222");
        rocketMQTemplate.syncSendOrderly(topic,"完成购物2222222222","订单2222222222");
        rocketMQTemplate.syncSendOrderly(topic,"创建订单3333333333","订单3333333333");
        rocketMQTemplate.syncSendOrderly(topic,"支付成功3333333333","订单3333333333");
        rocketMQTemplate.syncSendOrderly(topic,"完成购物3333333333","订单3333333333");

    }

    /**
     * 发送延迟消息，延迟级别 messageDelayLevel=1s,5s,10s,30s,1m,2m,3m,4m,5m,6m,7m,8m,9m,10m,20m,30m,1h,2h
     */
    public void sendDelayMessage(){
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload("rocketmq延迟1s消息").build(),3000,1);
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload("rocketmq延迟5s消息").build(),3000,2);
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload("rocketmq延迟10s消息").build(),3000,3);
    }

    /**
     * 发送事务消息
     */
    public void sendTransactionMessage(){
        Message message = MessageBuilder.withPayload("rocketmq事务消息0001").build();
        rocketMQTemplate.sendMessageInTransaction(topic,message,null);
    }
    @RocketMQTransactionListener
    class TransactionListenerImpl implements RocketMQLocalTransactionListener{

        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
            System.out.println("executeLocalTransaction");
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
            System.out.println("checkLocalTransaction");
            return RocketMQLocalTransactionState.COMMIT;
        }
    }

    /**
     * 发送带有tag的消息
     */
    public void sendMessageWithTag(){
        Message message = MessageBuilder.withPayload("rocketmq根据tag过滤消息-TAG01").build();
        Message message1 = MessageBuilder.withPayload("rocketmq根据tag过滤消息-TAG02").build();
        Message message2 = MessageBuilder.withPayload("rocketmq根据tag过滤消息-TAG03").build();
        rocketMQTemplate.convertAndSend(topic+":TAG01",message);
        rocketMQTemplate.convertAndSend(topic+":TAG02",message1);
        rocketMQTemplate.convertAndSend(topic+":TAG03",message2);
    }

    /**
     * 发送根据sql表达式过滤的消息
     */
    public void sendMessageWithSQL(){
        Message message = MessageBuilder.withPayload("rocketmq过滤消息").build();
        Map<String,Object> headers = new HashMap<>();
        headers.put("type","pay");
        headers.put("a",10);
        rocketMQTemplate.convertAndSend(topic,message,headers);

        Message message1 = MessageBuilder.withPayload("rocketmq过滤消息").build();
        Map<String,Object> headers1 = new HashMap<>();
        headers1.put("type","store");
        headers1.put("a",4);
        rocketMQTemplate.convertAndSend(topic,message1,headers1);

        Message message2 = MessageBuilder.withPayload("rocketmq过滤消息").build();
        Map<String,Object> headers2 = new HashMap<>();
        headers2.put("type","user");
        headers2.put("a",7);
        rocketMQTemplate.convertAndSend(topic,message2,headers2);

    }
}
