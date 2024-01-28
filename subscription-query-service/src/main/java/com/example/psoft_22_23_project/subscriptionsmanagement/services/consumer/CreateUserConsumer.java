package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.model.UserDTO;
import com.example.psoft_22_23_project.usermanagement.services.UserDTOMapper;
import com.example.psoft_22_23_project.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserConsumer {

    private final UserDTOMapper userDTOMapper;
    private final UserService userService;

    @RabbitListener(queues = "#{registerUserQueue.name}")
    public void receiveMessage(UserDTO obj) {
        User user = userDTOMapper.createUserFromDTO(obj);
        userService.saveCreatedUserRabbit(user);

    }
}
