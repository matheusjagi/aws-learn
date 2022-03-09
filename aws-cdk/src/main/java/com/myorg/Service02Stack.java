package com.myorg;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.Map;

public class Service02Stack extends Stack {

    public Service02Stack(final Construct scope, final String id, Cluster cluster, SnsTopic productEventsTopic, Table productEventsDynamodb) {
        this(scope, id, null, cluster, productEventsTopic, productEventsDynamodb);
    }

    public Service02Stack(final Construct scope, final String id, final StackProps props, Cluster cluster, SnsTopic productEventsTopic, Table productEventsDynamodb) {
        super(scope, id, props);

        Queue productEventsQueue = createdProductEventsQueueAndDlq();

        //Inscreve a fila criada no topico
        SqsSubscription sqsSubscription = SqsSubscription.Builder.create(productEventsQueue).build();
        productEventsTopic.getTopic().addSubscription(sqsSubscription);

        Map<String, String> envVariables = getEnviromentVariables(productEventsQueue);

        ApplicationLoadBalancedFargateService service02 = ApplicationLoadBalancedFargateService.Builder.create(this, "ALB02")
                .serviceName("service-02")
                .cluster(cluster)
                .cpu(256)
                .memoryLimitMiB(512)
                .desiredCount(2)
                .listenerPort(9090)
                .assignPublicIp(true)
                .taskImageOptions(getTaskImageOptions(envVariables))
                .publicLoadBalancer(true)
                .build();

        service02.getTargetGroup().configureHealthCheck(HealthCheck.builder()
                .path("/actuator/health")
                .port("9090")
                .healthyHttpCodes("200")
                .build());

        //Configuração do Auto Scalling
        ScalableTaskCount scalableTaskCount = service02.getService().autoScaleTaskCount(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(2)
                .build());

        scalableTaskCount.scaleOnCpuUtilization("Service02AutoScaling", CpuUtilizationScalingProps.builder()
                .targetUtilizationPercent(50)
                .scaleInCooldown(Duration.seconds(60))
                .scaleOutCooldown(Duration.seconds(60))
                .build());

        //Criação da permissão para consumo de mensagens da fila
        productEventsQueue.grantConsumeMessages(service02.getTaskDefinition().getTaskRole());
        //Criação da permissão para o DynamoDB
        productEventsDynamodb.grantReadWriteData(service02.getTaskDefinition().getTaskRole());
    }

    @NotNull
    private Queue createdProductEventsQueueAndDlq() {
        Queue productEventsDlq = Queue.Builder.create(this, "ProductEventsDlq")
                .queueName("product-events-dlq")
                .build();

        DeadLetterQueue deadLetterQueued = DeadLetterQueue.builder()
                .queue(productEventsDlq)
                .maxReceiveCount(3)
                .build();

        Queue productEventsQueue = Queue.Builder.create(this, "ProductEvents")
                .queueName("product-events")
                .deadLetterQueue(deadLetterQueued)
                .build();

        return productEventsQueue;
    }

    private Map<String, String> getEnviromentVariables(Queue productEventsQueue) {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("AWS_REGION", "us-east-1");
        envVariables.put("AWS_SQS_QUEUE_PRODUCT_EVENTS_NAME", productEventsQueue.getQueueName());
        return envVariables;
    }

    @NotNull
    private ApplicationLoadBalancedTaskImageOptions getTaskImageOptions(Map<String, String> envVariables) {
        return ApplicationLoadBalancedTaskImageOptions.builder()
                .containerName("aws_learn02")
                .image(ContainerImage.fromRegistry("matheusjagi/aws_learn_consumer:1.3.0"))
                .containerPort(9090)
                .logDriver(getLogDriver())
                .environment(envVariables)
                .build();
    }

    @NotNull
    private LogDriver getLogDriver() {
        return LogDriver.awsLogs(AwsLogDriverProps.builder()
                .logGroup(getLogGroup())
                .streamPrefix("Service02")
                .build());
    }

    @NotNull
    private LogGroup getLogGroup() {
        return LogGroup.Builder.create(this, "Service02LogGroup")
                .logGroupName("Service02")
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }

}
