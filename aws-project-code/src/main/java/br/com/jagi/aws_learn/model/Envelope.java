package br.com.jagi.aws_learn.model;

import br.com.jagi.aws_learn.model.enums.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Envelope {

    private EventType eventType;
    private String data;

    public Envelope(EventType eventType) {
        this.eventType = eventType;
    }
}
