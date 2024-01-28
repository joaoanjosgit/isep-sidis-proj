package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.PlansService;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
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


    @RabbitListener(queues = "subs_plans_bootstrap")
    public List<Plans> bootstrapPlan() {
        try {
            return plansService.bootstrapPlans();
        } catch (Exception e) {
            logger.error("Error in bootstrapSubscriptions method", e);

            return Collections.emptyList();
        }
    }
}
