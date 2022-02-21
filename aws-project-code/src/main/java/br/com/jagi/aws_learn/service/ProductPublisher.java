package br.com.jagi.aws_learn.service;

import br.com.jagi.aws_learn.model.Envelope;
import br.com.jagi.aws_learn.model.Product;
import br.com.jagi.aws_learn.model.ProductEvent;
import br.com.jagi.aws_learn.model.enums.EventTypeEnum;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductPublisher {

    private final AmazonSNS snsClient;
    private final Topic productEventsTopic;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void publishProductEvent(Product product, EventTypeEnum eventType, String username) {
        ProductEvent productEvent = createProductEvent(product, username);
        Envelope envelope = new Envelope(eventType);
        envelope.setData(objectMapper.writeValueAsString(productEvent));

        snsClient.publish(productEventsTopic.getTopicArn(), objectMapper.writeValueAsString(envelope));
    }

    private ProductEvent createProductEvent(Product product, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(product.getId());
        productEvent.setCode(productEvent.getCode());
        productEvent.setUsername(username);
        return productEvent;
    }
}