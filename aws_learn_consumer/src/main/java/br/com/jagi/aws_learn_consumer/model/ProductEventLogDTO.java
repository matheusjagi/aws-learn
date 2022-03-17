package br.com.jagi.aws_learn_consumer.model;

import br.com.jagi.aws_learn_consumer.model.enums.EventTypeEnum;
import lombok.Getter;

@Getter
public class ProductEventLogDTO {

    private final String code;
    private final EventTypeEnum eventType;
    private final Long productId;
    private final String username;
    private final Long timestamp;
    private final String messageId;

    public ProductEventLogDTO(ProductEventLog productEventLog) {
        this.code = productEventLog.getPk();
        this.eventType = productEventLog.getEventType();
        this.productId = productEventLog.getProductId();
        this.username = productEventLog.getUsername();
        this.timestamp = productEventLog.getTimestamp();
        this.messageId = productEventLog.getMessageId();
    }
}
