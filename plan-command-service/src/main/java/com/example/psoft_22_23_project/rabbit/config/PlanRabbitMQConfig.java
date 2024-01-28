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
public class PlanRabbitMQConfig {

//    @Value("${rabbitmp.queue.create}")
//    private String createPlanQueue;


    @Value("${rabbitmp.queue.subs.create}")
    private String createSubsBonusQueue;

    @Value("${rabbitmp.exchange.subs.create}")
    private String createSubsBonusExchange;

    @Value("${rabbitmp.routing.key.subs.create}")
    private String createSubsBonusKey;

    @Value("${rabbitmp.exchange.plan.bootstrap}")
    private String bootstrapPlanExchange;

    @Value("${rabbitmp.queue.plan.bootstrap}")
    private String bootstrapPlanQueue;

    @Value("${rabbitmp.routing.key.bootstrap}")
    private String bootstrapPlanKey;

    @Value("${rabbitmp.exchange.plan.create}")
    private String createPlanExchange;

    @Value("${rabbitmp.routing.key.create}")
    private String createPlanRoutingKey;

    @Value("${rabbitmp.exchange.plan.update}")
    private String updatePlanExchange;

    @Value("${rabbitmp.routing.key.update}")
    private String updatePlanRoutingKey;

    @Value("${rabbitmp.exchange.plan.delete}")
    private String deletePlanExchange;

    @Value("${rabbitmp.routing.key.delete}")
    private String deletePlanRoutingKey;


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


    //Update Plan
    @Bean
    public TopicExchange updatePlanExchange() {
        return new TopicExchange(updatePlanExchange);
    }

    @Bean
    public AnonymousQueue updatePlanQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding updatePlanBinding() {
        return BindingBuilder
                .bind(updatePlanQueue())
                .to(updatePlanExchange())
                .with(updatePlanRoutingKey);
    }

    //Delete Plan
    @Bean
    public TopicExchange deletePlanExchange() {
        return new TopicExchange(deletePlanExchange);
    }

    @Bean
    public AnonymousQueue deletePlanQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding deletePlanBinding() {
        return BindingBuilder
                .bind(deletePlanQueue())
                .to(deletePlanExchange())
                .with(deletePlanRoutingKey);
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
