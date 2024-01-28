package com.example.psoft_22_23_project.rabbit.consumer;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserJsonConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserJsonConsumer.class);

    private final UserRepositoryBD userRepositoryBD;

    @RabbitListener(queues = "#{registerUserQueue.name}")
    public void receiveMessage(User obj) {
        // Check if the plan already exists in the local database
        if (userRepositoryBD.findByUsername(obj.getUsername()).isEmpty()) {
            // Plan doesn't exist locally, so create it
            userRepositoryBD.save(obj);
            logger.info("User added to the local database.");
        } else {
            logger.warn("User already exists in the local database. No action taken.");
        }

    }

}
