package com.alibaba.internship.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ configuration for delayed order cancellation.
 *
 * <p><b>How the delayed queue works (dead-letter pattern):</b></p>
 * <ol>
 *   <li>Producer sends an "order created" message to {@code order.exchange}
 *       routed to {@code order.timeout.queue}.</li>
 *   <li>{@code order.timeout.queue} has a TTL ({@code x-message-ttl}).
 *       Messages that are not consumed before TTL become <b>dead letters</b>.</li>
 *   <li>The queue's {@code x-dead-letter-exchange} and
 *       {@code x-dead-letter-routing-key} forward dead letters to
 *       {@code order.dlx.exchange} → {@code order.dlx.queue}.</li>
 *   <li>The consumer listens on {@code order.dlx.queue}; when a message
 *       arrives it means the order has timed out, so it cancels the order.</li>
 * </ol>
 *
 * <p>This is the classic way to implement delayed / scheduled messages
 * in RabbitMQ without the commercial delayed-message plugin.</p>
 */
@Configuration
public class RabbitMQConfig {

    // ---- exchange / queue / routing-key names ----
    public static final String ORDER_EXCHANGE     = "order.exchange";
    public static final String ORDER_TIMEOUT_QUEUE  = "order.timeout.queue";
    public static final String ORDER_TIMEOUT_KEY    = "order.timeout";

    public static final String ORDER_DLX_EXCHANGE = "order.dlx.exchange";
    public static final String ORDER_DLX_QUEUE    = "order.dlx.queue";
    public static final String ORDER_DLX_KEY      = "order.timeout";

    @Value("${app.rabbitmq.order-timeout-ttl:1800000}")
    private long orderTimeoutTtl;

    // ---- 1. normal exchange ----
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    // ---- 2. TTL queue with dead-letter configuration ----
    @Bean
    public Queue orderTimeoutQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", ORDER_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", ORDER_DLX_KEY);
        args.put("x-message-ttl", orderTimeoutTtl);
        return new Queue(ORDER_TIMEOUT_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding timeoutBinding(Queue orderTimeoutQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderTimeoutQueue)
                .to(orderExchange)
                .with(ORDER_TIMEOUT_KEY);
    }

    // ---- 3. dead-letter exchange + queue ----
    @Bean
    public DirectExchange orderDlxExchange() {
        return new DirectExchange(ORDER_DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderDlxQueue() {
        return new Queue(ORDER_DLX_QUEUE, true);
    }

    @Bean
    public Binding dlxBinding(Queue orderDlxQueue, DirectExchange orderDlxExchange) {
        return BindingBuilder.bind(orderDlxQueue)
                .to(orderDlxExchange)
                .with(ORDER_DLX_KEY);
    }

    // ---- JSON message converter ----
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory,
                                         MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(converter);
        return template;
    }
}
