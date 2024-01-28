package com.example.psoft_22_23_project.bootstrapping;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryBD;
import com.example.psoft_22_23_project.plansmanagement.services.producer.CreatePlanJsonProducer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BootstrapRabbitClient {

    @Value("${rabbitmp.exchange.plan.bootstrap}")
    private String bootstrapPlanExchange;


    @Value("${rabbitmp.routing.key.bootstrap}")
    private String bootstrapPlanKey;

    private final RabbitTemplate rabbitTemplate;

    private final PlansRepositoryBD repositoryBD;

    public BootstrapRabbitClient(RabbitTemplate rabbitTemplate, PlansRepositoryBD repositoryBD) {
        this.rabbitTemplate = rabbitTemplate;
        this.repositoryBD = repositoryBD;
    }

    private static final Logger logger = LoggerFactory.getLogger(BootstrapRabbitClient.class);



    public void sendGetAllPlans() {
        logger.info("[x] request bootstrap plans");

        List<Plans> plansList = (List<Plans>) rabbitTemplate.convertSendAndReceive(
                bootstrapPlanExchange, bootstrapPlanKey, "send all plans please");

        if (plansList != null && !plansList.isEmpty()) {
            List<Plans> newPlans = plansList.stream()
                    .filter(plan -> repositoryBD.findByName_Name(plan.getName().getName()).isEmpty())
                    .collect(Collectors.toList());

            if (!newPlans.isEmpty()) {
                repositoryBD.saveAll(newPlans);
                newPlans.forEach(plan ->
                        logger.info("Plan {} saved", plan.getName().getName()));
            } else {
                logger.warn("All plans already exist. No new plans saved.");
            }
        }
    }


}
