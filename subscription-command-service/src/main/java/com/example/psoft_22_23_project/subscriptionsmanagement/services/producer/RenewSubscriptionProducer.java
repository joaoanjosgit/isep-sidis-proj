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
public class RenewSubscriptionProducer {

    @Value("${rabbitmp.exchange.subscription.renew}")
    private String renewSubscriptionExchange;

    @Value("${rabbitmp.routing.key.renew}")
    private String renewSubscriptionRoutingKey;


    private final SubscriptionsDTOMapper subscriptionsDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(RenewSubscriptionProducer.class);

    private RabbitTemplate rabbitTemplate;

    public RenewSubscriptionProducer(SubscriptionsDTOMapper subscriptionsDTOMapper,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.subscriptionsDTOMapper = subscriptionsDTOMapper;

    }

    public void sendMessage(Subscriptions obj) {

        // Create a DTO for sending to RabbitMQ
        SubsDTO message = subscriptionsDTOMapper.createDTOFromSubscription(obj);

        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(renewSubscriptionExchange, renewSubscriptionRoutingKey, message);
    }



}
