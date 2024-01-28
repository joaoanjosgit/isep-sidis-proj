package com.example.psoft_22_23_project.usermanagement.services.producer;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.model.UserDTO;
import com.example.psoft_22_23_project.usermanagement.services.UserDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserJsonProducer {

    @Value("${rabbitmp.exchange.user.register}")
    private String registerUserExchange;

    @Value("${rabbitmp.routing.key.register}")
    private String registerUserRoutingKey;

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserJsonProducer.class);

    private RabbitTemplate rabbitTemplate;

    private final UserDTOMapper userDTOMapper;

    public RegisterUserJsonProducer(RabbitTemplate rabbitTemplate, UserDTOMapper userDTOMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.userDTOMapper = userDTOMapper;
    }

    public void sendMessage(User newUser) {
        // Create a DTO for sending to RabbitMQ
        UserDTO message = userDTOMapper.createDTOFromUser(newUser);
        logger.info(String.format("Json message sent -> %s", message));
        rabbitTemplate.convertAndSend(registerUserExchange, registerUserRoutingKey, message);
    }

}
