package br.com.jagi.aws_learn_consumer.service;

import br.com.jagi.aws_learn_consumer.model.Envelope;
import br.com.jagi.aws_learn_consumer.model.ProductEvent;
import br.com.jagi.aws_learn_consumer.model.ProductEventLog;
import br.com.jagi.aws_learn_consumer.model.SnsMessage;
import br.com.jagi.aws_learn_consumer.repository.ProductEventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final ObjectMapper objectMapper;
    private final ProductEventLogRepository productEventLogRepository;

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

        SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);

        Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);

        ProductEvent productEvent = objectMapper.readValue(envelope.getData(), ProductEvent.class);

        log.info("Product event received - Message ID: {} - Event: {} - ProductId: {} ",
                snsMessage.getMessageId(), envelope.getEventType(), productEvent.getProductId());

        ProductEventLog productEventLog = buildProductEventLog(envelope, productEvent);
        productEventLogRepository.save(productEventLog);
    }

    private ProductEventLog buildProductEventLog(Envelope envelope, ProductEvent productEvent) {
        Long timestamp = Instant.now().toEpochMilli();

        ProductEventLog productEventLog = new ProductEventLog();
        productEventLog.setPk(productEvent.getCode());
        productEventLog.setSk(String.format("%s_%d", envelope.getEventType(), timestamp));
        productEventLog.setEventType(envelope.getEventType());
        productEventLog.setProductId(productEvent.getProductId());
        productEventLog.setUsername(productEvent.getUsername());
        productEventLog.setTimestamp(timestamp);
        productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());

        return productEventLog;
    }
}
