package com.example.psoft_22_23_project.plansmanagement.services.consumer;

import com.example.psoft_22_23_project.plansmanagement.model.PlanDTO;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.PlansService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BootstrapPlanServer {

    private final PlansService plansService;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapPlanServer.class);


    @RabbitListener(queues = "bootstrap_plan")
    public List<Plans> bootstrapPlans() {
        try {
            return plansService.bootstrapPlans();
        } catch (Exception e) {
            logger.error("Error in bootstrapPlans method", e);

            return Collections.emptyList();
        }
    }

}
