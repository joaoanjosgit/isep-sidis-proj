package com.example.psoft_22_23_project.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionRabbitMQconfig {

    @Value("${rabbitmp.queue.subs.create}")
    private String createSubsBonusQueue;

    @Value("${rabbitmp.exchange.subs.create}")
    private String createSubsBonusExchange;

    @Value("${rabbitmp.routing.key.subs.create}")
    private String createSubsBonusKey;

    @Value("${rabbitmp.exchange.subscription.bootstrap}")
    private String bootstrapSubscriptionExchange;

    @Value("${rabbitmp.queue.subscription.bootstrap}")
    private String bootstrapSubscriptionQueue;

    @Value("${rabbitmp.routing.key.bootstrap}")
    private String bootstrapSubscriptionKey;

    @Value("${rabbitmp.exchange.plans.bootstrap}")
    private String bootstrapPlanExchange;

    @Value("${rabbitmp.queue.plans.bootstrap}")
    private String bootstrapPlanQueue;

    @Value("${rabbitmp.routing.key.plans.bootstrap}")
    private String bootstrapPlanKey;

    @Value("${rabbitmp.exchange.users.bootstrap}")
    private String bootstrapUsersExchange;

    @Value("${rabbitmp.queue.users.bootstrap}")
    private String bootstrapUsersQueue;

    @Value("${rabbitmp.routing.key.users.bootstrap}")
    private String bootstrapUsersKey;


    @Value("${rabbitmp.exchange.plan.create}")
    private String createPlanExchange;

    @Value("${rabbitmp.routing.key.create}")
    private String createPlanRoutingKey;

    @Value("${rabbitmp.exchange.user.register}")
    private String registerUserExchange;

    @Value("${rabbitmp.routing.key.register}")
    private String registerUserRoutingKey;

    @Value("${rabbitmp.exchange.subscription.create}")
    private String createSubscriptionExchange;

    @Value("${rabbitmp.routing.key.subscription}")
    private String createSubscriptionRoutingKey;

    @Value("${rabbitmp.exchange.subscription.cancel}")
    private String cancelSubscriptionExchange;

    @Value("${rabbitmp.routing.key.cancel}")
    private String cancelSubscriptionRoutingKey;

    @Value("${rabbitmp.exchange.subscription.renew}")
    private String renewSubscriptionExchange;

    @Value("${rabbitmp.routing.key.renew}")
    private String renewSubscriptionRoutingKey;

    @Value("${rabbitmp.exchange.subscription.change}")
    private String changeSubscriptionExchange;

    @Value("${rabbitmp.routing.key.change}")
    private String changeSubscriptionRoutingKey;


    //Create bonus sub
    @Bean
    public DirectExchange createBonusSubExchange(){
        return new DirectExchange(createSubsBonusExchange);
    }

    @Bean
    public Queue createBonusSubQueue(){
        return new Queue(createSubsBonusQueue);
    }

    @Bean
    public Binding createBonusSubBinding(){
        return BindingBuilder
                .bind(createBonusSubQueue())
                .to(createBonusSubExchange())
                .with(createSubsBonusKey);
    }


    //Bootstrap Subscription

    @Bean
    public DirectExchange bootstrapSubscriptionExchange(){
        return new DirectExchange(bootstrapSubscriptionExchange);
    }

    @Bean
    public Queue bootstrapSubscriptionQueue(){
        return new Queue(bootstrapSubscriptionQueue);
    }

    @Bean
    public Binding bootstrapSubscriptionBinding(){
        return BindingBuilder
                .bind(bootstrapSubscriptionQueue())
                .to(bootstrapSubscriptionExchange())
                .with(bootstrapSubscriptionKey);
    }

    //Bootstrap Plan

    @Bean
    public DirectExchange bootstrapPlanExchange(){
        return new DirectExchange(bootstrapPlanExchange);
    }

    @Bean
    public Queue bootstrapPlanQueue(){
        return new Queue(bootstrapPlanQueue);
    }

    @Bean
    public Binding bootstrapPlanBinding(){
        return BindingBuilder
                .bind(bootstrapPlanQueue())
                .to(bootstrapPlanExchange())
                .with(bootstrapPlanKey);
    }

    //Bootstrap User

    @Bean
    public DirectExchange bootstrapUsersExchange(){
        return new DirectExchange(bootstrapUsersExchange);
    }

    @Bean
    public Queue bootstrapUsersQueue(){
        return new Queue(bootstrapUsersQueue);
    }

    @Bean
    public Binding bootstrapUsersBinding(){
        return BindingBuilder
                .bind(bootstrapUsersQueue())
                .to(bootstrapUsersExchange())
                .with(bootstrapUsersKey);
    }




    //Create Subscription

    @Bean
    public TopicExchange createSubscriptionExchange() {
        return new TopicExchange(createSubscriptionExchange);
    }

    @Bean
    public AnonymousQueue createSubscriptionQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding createSubscriptionBinding() {
        return BindingBuilder
                .bind(createSubscriptionQueue())
                .to(createSubscriptionExchange())
                .with(createSubscriptionRoutingKey);
    }

    //Cancel Subscription

    @Bean
    public TopicExchange cancelSubscriptionExchange() {
        return new TopicExchange(cancelSubscriptionExchange);
    }

    @Bean
    public AnonymousQueue cancelSubscriptionQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding cancelSubscriptionBinding() {
        return BindingBuilder
                .bind(cancelSubscriptionQueue())
                .to(cancelSubscriptionExchange())
                .with(cancelSubscriptionRoutingKey);
    }

    //Renew Subscription

    @Bean
    public TopicExchange renewSubscriptionExchange() {
        return new TopicExchange(renewSubscriptionExchange);
    }

    @Bean
    public AnonymousQueue renewSubscriptionQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding renewSubscriptionBinding() {
        return BindingBuilder
                .bind(renewSubscriptionQueue())
                .to(renewSubscriptionExchange())
                .with(renewSubscriptionRoutingKey);
    }

    //Change Subscription


    @Bean
    public TopicExchange changeSubscriptionExchange() {
        return new TopicExchange(changeSubscriptionExchange);
    }

    @Bean
    public AnonymousQueue changeSubscriptionQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding changeSubscriptionBinding() {
        return BindingBuilder
                .bind(changeSubscriptionQueue())
                .to(changeSubscriptionExchange())
                .with(changeSubscriptionRoutingKey);
    }




    //Register User
    @Bean
    public TopicExchange registerUserExchange(){
        return new TopicExchange(registerUserExchange);
    }

    @Bean
    public AnonymousQueue registerUserQueue(){
        return new AnonymousQueue();
    }

    @Bean
    public Binding registerUserBinding(){
        return BindingBuilder
                .bind(registerUserQueue())
                .to(registerUserExchange())
                .with(registerUserRoutingKey);
    }


    //Create Plan
    @Bean
    public TopicExchange createPlanExchange() {
        return new TopicExchange(createPlanExchange);
    }

    @Bean
    public AnonymousQueue createPlanQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding createPlanBinding() {
        return BindingBuilder
                .bind(createPlanQueue())
                .to(createPlanExchange())
                .with(createPlanRoutingKey);
    }



    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate createAmqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
