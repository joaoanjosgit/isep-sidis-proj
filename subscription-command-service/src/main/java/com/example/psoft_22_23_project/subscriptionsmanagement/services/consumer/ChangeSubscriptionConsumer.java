package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubsDTO;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryBD;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsDTOMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeSubscriptionConsumer {

    private final SubscriptionsDTOMapper subscriptionsDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(ChangeSubscriptionConsumer.class);

    private final SubscriptionsService subscriptionsService;

    @RabbitListener(queues = "#{changeSubscriptionQueue.name}")
    public void receiveMessage(SubsDTO dto) {
    Subscriptions subscription = subscriptionsDTOMapper.createSubscriptionFromDTO(dto);
        long version = Long.parseLong(dto.getVersion());
        subscriptionsService.saveChangedSubsRabbit(subscription, version);

    }

}
