package com.example.psoft_22_23_project.bootstrapping;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryBD;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepositoryBD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.core.support.RepositoryFragment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BootstrapRabbitClient {

    @Value("${rabbitmp.exchange.subscription.bootstrap}")
    private String bootstrapSubscriptionsExchange;


    @Value("${rabbitmp.routing.key.bootstrap}")
    private String bootstrapSubscriptionsKey;

    @Value("${rabbitmp.exchange.plans.bootstrap}")
    private String bootstrapPlanExchange;

    @Value("${rabbitmp.routing.key.plans.bootstrap}")
    private String bootstrapPlanKey;

    @Value("${rabbitmp.exchange.users.bootstrap}")
    private String bootstrapUsersExchange;

    @Value("${rabbitmp.routing.key.users.bootstrap}")
    private String bootstrapUsersKey;



    private final RabbitTemplate rabbitTemplate;

    private final PlansRepository plansRepository;
    private final UserRepositoryBD userRepositoryBD;

    private final SubscriptionsRepositoryBD subscriptionsRepositoryBD;

    public BootstrapRabbitClient(RabbitTemplate rabbitTemplate, PlansRepository repositoryBD, UserRepositoryBD userRepositoryBD, SubscriptionsRepositoryBD subscriptionsRepositoryBD) {
        this.rabbitTemplate = rabbitTemplate;
        this.plansRepository = repositoryBD;
        this.userRepositoryBD = userRepositoryBD;
        this.subscriptionsRepositoryBD = subscriptionsRepositoryBD;
    }

    private static final Logger logger = LoggerFactory.getLogger(BootstrapRabbitClient.class);


    public void sendGetAllPlans() {
        logger.info("[x] request bootstrap plans");

        List<Plans> plansList = (List<Plans>) rabbitTemplate.convertSendAndReceive(
                bootstrapPlanExchange, bootstrapPlanKey, "send all plans please");

        if (plansList != null && !plansList.isEmpty()) {
            List<Plans> newPlans = plansList.stream()
                    .filter(plan -> plansRepository.findByName_Name(plan.getName().getName()).isEmpty())
                    .collect(Collectors.toList());

            if (!newPlans.isEmpty()) {
                plansRepository.saveAll(newPlans);
                newPlans.forEach(plan ->
                        logger.info("Plan {} saved", plan.getName().getName()));
            } else {
                logger.warn("All plans already exist. No new plans saved.");
            }
        }
    }


    public void sendGetAllUsers() {
        logger.info("[x] request bootstrap users");

        List<User> userList = (List<User>) rabbitTemplate.convertSendAndReceive(
                bootstrapUsersExchange, bootstrapUsersKey, "send all users please");

        if (userList != null && !userList.isEmpty()) {
            List<User> newUser = userList.stream()
                    .filter(plan -> userRepositoryBD.findByUsername(plan.getUsername()).isEmpty())
                    .collect(Collectors.toList());

            if (!newUser.isEmpty()) {
                userRepositoryBD.saveAll(newUser);
                newUser.forEach(user ->
                        logger.info("User {} saved", user.getUsername()));
            } else {
                logger.warn("All users already exist. No new users saved.");
            }
        }
    }


    public void sendGetAllSubscriptions() {
        logger.info("[x] request bootstrap subscriptions");

        List<Subscriptions> subscriptionsList = (List<Subscriptions>) rabbitTemplate.convertSendAndReceive(
                bootstrapSubscriptionsExchange, bootstrapSubscriptionsKey, "send all subscriptions please");

        if (subscriptionsList != null && !subscriptionsList.isEmpty()) {
            List<Subscriptions> newSubscriptions = subscriptionsList.stream()
                    .filter(subscriptions -> subscriptionsRepositoryBD.findByUser(subscriptions.getUser()).isEmpty())
                    .collect(Collectors.toList());

            if (!newSubscriptions.isEmpty()) {
                subscriptionsRepositoryBD.saveAll(newSubscriptions);
                newSubscriptions.forEach(subscription ->
                        logger.info("Subscription {} saved", subscription.getUser().getUsername()));
            } else {
                logger.warn("All subscriptions already exist. No new subscriptions saved.");
            }
        }
    }


}
