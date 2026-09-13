package com.alibaba.internship.mq;

import com.alibaba.internship.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends an "order created" message to the TTL queue.
 *
 * <p>The message will sit in {@code order.timeout.queue} until TTL
 * expires, then be forwarded to the dead-letter queue where the
 * consumer cancels the order (if still unpaid).</p>
 */
@Component
public class OrderTimeoutProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String orderId) {
        log.info("sending order-timeout message for orderId={}", orderId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_TIMEOUT_KEY,
                orderId
        );
    }
}
