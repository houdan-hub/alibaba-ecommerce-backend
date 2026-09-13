package com.alibaba.internship.mq;

import com.alibaba.internship.config.RabbitMQConfig;
import com.alibaba.internship.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Consumes dead-letter messages from {@code order.dlx.queue}.
 *
 * <p>When a message arrives here it means the order's TTL has expired
 * (i.e. the user did not pay in time). We cancel the order and
 * restore the deducted inventory.</p>
 *
 * <p>Uses manual ACK ({@code acknowledge-mode: manual}) so that if
 * cancellation fails the message stays in the queue for retry.</p>
 */
@Component
public class OrderTimeoutConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutConsumer.class);

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_DLX_QUEUE)
    public void onTimeout(String orderId, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("received timeout-cancel for orderId={}", orderId);
            orderService.handleTimeoutCancel(orderId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("failed to cancel order {} on timeout, will requeue", orderId, e);
            // requeue the message so it can be retried
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
