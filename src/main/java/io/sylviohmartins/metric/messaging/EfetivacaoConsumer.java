package io.sylviohmartins.metric.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EfetivacaoConsumer {

    @KafkaListener(topics = "nome.topico", groupId = "nome.grupo", errorHandler = "consumerErrorHandler")
    public void consume(@Payload String message) {
        System.out.println("Mensagem recebida: " + message);
    }

}
