package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

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
public class BootstrapSubscriptionsServer {

    private final SubscriptionsService subscriptionService;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapSubscriptionsServer.class);


    @RabbitListener(queues = "subscription_bootstrap")
    public List<Subscriptions> bootstrapSubscriptions() {
        try {
            return subscriptionService.bootstrapSubscriptions();
        } catch (Exception e) {
            logger.error("Error in bootstrapSubscriptions method", e);

            return Collections.emptyList();
        }
    }
}
