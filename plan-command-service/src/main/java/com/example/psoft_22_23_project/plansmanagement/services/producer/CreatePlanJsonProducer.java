package com.example.psoft_22_23_project.plansmanagement.services.producer;

import com.example.psoft_22_23_project.plansmanagement.model.PlanDTO;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.PlanDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreatePlanJsonProducer {

    @Value("${rabbitmp.exchange.plan.create}")
    private String createPlanExchange;

    @Value("${rabbitmp.routing.key.create}")
    private String createPlanRoutingKey;

    private final PlanDTOMapper planDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(CreatePlanJsonProducer.class);

    private RabbitTemplate rabbitTemplate;

    public CreatePlanJsonProducer(PlanDTOMapper planDTOMapper, RabbitTemplate rabbitTemplate) {
        this.planDTOMapper = planDTOMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Plans obj) {

        // Create a DTO for sending to RabbitMQ
        PlanDTO message = planDTOMapper.createDTOFromPlan(obj);

        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(createPlanExchange, createPlanRoutingKey, message);
    }

}
