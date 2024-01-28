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
public class UserRabbitMQConfig {

    @Value("${rabbitmp.exchange.get.user}")
    private String getUserExchange;

    @Value("${rabbitmp.routing.key.get.user}")
    private String getUserRoutingKey;

    @Value("${rabbitmp.exchange.user.register}")
    private String registerUserExchange;

    @Value("${rabbitmp.routing.key.register}")
    private String registerUserRoutingKey;

    //Get Plan
    @Bean
    public TopicExchange getUserExchange() {
        return new TopicExchange(getUserExchange);
    }

    @Bean
    public AnonymousQueue getUserQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding getPlanBinding() {
        return BindingBuilder
                .bind(getUserQueue())
                .to(getUserExchange())
                .with(getUserRoutingKey);
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
