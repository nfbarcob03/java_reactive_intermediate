package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPconfig {
   public static final String EXCHANGE = "transaction.exchenage";
    public static final String ROUTING_KEY = "trasnaction.created";
    public static final  String TOPIC_EXCHANGE = "transaction.topic.exchange";

    // Extra
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx.exchange");
    }


    @Bean
    public Queue cashInQueue(){
        return new Queue("cash.in.queue");
    }

    @Bean
    public Queue cashOutQueue(){
        return QueueBuilder.durable("cash.out.queue")
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "dlx.routing.key")
                .build();
    }

    @Bean
    public Queue allCashQueue() {
        return new Queue("cash.all.queue");

    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("dlx.queue");

    }

    @Bean
    public Binding cashInBinding(Queue cashInQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(cashInQueue).to(topicExchange).with("cash.in");
    }

    @Bean
    public Binding cashOutBinding(Queue cashInQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(cashOutQueue()).to(topicExchange).with("cash.out");
    }

    @Bean
    public Binding allCashBinding(Queue cashInQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(allCashQueue()).to(topicExchange).with("cash.#");
    }

    @Bean
    public Binding dlxBinding(Queue cashInQueue, @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dlx.routing");
    }

    //Taller 7 B
    @Bean
    public DirectExchange ledgerExchange() {
        return new DirectExchange("ledger.exchange");
    }

    @Bean
    public Queue requestQueue() {
        return new Queue("ledger.entry.request.queue", true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(requestQueue())
                .to(ledgerExchange())
                .with("ledger.entry.request");
    }

 // clase profesor

    @Bean
    public Queue queue(){
        return new Queue("trasnaction.created.queue", true);
    }

    @Bean
    public DirectExchange transactionExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder
                .bind(queue())
                .to(transactionExchange())
                .with("ledger.entry.request");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cf);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyTimeout(5000); // Set a timeout for replies
        rabbitTemplate.setUseTemporaryReplyQueues(true);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
