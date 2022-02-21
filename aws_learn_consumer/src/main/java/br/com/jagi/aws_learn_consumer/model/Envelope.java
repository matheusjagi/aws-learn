package br.com.jagi.aws_learn_consumer.model;

import br.com.jagi.aws_learn_consumer.model.enums.EventTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelope {

    private EventTypeEnum eventType;
    private String data;
}
