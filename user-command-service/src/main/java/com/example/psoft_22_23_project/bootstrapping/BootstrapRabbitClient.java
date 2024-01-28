package com.example.psoft_22_23_project.bootstrapping;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class BootstrapRabbitClient {

    @Value("${rabbitmp.exchange.user.bootstrap}")
    private String bootstrapUserExchange;

    @Value("${rabbitmp.routing.key.bootstrap}")
    private String bootstrapUserKey;

    private final RabbitTemplate rabbitTemplate;

    private final UserRepositoryBD repositoryBD;

    public BootstrapRabbitClient(RabbitTemplate rabbitTemplate, UserRepositoryBD repositoryBD) {
        this.rabbitTemplate = rabbitTemplate;
        this.repositoryBD = repositoryBD;
    }


    private static final Logger logger = LoggerFactory.getLogger(BootstrapRabbitClient.class);


    @Transactional
    public void sendGetAllUsers(){

        List<User> userList = (List<User>) rabbitTemplate.convertSendAndReceive(
                bootstrapUserExchange, bootstrapUserKey, "Send all users please");

        if (userList != null && !userList.isEmpty()) {
            List<User> newUsers = userList.stream()
                    .filter(user -> repositoryBD.findByUsername(user.getUsername()).isEmpty())
                    .collect(Collectors.toList());

            if (!newUsers.isEmpty()) {
                repositoryBD.saveAll(newUsers);
                newUsers.forEach(user ->
                        logger.info("User {} saved", user.getUsername()));
            } else {
                logger.warn("All Users already exist. No new users saved.");
            }

        }

    }

}
