package com.example.psoft_22_23_project.subscriptionsmanagement.services.rpc;

import com.example.psoft_22_23_project.plansmanagement.model.BonusPlanRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BonusSubServer {

    private final SubscriptionsService service;

    private static final Logger logger = LoggerFactory.getLogger(BonusSubServer.class);


    @RabbitListener(queues = "create_subs_rpc_queue")
    public boolean receiveBonusPlan(BonusPlanRequest request){

        return service.createBonusSub(request);

    }

}
