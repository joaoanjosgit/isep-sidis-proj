package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.SubsDTO;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsDTOMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RenewSubscriptionConsumer {


    private final SubscriptionsDTOMapper subscriptionsDTOMapper;


    private static final Logger logger = LoggerFactory.getLogger(RenewSubscriptionConsumer.class);

    private final SubscriptionsService subscriptionsService;
    @RabbitListener(queues = "#{renewSubscriptionQueue.name}")
    public void receiveMessage(SubsDTO dto) {
        Subscriptions subscription = subscriptionsDTOMapper.createSubscriptionFromDTO(dto);
        long version = Long.parseLong(dto.getVersion());
        subscriptionsService.saveRenewedSubsRabbit(subscription, version);

    }
}
