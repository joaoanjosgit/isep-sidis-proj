package com.example.psoft_22_23_project.usermanagement.services.consumer;

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

    private final UserService service;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapUserServer.class);
    @RabbitListener(queues = "bootstrap_user")
    public List<User> bootstrapUsers() {
        try {
            return service.bootstrapUsers();
        } catch (Exception e) {
            logger.error("Error in bootstrapUsers method", e);

            return Collections.emptyList();
        }
    }


}
