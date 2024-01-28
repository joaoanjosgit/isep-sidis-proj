package com.example.psoft_22_23_project.subscriptionsmanagement.services.producer;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubsDTO;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CancelSubscriptionProducer {

    @Value("${rabbitmp.exchange.subscription.cancel}")
    private String cancelSubscriptionExchange;

    @Value("${rabbitmp.routing.key.cancel}")
    private String cancelSubscriptionRoutingKey;


    private final SubscriptionsDTOMapper subscriptionsDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(CancelSubscriptionProducer.class);

    private RabbitTemplate rabbitTemplate;

    public CancelSubscriptionProducer(SubscriptionsDTOMapper subscriptionsDTOMapper ,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.subscriptionsDTOMapper = subscriptionsDTOMapper;
    }

    public void sendMessage(Subscriptions obj) {

        // Create a DTO for sending to RabbitMQ
        SubsDTO message = subscriptionsDTOMapper.createDTOFromSubscription(obj);

        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(cancelSubscriptionExchange, cancelSubscriptionRoutingKey, message);
    }



}
