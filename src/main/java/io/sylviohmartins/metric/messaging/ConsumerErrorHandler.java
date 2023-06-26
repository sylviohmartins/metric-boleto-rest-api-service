package io.sylviohmartins.metric.messaging;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class ConsumerErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        System.out.println("Erro durante o consumo da mensagem: " + message.getPayload());
        System.out.println("Exceção: " + exception.getMessage());
        return null;
    }

}
