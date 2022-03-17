package br.com.jagi.aws_learn.service.consumer;

import br.com.jagi.aws_learn.model.SnsMessage;
import br.com.jagi.aws_learn.repository.InvoiceRepository;
import br.com.jagi.aws_learn.service.InvoiceService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceConsumer {

    private final ObjectMapper objectMapper;
    private final InvoiceRepository invoiceRepository;
    private final AmazonS3 amazonS3;
    private final InvoiceService invoiceService;

    @JmsListener(destination = "${aws.sqs.queue.invoice.events.name}")
    public void receiveS3Event(TextMessage textMessage) throws JMSException, IOException {
        SnsMessage snsMessage = objectMapper
                .readValue(textMessage.getText(), SnsMessage.class);

        S3EventNotification s3EventNotification = objectMapper
                .readValue(snsMessage.getMessage(), S3EventNotification.class);

        invoiceService.processInvoiceNotification(s3EventNotification);
    }
}
