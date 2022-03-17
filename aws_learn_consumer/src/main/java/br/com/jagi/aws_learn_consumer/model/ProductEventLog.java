package br.com.jagi.aws_learn_consumer.model;

import br.com.jagi.aws_learn_consumer.model.enums.EventTypeEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "product-events")
public class ProductEventLog {

    @Id
    private ProductEventKey productEventKey;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "eventType")
    private EventTypeEnum eventType;

    @DynamoDBAttribute(attributeName = "productId")
    private Long productId;

    @DynamoDBAttribute(attributeName = "username")
    private String username;

    @DynamoDBAttribute(attributeName = "timestamp")
    private Long timestamp;

    @DynamoDBAttribute(attributeName = "ttl")
    private Long ttl;

    @DynamoDBAttribute(attributeName = "messageId")
    private String messageId;

    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return Objects.nonNull(this.productEventKey) ? this.productEventKey.getPk() : null;
    }

    public void setPk(String pk) {
        if (Objects.isNull(this.productEventKey)) {
            this.productEventKey = new ProductEventKey();
        }

        this.productEventKey.setPk(pk);
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return Objects.nonNull(this.productEventKey) ? this.productEventKey.getSk() : null;
    }

    public void setSk(String sk) {
        if (Objects.isNull(this.productEventKey)) {
            this.productEventKey = new ProductEventKey();
        }

        this.productEventKey.setSk(sk);
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
