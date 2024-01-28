package com.example.psoft_22_23_project.usermanagement.services.consumer;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.model.UserDTO;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import com.example.psoft_22_23_project.usermanagement.services.UserDTOMapper;
import com.example.psoft_22_23_project.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserJsonConsumer {


    private final UserService service;

    private final UserDTOMapper userDTOMapper;

    @RabbitListener(queues = "#{registerUserQueue.name}")
    public void receiveMessage(UserDTO dto) {
        User obj = userDTOMapper.createUserFromDTO(dto);
        service.saveCreatedUserRabbit(obj);

    }

}
