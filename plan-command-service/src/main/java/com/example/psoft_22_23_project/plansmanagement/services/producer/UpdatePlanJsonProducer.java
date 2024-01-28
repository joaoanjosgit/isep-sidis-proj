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
public class UpdatePlanJsonProducer {

    @Value("${rabbitmp.exchange.plan.update}")
    private String updatePlanExchange;

    @Value("${rabbitmp.routing.key.update}")
    private String updatePlanRoutingKey;

    private final PlanDTOMapper planDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(UpdatePlanJsonProducer.class);

    private RabbitTemplate rabbitTemplate;

    public UpdatePlanJsonProducer(PlanDTOMapper planDTOMapper, RabbitTemplate rabbitTemplate) {
        this.planDTOMapper = planDTOMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Plans obj) {
        // Create a DTO for sending to RabbitMQ
        PlanDTO message = planDTOMapper.createDTOFromPlan(obj);
        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(updatePlanExchange, updatePlanRoutingKey, message);
    }

}
