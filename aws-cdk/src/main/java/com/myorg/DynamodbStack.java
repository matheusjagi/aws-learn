package com.myorg;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.constructs.Construct;

public class DynamodbStack extends Stack {

    private final Table productEventsDynammdb;

    public DynamodbStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DynamodbStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        productEventsDynammdb = Table.Builder.create(this, "ProductEventsDynamodb")
                .tableName("product-events")
                .billingMode(BillingMode.PROVISIONED) //Modo de cobrança
                .readCapacity(1)
                .writeCapacity(1)
                .partitionKey(Attribute.builder()
                        .name("pk")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sk")
                        .type(AttributeType.STRING)
                        .build())
                .timeToLiveAttribute("ttl")
                .removalPolicy(RemovalPolicy.DESTROY) //O interessante é colocar RETAIN para recuperar os dados caso remova a stack
                .build();
    }

    public Table getProductEventsDynammdb() {
        return productEventsDynammdb;
    }
}
