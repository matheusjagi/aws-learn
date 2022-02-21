package br.com.jagi.aws_learn.model;

import br.com.jagi.aws_learn.model.enums.EventTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Envelope {

    private EventTypeEnum eventType;
    private String data;

    public Envelope(EventTypeEnum eventType) {
        this.eventType = eventType;
    }
}
