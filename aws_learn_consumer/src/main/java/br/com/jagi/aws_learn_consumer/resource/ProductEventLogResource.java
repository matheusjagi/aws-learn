package br.com.jagi.aws_learn_consumer.resource;

import br.com.jagi.aws_learn_consumer.model.ProductEventLogDTO;
import br.com.jagi.aws_learn_consumer.service.ProductEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductEventLogResource {

    private final ProductEventService productEventService;

    @GetMapping("/events")
    public ResponseEntity<List<ProductEventLogDTO>> getAllEvents() {
        return new ResponseEntity<>(productEventService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/events/{code}")
    public ResponseEntity<List<ProductEventLogDTO>> getByCode(@PathVariable String code) {
        return new ResponseEntity<>(productEventService.getByCode(code), HttpStatus.OK);
    }

    @GetMapping("/events/{code}/{event}")
    public ResponseEntity<List<ProductEventLogDTO>> getByCodeAndEventType(@PathVariable String code,
                                                                          @PathVariable String event) {
        return new ResponseEntity<>(productEventService.getByCodeAndEventType(code, event), HttpStatus.OK);
    }
}
