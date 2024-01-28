package com.example.psoft_22_23_project.rabbit.producer;

import com.example.psoft_22_23_project.usermanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetUserJsonProducer {

    @Value("${rabbitmp.exchange.get.user}")
    private String getUserExchange;

    @Value("${rabbitmp.routing.key.get.user}")
    private String getUserRoutingKey;

    private static final Logger logger = LoggerFactory.getLogger(GetUserJsonProducer.class);

    private RabbitTemplate rabbitTemplate;

    public GetUserJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(User message) {
        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(getUserExchange, getUserRoutingKey, message);
    }

}
