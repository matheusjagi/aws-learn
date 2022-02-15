package br.com.jagi.aws_learn.service;

import br.com.jagi.aws_learn.model.Envelope;
import br.com.jagi.aws_learn.model.Product;
import br.com.jagi.aws_learn.model.ProductEvent;
import br.com.jagi.aws_learn.model.enums.EventType;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductPublisher {

    private final AmazonSNS snsClient;
    private final Topic productEventsTopic;
    private final ObjectMapper objectMapper;

    public ProductPublisher(AmazonSNS snsClient,
                            @Qualifier("productEventsTopic") Topic productEventsTopic,
                            ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void publishProductEvent(Product product, EventType eventType, String username) {
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
