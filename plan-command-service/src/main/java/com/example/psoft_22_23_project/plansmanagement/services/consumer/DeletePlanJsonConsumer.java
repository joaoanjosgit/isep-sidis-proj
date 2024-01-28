package com.example.psoft_22_23_project.plansmanagement.services.consumer;

import com.example.psoft_22_23_project.plansmanagement.model.PlanDTO;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryBD;
import com.example.psoft_22_23_project.plansmanagement.services.PlanDTOMapper;
import com.example.psoft_22_23_project.plansmanagement.services.PlansService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePlanJsonConsumer {

    private final PlanDTOMapper planDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(DeletePlanJsonConsumer.class);

    private final PlansService plansService;

    @RabbitListener(queues = "#{deletePlanQueue.name}")
    public void receiveMessage(PlanDTO dto) {
        Plans obj = planDTOMapper.createPlanFromDTO(dto);
        long version = Long.parseLong(dto.getVersion());
        plansService.saveDeletedPlanRabbit(obj, version);
    }

}