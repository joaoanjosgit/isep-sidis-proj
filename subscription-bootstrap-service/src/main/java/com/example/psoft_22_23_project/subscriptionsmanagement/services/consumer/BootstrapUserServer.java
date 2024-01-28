package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BootstrapUserServer {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapUserServer.class);


    @RabbitListener(queues = "subs_users_bootstrap")
    public List<User> bootstrapUser() {
        try {
            return userService.bootstrapUsers();
        } catch (Exception e) {
            logger.error("Error in bootstrapSubscriptions method", e);

            return Collections.emptyList();
        }
    }
}
