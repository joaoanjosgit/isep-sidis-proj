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
public class CreateSubscriptionProducer {

    @Value("${rabbitmp.exchange.subscription.create}")
    private String createSubscriptionExchange;

    @Value("${rabbitmp.routing.key.subscription}")
    private String createSubscriptionRoutingKey;

    private final SubscriptionsDTOMapper subscriptionsDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(CreateSubscriptionProducer.class);

    private RabbitTemplate rabbitTemplate;

    public CreateSubscriptionProducer(SubscriptionsDTOMapper subscriptionsDTOMapper, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.subscriptionsDTOMapper = subscriptionsDTOMapper;
    }

    public void sendMessage(Subscriptions obj) {

        // Create a DTO for sending to RabbitMQ
        SubsDTO message = subscriptionsDTOMapper.createDTOFromSubscription(obj);

        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(createSubscriptionExchange, createSubscriptionRoutingKey, message);
    }

}
