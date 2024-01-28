package com.example.psoft_22_23_project.plansmanagement.services.producer;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetPlanJsonProducer {

    @Value("${rabbitmp.exchange.get.plan}")
    private String getPlanExchange;

    @Value("${rabbitmp.routing.key.get.plan}")
    private String getPlanRoutingKey;

    private static final Logger logger = LoggerFactory.getLogger(GetPlanJsonProducer.class);

    private RabbitTemplate rabbitTemplate;

    public GetPlanJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Plans message) {
        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(getPlanExchange, getPlanRoutingKey, message);
    }

}
