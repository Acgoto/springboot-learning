package com.zzw.springboot10amqp;

import com.zzw.springboot10amqp.pojo.Book;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringBoot10AmqpApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange( ) {
        // amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
        // amqpAdmin.declareQueue(new Queue("amqpadmin.queue", true));
        // 创建绑定规则
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.exchange", "amqp.haha", null));
        System.out.println("创建完成！");
    }

    /**
     * 1、单播（点对点）
     */
    @Test
    void contextLoads() {
        // Message 需要自己构造一个；定义消息体内容和消息头
        // rabbitTemplate.send(exchange, routeKey, message);
        // object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq
        // rabbitTemplate.convertAndSend(exchange, routeKey, object);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是第一个消息");
        map.put("data", Arrays.asList("helloWorld", 123, true));
        //对象被默认序列化后发送出去
        //rabbitTemplate.convertAndSend("exchange.direct", "zzw.news", map);
        rabbitTemplate.convertAndSend("exchange.direct", "zzw.news", new Book("西游记", "吴承恩"));
    }

    // 从队列中接收，如何将数据自动地转为json发送出去
    @Test
    public void receive() {
        Object o = rabbitTemplate.receiveAndConvert("zzw.news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

    /**
     * 广播
     */
    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("红楼梦", "曹雪芹"));
    }
}
