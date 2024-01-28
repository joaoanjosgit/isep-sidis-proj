package com.example.psoft_22_23_project.subscriptionsmanagement.services.consumer;

import com.example.psoft_22_23_project.plansmanagement.model.PlanDTO;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;

import com.example.psoft_22_23_project.plansmanagement.service.PlanDTOMapper;
import com.example.psoft_22_23_project.plansmanagement.service.PlansService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreatePlanConsumer {

    private final PlanDTOMapper planDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(CreatePlanConsumer.class);

    private final PlansService plansService;

    @RabbitListener(queues = "#{createPlanQueue.name}")
    public void receiveMessage(PlanDTO dto) {
        Plans plan = planDTOMapper.createPlanFromDTO(dto);
        plansService.saveCreatedPlanRabbit(plan);

    }
}
