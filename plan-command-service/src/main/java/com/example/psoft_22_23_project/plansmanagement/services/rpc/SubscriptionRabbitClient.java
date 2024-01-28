package com.example.psoft_22_23_project.plansmanagement.services.rpc;

import com.example.psoft_22_23_project.bootstrapping.BootstrapRabbitClient;
import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.model.BonusPlanRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionRabbitClient {

    @Value("${rabbitmp.exchange.subs.create}")
    private String createSubsBonusExchange;

    @Value("${rabbitmp.routing.key.subs.create}")
    private String createSubsBonusKey;

    private final RabbitTemplate rabbitTemplate;

    public SubscriptionRabbitClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionRabbitClient.class);

    public boolean sendGetCreateBonusSub(long userId, Plans plan){

        logger.info("[x] request bootstrap plans");
        BonusPlanRequest request = new BonusPlanRequest(userId, plan);
        Boolean response = (Boolean) rabbitTemplate.convertSendAndReceive(createSubsBonusExchange, createSubsBonusKey, request);
        logger.info(String.valueOf(response));
        return Boolean.TRUE.equals(response);

    }

}
